package io.github.gaming32.uhcclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import io.github.gaming32.uhcclient.UHCClientMod;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Inject(
        method = "<init>(Ljava/awt/Component;Ljava/awt/Canvas;Lnet/minecraft/client/MinecraftApplet;IIZ)V",
        at = @At("TAIL")
    )
    private void init(CallbackInfo ci) {
        UHCClientMod.MINECRAFT = (Minecraft)(Object)this;
    }

//    @Redirect(
//        method = "tick",
//        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;isConnectedToServer()Z", ordinal = 0)
//    )
//    private boolean isConnectedToServer(Minecraft minecraft) {
//        return true;
//    }
}
