package io.github.gaming32.uhcserver.managers;

import io.github.gaming32.uhcserver.commands.*;
import net.minecraft.server.command.Command;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class CommandManager {

    private final Map<String, AbstractCommand> commandMap = new HashMap<>();

    public CommandManager() {
        registerCommand(Difficulty::new);
        registerCommand(Give::new);
        registerCommand(Glow::new);
        registerCommand(Help::new);
        registerCommand(Lightning::new);
        registerCommand(PvP::new);
        registerCommand(ResetUHC::new);
        registerCommand(Spectator::new);
        registerCommand(StartUHC::new);
        registerCommand(Summon::new);
        registerCommand(Team::new);
        registerCommand(TestMode::new);
        registerCommand(TPCoords::new);
        registerCommand(Worldborder::new);
    }

    public Collection<AbstractCommand> getCommands() {
        return commandMap.values();
    }

    public void registerCommand(Function<CommandManager, AbstractCommand> command) {
        AbstractCommand abstractCommand = command.apply(this);
        commandMap.put(abstractCommand.name, abstractCommand);
    }

    public void registerCommand(AbstractCommand command) {
        commandMap.put(command.name, command);
    }

    public boolean processCommand(Command command) {
        String[] args = command.commandString.split(" ");
        Optional<AbstractCommand> cmd = Optional.ofNullable(commandMap.get(args[0]));
        cmd.ifPresent(e -> e.execute(command));
        return cmd.isPresent() && (!cmd.get().name.equals("help") || AbstractCommand.nonOperatoreCheck(command.source));
    }
}
