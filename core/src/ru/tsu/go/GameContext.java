package ru.tsu.go;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import io.netty.util.AttributeKey;

public interface GameContext {

    <T extends Screen> void startScreen(final AttributeKey<T> screen);
    Game getGame();
    void setGame(final Game game);
}
