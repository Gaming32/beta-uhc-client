package io.github.gaming32.uhcserver.commands;

import io.github.gaming32.uhcserver.Formatting;
import io.github.gaming32.uhcserver.UHCServerMod;
import io.github.gaming32.uhcserver.managers.CommandManager;
import net.minecraft.server.command.Command;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.network.ServerPlayPacketHandler;
import net.minecraft.server.player.ServerPlayer;

import java.util.Optional;

public abstract class AbstractCommand {

    protected final CommandManager commandManager;
    public final String name;

    public AbstractCommand(CommandManager manager, String name) {
        commandManager = manager;
        this.name = name;
    }

    public static boolean nonOperatorCheckLog(CommandSource source) {
        if (nonOperatoreCheck(source)) {
            source.sendFeedback(Formatting.RED + "You must be an operator to use this command.");
            return true;
        }
        return false;
    }

    public static boolean nonOperatoreCheck(CommandSource source) {
        if (source instanceof ServerPlayPacketHandler) {
            return !UHCServerMod.getServer().playerManager.isOperator(source.getName());
        } else {
            return false;
        }
    }

    public void execute(Command command) {
        if (needsOp() && nonOperatorCheckLog(command.source)) {
            return;
        }
        String[] args = command.commandString.split(" ");
        if (command.source instanceof ServerPlayPacketHandler) {
            execute(args, (ServerPlayPacketHandler) command.source);
        } else {
            execute(args, command.source);
        }
    }

    public Optional<ServerPlayer> getPlayer(String name) {
        return Optional.ofNullable(UHCServerMod.getServer().playerManager.getPlayer(name));
    }

    public abstract void execute(String[] args, ServerPlayPacketHandler handler);

    public abstract void execute(String[] args, CommandSource source);

    public abstract boolean needsOp();

    public abstract String getUsage(boolean hasOp, boolean isConsole);
}
