package uhcclient.mixin;

import java.util.ArrayList;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.class_266;
import net.minecraft.class_267;

@Mixin(class_266.class)
public interface class_266Accessor {
    @Accessor
    Map<String, ArrayList<class_267>> getField_1089();
}
