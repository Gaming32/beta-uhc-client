package uhcserver;

import net.minecraft.entity.Entity;

public interface IEntity {

    NullDamageCause getDamageCause();

    void setDamageCause(NullDamageCause cause);

    Entity getDamagedBy();

    long getDamagedByTime();
}
