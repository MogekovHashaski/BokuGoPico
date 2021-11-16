package ru.tsu.go.view.screen;

import com.badlogic.gdx.Screen;
import io.netty.util.AttributeKey;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Value
@AllArgsConstructor(access = AccessLevel.PUBLIC, onConstructor = @__({ @Inject }))
public class DefaultScreenRepository implements ScreenRepository {

    @Getter(AccessLevel.NONE)
    Map<String, ScreenFactory<?>> screens = new HashMap<>();



    @Override
    @SuppressWarnings("unchecked")
    public <T extends Screen> T getScreen(final AttributeKey<T> screen) {
        return (T) screens.get(screen.name()).getInstance();
    }

    @Override
    public <T extends Screen> void registerFactory(final AttributeKey<T> screen, final ScreenFactory<T> factory) {
        screens.put(screen.name(), factory);
    }
}
