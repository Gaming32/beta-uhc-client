package uhcclient;

import java.util.HashSet;
import java.util.Set;

import io.github.minecraftcursedlegacy.api.event.ActionResult;
import net.fabricmc.api.ModInitializer;
import uhcclient.packets.CustomPacketCallback;
import uhcclient.packets.CustomPacketManager;

public class UHCClientMod implements ModInitializer {
    public static Set<String> spectatingPlayers = new HashSet<>();
    public static double worldBorder;

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
                    worldBorder = CustomPacketManager.stringToDouble(data);
                    return ActionResult.PASS;
                }
                default:
                    System.err.println("Unknown packet type: " + packetType);
                    return ActionResult.PASS;
            }
        });

        System.out.println("Loaded uhc-client");
    }
}
