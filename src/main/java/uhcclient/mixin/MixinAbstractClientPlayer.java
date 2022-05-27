package uhcclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.player.AbstractClientPlayer;
import uhcclient.UHCClientMod;

@Mixin(AbstractClientPlayer.class)
public class MixinAbstractClientPlayer {
    @Inject(
        method = "method_1372(DDD)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void updateDespawnCounter(CallbackInfoReturnable<Boolean> ci) {
        if (UHCClientMod.spectatingPlayers.contains(((AbstractClientPlayer)(Object)this).name)) {
            ci.setReturnValue(false);
        }
    }
}
