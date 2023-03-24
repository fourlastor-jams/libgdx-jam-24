package io.github.fourlastor.game.intro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.inject.Inject;

public class IntroScreen extends ScreenAdapter {

    public static final Color CLEAR_COLOR = Color.valueOf("000000");

    private final InputMultiplexer inputMultiplexer;
    private final Stage stage;
    private final Viewport viewport;
    private final TextureAtlas textureAtlas;
    private Image clouds0;
    private Image clouds1;
    private Image character;
    private Image textWedding;
    private Image textSurvivor;
    private float cloudSpeed = .025f;

    @Inject
    public IntroScreen(InputMultiplexer inputMultiplexer, TextureAtlas textureAtlas) {
        this.inputMultiplexer = inputMultiplexer;
        this.textureAtlas = textureAtlas;

        viewport = new FitViewport(192, 108);
        stage = new Stage(viewport);
    }

    @Override
    public void show() {
        Image image = new Image();
        stage.addActor(image);
        image.addAction(Actions.sequence(
                Actions.delay(0f),
                Actions.run(() -> {
                    imageSetup();
                })
        ));
        inputMultiplexer.addProcessor(processor);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        inputMultiplexer.removeProcessor(processor);
    }

    private final InputProcessor processor = new InputAdapter() {
        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            character.addAction(Actions.moveTo(0, -screenHeight(), 1.75f, Interpolation.exp10In));
            textWedding.addAction(Actions.sequence(
                    Actions.delay(1),
                    Actions.moveTo(-screenWidth(), 0, 1, Interpolation.exp10In)
            ));
            textSurvivor.addAction(Actions.sequence(
                    Actions.delay(1),
                    Actions.moveTo(2 * screenWidth(), 0, 1, Interpolation.exp10In)
            ));
            character.addAction(Actions.sequence(
                    Actions.delay(2),
                    Actions.run(() -> {
                        // TODO: when the player click, the go to next screen here
                        System.out.println("Go to level");
                    })
            ));
            return true;
        }

        @Override
        public boolean keyDown(int keycode) {
            if (keycode == Input.Keys.Q) // TODO: remove for production/release
                Gdx.app.exit();
            return super.keyDown(keycode);
        }
    };

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(CLEAR_COLOR.r, CLEAR_COLOR.g, CLEAR_COLOR.b, CLEAR_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.act(delta);

        if (clouds0 != null) {
            clouds0.setPosition(clouds0.getX() - cloudSpeed, clouds0.getY());
            if (clouds0.getX() + clouds0.getWidth() < 0)
                clouds0.setPosition(screenWidth(), 0);
        }

        if (clouds1 != null) {
            clouds1.setPosition(clouds1.getX() - cloudSpeed, clouds1.getY());
            if (clouds1.getX() + clouds1.getWidth() < 0)
                clouds1.setPosition(screenWidth(), 0);
        }

        stage.draw();
    }

    private void imageSetup() {
        // sky
        Image sky = new Image(textureAtlas.findRegion("intro/cover image_sky"));
        sky.getColor().a = .8f;
        sky.addAction(Actions.sequence(
                Actions.delay(3),
                Actions.alpha(1, 3)
        ));
        stage.addActor(sky);

        // game controller
        Image gameController = new Image(textureAtlas.findRegion("intro/cover image_game controller"));
        stage.addActor(gameController);
        gameController.setPosition(-25, -25);
        float wiggleDuration = .2f;
        float wiggleAmount = .5f;
        gameController.addAction(Actions.sequence(
                Actions.delay(6f),
                Actions.moveTo(0, 0, 1, Interpolation.bounceOut),
                Actions.delay(2f),
                Actions.moveTo(-1f, -1, 1),
                Actions.delay(2f),
                Actions.forever(Actions.sequence(Actions.sequence(
                        Actions.rotateTo(wiggleAmount, wiggleDuration),
                        Actions.rotateTo(-wiggleAmount, wiggleDuration),
                        Actions.rotateTo(wiggleAmount, wiggleDuration),
                        Actions.rotateTo(-wiggleAmount, wiggleDuration),
                        Actions.rotateTo(wiggleAmount, wiggleDuration),
                        Actions.rotateTo(-wiggleAmount, wiggleDuration),
                        Actions.rotateTo(wiggleAmount, wiggleDuration),
                        Actions.rotateTo(-wiggleAmount, wiggleDuration),
                        Actions.delay(5f)
                )))
        ));

        // sun
        Image sun = new Image(textureAtlas.findRegion("intro/cover image_sun"));
        stage.addActor(sun);
        sun.getColor().a = 0;
        sun.setOrigin(Align.right);
        sun.addAction(Actions.sequence(
                Actions.delay(3f),
                Actions.fadeIn(3f)
        ));

        // mountains
        Image mountains = new Image(textureAtlas.findRegion("intro/cover image_mountains"));
        stage.addActor(mountains);

        // birds
        Image birds = new Image(textureAtlas.findRegion("intro/cover image_birds"));
        stage.addActor(birds);
        birds.getColor().a = 0f;
        birds.setPosition(0, -15);
        birds.addAction(Actions.sequence(
                Actions.delay(3.5f),
                Actions.parallel(
                        Actions.sequence(
                                Actions.delay(5f),
                                Actions.alpha(1f, 2f)
                        ),
                        Actions.moveTo(0, 0, 6f)
                ),
                Actions.moveTo(0, -screenHeight(), 30f)
        ));

        // bushes
        Image bushes = new Image(textureAtlas.findRegion("intro/cover image_bushes"));
        stage.addActor(bushes);

        // grass
        Image grass = new Image(textureAtlas.findRegion("intro/cover image_grass"));
        stage.addActor(grass);

        // clouds 0
        clouds0 = new Image(textureAtlas.findRegion("intro/cover image_clouds"));
        clouds0.getColor().a = .9f;
        stage.addActor(clouds0);

        // clouds 1
        clouds1 = new Image(textureAtlas.findRegion("intro/cover image_clouds"));
        clouds1.setPosition(screenWidth(), 0);
        clouds1.getColor().a = .9f;
        stage.addActor(clouds1);

        // character
        character = new Image(textureAtlas.findRegion("intro/cover image_character"));
        stage.addActor(character);
        character.setPosition(0, -character.getHeight() + 20);
        character.addAction(Actions.moveTo(0, 0, 1.75f, Interpolation.exp10In));

        // text - wedding
        textWedding = new Image(textureAtlas.findRegion("intro/cover image_text - wedding"));
        stage.addActor(textWedding);
        textWedding.setPosition(-screenWidth(), 0);
        textWedding.addAction(Actions.sequence(
                Actions.delay(2),
                Actions.moveTo(0, 0, 1, Interpolation.exp10Out)
        ));

        // text - survivor
        textSurvivor = new Image(textureAtlas.findRegion("intro/cover image_text - survivor"));
        stage.addActor(textSurvivor);
        textSurvivor.setPosition(screenWidth(), 0);
        textSurvivor.addAction(Actions.sequence(
                Actions.delay(2.1f),
                Actions.moveTo(0, 0, 1, Interpolation.exp10Out)
        ));
    }

    private float screenWidth() {
        return viewport.getWorldWidth();
    }

    private float screenHeight() {
        return viewport.getWorldHeight();
    }
}
