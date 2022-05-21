package uhcclient.packets;

import io.github.minecraftcursedlegacy.api.event.ActionResult;
import net.minecraft.entity.player.ClientPlayer;
import uhcclient.ChatMessageCallback;
import uhcclient.mixin.MinecraftAccessor;

public final class CustomPacketManager implements ChatMessageCallback {
    public CustomPacketManager() {
    }

    private static ClientPlayer getClientPlayer() {
        return (ClientPlayer)MinecraftAccessor.getInstance().player;
    }

    public void sendPacket(String packetType) {
        getClientPlayer().sendChatMessage("canyonuhc:" + packetType);
    }

    public void sendPacket(String packetType, String data) {
        getClientPlayer().sendChatMessage("canyonuhc:" + packetType + ' ' + data);
    }

    @Override
    public ActionResult receive(String message) {
        if (message.startsWith("canyonuhc:")) {
            int endIndex = message.indexOf(' ', 10);
            String data;
            if (endIndex == -1) {
                endIndex = message.length();
                data = null;
            } else {
                data = message.substring(endIndex + 1);
            }
            String packetType = message.substring(10, endIndex);
            CustomPacketCallback.EVENT.invoker().handle(packetType, data);
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }

    public static String doubleToString(double d) {
        return Long.toHexString(Double.doubleToRawLongBits(d));
    }

    public static double stringToDouble(String s) {
        return Double.longBitsToDouble(Long.parseUnsignedLong(s, 16));
    }
}
