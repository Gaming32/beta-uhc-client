package io.github.gaming32.uhcserver.commands;

import io.github.gaming32.uhcserver.managers.CommandManager;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.network.ServerPlayPacketHandler;

public class Help extends AbstractCommand {

    public Help(CommandManager manager) {
        super(manager, "help");
    }

    @Override
    public void execute(String[] args, ServerPlayPacketHandler handler) {
        execute(args, (CommandSource) handler);
    }

    @Override
    public void execute(String[] args, CommandSource source) {
        for (AbstractCommand command : commandManager.getCommands()) {
            if (command.needsOp() && nonOperatoreCheck(source)) {
                continue;
            }
            source.sendFeedback("/" + command.name + " " + command.getUsage(!nonOperatoreCheck(source), !(source instanceof ServerPlayPacketHandler)));
        }
    }

    @Override
    public boolean needsOp() {
        return false;
    }

    @Override
    public String getUsage(boolean hasOp, boolean isConsole) {
        return "";
    }

}
