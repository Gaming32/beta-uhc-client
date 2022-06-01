package io.github.gaming32.uhcclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.packet.play.ChatMessagePacket;
import io.github.gaming32.uhcclient.UHCClientMod;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {
    @Inject(
        method = "handleChatMessage(Lnet/minecraft/packet/play/ChatMessagePacket;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void handleChatMessage(ChatMessagePacket packet, CallbackInfo ci) {
        if (UHCClientMod.getPacketManager().handleMessage(packet.message)) {
            ci.cancel();
        }
    }
}
