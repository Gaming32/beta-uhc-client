package io.github.gaming32.uhcserver.commands;

import io.github.gaming32.uhcserver.Formatting;
import io.github.gaming32.uhcserver.UHCServerMod;
import io.github.gaming32.uhcserver.managers.CommandManager;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.network.ServerPlayPacketHandler;

public class TPCoords extends AbstractCommand {
    public TPCoords(CommandManager manager) {
        super(manager, "tpcoords");
    }

    @Override
    public void execute(String[] args, ServerPlayPacketHandler handler) {
        if (args.length == 4) {
            try {
                UHCServerMod.teleportPlayer(
                    handler.getName(),
                    Integer.parseInt(args[1]),
                    Integer.parseInt(args[2]),
                    Integer.parseInt(args[3])
                );
            } catch (NumberFormatException e) {
                handler.sendFeedback(Formatting.RED + "Invalid args; usage: " + Formatting.GRAY + " /tpcoords " + getUsage(true, false));
            }
        } else {
            handler.sendFeedback(Formatting.RED + "Invalid args; usage: " + Formatting.GRAY + " /tpcoords " + getUsage(true, false));
        }
    }

    @Override
    public void execute(String[] args, CommandSource source) {
        if (args.length == 5) {
            try {
                UHCServerMod.teleportPlayer(
                    args[1],
                    Integer.parseInt(args[2]),
                    Integer.parseInt(args[3]),
                    Integer.parseInt(args[4])
                );
            } catch (NumberFormatException e) {
                source.sendFeedback(Formatting.RED + "Invalid args; usage: " + Formatting.GRAY + "/tpcoords " + getUsage(true, true));
            }
        } else {
            source.sendFeedback(Formatting.RED + "Invalid args; usage: " + Formatting.GRAY + " /tpcoords " + getUsage(true, true));
        }
    }

    @Override
    public boolean needsOp() {
        return true;
    }

    @Override
    public String getUsage(boolean hasOp, boolean isConsole) {
        return isConsole? "<player> <x> <y> <z>" : "<x> <y> <z>";
    }

}
