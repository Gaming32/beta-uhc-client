package uhcserver.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.server.player.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uhcserver.IEntity;
import uhcserver.NullDamageCause;
import uhcserver.UHCServerMod;

@Mixin(Entity.class)
public class MixinEntity implements IEntity {
    @Shadow public double x;
    @Shadow public double z;
    @Unique protected NullDamageCause damageCause;

    @Unique protected Entity damagedBy;
    @Unique protected long time;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/Entity;I)Z"), method = "baseTick")
    public void onBurn(CallbackInfo ci) {
        damageCause = NullDamageCause.BURN;
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;destroy()V"), method = "baseTick")
    public void onVoid(CallbackInfo ci) {
        damageCause = NullDamageCause.VOID;
        if ((Object) this instanceof ServerPlayer) {
            UHCServerMod.onDeath((ServerPlayer) (Object) this, null);
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "method_1392")
    public void onFire(CallbackInfo ci) {
        damageCause = NullDamageCause.FIRE;
    }

    @Inject(at = @At(value = "HEAD"), method = "method_1332")
    public void onLava(CallbackInfo ci) {
        damageCause = NullDamageCause.LAVA;
    }

    @Inject(at = @At("HEAD"), method = "damage")
    private void onDamage(Entity i, int par2, CallbackInfoReturnable<Boolean> cir) {
        if (i != null) {
            damagedBy = i;
            time = System.currentTimeMillis();
        }
    }

    @Override
    public NullDamageCause getDamageCause() {
        return damageCause;
    }

    @Override
    public void setDamageCause(NullDamageCause cause) {
        damageCause = cause;
    }

    @Override
    public Entity getDamagedBy() {
        return damagedBy;
    }

    @Override
    public long getDamagedByTime() {
        return time;
    }



}
