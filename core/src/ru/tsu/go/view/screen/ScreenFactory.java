package ru.tsu.go.view.screen;

import com.badlogic.gdx.Screen;

@FunctionalInterface
public interface ScreenFactory<T extends Screen> {

    T getInstance();
}
