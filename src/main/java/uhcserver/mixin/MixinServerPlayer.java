package uhcserver.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.server.player.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uhcserver.IServerPlayer;
import uhcserver.UHCServerMod;

@Mixin(ServerPlayer.class)
public class MixinServerPlayer implements IServerPlayer {

    @Shadow private int field_258;

    @Inject(at = @At("HEAD"), method = "onKilledBy")
    public void onDamage(Entity par1, CallbackInfo ci) {
        UHCServerMod.onDeath((ServerPlayer) (Object) this, par1);
    }

    @Override
    public void setInvulnTicks(int ticks) {
        this.field_258 = ticks;
    }

}
