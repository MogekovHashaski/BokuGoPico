package ru.tsu.go.di.module;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import ru.tsu.go.GoGame;
import ru.tsu.go.di.PixelWorldUnitRatio;
import ru.tsu.go.di.WorldHeight;
import ru.tsu.go.di.WorldWidth;
import ru.tsu.go.di.scope.GoScreenScope;
import ru.tsu.go.scene.graphics.CameraComponent;
import ru.tsu.go.scene.graphics.CameraSystem;
import ru.tsu.go.scene.graphics.RenderingSystem;
import ru.tsu.go.scene.graphics.TextureComponent;
import ru.tsu.go.scene.graphics.TextureProvider;
import ru.tsu.go.scene.hci.ClickComponent;
import ru.tsu.go.scene.hci.InputHandlerSystem;
import ru.tsu.go.scene.world.EngineFactory;
import ru.tsu.go.scene.world.WorldUnits;
import ru.tsu.go.scene.world.World;
import ru.tsu.go.scene.world.entity.BoardFactory;
import ru.tsu.go.scene.world.entity.TransformComponent;
import ru.tsu.go.scene.world.entity.stone.StoneColor;
import ru.tsu.go.scene.world.entity.stone.StoneFactory;
import ru.tsu.go.view.screen.ScreenFactory;
import ru.tsu.go.view.screen.game.GoScreen;

import javax.annotation.Nullable;

@Module(includes = GraphicsModule.class)
public class GameModule {

    @Provides
    @GoScreenScope
    public WorldUnits provideUnitConverter(@WorldWidth final float worldWidth, @WorldHeight final float worldHeight,
                                           @PixelWorldUnitRatio final float pixelWorldUnitRatio) {
        return new WorldUnits(worldWidth, worldHeight, pixelWorldUnitRatio);
    }

    @Provides
    @GoScreenScope
    OrthographicCamera provideOrthographicCamera(final WorldUnits worldUnits) {
        return new OrthographicCamera(worldUnits.toWorldUnits(Gdx.graphics.getWidth()), worldUnits.toWorldUnits(Gdx.graphics.getHeight()));
    }

    @Provides
    @GoScreenScope
    Viewport provideViewport(final WorldUnits worldUnits, final OrthographicCamera camera) { // camera.viewportHeight
        final Viewport viewport = new StretchViewport(worldUnits.getWorldWidth(), worldUnits.getWorldHeight(), camera);
        viewport.apply(true);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return viewport;
    }

    @Provides
    @GoScreenScope
    public StoneFactory provideStoneFactory(final WorldUnits worldUnits, final EngineFactory engineFactory, final TextureProvider textureProvider) {
        return color -> {
            final Engine engine = engineFactory.getInstance();
            final TransformComponent position = engine.createComponent(TransformComponent.class);
            final TextureComponent texture = engine.createComponent(TextureComponent.class);
            final ClickComponent click = engine.createComponent(ClickComponent.class);
            final Entity entity = engine.createEntity();

            Pixmap ds = new Pixmap(99, 99, Pixmap.Format.RGBA8888);
            ds.setColor(Color.WHITE);
            ds.fillCircle(ds.getWidth()/2, ds.getWidth()/2, (int) ((ds.getWidth()/3.14f) /2f));
            Texture tex = new Texture(ds);
            texture.region = new TextureRegion(tex);//textureProvider.getTexture("canti");
            position.scale = new Vector2(worldUnits.getScaleForPixels(99, 0.7f), worldUnits.getScaleForPixels(99, 0.7f));
            ds.dispose();
            final float f = tex.getWidth();
            //click.listener = (x, y, z) -> System.out.println("x " + x + " y " + y + " z " + z);
            entity.add(position);
            entity.add(texture);
            //entity.add(click);
            return entity;
        };
    }

    @Provides
    @GoScreenScope
    public CameraComponent provideCameraComponent(final EngineFactory engineFactory, final OrthographicCamera camera, final Viewport viewport) {
        final Engine engine = engineFactory.getInstance();
        final CameraComponent cameraComponent = engine.createComponent(CameraComponent.class);
        cameraComponent.camera = camera;
        cameraComponent.viewport = viewport;
        return cameraComponent;
    }

