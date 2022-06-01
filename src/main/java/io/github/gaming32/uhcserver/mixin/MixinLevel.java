package io.github.gaming32.uhcserver.mixin;

import io.github.gaming32.uhcserver.UHCServerMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.Player;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(Level.class)
public class MixinLevel {

    @Shadow public List<Player> players;

    /**
     * @author wagyourtail
     * @reason to fix spectator targeting
     */
    @Overwrite
    public Player getClosestPlayerTo(Entity d, double par2) {
        double distSq[] = new double[] { -1 };
        Player closest[] = new Player[] { null };

        players.stream().filter(e -> !UHCServerMod.getSpectatorManager().isSpectator(e.name)).forEach(p -> {
            double dSq = p.squaredDistanceTo(d.x, d.y, d.z);
            if ((par2 < 0 || dSq < par2 * par2) && (distSq[0] == -1 || dSq < distSq[0])) {
                distSq[0] = dSq;
                closest[0] = p;
            }
        });

        return closest[0];
    }

}
