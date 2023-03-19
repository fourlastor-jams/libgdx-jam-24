package io.github.fourlastor.game.ui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ParallaxImage extends Actor {

    private final TextureRegion texture;
    private final float factor;

    private final Vector2 currentDelta = new Vector2();

    public ParallaxImage(TextureRegion texture, float factor) {
        super();
        setBounds(0f, 0f, texture.getRegionWidth(), texture.getRegionHeight());
        setPosition(0f, 0f);
        this.factor = factor;
        this.texture = texture;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Camera camera = getStage().getCamera();

        currentDelta.x = -(camera.position.x * factor);
        currentDelta.y = -(camera.position.y * factor);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Camera camera = getStage().getCamera();

        float targetWidth = getWidth() * getScaleX();
        float targetHeight = getHeight() * getScaleY();
        float targetX = camera.position.x - targetWidth / 2;
        float targetY = camera.position.y - targetHeight / 2;
        float deltaX = currentDelta.x % targetHeight;
        float deltaY = currentDelta.y % targetHeight;

        float x = targetX + deltaX;
        float y = targetY + deltaY;
        batch.draw(texture, x, y, targetWidth, targetHeight);
    }
}
