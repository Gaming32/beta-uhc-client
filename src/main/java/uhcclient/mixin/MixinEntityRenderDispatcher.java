package uhcclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.Player;
import uhcclient.UHCClientMod;

@Mixin(EntityRenderDispatcher.class)
public class MixinEntityRenderDispatcher {
    @Inject(
        method = "method_1921(Lnet/minecraft/entity/Entity;F)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void method_1921(Entity entity, float tickTime, CallbackInfo ci) {
        if (
            entity instanceof Player &&
            !UHCClientMod.spectatingPlayers.contains(MinecraftAccessor.getInstance().player.name) &&
            UHCClientMod.spectatingPlayers.contains(((Player)entity).name)
        ) {
            ci.cancel();
        }
    }
}
