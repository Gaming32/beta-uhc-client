package io.github.gaming32.uhcserver.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import io.github.gaming32.uhcserver.UHCServerMod;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    @Inject(method = "start", at = @At("HEAD"))
    public void onStart(CallbackInfoReturnable<Boolean> cir) {
        UHCServerMod.updateServer((MinecraftServer) (Object) this);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void onTick(CallbackInfo ci) {
        UHCServerMod.tick();
    }

}
