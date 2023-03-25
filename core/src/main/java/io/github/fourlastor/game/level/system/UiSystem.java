package io.github.fourlastor.game.level.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.github.tommyettinger.textra.Font;
import com.github.tommyettinger.textra.TextraLabel;
import io.github.fourlastor.game.level.Message;
import io.github.fourlastor.game.level.component.Player;
import io.github.fourlastor.game.route.Router;
import io.github.fourlastor.game.ui.XpBar;
import javax.inject.Inject;
import javax.inject.Named;

public class UiSystem extends EntitySystem implements Telegraph {
    private static final Family FAMILY_PLAYER = Family.all(Player.class).get();

    private static final Color DARK_GRAY = new Color(0x25282dff);
    private final Stage stage;
    private final BitmapFont bold;
    private final BitmapFont regular;
    private final ComponentMapper<Player> players;
    private final TextureAtlas textureAtlas;
    private final MessageDispatcher dispatcher;
    private final InputMultiplexer inputMultiplexer;
    private final Router router;
    private final RetryProcessor retryProcessor = new RetryProcessor();
    private Label timerLaberl;
    private XpBar bar;
    private ImmutableArray<Entity> playerEntities;
    private TextraLabel killLabel;
    private Image gameOver;
    private boolean inGameOver = false;

    @Inject
    public UiSystem(
            @Named("ui") Stage stage,
            AssetManager manager,
            ComponentMapper<Player> players,
            TextureAtlas textureAtlas,
            MessageDispatcher dispatcher,
            InputMultiplexer inputMultiplexer,
            Router router) {
        this.stage = stage;
        bold = manager.get("fonts/play-bold.fnt", BitmapFont.class);
        regular = manager.get("fonts/play-regular.fnt", BitmapFont.class);
        this.players = players;
        this.textureAtlas = textureAtlas;
        this.dispatcher = dispatcher;
        this.inputMultiplexer = inputMultiplexer;
        this.router = router;
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
        weapons.setPosition(stage.getWidth() - x - 57, y - bar.getHeight() * bar.getScaleY() - 19);
        stage.addActor(weapons);
        Image killIcon = new Image(textureAtlas.findRegion("ui/kill_counter"));
        killIcon.setPosition(x + 7, y - bar.getHeight() * bar.getScaleY() - 7 + 3);
        stage.addActor(killIcon);
        this.killLabel = new TextraLabel("123", new Font(regular).scale(0.4f, 0.4f));
        killLabel.setPosition(x + 25, y - bar.getHeight() * bar.getScaleY() + 6 + 3);
        killLabel.setColor(DARK_GRAY);
        stage.addActor(killLabel);
        dispatcher.addListener(this, Message.GAME_OVER.ordinal());
        inputMultiplexer.addProcessor(retryProcessor);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        inputMultiplexer.removeProcessor(retryProcessor);
        super.removedFromEngine(engine);
    }

    private float timer = 0f;
    private int lastSecond = -1;
    private int lastMinute = -1;

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Entity playerEntity = getPlayer();
        if (playerEntity == null || !players.has(playerEntity)) {
            return;
        }
        Player player = players.get(playerEntity);
        if (player.hp > 0) {
            timer += deltaTime;
        }
        int minutes = (int) (timer / 60);
        int seconds = (int) (timer % 60);
        if (lastMinute != minutes || lastSecond != seconds) {
            lastSecond = seconds;
            lastMinute = minutes;
            String text = (minutes > 10 ? minutes : "0" + minutes) + ":" + (seconds > 10 ? seconds : "0" + seconds);
            timerLaberl.setText(text);
        }
        stage.act(deltaTime);
        stage.draw();
        float amount = player.xp / player.maxXp;
        bar.setAmount(amount);
        killLabel.setText(String.valueOf(player.killCounter));
        gameOver = new Image(textureAtlas.findRegion("ui/game_over"));
        gameOver.setPosition(stage.getWidth() / 2, stage.getHeight() / 2, Align.center);
        gameOver.setVisible(false);
        stage.addActor(gameOver);
    }

    private Entity getPlayer() {
        return playerEntities.size() > 0 ? playerEntities.get(0) : null;
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        if (msg.message == Message.GAME_OVER.ordinal()) {
            inGameOver = true;
            gameOver.addAction(Actions.sequence(
                    Actions.run(() -> {
                        gameOver.setVisible(true);
                        gameOver.setPosition(gameOver.getX(), 0);
                    }),
                    Actions.moveTo(gameOver.getX(), gameOver.getY(), 1)));
            return true;
        }
        return false;
    }

    private class RetryProcessor extends InputAdapter {
        @Override
        public boolean keyUp(int keycode) {
            if (inGameOver && Input.Keys.R == keycode) {
                router.goToLevel();
                return true;
            }
            return super.keyUp(keycode);
        }
    }
}
