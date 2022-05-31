package uhcserver;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.packet.play.ChatMessagePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.player.ServerPlayer;

import java.util.logging.Logger;


public class UHCServerMod implements DedicatedServerModInitializer {
    public static final boolean TEST_MODE = Boolean.getBoolean("uhc.testMode");
    public static final Logger LOGGER = Logger.getLogger("UHC");
    private static CommandManager commandManager;
    private static UHCStateManager stateManager;
    private static WorldBorder worldBorder;
    private static MinecraftServer server;

    @Override
    public void onInitializeServer() {
        LOGGER.info("Server Mod initialized");
    }

    public static void handleCustomPacket(ServerPlayer player, String messageType, String data) {

    }

    public static void sendCustomPacket(String messageType) {
        server.playerManager.sendPacketToAll(new ChatMessagePacket("canyonuhc:" + messageType));
    }

    public static void sendCustomPacket(String messageType, String data) {
        server.playerManager.sendPacketToAll(new ChatMessagePacket("canyonuhc:" + messageType + " " + data));
    }

    public static void updateServer(MinecraftServer server) {
        UHCServerMod.server = server;
        commandManager = new CommandManager();
        stateManager = new UHCStateManager();
        worldBorder = new WorldBorder();
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }

    public static UHCStateManager getStateManager() {
        return stateManager;
    }

    public static WorldBorder getWorldBorder() {
        return worldBorder;
    }

    public static MinecraftServer getServer() {
        return server;
    }

    public static void setGlow(String player) {

    }

    public static void setSpectator(String player) {

    }

    public static void teleportPlayer(String player, double x, double y, double z) {
        server.playerManager.getPlayer(player).packetHandler.method_832(x, y, z, 0.0F, 0.0F);

    }

    public static void onDeath(ServerPlayer entity, Entity killer) {
        String message = entity.name;
        if (killer == null) {
           switch (((IEntity) entity).getDamageCause()) {
               case BURN:
                   message += " burned to death";
                   break;
               case FIRE:
                   message += " stood in fire for too long";
                   break;
               case LAVA:
                   message += " melted";
                   break;
               case VOID:
                   message += " fell out of the world";
                   break;
               case FALL:
                   message += " hit the ground too hard";
                   break;
               case SUFFOCATION:
                   message += " suffocated in a wall";
                   break;
               case DROWN:
                   message += " drowned";
                   break;
               case CACTUS:
                   message += " was pricked to death by a cactus";
                   break;
               case WORLD_BORDER:
                   message += " died to the world border";
                   break;
           }
           if (System.currentTimeMillis() - ((IEntity) entity).getDamagedByTime() < 10000) {
               killer = ((IEntity) entity).getDamagedBy();
               message += " while trying to escape " + EntityMap.getEntityName(killer);
           }
        } else {
            message += " was slain by " + EntityMap.getEntityName(killer);
            if (killer instanceof ServerPlayer) {
                message += " using a " + I18n.translate(((ServerPlayer) killer).inventory.getHeldItem().getTranslationKey());
            }
        }
        server.playerManager.sendPacketToAll(new ChatMessagePacket(message + "."));
    }

    public static void tick() {
        stateManager.tick();
        worldBorder.tick();
    }

    public static void onPlayerJoin(ServerPlayer player) {
        worldBorder.onPlayerJoin(player);
    }
}
