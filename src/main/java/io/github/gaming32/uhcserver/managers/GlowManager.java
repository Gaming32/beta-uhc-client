package io.github.gaming32.uhcserver.managers;

import io.github.gaming32.uhcserver.UHCServerMod;
import net.minecraft.packet.play.ChatMessagePacket;
import net.minecraft.server.player.ServerPlayer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GlowManager {
    private final Map<String, Integer> glowing = new HashMap<>();

    public synchronized void clear() {
        glowing.clear();
        UHCServerMod.sendCustomPacket("noglowing");
    }

    public synchronized void setGlow(String name, int color) {
        glowing.put(name, color);
        UHCServerMod.sendCustomPacket("glowing", Integer.toString(color) + " " + name);
    }

    public synchronized void removeGlow(String name) {
        glowing.remove(name);
        UHCServerMod.sendCustomPacket("noglowing", name);
    }

    public synchronized boolean isGlow(String name) {
        return glowing.containsKey(name);
    }

    public synchronized void sendAllGlow(ServerPlayer player) {
        for (String p : glowing.keySet()) {
            player.packetHandler.send(new ChatMessagePacket("canyonuhc:glowing " + glowing.get(p) + " " + p));
        }
    }
}
