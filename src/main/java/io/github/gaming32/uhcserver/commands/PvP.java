package io.github.gaming32.uhcserver.commands;

import io.github.gaming32.uhcserver.UHCServerMod;
import io.github.gaming32.uhcserver.managers.CommandManager;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.network.ServerPlayPacketHandler;

public class PvP extends AbstractCommand {
    public PvP(CommandManager manager) {
        super(manager, "pvp");
    }

    @Override
    public void execute(String[] args, ServerPlayPacketHandler handler) {
        execute(args, (CommandSource) handler);
    }

    @Override
    public void execute(String[] args, CommandSource source) {
        if (args.length == 2) {
            UHCServerMod.getServer().allowPvp = args[1].equals("true") || args[1].equals("enable");
            source.sendFeedback("Test mode is now " + (UHCServerMod.TEST_MODE ? "enabled" : "disabled"));
        } else {
            UHCServerMod.getServer().allowPvp = !UHCServerMod.getServer().allowPvp;
            source.sendFeedback("Test mode is now " + (UHCServerMod.TEST_MODE ? "enabled" : "disabled"));
        }
    }

    @Override
    public boolean needsOp() {
        return true;
    }

    @Override
    public String getUsage(boolean hasOp, boolean isConsole) {
        return "[<true/false>]";
    }

}
