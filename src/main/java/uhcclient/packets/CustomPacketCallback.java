package uhcclient.packets;

import io.github.minecraftcursedlegacy.api.event.ActionResult;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface CustomPacketCallback {
    Event<CustomPacketCallback> EVENT = EventFactory.createArrayBacked(CustomPacketCallback.class,
        listeners -> (packetType, data) -> {
            for (CustomPacketCallback listener : listeners) {
                ActionResult result = listener.handle(packetType, data);
                if (result != ActionResult.PASS) {
                    return result;
                }
            }
            return ActionResult.PASS;
        }
    );

    ActionResult handle(String packetType, String data);
}
