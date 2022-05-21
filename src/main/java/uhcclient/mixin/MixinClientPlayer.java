package uhcclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.ClientPlayer;
import uhcclient.UHCClientMod;

@Mixin(ClientPlayer.class)
public class MixinClientPlayer {
    @Inject(
        method = "tick()V",
        at = @At("TAIL")
    )
    private void tick(CallbackInfo ci) {
        final ClientPlayer player = (ClientPlayer)(Object)this;
        if (!UHCClientMod.spectatingPlayers.contains(player.name)) {
            return;
        }
        if (player.keypressManager.jump) {
            player.velocityY = 0.42; // Standard jump velocity
        }
    }
}
