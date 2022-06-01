package io.github.gaming32.uhcclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Lightning;
import net.minecraft.level.Level;

@Mixin(Lightning.class)
public class MixinLightning {
    @Redirect(
        method = "tick()V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/level/Level;playSound(DDDLjava/lang/String;FF)V"
        )
    )
    private void playSound(Level level, double x, double y, double z, String sound, float param1, float param2) {
    }
}
