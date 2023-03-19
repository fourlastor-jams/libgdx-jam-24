package io.github.fourlastor.game.di.modules;

import com.badlogic.gdx.assets.AssetManager;
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
}
