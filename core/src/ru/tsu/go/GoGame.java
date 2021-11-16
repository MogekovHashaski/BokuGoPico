package ru.tsu.go;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import ru.tsu.go.view.screen.ScreenFactory;

import javax.inject.Inject;

public class GoGame extends Game {

    private ScreenFactory<? extends Screen> screenFactory;

    @Inject
    public GoGame() {
    }

    /**
     * Called when the {@link Application} is first created.
     */
    @Override
    public void create() {
        /*if (screenFactory != null) {
            setScreen(screenFactory.getInstance());
        }*/
    }

    public <T extends Screen> void setScreenFactory(final ScreenFactory<T> screenFactory) {
        this.screenFactory = screenFactory;
    }

    @Override
    public void render() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
        if (screen == null && screenFactory != null) {
            setScreen(screenFactory.getInstance());
        }
    }
}

