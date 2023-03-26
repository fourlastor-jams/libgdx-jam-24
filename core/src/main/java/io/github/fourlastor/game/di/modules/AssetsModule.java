package io.github.fourlastor.game.di.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonReader;

import dagger.Module;
import dagger.Provides;
import io.github.fourlastor.harlequin.animation.AnimationNode;
import io.github.fourlastor.harlequin.loader.dragonbones.DragonBonesLoader;
import io.github.fourlastor.harlequin.loader.dragonbones.model.DragonBonesEntity;
import io.github.fourlastor.harlequin.loader.spine.SpineLoader;
import io.github.fourlastor.harlequin.loader.spine.model.SpineEntity;
import io.github.fourlastor.json.JsonParser;
import io.github.fourlastor.ldtk.LdtkLoader;
import io.github.fourlastor.ldtk.model.LdtkMapData;
import io.github.fourlastor.text.Text;
import io.github.fourlastor.text.TextLoader;

import javax.inject.Named;
import javax.inject.Singleton;

@Module
public class AssetsModule {

    private static final String PATH_TEXTURE_ATLAS = "images/included/packed/images.pack.atlas";
    public static final String WHITE_PIXEL = "white-pixel";

    @Provides
    public DragonBonesLoader dragonBonesLoader(JsonReader json, JsonParser<DragonBonesEntity> parser) {
        return new DragonBonesLoader(
                new DragonBonesLoader.Options(PATH_TEXTURE_ATLAS, "images/included"), json, parser);
    }

    @Provides
    @Singleton
    public AssetManager assetManager(
            LdtkLoader ldtkLoader,
            TextLoader textLoader,
            SpineLoader spineLoader,
            DragonBonesLoader dragonBonesLoader) {
        AssetManager assetManager = new AssetManager();
        assetManager.setLoader(LdtkMapData.class, ldtkLoader);
        assetManager.setLoader(Text.class, textLoader);
        assetManager.setLoader(SpineEntity.class, spineLoader);
        assetManager.setLoader(AnimationNode.Group.class, dragonBonesLoader);
        assetManager.load(PATH_TEXTURE_ATLAS, TextureAtlas.class);

        assetManager.load("audio/sounds/pickups/xp 0.wav", Sound.class);
        assetManager.load("audio/sounds/pickups/xp 1.wav", Sound.class);
        assetManager.load("audio/sounds/pickups/xp 2.wav", Sound.class);
        assetManager.load("audio/sounds/pickups/pasta.wav", Sound.class);

        assetManager.load("audio/sounds/enemies/death/pigeon 0.wav", Sound.class);

        assetManager.load("audio/sounds/player/whip.wav", Sound.class);

        assetManager.load("audio/sounds/intro/607252__d4xx__swoosh-1.wav", Sound.class);
        assetManager.load("audio/sounds/intro/607252__d4xx__swoosh-1_REVERSE.wav", Sound.class);
        assetManager.load("audio/sounds/intro/411494__inspectorj__jews-harp-single-a-h1.wav", Sound.class);
        assetManager.load("audio/sounds/intro/436107__drfortyseven__ahem-1.wav", Sound.class);
        assetManager.load("audio/sounds/intro/rumble 0.wav", Sound.class);
        assetManager.load("audio/sounds/intro/rumble 1.wav", Sound.class);
        assetManager.load("audio/sounds/intro/pigeons flying.wav", Sound.class);
        assetManager.load("audio/sounds/intro/661837__rslebs__toast-fork-hitting-glass.wav", Sound.class);
        assetManager.load("audio/sounds/intro/drama.wav", Sound.class);

        assetManager.load("audio/music/Edvard-grieg-morning-mood.ogg", Music.class);
        assetManager.load("audio/music/362353__pandos__crowd-at-a-british-wedding-reception-venue (1).ogg", Music.class);
        assetManager.load("audio/music/429347__doctor_dreamchip__2018-05-19.ogg", Music.class);

        assetManager.finishLoading();
        return assetManager;
    }

    @Provides
    @Singleton
    public TextureAtlas textureAtlas(AssetManager assetManager) {
        return assetManager.get(PATH_TEXTURE_ATLAS, TextureAtlas.class);
    }

    @Provides
    @Singleton
    @Named(WHITE_PIXEL)
    public TextureRegion whitePixel(TextureAtlas atlas) {
        return atlas.findRegion("whitePixel");
    }

    @Provides
    @Singleton
    @Named("bold")
    BitmapFont bold(TextureAtlas atlas) {
        return new BitmapFont(
                Gdx.files.internal("images/included/fonts/play-bold.fnt"),
                atlas.findRegion("images/included/fonts/play-bold"));
    }

    @Provides
    @Singleton
    @Named("regular")
    BitmapFont regular(TextureAtlas atlas) {
        return new BitmapFont(
                Gdx.files.internal("images/included/fonts/play-regular.fnt"),
                atlas.findRegion("images/included/fonts/play-regular"));
    }
}
