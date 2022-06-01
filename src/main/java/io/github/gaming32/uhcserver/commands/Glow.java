package io.github.gaming32.uhcserver.commands;

import io.github.gaming32.uhcserver.Formatting;
import io.github.gaming32.uhcserver.UHCServerMod;
import io.github.gaming32.uhcserver.managers.CommandManager;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.network.ServerPlayPacketHandler;

public class Glow extends AbstractCommand {
    public Glow(CommandManager manager) {
        super(manager, "glow");
    }

    @Override
    public void execute(String[] args, ServerPlayPacketHandler handler) {
        if (args.length == 1) {
            if (UHCServerMod.getGlowManager().isGlow(handler.getName())) {
                UHCServerMod.getGlowManager().removeGlow(handler.getName());
                handler.sendFeedback(Formatting.RED + "Glow disabled.");
            } else {
                UHCServerMod.getGlowManager().setGlow(handler.getName(), 0xFF0000);
                handler.sendFeedback(Formatting.GREEN + "Glow enabled.");
            }
        } else {
            execute(args, (CommandSource) handler);
        }
    }

    @Override
    public void execute(String[] args, CommandSource source) {
        if (args.length != 2) {
            source.sendFeedback(Formatting.RED + "Invalid args; usage: " + Formatting.GRAY + " /glow " + getUsage(true, !(source instanceof ServerPlayPacketHandler)));
        }
        if (UHCServerMod.getGlowManager().isGlow(args[1])) {
            UHCServerMod.getGlowManager().removeGlow(args[1]);
            source.sendFeedback(Formatting.RED + "Glow disabled for " + args[1] + ".");
        } else {
            UHCServerMod.getGlowManager().setGlow(args[1], 0xFF0000);
            source.sendFeedback(Formatting.GREEN + "Glow enabled for " + args[1] + ".");
        }
    }

    @Override
    public boolean needsOp() {
        return true;
    }

    @Override
    public String getUsage(boolean hasOp, boolean isConsole) {
        return isConsole ? "<player>" : "[<player>]";
    }

}
