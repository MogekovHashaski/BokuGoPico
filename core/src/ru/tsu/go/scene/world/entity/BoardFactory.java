package ru.tsu.go.scene.world.entity;

import com.badlogic.ashley.core.Entity;

public interface BoardFactory {
    Entity newInstance();
}
