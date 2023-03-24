package io.github.fourlastor.game.level.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import io.github.fourlastor.game.level.component.Player;
import io.github.fourlastor.game.level.component.XpBarComponent;
import io.github.fourlastor.game.ui.XpBar;
import javax.inject.Inject;

public class XpBarSystem extends IteratingSystem {

    private static final Family FAMILY = Family.all(XpBarComponent.class).get();

    private final ComponentMapper<XpBarComponent> bars;
    private final ComponentMapper<Player> players;
    private final Camera camera;

    @Inject
    public XpBarSystem(ComponentMapper<XpBarComponent> bars, ComponentMapper<Player> players, Camera camera) {
        super(FAMILY);
        this.bars = bars;
        this.players = players;
        this.camera = camera;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        XpBarComponent xpBarComponent = bars.get(entity);
        XpBar bar = xpBarComponent.bar;
        float x = camera.position.x - bar.getWidth() * bar.getScaleX() / 2f;
        float y = camera.position.y + camera.viewportHeight / 2f - bar.getHeight() * bar.getScaleY();
        bar.setPosition(x, y);
        Entity playerEntity = xpBarComponent.player;
        if (!players.has(playerEntity)) {
            return;
        }
        float amount = players.get(playerEntity).xp / 1000f;
        System.out.println(amount);
        bar.setAmount(amount);
    }
}
