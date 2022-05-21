package uhcclient;

import io.github.minecraftcursedlegacy.api.event.ActionResult;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface ChatMessageCallback {
    Event<ChatMessageCallback> EVENT = EventFactory.createArrayBacked(ChatMessageCallback.class,
        listeners -> message -> {
            for (ChatMessageCallback listener : listeners) {
                ActionResult result = listener.receive(message);
                if (result != ActionResult.PASS) {
                    return result;
                }
            }
            return ActionResult.PASS;
        }
    );

    ActionResult receive(String message);
}
