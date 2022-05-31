package uhcserver.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uhcserver.NullDamageCause;
import uhcserver.UHCServerMod;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends MixinEntity {


    @Shadow public abstract boolean damage(Entity arg, int i);

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/Entity;I)Z", ordinal = 0), method = "baseTick")
    private void onSuffocation(CallbackInfo ci) {
        damageCause = NullDamageCause.SUFFOCATION;
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/Entity;I)Z", ordinal = 1), method = "baseTick")
    private void onDrown(CallbackInfo ci) {
        damageCause = NullDamageCause.DROWN;
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/Entity;I)Z"), method = "handleFallDamage")
    private void onFall(float par1, CallbackInfo ci) {
        damageCause = NullDamageCause.FALL;
    }

    @Inject(at = @At("HEAD"), method = "damage")
    private void onDamage(Entity i, int par2, CallbackInfoReturnable<Boolean> cir) {
        if (i != null) {
            damagedBy = i;
            time = System.currentTimeMillis();
        }
    }

    @Inject(at = @At("TAIL"), method = "baseTick")
    private void onTick(CallbackInfo ci) {
        if (Math.abs(x) - UHCServerMod.getWorldBorder().getRadius() > 6 || Math.abs(z) - UHCServerMod.getWorldBorder().getRadius() > 6) {
            damageCause = NullDamageCause.WORLD_BORDER;
            damage(null, 2);
        }
    }
}
