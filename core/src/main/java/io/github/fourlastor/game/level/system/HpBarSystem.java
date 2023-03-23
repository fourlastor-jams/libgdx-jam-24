package io.github.fourlastor.game.level.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.fourlastor.game.level.component.ActorComponent;
import io.github.fourlastor.game.level.component.HpBar;
import io.github.fourlastor.game.ui.Bar;

import javax.inject.Inject;

public class HpBarSystem extends IteratingSystem {

    private static final Family FAMILY = Family.all(HpBar.class).get();

    private final ComponentMapper<HpBar> hpBars;
    private final ComponentMapper<ActorComponent> actors;

    @Inject
    public HpBarSystem(ComponentMapper<HpBar> hpBars, ComponentMapper<ActorComponent> actors) {
        super(FAMILY);
        this.hpBars = hpBars;
        this.actors = actors;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HpBar hpBar = hpBars.get(entity);
        Bar bar = hpBar.bar;
        Actor playerActor = actors.get(hpBar.player).actor;
        float hPlayerW = playerActor.getWidth() * playerActor.getScaleX() / 2;
        float hPlayerH = playerActor.getHeight() * playerActor.getScaleY() / 2;
        float hBarW = bar.getWidth() * bar.getScaleX() / 2;
        float hBarH = bar.getHeight() * bar.getScaleY() / 2;
        float x = playerActor.getX() + hPlayerW - hBarW;
        float y = playerActor.getY() + hPlayerH - hBarH;
        bar.setPosition(x, y - 0.6f);
    }
}
