package io.github.fourlastor.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.github.tommyettinger.textra.Font;
import com.github.tommyettinger.textra.TypingLabel;

public class XpBar extends WidgetGroup {

    private static final float END_WIDTH = 6f;
    private static final float HEIGHT = 14f;
    private static final float MIDDLE_WIDTH = 363f;
    private final Image filledStart;
    private final Image filledMiddle;
    private final Image filledEnd;
    private final TypingLabel levelLabel;
    private float amount = 0f;
    private int level = 1;

    public XpBar(TextureAtlas atlas, BitmapFont font) {
        super();
        Drawable emptyEndDrawable = new TextureRegionDrawable(atlas.findRegion("xp/bar_empty_end"));
        Drawable emptyMidDrawable = new TextureRegionDrawable(atlas.findRegion("xp/bar_empty_middle"));
        Drawable filledEndDrawable = new TextureRegionDrawable(atlas.findRegion("xp/bar_filled_end"));
        Drawable filledMidDrawable = new TextureRegionDrawable(atlas.findRegion("xp/bar_filled_middle"));
        setSize(END_WIDTH * 2 + MIDDLE_WIDTH, HEIGHT);
        Image emptyStart = new Image(emptyEndDrawable);
        emptyStart.setSize(END_WIDTH, HEIGHT);
        emptyMidDrawable.setMinWidth(MIDDLE_WIDTH);
        Image emptyMiddle = new Image(emptyMidDrawable);
        emptyMiddle.setSize(MIDDLE_WIDTH * 10, HEIGHT);
        emptyMiddle.setOrigin(Align.left);
        Image emptyEnd = new Image(emptyEndDrawable);
        emptyEnd.setSize(END_WIDTH, HEIGHT);
        emptyEnd.setOrigin(Align.center);
        emptyEnd.setScaleX(-1f);
        filledStart = new Image(filledEndDrawable);
        filledStart.setSize(END_WIDTH, HEIGHT);
        filledMiddle = new Image(filledMidDrawable);
        filledMiddle.setSize(MIDDLE_WIDTH, HEIGHT);
        filledMiddle.setOrigin(Align.left);
        filledEnd = new Image(filledEndDrawable);
        filledEnd.setSize(END_WIDTH, HEIGHT);
        filledEnd.setOrigin(Align.center);
        filledEnd.setScaleX(-1f);
        HorizontalGroup empty = new HorizontalGroup();
        empty.addActor(emptyStart);
        empty.addActor(emptyMiddle);
        empty.addActor(emptyEnd);
        HorizontalGroup filled = new HorizontalGroup();
        filled.addActor(filledStart);
        filled.addActor(filledMiddle);
        filled.addActor(filledEnd);
        addActor(empty);
        addActor(filled);
        filledStart.setVisible(false);
        filledEnd.setVisible(false);
        filledMiddle.setScaleX(0f);
        levelLabel = new TypingLabel("Level {VAR=level}", new Font(font).scale(0.3f, 0.3f));
        levelLabel.setVariable("level", "1");
        levelLabel.setPosition(getWidth() - 40f, 5f);
        levelLabel.setColor(new Color(0xa5a7aeff));
        addActor(levelLabel);
    }

    public void setAmount(float amount, int level) {
        System.out.println(level);
        amount = Math.min(1f, Math.max(0f, amount));
        if (amount == this.amount && level == this.level) {
            return;
        }
        this.amount = amount;
        boolean levelUp = this.level < level;
        this.level = level;
        filledMiddle.clearActions();
        filledStart.setVisible(amount > 0f);
        boolean visible = amount == 1f;
        Action action;
        SequenceAction setProgressAction = Actions.sequence(
                Actions.scaleTo(amount, 1f, 0.15f, Interpolation.pow2),
                Actions.run(() -> filledEnd.setVisible(visible)));
        if (!levelUp) {
            action = setProgressAction;
        } else {
            action = Actions.sequence(
                    Actions.scaleTo(1, 1f, 0.15f, Interpolation.pow2),
                    Actions.run(() -> filledEnd.setVisible(true)),
                    Actions.run(() -> levelLabel.setVariable("level", String.valueOf(level))),
                    Actions.delay(0.2f),
                    Actions.parallel(
                            Actions.run(() -> {
                                filledEnd.setVisible(visible);
                                levelLabel.restart();
                            }),
                            setProgressAction

                    )
            );
        }
        filledMiddle.addAction(action);
    }
}
