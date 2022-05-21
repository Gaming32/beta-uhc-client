package uhcclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.minecraftcursedlegacy.api.event.ActionResult;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.packet.play.ChatMessagePacket;
import uhcclient.ChatMessageCallback;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {
    @Inject(
        method = "handleChatMessage(Lnet/minecraft/packet/play/ChatMessagePacket;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void handleChatMessage(ChatMessagePacket packet, CallbackInfo ci) {
        ActionResult result = ChatMessageCallback.EVENT.invoker().receive(packet.message);
        if (result == ActionResult.FAIL) {
            ci.cancel();
        }
    }
}
