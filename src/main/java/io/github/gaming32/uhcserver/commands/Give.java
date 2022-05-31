package io.github.gaming32.uhcserver.commands;

import io.github.gaming32.uhcserver.Formatting;
import io.github.gaming32.uhcserver.UHCServerMod;
import io.github.gaming32.uhcserver.managers.CommandManager;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.ItemType;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.network.ServerPlayPacketHandler;
import net.minecraft.server.player.ServerPlayer;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public class Give extends AbstractCommand {

    public Give(CommandManager manager) {
        super(manager, "give");
    }

    @Override
    public void execute(String[] args, ServerPlayPacketHandler handler) {
        if (args.length == 3) {
            Optional<ItemType> type = Arrays.stream(ItemType.byId).filter(Objects::nonNull).filter(e -> e.getTranslatedName().toLowerCase(
                Locale.ROOT).replace(" ", "_").equals(args[1])).findFirst();
            if (!type.isPresent()) {
                handler.sendFeedback("Unknown Item!");
            } else {
                try {
                    int count = Integer.parseInt(args[2]);
                    give(UHCServerMod.getServer().playerManager.getPlayer(handler.getName()), type.get(), count);
                } catch (NumberFormatException ex) {
                    handler.sendFeedback(Formatting.RED + "Invalid number; correct usage: " +
                        Formatting.GRAY + "/give " + getUsage(true, false));
                }
            }
        }
    }

    @Override
    public void execute(String[] args, CommandSource source) {
        if (args.length != 4) {
            source.sendFeedback(Formatting.RED + "Invalid args; usage: " + Formatting.GRAY + " /give " + getUsage(true, !(source instanceof ServerPlayPacketHandler)));
        }
        Optional<ServerPlayer> player = getPlayer(args[1]);
        if (!player.isPresent()) {
            source.sendFeedback("Unknown player!");
        } else {
            Optional<ItemType> type = Arrays.stream(ItemType.byId).filter(Objects::nonNull).filter(e -> e.getTranslatedName().toLowerCase(
                Locale.ROOT).replace(" ", "_").equals(args[2])).findFirst();
            if (!type.isPresent()) {
                source.sendFeedback("Unknown Item!");
            } else {
                try {
                    int count = Integer.parseInt(args[3]);
                    give(player.get(), type.get(), count);
                } catch (NumberFormatException ex) {
                    source.sendFeedback(Formatting.RED + "Invalid number; correct usage: " + Formatting.GRAY + " /give " + getUsage(true, false));
                }
            }
        }
    }

    protected void give(ServerPlayer player, ItemType type, int count) {
        player.level.spawnEntity(new ItemEntity(
            player.level,
            player.x,
            player.y,
            player.z,
            new ItemInstance(type, count)
        ));
    }

    @Override
    public boolean needsOp() {
        return true;
    }

    @Override
    public String getUsage(boolean hasOp, boolean isConsole) {
        return isConsole ? "<player> <item> <amount>" : "[<player>] <item> <amount>";
    }

}
