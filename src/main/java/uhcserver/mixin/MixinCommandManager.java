package uhcserver.mixin;

import net.minecraft.server.command.Command;
import net.minecraft.server.command.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uhcserver.UHCServerMod;

@Mixin(CommandManager.class)
public class MixinCommandManager {

    @Inject(at = @At("HEAD"), method = "processCommand", cancellable = true)
    public void onProcessCommand(Command par1, CallbackInfo ci) {
        if (UHCServerMod.getCommandManager().processCommand(par1)) {
            ci.cancel();
            return;
        }
        if (UHCServerMod.getCommandManager().nonOperatorCheckLog(par1.source)) {
            ci.cancel();
        }
    }

}
