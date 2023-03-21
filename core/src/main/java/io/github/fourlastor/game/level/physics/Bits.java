package io.github.fourlastor.game.level.physics;

public class Bits {

    public enum Category {
        PLAYER,
        ENEMY,
        GROUND;
        public final short bits;

        Category() {
            this.bits = (short) (1 << ordinal());
        }
    }

    public enum Mask {
        PLAYER(Category.GROUND, Category.ENEMY),
        ENEMY(Category.PLAYER, Category.GROUND, Category.ENEMY),
        GROUND(Category.PLAYER, Category.ENEMY)
        ;

        public final short bits;

        Mask(Category... categories) {
            short bits = 0;
            for (Category category : categories) {
                bits |= category.bits;
            }
            this.bits = bits;
        }
    }
}
