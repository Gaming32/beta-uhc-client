package uhcserver;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.entity.*;
import net.minecraft.entity.animal.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.projectile.Arrow;
import net.minecraft.entity.projectile.Snowball;
import net.minecraft.entity.projectile.ThrownEgg;
import net.minecraft.entity.projectile.ThrownSnowball;
import net.minecraft.server.player.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

public class EntityMap {
    public static final BiMap<Class<? extends Entity>, String> entities;

    static {
        Map<Class<? extends Entity>, String> temp = new HashMap<>();
        temp.put(Boat.class, "boat");
        temp.put(FishHook.class, "fish hook");
        temp.put(Lightning.class, "lightning");
        temp.put(Minecart.class, "minecart");
        temp.put(PrimedTnt.class, "tnt");
        temp.put(Snowball.class, "snowball");
        temp.put(Chicken.class, "chicken");
        temp.put(Cow.class, "cow");
        temp.put(Pig.class, "pig");
        temp.put(Sheep.class, "sheep");
        temp.put(Squid.class, "squid");
        temp.put(Wolf.class, "wolf");
        temp.put(Creeper.class, "creeper");
        temp.put(Ghast.class, "ghast");
        temp.put(Skeleton.class, "skeleton");
        temp.put(Slime.class, "slime");
        temp.put(Spider.class, "spider");
        temp.put(Zombie.class, "zombie");
        temp.put(ZombiePigman.class, "zombie pigman");
        temp.put(Arrow.class, "arrow");
        temp.put(ThrownEgg.class, "egg");
        temp.put(ThrownSnowball.class, "thrown snowball");
        entities = ImmutableBiMap.copyOf(temp);
    }

    public static String getEntityName(Entity entity) {
        if (entity instanceof ServerPlayer) {
            return ((ServerPlayer) entity).name;
        } else {
            return entities.get(entity.getClass());
        }
    }

}
