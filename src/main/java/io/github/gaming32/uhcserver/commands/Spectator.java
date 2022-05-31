package io.github.gaming32.uhcserver.commands;

import io.github.gaming32.uhcserver.Formatting;
import io.github.gaming32.uhcserver.UHCServerMod;
import io.github.gaming32.uhcserver.managers.CommandManager;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.network.ServerPlayPacketHandler;

public class Spectator extends AbstractCommand {


    public Spectator(CommandManager manager) {
        super(manager, "spectator");
    }

    @Override
    public void execute(String[] args, ServerPlayPacketHandler handler) {
        if (args.length == 1) {
            UHCServerMod.getStateManager().setSpectator(handler.getName());
        }
        execute(args, (CommandSource) handler);
    }

    @Override
    public void execute(String[] args, CommandSource source) {
        if (args.length != 2) {
            source.sendFeedback(Formatting.RED + "Invalid args; usage: " + Formatting.GRAY + " /spectator " + getUsage(true, !(source instanceof ServerPlayPacketHandler)));
        }
        UHCServerMod.getStateManager().setSpectator(args[1]);
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
