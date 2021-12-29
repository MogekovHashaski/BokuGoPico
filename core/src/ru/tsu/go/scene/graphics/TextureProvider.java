package ru.tsu.go.scene.graphics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface TextureProvider {

    TextureRegion getTexture(final String region);
}
