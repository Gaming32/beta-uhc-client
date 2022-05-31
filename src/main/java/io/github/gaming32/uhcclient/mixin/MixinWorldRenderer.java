package io.github.gaming32.uhcclient.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.class_266;
import net.minecraft.class_267;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.player.Player;
import io.github.gaming32.uhcclient.UHCClientMod;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
    @Inject(
        method = "playLevelEvent(Lnet/minecraft/entity/player/Player;IIIII)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/level/Level;method_179(Ljava/lang/String;III)V"
        ),
        cancellable = true
    )
    private void playLevelEvent(Player player, int id, int x, int y, int z, int data, CallbackInfo ci) {
        if (data == 1) {
            ci.cancel();
            class_266 audioRandomizer = ((SoundHelperAccessor)UHCClientMod.MINECRAFT.soundHelper).getField_2669();
            ((class_266Accessor)audioRandomizer).getField_1089().computeIfAbsent("winsound", k -> {
                List<class_267> sounds = new ArrayList<>();
                sounds.add(new class_267("13 - Ending.wav", null));
                return sounds;
            });
            UHCClientMod.MINECRAFT.soundHelper.method_2010("winsound", x, y, z, 1, 1);
        }
    }
}
