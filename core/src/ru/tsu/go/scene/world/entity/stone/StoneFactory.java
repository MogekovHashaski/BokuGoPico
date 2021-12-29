package ru.tsu.go.scene.world.entity.stone;

import com.badlogic.ashley.core.Entity;

public interface StoneFactory {

    Entity newInstance(final StoneColor color);
}
