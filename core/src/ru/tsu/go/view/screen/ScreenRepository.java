package ru.tsu.go.view.screen;

import com.badlogic.gdx.Screen;
import io.netty.util.AttributeKey;

public interface ScreenRepository {

    <T extends Screen> T getScreen(final AttributeKey<T> screen);
    <T extends Screen> void registerFactory(final AttributeKey<T> screen, final ScreenFactory<T> factory);
}
