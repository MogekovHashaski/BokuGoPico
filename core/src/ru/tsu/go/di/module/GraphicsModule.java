package ru.tsu.go.di.module;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import ru.tsu.go.di.scope.GoScreenScope;
import ru.tsu.go.scene.graphics.TextureProvider;

@Module
public class GraphicsModule {

    @Provides
    @GoScreenScope
    public SpriteBatch provideSpriteBatch() {
        return new SpriteBatch();
    }

    @Provides
    @GoScreenScope
    public TextureAtlas provideTextureAtlas() {
        return new TextureAtlas(Gdx.files.internal("images/game.atlas"));
    }

    @Provides
    @GoScreenScope
    public TextureProvider provideTextureProvider(final Lazy<TextureAtlas> atlas) {
        return (texture) -> atlas.get().findRegion(texture);
    }
}
