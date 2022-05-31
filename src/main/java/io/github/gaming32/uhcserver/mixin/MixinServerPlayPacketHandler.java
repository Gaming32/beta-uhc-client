package io.github.gaming32.uhcserver.mixin;

import io.github.gaming32.uhcserver.DamageSource;
import io.github.gaming32.uhcserver.access.IEntity;
import io.github.gaming32.uhcserver.access.IServerPlayer;
import net.minecraft.packet.play.BasePlayerPacket;
import net.minecraft.server.network.ServerPlayPacketHandler;
import net.minecraft.server.player.PlayerManager;
import net.minecraft.server.player.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import io.github.gaming32.uhcserver.UHCServerMod;

@Mixin(ServerPlayPacketHandler.class)
public abstract class MixinServerPlayPacketHandler {

    @Shadow private ServerPlayer player;

    @Shadow public abstract void method_832(double d, double d1, double d2, float f, float f1);

    @Inject(method = "method_836", at = @At("HEAD"), cancellable = true)
    private void onMessage(String message, CallbackInfo ci) {
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
            UHCServerMod.handleCustomPacket(player, packetType, data);
            ci.cancel();
        }
    }


    // allow custom commnd parsing, we fix this later...
    @Redirect(method = "method_836", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/player/PlayerManager;isOperator(Ljava/lang/String;)Z"))
    private boolean isOperator(PlayerManager instance, String s) {
        return true;
    }

    @Inject(method = "handleBasePlayer", at = @At("HEAD"))
    private void onHandleBasePlayer(BasePlayerPacket p, CallbackInfo ci) {
        if (UHCServerMod.getWorldBorder().moving()) return;
        double wbr = UHCServerMod.getWorldBorder().getRadius();
        if (Math.abs(p.x) > wbr || Math.abs(p.z) > wbr) {
            if (Math.abs(player.x) < wbr && Math.abs(player.z) < wbr) {
                //move back in
                method_832(player.x, player.y, player.z, player.yaw, player.pitch);
//                System.out.println("Moving player back to border");
            }
        }
    }

    @Inject(method = "method_836", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/player/ServerPlayer;damage(Lnet/minecraft/entity/Entity;I)Z"))
    private void onKill(String par1, CallbackInfo ci) {
        ((IEntity) player).setDamageCause(DamageSource.SUICIDE);
    }

}
