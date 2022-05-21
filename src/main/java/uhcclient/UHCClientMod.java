package uhcclient;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.github.minecraftcursedlegacy.api.event.ActionResult;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import uhcclient.packets.CustomPacketCallback;
import uhcclient.packets.CustomPacketManager;

public class UHCClientMod implements ModInitializer {
    public static Minecraft MINECRAFT;

    public static Set<String> spectatingPlayers = new HashSet<>();
    public static Map<Integer, String> mapIdToPlayerName = new HashMap<>();
    private static double worldBorder;
    private static double worldBorderDest;
    private static double worldBorderTicksRemaining;
    private static double worldBorderInterp;
    private static long worldBorderInterpStart;

    public CustomPacketManager packetManager;

    @Override
    public void onInitialize() {
        packetManager = new CustomPacketManager();
        ChatMessageCallback.EVENT.register(packetManager);

        CustomPacketCallback.EVENT.register((packetType, data) -> {
            System.out.println(packetType + ' ' + data);
            switch (packetType) {
                case "ping": {
                    packetManager.sendPacket("pong");
                    return ActionResult.PASS;
                }
                case "spectator": {
                    spectatingPlayers.add(data);
                    return ActionResult.PASS;
                }
                case "reset-spectators": {
                    spectatingPlayers.clear();
                    return ActionResult.PASS;
                }
                case "worldborder": {
                    worldBorderDest = worldBorder = CustomPacketManager.stringToDouble(data);
                    worldBorderInterp = worldBorderInterpStart = 0;
                    return ActionResult.PASS;
                }
                case "worldborderinterp": {
                    int midIndex = data.indexOf(' ');
                    worldBorderDest = CustomPacketManager.stringToDouble(data.substring(0, midIndex));
                    worldBorderTicksRemaining = Integer.parseUnsignedInt(data.substring(midIndex + 1), 16);
                    worldBorderInterp = (worldBorder - worldBorderDest) / (double)worldBorderTicksRemaining;
                    worldBorderInterpStart = System.currentTimeMillis();
                    return ActionResult.PASS;
                }
                case "mapplayer": {
                    int midIndex = data.indexOf(' ');
                    mapIdToPlayerName.put(
                        Integer.parseUnsignedInt(data.substring(0, midIndex), 16),
                        data.substring(midIndex + 1)
                    );
                    return ActionResult.PASS;
                }
                default:
                    System.err.println("Unknown packet type: " + packetType);
                    return ActionResult.PASS;
            }
        });

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
}
