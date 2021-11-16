package ru.tsu.go.view.screen.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Getter;
import ru.tsu.go.GoGame;
import ru.tsu.go.scene.world.World;

@Getter
public final class GoScreen implements GameScreen {

    private final GoGame game;
    private final Viewport viewport;
    private final World world;

    public GoScreen(final GoGame game, final Viewport viewport, final World world) {
        this.game = game;
        this.viewport = viewport;
        this.world = world;
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        if (world != null) {
            world.initialize();
        }
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (world != null) {
            world.pulse(delta);
        }
    }

    /**
     * @param width
     * @param height
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(final int width, final int height) {
        if (viewport != null) {
            viewport.update(width, height);
        }
    }

    /**
     * @see ApplicationListener#pause()
     */
    @Override
    public void pause() {

    }

    /**
     * @see ApplicationListener#resume()
     */
    @Override
    public void resume() {

    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {

    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {

    }
}
