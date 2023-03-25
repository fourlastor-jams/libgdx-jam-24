package io.github.fourlastor.game.level.reward;

public enum RewardType {
    XP_S("xp-0"),
    XP_M("xp-1"),
    XP_L("xp-2"),
    PASTA("pasta"),
    ;
    public final String image;

    RewardType(String image) {
        this.image = image;
    }
}
