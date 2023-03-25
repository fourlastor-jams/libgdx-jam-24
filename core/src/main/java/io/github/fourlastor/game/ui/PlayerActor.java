package io.github.fourlastor.game.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import io.github.fourlastor.harlequin.animation.Animation;
import io.github.fourlastor.harlequin.ui.AnimatedImage;

public class PlayerActor extends AnimatedImage {

    private final ParticleEffect bleedEffect;
    private boolean bleeding = false;

    public PlayerActor(Animation<? extends Drawable> animation, ParticleEffect bleedEffect) {
        super(animation);
        this.bleedEffect = bleedEffect;
    }

    public void setBleeding(boolean bleeding) {
        this.bleeding = bleeding;
        if (!bleeding) {
            bleedEffect.reset(false);
        } else {
            bleedEffect.start();
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (bleeding) {
            float x = getX() * getScaleX();
            float y = getY() * getScaleY();
            bleedEffect.setPosition(x, y);
            bleedEffect.update(delta);
            if (bleedEffect.isComplete()) {
                bleedEffect.reset(false);
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (bleeding) {
            bleedEffect.draw(batch);
        }
    }
}
