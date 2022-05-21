package uhcclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.Player;
import net.minecraft.entity.player.RemoteClientPlayer;
import uhcclient.UHCClientMod;

@Mixin(RemoteClientPlayer.class)
public class MixinRemoteClientPlayer {
    @Inject(
        method = "tick()V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void tick(CallbackInfo ci) {
        if (UHCClientMod.spectatingPlayers.contains(MinecraftAccessor.getInstance().player.name)) {
            return;
        }
        final Player player = (Player)(Object)this;
        if (UHCClientMod.spectatingPlayers.contains(player.name)) {
            player.remove();
            ci.cancel();
        }
    }
}
