package io.github.gaming32.uhcserver.managers;

import net.minecraft.packet.play.ChatMessagePacket;
import net.minecraft.server.player.ServerPlayer;
import io.github.gaming32.uhcserver.UHCServerMod;

public class WorldBorderManager {
    private double radius = 500;
    private long interpTicks;
    private double newRadius = radius;

    private int tickCount;

    private boolean done = true;
    public double getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = this.newRadius = radius;
        this.interpTicks = 0;
        sync();
    }

    public void setRadius(int radius, long ticks) {
        this.newRadius = radius;
        this.interpTicks = ticks;
        done = false;
        sync();
    }

    public void tick() {
        tickCount++;
        if (Math.abs(radius - newRadius) > 0.01) {
            radius += (newRadius - radius) / interpTicks;
            if (interpTicks > 1) {
                interpTicks--;
            } else {
                interpTicks = 1;
            }
            if (tickCount % 100 == 0) {
                sync();
                System.out.println("Radius: " + radius);
            }
        } else {
            if (!done) {
                done = true;
                sync();
            }
        }
    }

    public boolean moving() {
        return !done;
    }

    private void sync() {
        UHCServerMod.sendCustomPacket("worldborder", doubleToString(radius));
        if (interpTicks > 0) UHCServerMod.sendCustomPacket("worldborderinterp", doubleToString(newRadius) + " " + Long.toHexString(interpTicks));
    }


    private static String doubleToString(double d) {
        return Long.toHexString(Double.doubleToRawLongBits(d));
    }

    public void onPlayerJoin(ServerPlayer player) {
        player.packetHandler.send(new ChatMessagePacket("canyonuhc:worldborder " + doubleToString(radius)));
        if (interpTicks > 0) player.packetHandler.send(new ChatMessagePacket("canyonuhc:worldborderinterp " + doubleToString(newRadius) + " " + Long.toHexString(interpTicks)));
    }

}