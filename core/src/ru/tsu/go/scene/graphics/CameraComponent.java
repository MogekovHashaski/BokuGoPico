package ru.tsu.go.scene.graphics;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Data;

@Data
public class CameraComponent implements Component {
    public Entity target;
    public OrthographicCamera camera;
    public Viewport viewport;
}
