package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import io.github.fourlastor.game.ui.Bar;

public class HpBar implements Component {
    public final Bar bar;
    public final Entity player;

    public HpBar(Bar bar, Entity player) {
        this.bar = bar;
        this.player = player;
    }
}
