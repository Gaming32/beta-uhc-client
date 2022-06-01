package io.github.gaming32.uhcserver.managers;

import com.google.common.collect.ImmutableSet;
import io.github.gaming32.uhcserver.Formatting;
import io.github.gaming32.uhcserver.UHCServerMod;
import net.minecraft.packet.play.ChatMessagePacket;
import net.minecraft.server.player.ServerPlayer;

import java.util.*;

public class TeamManager {

    private final Set<String> teams = new HashSet<>();

    private final Map<String, String> playerToTeams = new HashMap<>();

    private final Map<String, Set<String>> teamsToPlayers = new HashMap<>();

    private boolean frozen = false;

    public synchronized boolean addTeam(String team) {
        if (frozen) return false;
        if (teams.contains(team)) return false;
        teams.add(team);
        return true;
    }

    public synchronized boolean removeTeam(String team) {
        if (frozen) return false;
        if (!teams.contains(team)) return false;
        teams.remove(team);
        teamsToPlayers.get(team).forEach(player -> {
            playerToTeams.remove(player);
            updatePlayer(player);
        });
        teamsToPlayers.remove(team);
        return true;
    }

    public synchronized boolean setTeam(String team, String player) {
        if (frozen) return false;
        if (teams.contains(team)) {
            if (playerToTeams.containsKey(player)) {
                teamsToPlayers.get(playerToTeams.get(player)).remove(player);
            }
            playerToTeams.put(player, team);
            teamsToPlayers.computeIfAbsent(team, k -> new HashSet<>()).add(player);
            updatePlayer(player);
            return true;
        }
        return false;
    }

    public synchronized boolean removePlayer(String player) {
        if (frozen) return false;
        if (playerToTeams.containsKey(player)) {
            String team = playerToTeams.get(player);
            teamsToPlayers.get(team).remove(player);
            playerToTeams.remove(player);
            updatePlayer(player);
            return true;
        }
        return false;
    }

    public synchronized Set<String> getTeam(String team) {
        Set<String> teamS = teamsToPlayers.getOrDefault(team, null);
        if (teamS != null) {
            return ImmutableSet.copyOf(teamS);
        }
        return null;
    }

    public synchronized String getTeamForPlayer(String player) {
        return playerToTeams.get(player);
    }

    public Set<String> teams() {
        return teams;
    }

    public synchronized void freeze() {
        frozen = true;
    }

    public synchronized void unfreeze() {
        frozen = false;
    }


    public synchronized void sendAllTeams(ServerPlayer player) {
        for (Map.Entry<String, Set<String>> team : teamsToPlayers.entrySet()) {
            String teamName = team.getKey();
            Set<String> players = team.getValue();
            for (String playerName : players) {
                player.packetHandler.send(new ChatMessagePacket("canyonuhc:displayname " + Formatting.GRAY + "[" + Formatting.GREEN + teamName + Formatting.GRAY + "] " + Formatting.WHITE + playerName));
            }
        }
    }

    private void updatePlayer(String player) {
        UHCServerMod.getServer().playerManager.sendPacketToAll(new ChatMessagePacket("canyonuhc:displayname " + Formatting.GRAY + "[" + Formatting.GREEN + playerToTeams.get(player) + Formatting.GRAY + "] " + Formatting.WHITE + player));
    }


    public String formatPlayer(String player) {
        String team = playerToTeams.get(player);
        if (team == null) return player;
        return Formatting.GRAY + "[" + Formatting.GREEN + team + Formatting.GRAY + "] " + Formatting.WHITE + player;
    }
}
