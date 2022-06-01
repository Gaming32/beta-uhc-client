package io.github.gaming32.uhcserver.commands;

import io.github.gaming32.uhcserver.Formatting;
import io.github.gaming32.uhcserver.UHCServerMod;
import io.github.gaming32.uhcserver.managers.CommandManager;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.network.ServerPlayPacketHandler;

import java.util.Set;

public class Team extends AbstractCommand {
    public Team(CommandManager manager) {
        super(manager, "team");
    }

    @Override
    public void execute(String[] args, ServerPlayPacketHandler handler) {
        execute(args, (CommandSource) handler);
    }

    @Override
    public void execute(String[] args, CommandSource source) {
        if (args.length < 2) {
            source.sendFeedback(Formatting.RED + "Invalid args; usage: " + Formatting.GRAY + " /team " + getUsage(true, !(source instanceof ServerPlayPacketHandler)));
        }
        switch (args[1]) {
            case "add":
                if (args.length != 4) {
                    source.sendFeedback(Formatting.RED + "Invalid args; usage: " + Formatting.GRAY + " /team add <player> <team>");
                } else {
                    if (UHCServerMod.getTeamManager().setTeam(args[2], args[3])) {
                        source.sendFeedback(Formatting.GREEN + "Player " + Formatting.GRAY + args[2] + Formatting.GREEN + " has been added to team " + Formatting.GRAY + args[3]);
                    } else {
                        source.sendFeedback(Formatting.RED + "failed to add player " + Formatting.GRAY + args[2] + Formatting.RED + " to team " + Formatting.GRAY + args[3]);
                        if (!UHCServerMod.getTeamManager().teams().contains(args[2])) {
                            source.sendFeedback(Formatting.RED + "Team " + Formatting.GRAY + args[3] + Formatting.RED + " does not exist");
                        }
                    }
                }
                break;
            case "remove":
                if (args.length != 3) {
                    source.sendFeedback(Formatting.RED + "Invalid args; usage: " + Formatting.GRAY + " /team remove <player>");
                } else {
                    if (UHCServerMod.getTeamManager().removePlayer(args[2])) {
                        source.sendFeedback(Formatting.GREEN + "Player " + Formatting.GRAY + args[2] + Formatting.GREEN + " has been removed from team");
                    } else {
                        source.sendFeedback(Formatting.RED + "failed to remove player " + Formatting.GRAY + args[2] + Formatting.RED + " from team");
                        if (UHCServerMod.getTeamManager().getTeamForPlayer(args[2]) == null) {
                            source.sendFeedback(Formatting.RED + "Player " + Formatting.GRAY + args[2] + Formatting.RED + " is not in a team");
                        }
                    }
                }
                break;
            case "list":
                if (args.length == 3) {
                    Set<String> players = UHCServerMod.getTeamManager().getTeam(args[2]);
                    if (players == null) {
                        source.sendFeedback(Formatting.RED + "Team " + Formatting.GRAY + args[2] + Formatting.RED + " does not exist");
                    } else {
                        source.sendFeedback(Formatting.GREEN + "Players in team " + Formatting.GRAY + args[2] + Formatting.GREEN + ":");
                        for (String player : players) {
                            source.sendFeedback(Formatting.GRAY + player);
                        }
                    }
                } else if (args.length == 2) {
                    source.sendFeedback(Formatting.GREEN + "Teams:");
                    for (String team : UHCServerMod.getTeamManager().teams()) {
                        source.sendFeedback(Formatting.GRAY + team);
                    }
                } else {
                    source.sendFeedback(Formatting.RED + "Invalid args; usage: " + Formatting.GRAY + " /team list");
                }
                break;
            case "create":
                if (args.length != 3) {
                    source.sendFeedback(Formatting.RED + "Invalid args; usage: " + Formatting.GRAY + " /team create <team>");
                } else {
                    if (UHCServerMod.getTeamManager().addTeam(args[2])) {
                        source.sendFeedback(Formatting.GREEN + "Team " + Formatting.GRAY + args[2] + Formatting.GREEN + " has been created");
                    } else {
                        source.sendFeedback(Formatting.RED + "failed to create team " + Formatting.GRAY + args[2]);
                        if (UHCServerMod.getTeamManager().teams().contains(args[2])) {
                            source.sendFeedback(Formatting.RED + "Team " + Formatting.GRAY + args[2] + Formatting.RED + " already exists");
                        }
                    }
                }
                break;
            case "delete":
                if (args.length != 3) {
                    source.sendFeedback(Formatting.RED + "Invalid args; usage: " + Formatting.GRAY + " /team delete <team>");
                } else {
                    if (UHCServerMod.getTeamManager().removeTeam(args[2])) {
                        source.sendFeedback(Formatting.GREEN + "Team " + Formatting.GRAY + args[2] + Formatting.GREEN + " has been deleted");
                    } else {
                        source.sendFeedback(Formatting.RED + "failed to delete team " + Formatting.GRAY + args[2]);
                        if (!UHCServerMod.getTeamManager().teams().contains(args[2])) {
                            source.sendFeedback(Formatting.RED + "Team " + Formatting.GRAY + args[2] + Formatting.RED + " does not exist");
                        }
                    }
                }
                break;
            case "freeze":
                if (args.length == 2) {
                    UHCServerMod.getTeamManager().freeze();
                    source.sendFeedback(Formatting.GREEN + "Teams are now frozen");
                } else if (args.length == 3) {
                    if (args[2].equalsIgnoreCase("true")) {
                        UHCServerMod.getTeamManager().freeze();
                        source.sendFeedback(Formatting.GREEN + "Teams are now frozen");
                    } else if (args[2].equalsIgnoreCase("false")) {
                        UHCServerMod.getTeamManager().unfreeze();
                        source.sendFeedback(Formatting.GREEN + "Teams are now unfrozen");
                    } else {
                        source.sendFeedback(Formatting.RED + "Invalid args; usage: " + Formatting.GRAY + " /team freeze [<true/false>]");
                    }
                } else {
                    source.sendFeedback(Formatting.RED + "Invalid args; usage: " + Formatting.GRAY + " /team freeze [<true/false>]");
                }

        }
    }

    @Override
    public boolean needsOp() {
        return true;
    }

    @Override
    public String getUsage(boolean hasOp, boolean isConsole) {
        return "[add <team> <player>] | [remove <player>] | [list [<team>]] | [create <team>] | [delete <team>]";
    }

}
