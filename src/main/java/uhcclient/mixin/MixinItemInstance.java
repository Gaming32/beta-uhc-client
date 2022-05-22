package uhcclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.ItemInstance;
import net.minecraft.item.ItemType;
import uhcclient.UHCClientMod;

@Mixin(ItemInstance.class)
public class MixinItemInstance {
    @Inject(
        method = "getTranslationKey()Ljava/lang/String;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void getTranlsationKey(CallbackInfoReturnable<String> cir) {
        ItemInstance item = (ItemInstance)(Object)this;
        if (item.itemId == ItemType.map.id) {
            String playerName = UHCClientMod.mapIdToPlayerName.get(item.getDamage());
            if (playerName != null) {
                cir.setReturnValue("rawtext:" + playerName);
            }
        }
    }
}
