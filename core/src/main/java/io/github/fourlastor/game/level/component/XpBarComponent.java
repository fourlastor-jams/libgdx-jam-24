package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import io.github.fourlastor.game.ui.XpBar;

public class XpBarComponent implements Component {
    public final XpBar bar;
    public final Entity player;

    public XpBarComponent(XpBar bar, Entity player) {
        this.bar = bar;
        this.player = player;
    }
}
