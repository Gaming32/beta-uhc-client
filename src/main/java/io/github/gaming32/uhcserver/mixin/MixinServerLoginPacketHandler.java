package io.github.gaming32.uhcserver.mixin;

import net.minecraft.packet.login.LoginRequestPacket;
import net.minecraft.server.network.ServerLoginPacketHandler;
import net.minecraft.server.player.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import io.github.gaming32.uhcserver.UHCServerMod;

@Mixin(ServerLoginPacketHandler.class)
public class MixinServerLoginPacketHandler {


    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/player/ServerPlayer;method_317()V", shift = At.Shift.AFTER), method = "complete", locals = LocalCapture.CAPTURE_FAILHARD)
    public void onComplete(LoginRequestPacket p, CallbackInfo ci, ServerPlayer player) {
        if (player != null) UHCServerMod.onPlayerJoin(player);
    }
}
