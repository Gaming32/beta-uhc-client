package uhcclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import uhcclient.UHCClientMod;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Inject(
        method = "<init>(Ljava/awt/Component;Ljava/awt/Canvas;Lnet/minecraft/client/MinecraftApplet;IIZ)V",
        at = @At("TAIL")
    )
    private void init(CallbackInfo ci) {
        UHCClientMod.MINECRAFT = (Minecraft)(Object)this;
    }
}
