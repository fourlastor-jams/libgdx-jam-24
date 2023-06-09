package io.github.fourlastor.game.level.physics;

public class BodyData {

    public enum Category {
        PLAYER,
        ENEMY,
        REWARD,
        WEAPON,
        GROUND;
        public final short bits;

        Category() {
            this.bits = (short) (1 << ordinal());
        }
    }

    public enum Mask {
        PLAYER(Category.GROUND, Category.ENEMY, Category.REWARD),
        ENEMY(Category.PLAYER, Category.GROUND, Category.ENEMY, Category.WEAPON),
        REWARD(Category.PLAYER),
        WEAPON(Category.ENEMY),
        DISABLED();

        public final short bits;

        Mask(Category... categories) {
            short bits = 0;
            for (Category category : categories) {
                bits |= category.bits;
            }
            this.bits = bits;
        }
    }

    public enum Type {
        PLAYER,
        ENEMY,
        REWARD,
        WEAPON_FRONT,
        WEAPON_BACK,
        WEAPON_TOP,
        WEAPON_BOTTOM,
    }
}
