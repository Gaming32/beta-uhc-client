package io.github.gaming32.uhcserver.commands;

import io.github.gaming32.uhcserver.UHCServerMod;
import io.github.gaming32.uhcserver.managers.CommandManager;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.network.ServerPlayPacketHandler;

public class Worldborder extends AbstractCommand {
    public Worldborder(CommandManager manager) {
        super(manager, "worldborder");
    }

    @Override
    public void execute(String[] args, ServerPlayPacketHandler handler) {
        execute(args, (CommandSource) handler);
    }

    @Override
    public void execute(String[] args, CommandSource source) {
        if (args.length == 1) {
            double worldBorderRadius = UHCServerMod.getWorldBorder().getRadius();
            source.sendFeedback(String.format("World border radius: %d", (int) worldBorderRadius));
        } else if (!nonOperatorCheckLog(source)) {
            try {
                if (args.length < 3) {
                    UHCServerMod.getWorldBorder().setRadius(Integer.parseInt(args[1]));
                } else {
                    UHCServerMod.getWorldBorder().setRadius(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                }
            } catch (NumberFormatException e) {
                source.sendFeedback("Invalid number; correct usage: /worldborder " + getUsage(true, !(source instanceof ServerPlayPacketHandler)));
            }
        }
    }
    @Override
    public boolean needsOp() {
        return false;
    }

    @Override
    public String getUsage(boolean hasOp, boolean isConsole) {
        return hasOp ? "[<radius> [<ticks>]]" : "";
    }

}
