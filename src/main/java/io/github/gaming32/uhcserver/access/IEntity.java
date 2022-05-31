package io.github.gaming32.uhcserver.access;

import net.minecraft.entity.Entity;
import io.github.gaming32.uhcserver.DamageSource;

public interface IEntity {

    DamageSource getDamageCause();

    void setDamageCause(DamageSource cause);

    Entity getDamagedBy();

    long getDamagedByTime();
}
