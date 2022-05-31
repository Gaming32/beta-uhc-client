package io.github.gaming32.uhcserver.commands;

import io.github.gaming32.uhcserver.UHCServerMod;
import io.github.gaming32.uhcserver.managers.CommandManager;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.network.ServerPlayPacketHandler;

public class StartUHC extends AbstractCommand {

    public StartUHC(CommandManager manager) {
        super(manager, "start-uhc");
    }

    @Override
    public void execute(String[] args, ServerPlayPacketHandler handler) {
        execute(args, (CommandSource) handler);
    }

    @Override
    public void execute(String[] args, CommandSource source) {
        UHCServerMod.getStateManager().startUHC();
    }

    @Override
    public boolean needsOp() {
        return true;
    }

    @Override
    public String getUsage(boolean hasOp, boolean isConsole) {
        return "";
    }

}
