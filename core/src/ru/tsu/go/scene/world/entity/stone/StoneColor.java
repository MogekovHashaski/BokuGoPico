package ru.tsu.go.scene.world.entity.stone;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoneColor {

    WHITE("stone-white"),
    BLACK("stone-black");

    private final String atlasRegion;
}
