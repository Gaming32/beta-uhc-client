package io.github.gaming32.uhcclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.resource.language.TranslationStorage;

@Mixin(TranslationStorage.class)
public class MixinTranslationStorage {
    @Inject(
        method = "method_995(Ljava/lang/String;)Ljava/lang/String;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void method_995(String key, CallbackInfoReturnable<String> cir) {
        if (key.startsWith("rawtext:")) {
            cir.setReturnValue(key.substring(8));
        }
    }
}