    @Provides
    @GoScreenScope
    public BoardFactory provideBoardFactory(final WorldUnits worldUnits, final EngineFactory engineFactory, final CameraComponent cameraComponent, final StoneFactory stoneFactory) {
        return () -> {
            // we don't talk about this
            final float boardSizeMeters = 3.8f;
            final int borderSize = 1;
            final int tiles = 18;
            final int boardSize = tiles + borderSize * 2;
            final int tileSize = (int) Math.ceil(worldUnits.toPixels(boardSizeMeters) / (float) tiles);
            final int pixelSize = tileSize * boardSize;
            final Pixmap pixmap = new Pixmap(pixelSize, pixelSize, Pixmap.Format.RGBA8888 );
            for (int row = 0; row < borderSize * tileSize; row++) {
                for (int column = 0; column < pixelSize; column++) {
                    pixmap.setColor(new Color(0.925f, 0.807f, 0.486f, 1f));
                    pixmap.drawPixel(row, column);
                }
            }

            for (int row = 0; row < pixelSize; row++) {
                for (int column = 0; column < borderSize * tileSize; column++) {
                    pixmap.setColor(new Color(0.925f, 0.807f, 0.486f, 1f));
                    pixmap.drawPixel(row, column);
                }
            }

            for (int column = pixelSize - 1; column > pixelSize - (borderSize * tileSize); column--) {
                for (int row = 0; row < pixelSize; row++) {
                    pixmap.setColor(new Color(0.925f, 0.807f, 0.486f, 1f));
                    pixmap.drawPixel(column, row);
                }
            }

            for (int column = pixelSize - 1; column > borderSize * tileSize - 1; column--) {
                for (int row = pixelSize - 1; row > pixelSize - (borderSize * tileSize); row--) {
                    pixmap.setColor(new Color(0.925f, 0.807f, 0.486f, 1f));
                    pixmap.drawPixel(column, row);
                }
            }

            for (int row = 0; row < pixelSize - (borderSize * 2 * tileSize); row++) {
                for (int column = 0; column < pixelSize - (borderSize * 2 * tileSize); column++) {
                    pixmap.setColor(new Color(0.925f, 0.807f, 0.486f, 1f));
                    if (row % tileSize == 0 || column % tileSize == 0) {
                        pixmap.setColor(Color.BLACK);
                    } else {
                        pixmap.setColor(new Color(0.925f, 0.807f, 0.486f, 1f));
                    }
                    pixmap.drawPixel(row + borderSize * tileSize, column + borderSize * tileSize);
                }
            }
            final Texture tex = new Texture(pixmap);
            pixmap.dispose();
            final Engine engine = engineFactory.getInstance();
            final TransformComponent position = engine.createComponent(TransformComponent.class);
            final TextureComponent texture = engine.createComponent(TextureComponent.class);
            final ClickComponent click = engine.createComponent(ClickComponent.class);
            final Entity entity = engine.createEntity();
            texture.region = new TextureRegion(tex);
            click.listener = (x, y, maxX, maxY, button) -> {
                final float tileSizeWU = worldUnits.toWorldUnits(borderSize * tileSize);
                final float radius = tileSizeWU / 1.5f;
                final float origin = radius / 2;
                if (x <= tileSizeWU - origin || x >= maxX - tileSizeWU + origin) {
                    return;
                }
                if (y <= tileSizeWU - origin || y >= maxY - tileSizeWU + origin) {
                    return;
                }
                final float tileX = Math.round(x / tileSizeWU);
                final float tileY = Math.round(y / tileSizeWU);
                final Rectangle bounds = new Rectangle(tileX * tileSizeWU - origin, tileY * tileSizeWU - origin, radius, radius);
                if (bounds.contains(x, y)) {
                    System.out.println("x " + tileX + " y " + tileY);
                    final Entity stone = stoneFactory.newInstance(StoneColor.WHITE);
                    final TransformComponent stoneTransform = stone.getComponent(TransformComponent.class);
                    final float newX = position.position.x - maxX / 2 + tileX * tileSizeWU;
                    final float newY = position.position.y - maxY / 2 + tileY * tileSizeWU;
                    stoneTransform.position = new Vector3(newX, newY, 0);
                    engine.addEntity(stone);
                }
            };
            cameraComponent.target = entity;
            position.position.set(3,3,0);
            entity.add(position);
            entity.add(texture);
            entity.add(click);
            entity.add(cameraComponent);
            return entity;
        };
    }

    @Provides
    @GoScreenScope
    public InputHandlerSystem inputHandlerSystem(final WorldUnits worldUnits, final Viewport viewport, final OrthographicCamera camera) {
        return new InputHandlerSystem(worldUnits, viewport, camera);
    }

    @Provides
    @GoScreenScope
    public CameraSystem cameraSystem() {
        return new CameraSystem();
    }

    @Provides
    @GoScreenScope
    public RenderingSystem provideRenderingSystem(final WorldUnits worldUnits, final OrthographicCamera camera, @Nullable final Viewport viewport, SpriteBatch spriteBatch) {
        return new RenderingSystem(worldUnits, spriteBatch, camera/*, viewport*/);
    }

    @Provides
    @GoScreenScope
    public Engine provideEngine(final RenderingSystem renderingSystem, final InputHandlerSystem inputHandlerSystem, final CameraSystem cameraSystem) {
        final Engine engine = new PooledEngine();
        engine.addSystem(renderingSystem);
        engine.addSystem(inputHandlerSystem);
        engine.addSystem(cameraSystem);
        return engine;
    }

    @Provides
    @GoScreenScope
    public EngineFactory provideEngineFactory(final Lazy<Engine> engine) {
        return engine::get;
    }

    @Provides
    @GoScreenScope
    public ScreenFactory<GoScreen> screenFactory(final Lazy<GoGame> game, final Lazy<Viewport> viewport, final Lazy<World> world/*Lazy<Engine> engine, final Lazy<StoneFactory> stoneFactory*/) {
        return () -> new GoScreen(game.get(), viewport.get(), world.get()/*engine.get(), stoneFactory.get()*/);
    }
}
