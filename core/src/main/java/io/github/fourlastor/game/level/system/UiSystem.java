package io.github.fourlastor.game.level.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import io.github.fourlastor.game.level.component.Player;
import io.github.fourlastor.game.ui.XpBar;

import javax.inject.Inject;
import javax.inject.Named;

public class UiSystem extends EntitySystem {
    private static final Family FAMILY_PLAYER = Family.all(Player.class).get();

    private static final Color DARK_GRAY = new Color(0x25282dff);
    private final Stage stage;
    private final BitmapFont bold;
    private final BitmapFont regular;
    private final ComponentMapper<Player> players;
    private final TextureAtlas textureAtlas;
    private Label timerLaberl;
    private XpBar bar;
    private ImmutableArray<Entity> playerEntities;

    @Inject
    public UiSystem(@Named("ui") Stage stage, AssetManager manager, ComponentMapper<Player> players, TextureAtlas textureAtlas) {
        this.stage = stage;
        bold = manager.get("fonts/play-bold.fnt", BitmapFont.class);
        regular = manager.get("fonts/play-regular.fnt", BitmapFont.class);
        this.players = players;
        this.textureAtlas = textureAtlas;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        timer = 0;
        timerLaberl = new Label("00:00", new Label.LabelStyle(bold, DARK_GRAY));
        timerLaberl.setPosition(stage.getWidth() / 2, stage.getHeight() - 40f, Align.center);
        stage.addActor(timerLaberl);
        bar = new XpBar(textureAtlas, regular);
        bar.setScale(0.95f * stage.getWidth() / bar.getWidth());

        float x = stage.getWidth() / 2 - bar.getWidth() * bar.getScaleX() / 2f;
        float y = stage.getHeight() - bar.getHeight() * bar.getScaleY();
        bar.setPosition(x, y);
        stage.addActor(bar);
        playerEntities = engine.getEntitiesFor(FAMILY_PLAYER);
        Image weapons = new Image(textureAtlas.findRegion("ui/weapons"));
        weapons.setPosition(x + 7, y - bar.getHeight() * bar.getScaleY() - 19);
        stage.addActor(weapons);
    }

    private float timer = 0f;
    private int lastSecond = -1;
    private int lastMinute = -1;

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        timer += deltaTime;
        int minutes = (int) (timer / 60);
        int seconds = (int) (timer % 60);
        if (lastMinute != minutes || lastSecond != seconds) {
            lastSecond = seconds;
            lastMinute = minutes;
            timerLaberl.setText(String.format("%02d:%02d", minutes, seconds));
        }
        stage.act(deltaTime);
        stage.draw();


        Entity playerEntity = getPlayer();
        if (playerEntity == null || !players.has(playerEntity)) {
            return;
        }
        Player player = players.get(playerEntity);
        float amount = player.xp / player.maxXp;
        bar.setAmount(amount);
    }

    private Entity getPlayer() {
        return playerEntities.size() > 0 ? playerEntities.get(0) : null;
    }
}
