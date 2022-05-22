package uhcclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.class_266;
import net.minecraft.client.sound.SoundHelper;

@Mixin(SoundHelper.class)
public interface SoundHelperAccessor {
    @Accessor
    class_266 getField_2669();
}
