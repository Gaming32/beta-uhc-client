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
        UHCClientMod.customJukeboxMessage =
            "World border: " + (int)UHCClientMod.getWorldBorder() +
            " | Distance from border: " + (int)UHCClientMod.getDistanceFromBorder();
        if (!UHCClientMod.spectatingPlayers.contains(player.name)) {
            return;
        }
        if (player.keypressManager.jump && !player.keypressManager.sneak) {
            player.velocityY = 0.42; // Standard jump velocity
        } else if (player.keypressManager.sneak && !player.keypressManager.jump) {
            player.velocityY = -0.42;
        } else {
            player.velocityY = 0;
        }
    }

    @Inject(
        method = "dropSelectedItem()V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void dropSelectedItem(CallbackInfo ci) {
        final ClientPlayer player = (ClientPlayer)(Object)this;
        if (UHCClientMod.spectatingPlayers.contains(player.name)) {
            ci.cancel();
        }
    }
}
