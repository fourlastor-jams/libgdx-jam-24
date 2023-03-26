package io.github.fourlastor.game.level.weapon.whip.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import io.github.fourlastor.game.SoundController;
import io.github.fourlastor.game.level.component.Whip;
import javax.inject.Inject;

public class Enabled extends WhipState {
    private final SoundController soundController;
    private final AssetManager assetManager;

    @Inject
    public Enabled(Dependencies mappers, SoundController soundController, AssetManager assetManager) {
        super(mappers);
        this.soundController = soundController;
        this.assetManager = assetManager;
    }

    @Override
    protected boolean canCollide() {
        return true;
    }

    @Override
    protected WhipState nextState(Whip whip) {
        return whip.disabled;
    }

    @Override
    protected float timer(Entity entity) {
        return 0.2f;
    }

    @Override
    public void enter(Entity entity) {
        super.enter(entity);
        soundController.play(assetManager.get("audio/sounds/player/whip.wav", Sound.class), .1f);
    }
}
