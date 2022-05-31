package uhcserver;

import net.minecraft.level.chunk.Chunk;
import net.minecraft.packet.play.ChatMessagePacket;
import net.minecraft.server.player.ServerPlayer;

import java.util.concurrent.ThreadLocalRandom;

public class UHCStateManager {

    private UHCState uhcState = UHCState.WAITING;
    private UHCStages uhcStage = UHCStages.GRACE_PERIOD;
    private int tickLeft = uhcStage.getTime();
    public void endUHC() {
        uhcState = UHCState.ENDED;
        //TODO: teleport players to spawn, reset world border, etc.
    }

    public void startUHC() {
        if (uhcState == UHCState.RUNNING) {
            throw new IllegalStateException("UHC already started!");
        }
        uhcState = UHCState.RUNNING;

        uhcStage = UHCStages.GRACE_PERIOD;
        tickLeft = uhcStage.getTime();
        UHCServerMod.getWorldBorder().setRadius(uhcStage.getStartSize());
        UHCServerMod.getWorldBorder().setRadius(uhcStage.getEndSize(), uhcStage.getTime());
        UHCServerMod.getServer().allowPvp = uhcStage.isPvp();

        for (Object player : UHCServerMod.getServer().playerManager.players) {
            ThreadLocalRandom rand = ThreadLocalRandom.current();
            int x = rand.nextInt(-2000, 2001);
            int z = rand.nextInt(-2000, 2001);
            Chunk chk = UHCServerMod.getServer().levels[0].chunkCache.loadChunk(x >> 4, z >> 4);
            ServerPlayer pl = (ServerPlayer) player;
            ((IServerPlayer) pl).setInvulnTicks(400);
            ;
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
            UHCServerMod.getServer().playerManager.sendPacketToAll(new ChatMessagePacket(Formatting.GOLD + "2 minutes left in grace period!"));
        }
    }

    private enum UHCState {
        WAITING,
        RUNNING,
        ENDED
    }
}
