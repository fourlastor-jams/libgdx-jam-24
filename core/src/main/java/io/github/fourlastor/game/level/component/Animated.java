package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;
import io.github.fourlastor.harlequin.ui.AnimatedImage;

public class Animated implements Component {
    public final AnimatedImage animation;

    public Animated(AnimatedImage animation) {
        this.animation = animation;
    }
}
