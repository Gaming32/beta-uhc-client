package io.github.gaming32.uhcserver.commands;

import io.github.gaming32.uhcserver.Formatting;
import io.github.gaming32.uhcserver.managers.CommandManager;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.network.ServerPlayPacketHandler;

public class Lightning extends AbstractCommand {

    public Lightning(CommandManager manager) {
        super(manager, "lightning");
    }

    @Override
    public void execute(String[] args, ServerPlayPacketHandler handler) {
        if (args.length == 1) {
            //TODO:
        } else {
            execute(args, (CommandSource) handler);
        }
    }

    @Override
    public void execute(String[] args, CommandSource source) {
        if (args.length != 2) {
            source.sendFeedback(Formatting.RED + "Invalid args; usage: " + Formatting.GRAY + " /lightning " + getUsage(true, !(source instanceof ServerPlayPacketHandler)));
        }
        //TODO:
    }

    @Override
    public boolean needsOp() {
        return false;
    }

    @Override
    public String getUsage(boolean hasOp, boolean isConsole) {
        return isConsole ? "<player>" : "[<player>]";
    }

}
