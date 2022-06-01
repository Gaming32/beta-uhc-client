package io.github.gaming32.uhcclient;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;

public class UHCClientMod implements ClientModInitializer {
    public static Minecraft MINECRAFT;

    public static final Set<String> spectatingPlayers = new HashSet<>();
    public static final Map<String, Integer> glowingEffects = new HashMap<>();
    public static final Map<Integer, String> mapIdToPlayerName = new HashMap<>();
    public static final Map<String, String> displayNames = new HashMap<>();
    public static String customJukeboxMessage = null;

    private static double worldBorder;
    private static double worldBorderDest;
    private static double worldBorderInterp;
    private static long worldBorderTicksRemaining;
    private static long worldBorderInterpStart;
    private static CustomPacketManager packetManager;

    @Override
    public void onInitializeClient() {
        packetManager = new CustomPacketManager();
        System.out.println("Loaded uhc-client");
    }

    public static int worldBorderInterpDir() {
        return worldBorderInterpStart == 0 ? 0 : Double.compare(worldBorderDest, worldBorder);
    }

    public static double getWorldBorder() {
        return worldBorderInterpStart == 0
            ? worldBorder
            : worldBorderDest
                + (worldBorderInterp * worldBorderTicksRemaining)
                - (worldBorderInterp * (System.currentTimeMillis() - worldBorderInterpStart) / 50);
    }

    public static double getWorldBorderInterpDest() {
        return worldBorderDest;
    }

    public static double getWorldBorderInterpSpeed() {
        return worldBorderInterpStart == 0 ? 0.0 : worldBorderInterp;
    }

    public static double getDistanceFromBorder() {
        LivingEntity player = UHCClientMod.MINECRAFT.player;
        double worldBorder = getWorldBorder();
        return Math.min(
            Math.min(
                worldBorder - player.x,
                player.x + worldBorder
            ),
            Math.min(
                worldBorder - player.z,
                player.z + worldBorder
            )
        );
    }

    public static CustomPacketManager getPacketManager() {
        return packetManager;
    }

    public static void handleCustomPacket(String packetType, String data) {
        System.out.println(packetType + ' ' + data);
        switch (packetType) {
            case "ping": {
                packetManager.sendPacket("pong");
                break;
            }
            case "spectator": {
                spectatingPlayers.add(data);
                break;
            }
            case "reset-spectators": {
                spectatingPlayers.clear();
                break;
            }
            case "worldborder": {
                worldBorderDest = worldBorder = CustomPacketManager.stringToDouble(data);
                worldBorderInterp = worldBorderInterpStart = 0;
                break;
            }
            case "worldborderinterp": {
                int midIndex = data.indexOf(' ');
                worldBorderDest = CustomPacketManager.stringToDouble(data.substring(0, midIndex));
                worldBorderTicksRemaining = Long.parseUnsignedLong(data.substring(midIndex + 1), 16);
                worldBorderInterp = (worldBorder - worldBorderDest) / (double)worldBorderTicksRemaining;
                worldBorderInterpStart = System.currentTimeMillis();
                break;
            }
            case "mapplayer": {
                int midIndex = data.indexOf(' ');
                mapIdToPlayerName.put(
                    Integer.valueOf(data.substring(0, midIndex), 16),
                    data.substring(midIndex + 1)
                );
                break;
            }
            case "glowing": {
                int midIndex = data.indexOf(' ');
                glowingEffects.put(
                    data.substring(midIndex + 1),
                    Integer.valueOf(data.substring(0, midIndex), 16)
                );
                break;
            }
            case "noglowing": {
                if (data == null) {
                    glowingEffects.clear();
                } else {
                    glowingEffects.remove(data);
                }
                break;
            }
            case "displayname": {
                int midIndex = data.indexOf(' ');
                if (midIndex == -1) {
                    displayNames.remove(data);
                } else {
                    displayNames.put(data.substring(0, midIndex), data.substring(midIndex + 1));
                }
                break;
            }
            case "cleardisplaynames": {
                displayNames.clear();
                break;
            }
            case "removeplayer": {
                displayNames.remove(data);
                break;
            }
            case "lightning": {
                ThreadLocalRandom tlr = ThreadLocalRandom.current();
                MINECRAFT.level.playSound(MINECRAFT.player, "ambient.weather.thunder", 10000.0F, 0.8F + tlr.nextFloat() * 0.2F);
                MINECRAFT.level.playSound(MINECRAFT.player, "random.explode", 2.0F, 0.5F + tlr.nextFloat() * 0.2F);
                break;
            }
            default:
                System.err.println("Unknown packet type: " + packetType);
                break;
        }
    }
}
