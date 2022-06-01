package io.github.gaming32.uhcserver.commands;

import io.github.gaming32.uhcserver.Formatting;
import io.github.gaming32.uhcserver.UHCServerMod;
import io.github.gaming32.uhcserver.managers.CommandManager;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.network.ServerPlayPacketHandler;

public class Difficulty extends AbstractCommand{
    public Difficulty(CommandManager manager) {
        super(manager, "difficulty");
    }

    @Override
    public void execute(String[] args, ServerPlayPacketHandler handler) {

    }

    @Override
    public void execute(String[] args, CommandSource source) {
        try {
            int diff = Integer.parseInt(args[1]);
            if (diff < 0 || diff > 3) {
                source.sendFeedback(Formatting.RED + "Invalid difficulty; correct usage: " + Formatting.GRAY + "/difficulty " + getUsage(true, true));
                return;
            }
            UHCServerMod.getServer().levels[0].difficulty = diff;
            UHCServerMod.getServer().levels[1].difficulty = diff;
            source.sendFeedback("Difficulty set to " + diff);
        } catch (NumberFormatException e) {
            source.sendFeedback(Formatting.RED + "Invalid difficulty; correct usage: /difficulty " + getUsage(true, true));
        }
    }

    @Override
    public boolean needsOp() {
        return true;
    }

    @Override
    public String getUsage(boolean hasOp, boolean isConsole) {
        return "<number>";
    }

}
