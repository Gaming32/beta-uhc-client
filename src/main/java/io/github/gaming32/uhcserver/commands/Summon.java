package io.github.gaming32.uhcserver.commands;

import io.github.gaming32.uhcserver.EntityMap;
import io.github.gaming32.uhcserver.Formatting;
import io.github.gaming32.uhcserver.UHCServerMod;
import io.github.gaming32.uhcserver.managers.CommandManager;
import net.minecraft.entity.Entity;
import net.minecraft.level.Level;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.network.ServerPlayPacketHandler;
import net.minecraft.server.player.ServerPlayer;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

public class Summon extends AbstractCommand {

    public Summon(CommandManager manager) {
        super(manager, "summon");
    }

    @Override
    public void execute(String[] args, ServerPlayPacketHandler handler) {
        if (args.length == 2) {
            Class<? extends Entity> type = EntityMap.entities.inverse().get(args[1].toLowerCase(Locale.ROOT).replace("_", " "));
            if (type == null) {
                handler.sendFeedback(Formatting.RED + "Unknown Entity!");
            } else {
                ServerPlayer player = UHCServerMod.getServer().playerManager.getPlayer(handler.getName());
                try {
                    summon(type, player.level, (int) player.x, (int) player.y, (int) player.z);
                } catch (InvocationTargetException | InstantiationException | NoSuchMethodException |
                         IllegalAccessException e) {
                    e.printStackTrace();
                    handler.sendFeedback(Formatting.RED + "Failed to spawn entity!");
                }
            }
        } else {
            handler.sendFeedback(Formatting.RED + "Invalid number of arguments; correct usage:" + Formatting.GRAY + " /summon " + getUsage(true, false));
        }
    }

    @Override
    public void execute(String[] args, CommandSource source) {
        source.sendFeedback(Formatting.RED + "This command is only available in-game!");
    }

    public void summon(Class<? extends Entity> type, Level level, int x, int y, int z) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Entity entity = type.getConstructor(Level.class).newInstance(level);
        entity.setPosition(x, y, z);
        level.spawnEntity(entity);
    }

    @Override
    public boolean needsOp() {
        return true;
    }

    @Override
    public String getUsage(boolean hasOp, boolean isConsole) {
        return isConsole ? "" : "<entity>";
    }

}
