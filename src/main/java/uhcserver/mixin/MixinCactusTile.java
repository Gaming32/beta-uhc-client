package uhcserver.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.level.Level;
import net.minecraft.tile.CactusTile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uhcserver.IEntity;
import uhcserver.NullDamageCause;

@Mixin(CactusTile.class)
public class MixinCactusTile {

    @Inject(at = @At("HEAD"), method = "onEntityCollision")
    public void onCactudDamage(Level i, int j, int k, int arg1, Entity par5, CallbackInfo ci) {
        ((IEntity) par5).setDamageCause(NullDamageCause.CACTUS);
    }
}
