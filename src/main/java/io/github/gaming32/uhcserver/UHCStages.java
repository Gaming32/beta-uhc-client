package io.github.gaming32.uhcserver;

public enum UHCStages {
    GRACE_PERIOD(3064, 3064, 600, false),
    FIRST(3064, 1532, 1800, true),
    SECOND(1532, 766, 1532-766, true),
    THIRD(766, 383, 900, true),
    FOURTH(383, 191, 800, true),
    FIFTH(191, 90, 700, true),
    SIX(90, 25, 500, true),
    FINAL(25, 10, 200, true),
    ;

    private int startSize;
    private int endSize;
    private int time;
    private boolean pvp;

    private UHCStages(int startSize, int endSize, int time, boolean pvp) {
        this.startSize = startSize;
        this.endSize = endSize;
        this.time = time * 20;
        this.pvp = pvp;
    }

    public int getStartSize() {
        return startSize;
    }

    public int getEndSize() {
        return endSize;
    }

    public int getTime() {
        return time;
    }

    public boolean isPvp() {
        return pvp;
    }
}
