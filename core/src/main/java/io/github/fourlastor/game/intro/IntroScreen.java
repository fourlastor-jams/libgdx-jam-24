package io.github.fourlastor.game.intro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.inject.Inject;

public class IntroScreen extends ScreenAdapter {

    public static final Color CLEAR_COLOR = Color.valueOf("000000");

    private final InputMultiplexer inputMultiplexer;
    private final Stage stage;
    private final Viewport viewport;

    @Inject
    public IntroScreen(InputMultiplexer inputMultiplexer) {
        this.inputMultiplexer = inputMultiplexer;

        viewport = new FitViewport(256, 144);
        stage = new Stage(viewport);
    }

    @Override
    public void show() {
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
            System.out.println("Go to level");
            return true;
        }
    };

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(CLEAR_COLOR.r, CLEAR_COLOR.g, CLEAR_COLOR.b, CLEAR_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }
}
