package io.github.gaming32.uhcserver.mixin;

import io.github.gaming32.uhcserver.DamageSource;
import io.github.gaming32.uhcserver.access.IEntity;
import net.minecraft.entity.Entity;
import net.minecraft.packet.play.BasePlayerPacket;
import net.minecraft.packet.play.ItemUseC2S;
import net.minecraft.packet.play.PlayerDiggingC2S;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayPacketHandler;
import net.minecraft.server.player.PlayerManager;
import net.minecraft.server.player.ServerPlayer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import io.github.gaming32.uhcserver.UHCServerMod;

import java.util.Objects;

@Mixin(ServerPlayPacketHandler.class)
public abstract class MixinServerPlayPacketHandler {

    @Shadow private ServerPlayer player;

    @Shadow public abstract void method_832(double d, double d1, double d2, float f, float f1);

    @Shadow private MinecraftServer server;

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
        if (UHCServerMod.getSpectatorManager().isSpectator(player.name)) return;
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

    @Inject(method = "handlePlayerDigging", at = @At("HEAD"), cancellable = true)
    private void onHandlePlayerBreak(PlayerDiggingC2S par1, CallbackInfo ci) {
        if (UHCServerMod.getSpectatorManager().isSpectator(player.name)) {
            ci.cancel();
        }
    }

    @Inject(method = "handleItemUse", at = @At("HEAD"), cancellable = true)
    private void onHandleItemUse(ItemUseC2S par1, CallbackInfo ci) {
        if (UHCServerMod.getSpectatorManager().isSpectator(player.name)) {
            ci.cancel();
        }
    }

    @Redirect(method = "handleBasePlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/server/MinecraftServer;allowFlight:Z", opcode = Opcodes.GETFIELD))
    private boolean onAllowFlight(MinecraftServer instance) {
        return server.allowFlight || UHCServerMod.getSpectatorManager().isSpectator(player.name);
    }

    @Redirect(method = "handleEntityInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/player/ServerPlayer;attack(Lnet/minecraft/entity/Entity;)V"))
    private void onAttack(ServerPlayer instance, Entity entity) {
        if (UHCServerMod.getSpectatorManager().isSpectator(player.name)) {
            return;
        }
        if (entity instanceof ServerPlayer && Objects.equals(UHCServerMod.getTeamManager().getTeamForPlayer(((ServerPlayer) entity).name), UHCServerMod.getTeamManager().getTeamForPlayer(player.name))) {
            return;
        }
        instance.attack(entity);
    }

    @Redirect(method = "handleChatMessage", at = @At(value = "FIELD", target = "Lnet/minecraft/server/player/ServerPlayer;name:Ljava/lang/String;"))
    private String onChat(ServerPlayer instance) {
        return UHCServerMod.getTeamManager().formatPlayer(instance.name);
    }

}
