package io.github.gaming32.uhcserver.managers;

import net.minecraft.level.chunk.Chunk;
import net.minecraft.packet.play.ChatMessagePacket;
import net.minecraft.server.player.ServerPlayer;
import io.github.gaming32.uhcserver.Formatting;
import io.github.gaming32.uhcserver.UHCServerMod;
import io.github.gaming32.uhcserver.UHCStages;
import io.github.gaming32.uhcserver.access.IServerPlayer;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class UHCStateManager {

    private UHCState uhcState = UHCState.WAITING;
    private UHCStages uhcStage = UHCStages.GRACE_PERIOD;
    private final Set<String> spectators = new HashSet<>();
    private int tickLeft = uhcStage.getTime();
    public void endUHC() {
        uhcState = UHCState.ENDED;
        synchronized (spectators) {
            spectators.clear();
        }
        UHCServerMod.sendCustomPacket("reset-spectators");
        //TODO: teleport players to spawn, reset world border, etc.
    }

    public void startUHC() {
        if (uhcState == UHCState.RUNNING) {
            throw new IllegalStateException("UHC already started!");
        }
        uhcState = UHCState.RUNNING;
        if (UHCServerMod.TEST_MODE) {
            UHCServerMod.getServer().playerManager.sendPacketToAll(new ChatMessagePacket(Formatting.GOLD + "UHC in test mode, all values are quatered!"));
        }

        uhcStage = UHCStages.GRACE_PERIOD;
        tickLeft = uhcStage.getTime();
        UHCServerMod.getWorldBorder().setRadius(uhcStage.getEndSize());
        UHCServerMod.getWorldBorder().setRadius(uhcStage.getEndSize(), uhcStage.getTime());
        UHCServerMod.getServer().allowPvp = uhcStage.isPvp();

        for (Object player : UHCServerMod.getServer().playerManager.players) {
            ThreadLocalRandom rand = ThreadLocalRandom.current();
            int x = rand.nextInt(-2000, 2001);
            int z = rand.nextInt(-2000, 2001);
            if (UHCServerMod.TEST_MODE) {
                x >>= 2;
                z >>= 2;
            }
            Chunk chk = UHCServerMod.getServer().levels[0].chunkCache.loadChunk(x >> 4, z >> 4);
            ServerPlayer pl = (ServerPlayer) player;
            ((ServerPlayer) player).addHealth(20);
            ((IServerPlayer) pl).setInvulnTicks(400);

            UHCServerMod.teleportPlayer(pl.name, x, chk.getHeight(x & 15, z & 15) + 1, z);
        }
    }

    public void tick() {
        if (uhcState != UHCState.RUNNING) {
            return;
        }

        tickLeft--;
        if (tickLeft <= 0 && uhcStage != UHCStages.FINAL) {
            if (uhcStage == UHCStages.GRACE_PERIOD) {
                // heal all
                for (Object player : UHCServerMod.getServer().playerManager.players) {
                    ((ServerPlayer) player).addHealth(20);
                }
            }
            uhcStage = UHCStages.values()[uhcStage.ordinal() + 1];
            tickLeft = uhcStage.getTime();
            UHCServerMod.getWorldBorder().setRadius(uhcStage.getEndSize(), uhcStage.getTime());
            UHCServerMod.getServer().allowPvp = uhcStage.isPvp();
        }

        if (tickLeft == 2400 && uhcStage == UHCStages.GRACE_PERIOD) {
            UHCServerMod.getServer().playerManager.sendPacketToAll(new ChatMessagePacket(
                Formatting.GOLD + String.format("%.1g", tickLeft / 20. / 60.) + " minutes left in grace period!"));
        }
    }

    public boolean uhcRunning() {
        return uhcState == UHCState.RUNNING;
    }

    public void sendAllSpectators(ServerPlayer player) {
        synchronized (spectators) {
            for (String p : spectators) {
                player.packetHandler.send(new ChatMessagePacket("canyonuhc:spectator " + p));
            }
        }
    }

    public void dead(String name) {
        if (uhcRunning()) {
            setSpectator(name);
        }
    }

    public void setSpectator(String name) {
        spectators.add(name);
        UHCServerMod.sendCustomPacket("spectator", name);
    }

    public boolean isSpectator(String name) {
        return spectators.contains(name);
    }

    private enum UHCState {
        WAITING,
        RUNNING,
        ENDED
    }
}
