package io.github.gaming32.uhcserver;

import static io.github.gaming32.uhcserver.UHCServerMod.TEST_MODE;

public enum UHCStages {
    GRACE_PERIOD(3064, 600, false),
    FIRST(1532, 1800, true),
    SECOND(766, 1532-766, true),
    THIRD(383, 900, true),
    FOURTH(191, 800, true),
    FIFTH(90, 700, true),
    SIX(25, 500, true),
    FINAL(10, 200, true),
    ;

    private int endSize;
    private int time;
    private boolean pvp;

    private UHCStages(int endSize, int time, boolean pvp) {
        this.endSize = endSize;
        this.time = time * 20;
        this.pvp = pvp;
    }

    public int getEndSize() {
        if (TEST_MODE) return endSize >> 2;
        return endSize;
    }

    public int getTime() {
        if (TEST_MODE) return time >> 2;
        return time;
    }

    public boolean isPvp() {
        return pvp;
    }
}
