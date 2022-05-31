package io.github.gaming32.uhcserver.managers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.ItemType;
import net.minecraft.level.Level;
import net.minecraft.server.command.Command;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.network.ServerPlayPacketHandler;
import net.minecraft.server.player.ServerPlayer;
import io.github.gaming32.uhcserver.EntityMap;
import io.github.gaming32.uhcserver.Formatting;
import io.github.gaming32.uhcserver.UHCServerMod;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public class CommandManager {

    public boolean processCommand(Command command) {
        String[] args = command.commandString.split(" ");
        switch (args[0]) {
            case "reset-uhc":
                if (nonOperatorCheckLog(command.source)) return true;
                UHCServerMod.getStateManager().endUHC();
                return true;
            case "start-uhc":
                if (nonOperatorCheckLog(command.source)) return true;
                try {
                    UHCServerMod.getStateManager().startUHC();
                } catch (IllegalArgumentException e) {
                    command.source.sendFeedback(e.getMessage());
                }
                return true;
            case "worldborder":
                if (args.length < 2) {
                    double worldBorderRadius = UHCServerMod.getWorldBorder().getRadius();
                    command.source.sendFeedback(String.format("World border radius: %d", (int) worldBorderRadius));
                    if (command.source instanceof ServerPlayPacketHandler) UHCServerMod.getWorldBorder().onPlayerJoin(UHCServerMod.getServer().playerManager.getPlayer(command.source.getName()));
                } else {
                    if (nonOperatorCheckLog(command.source)) return true;
                    try {
                        if (args.length < 3) {
                            UHCServerMod.getWorldBorder().setRadius(Integer.parseInt(args[1]));
                        } else {
                            UHCServerMod.getWorldBorder().setRadius(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                        }
                    } catch (NumberFormatException e) {
                        command.source.sendFeedback("Invalid number; correct usage: /worldborder <radius> [<ticks>]");
                    }
                }
                return true;
            case "glow":
                if (nonOperatorCheckLog(command.source)) return true;
                if (args.length < 2) {
                    if (command.source instanceof ServerPlayPacketHandler) {
                        UHCServerMod.setGlow(command.source.getName());
                    } else {
                        command.source.sendFeedback("Player argument required");
                    }
                } else {
                    UHCServerMod.setGlow(args[1]);
                }
                return true;
            case "spectator":
                if (nonOperatorCheckLog(command.source)) return true;
                if (args.length < 2) {
                    if (command.source instanceof ServerPlayPacketHandler) {
                        UHCServerMod.setSpectator(command.source.getName());
                    } else {
                        command.source.sendFeedback("Player argument required");
                    }
                } else {
                    UHCServerMod.setSpectator(args[1]);
                }
                return true;
            case "tpcoords":
                if (nonOperatorCheckLog(command.source)) return true;
                if (args.length < 4) {
                    command.source.sendFeedback("Invalid number of arguments; correct usage: /tpcoords <x> <y> <z>");
                } else {
                    try {
                        if (command.source instanceof ServerPlayPacketHandler) {
                            UHCServerMod.teleportPlayer(command.source.getName(), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                        } else {
                            command.source.sendFeedback("Error: must be run as a player");
                        }
                    } catch (NumberFormatException e) {
                        command.source.sendFeedback("Invalid number; correct usage: /tpcoords <x> <y> <z>");
                    }
                }
                return true;
            case "lightning":
                if (nonOperatorCheckLog(command.source)) return true;
                if (args.length < 2) {
                    command.source.sendFeedback("Invalid number of arguments; correct usage: /lightning <player>");
                } else {
                    //TODO: fix
//                    UHCServerMod.getServer().playerManager.getPlayer(args[1]).level.strikeLightning(UHCServerMod.getServer().getPlayerManager().getPlayer(args[1]).getWorld().getPos());
                }
                return true;
            case "give":
                if (nonOperatorCheckLog(command.source)) return true;
                if (args.length < 3) {
                    command.source.sendFeedback("Invalid number of arguments; correct usage: /give <item> <count>");
                } else {
                    ServerPlayer player = UHCServerMod.getServer().playerManager.getPlayer(command.source.getName());
                    Optional<ItemType> type = Arrays.stream(ItemType.byId).filter(Objects::nonNull).filter(e -> e.getTranslatedName().toLowerCase(Locale.ROOT).replace(" ", "_").equals(args[1])).findFirst();
                    if (!type.isPresent()) {
                        command.source.sendFeedback("Unknown Item!");
                    } else {
                        try {
                            player.level.spawnEntity(new ItemEntity(
                                player.level,
                                player.x,
                                player.y,
                                player.z,
                                new ItemInstance(type.get(), Integer.parseInt(args[2]))
                            ));
                        } catch (NumberFormatException ex) {
                            command.source.sendFeedback("Invalid number; correct usage: /give <item> <count>");
                        }
                    }
                    return true;
                }
            case "summon":
                if (nonOperatorCheckLog(command.source)) return true;
                if (args.length < 2) {
                    command.source.sendFeedback("Invalid number of arguments; correct usage: /summon <entity>");
                } else {
                    ServerPlayer player = UHCServerMod.getServer().playerManager.getPlayer(command.source.getName());
                    Class<? extends Entity> type = EntityMap.entities.inverse().get(args[1].toLowerCase(Locale.ROOT).replace("_", " "));
                    if (type == null) {
                        command.source.sendFeedback("Unknown Entity!");
                    } else {
                        try {
                            Entity entity = type.getConstructor(Level.class).newInstance(player.level);
                            entity.setPosition(player.x, player.y, player.z);
                            player.level.spawnEntity(entity);
                        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                                 NoSuchMethodException e) {
                            e.printStackTrace();
                            command.source.sendFeedback("Failed to spawn entity!");
                        }
                    }
                    return true;
                }
                return true;
            case "help":
                boolean nonOp = nonOperatoreCheck(command.source);
                if (nonOp) {
                    command.source.sendFeedback(
                        Formatting.WHITE + "/worldborder" + Formatting.GRAY + " - Get the world border radius");
                } else {
                    command.source.sendFeedback(Formatting.WHITE + "/worldborder [<radius> [<ticks>]] " + Formatting.GRAY + " - Get or set the world border radius");
                    command.source.sendFeedback(Formatting.WHITE + "/glow [<player>] " + Formatting.GRAY + " - Set the glow of a player");
                    command.source.sendFeedback(Formatting.WHITE + "/spectator [<player>] " + Formatting.GRAY + " - Set the spectator of a player");
                    command.source.sendFeedback(Formatting.WHITE + "/tpcoords <x> <y> <z> " + Formatting.GRAY + " - Teleport a player to a coordinate");
                    command.source.sendFeedback(Formatting.WHITE + "/lightning [<player>] " + Formatting.GRAY + " - Strike lightning at a player");
                    command.source.sendFeedback(Formatting.WHITE + "/give <item> <count> " + Formatting.GRAY + " - Give a player an item");
                    command.source.sendFeedback(Formatting.WHITE + "/summon <entity> " + Formatting.GRAY + " - Summon an entity");
                }
                if (nonOp) return true;

        }
        return false;
    }

    public boolean nonOperatorCheckLog(CommandSource source) {
        if (nonOperatoreCheck(source)) {
            source.sendFeedback(Formatting.RED + "You must be an operator to use this command.");
            return true;
        }
        return false;
    }

    public boolean nonOperatoreCheck(CommandSource source) {
        if (source instanceof ServerPlayPacketHandler) {
            return !UHCServerMod.getServer().playerManager.isOperator(source.getName());
        } else {
            return false;
        }
    }
}
