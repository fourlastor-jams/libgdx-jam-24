package io.github.fourlastor.game.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class Bar extends Group {

    private final Image barBg;
    private final Image bar;

    public Bar(Drawable top, Drawable bottom) {
        this.barBg = new Image(bottom);
        this.bar = new Image(top);
        addActor(barBg);
        addActor(bar);
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        barBg.setSize(width, height);
        bar.setSize(width, height);
    }

    public void setAmount(float amount) {
        bar.setScaleX(amount);
    }
}
