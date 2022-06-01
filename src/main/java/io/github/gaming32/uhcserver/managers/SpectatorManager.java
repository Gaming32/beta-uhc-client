package io.github.gaming32.uhcserver.managers;

import io.github.gaming32.uhcserver.UHCServerMod;
import net.minecraft.packet.play.ChatMessagePacket;
import net.minecraft.server.player.ServerPlayer;

import java.util.HashSet;
import java.util.Set;

public class SpectatorManager {
    private final Set<String> spectators = new HashSet<>();


    public synchronized void clear() {
        spectators.clear();
        UHCServerMod.sendCustomPacket("reset-spectators");
    }

    public synchronized void setSpectator(String name) {
        spectators.add(name);
        UHCServerMod.getServer().playerManager.getPlayer(name).field_1642 = true;
        UHCServerMod.sendCustomPacket("spectator", name);
    }

    public synchronized void sendAllSpectators(ServerPlayer player) {
        if (spectators.contains(player.name)) {
            player.field_1642 = true;
        }

        for (String p : spectators) {
            player.packetHandler.send(new ChatMessagePacket("canyonuhc:spectator " + p));
        }
    }

    public boolean isSpectator(String name) {
        return spectators.contains(name);
    }
}
