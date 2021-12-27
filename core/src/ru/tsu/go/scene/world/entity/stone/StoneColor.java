package ru.tsu.go.scene.world.entity.stone;

import com.badlogic.gdx.graphics.Color;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoneColor {

    WHITE(Color.WHITE),
    BLACK(Color.BLACK);

    private final Color color;
}
