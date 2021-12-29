package ru.tsu.go.scene.graphics;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Data;

@Data
public class TextureComponent implements Component {
    public TextureRegion region = null;
}
