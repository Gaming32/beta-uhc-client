package io.github.gaming32.uhcserver.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.Player;
import net.minecraft.level.Level;
import net.minecraft.server.player.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import io.github.gaming32.uhcserver.access.IServerPlayer;
import io.github.gaming32.uhcserver.UHCServerMod;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer extends Player implements IServerPlayer {

    @Shadow private int field_258;


    @Inject(at = @At("HEAD"), method = "onKilledBy")
    public void onDamage(Entity par1, CallbackInfo ci) {
        UHCServerMod.onDeath((ServerPlayer) (Object) this, par1);
    }

    @Inject(at = @At("HEAD"), method = "damage", cancellable = true)
    private void onDamage(Entity i, int par2, CallbackInfoReturnable<Boolean> cir) {
        if (UHCServerMod.getSpectatorManager().isSpectator(name)) {
            cir.setReturnValue(false);
        }
    }

    @Override
    public void setInvulnTicks(int ticks) {
        this.field_258 = ticks;
    }

    //ignore
    public MixinServerPlayer(Level arg) {
        super(arg);
    }

}
