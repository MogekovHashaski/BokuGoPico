package ru.tsu.go.scene.world;

import com.badlogic.gdx.math.Vector3;
import lombok.Value;

@Value
public class WorldUnits {

    float worldWidth;
    float worldHeight;
    float pixelToWorldUnitRatio;

    public float toWorldUnits(float pixels) {
        return pixels / pixelToWorldUnitRatio;
    }

    public float toPixels(float worldUnits) {
        return worldUnits * pixelToWorldUnitRatio;
    }

    public Vector3 toWorldUnits(final Vector3 pixels) {
        return pixels.scl(1f / pixelToWorldUnitRatio);
    }

    public Vector3 toPixels(final Vector3 worldUnits) {
        return worldUnits.scl(pixelToWorldUnitRatio);
    }

    public float getScaleForPixels(float pixels, float targetWorldUnits) {
        return targetWorldUnits / (pixels / pixelToWorldUnitRatio);
    }
}
