package ru.tsu.go.scene.world;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import ru.tsu.go.di.scope.GoScreenScope;
import ru.tsu.go.scene.world.entity.BoardFactory;
import ru.tsu.go.scene.world.entity.TransformComponent;
import ru.tsu.go.scene.world.entity.stone.StoneColor;
import ru.tsu.go.scene.world.entity.stone.StoneFactory;

import javax.inject.Inject;

@GoScreenScope
public final class World {

    private final Engine engine;
    private final BoardFactory boardFactory;
    private final StoneFactory stoneFactory;

    @Inject
    public World(final Engine engine, final BoardFactory boardFactory, final StoneFactory stoneFactory) {
        this.engine = engine;
        this.boardFactory = boardFactory;
        this.stoneFactory = stoneFactory;
    }

    public void initialize() {
        final Entity board = boardFactory.newInstance();
        engine.addEntity(board);
    }

    public void pulse(final float delta) {
        engine.update(delta);
    }

    public void createStone(Vector2 position, final StoneColor color) {
        createStone(new Vector3(position.x, position.y, 0), color);
    }

    public void createStone(Vector3 position, final StoneColor color) {
        final Entity stone = stoneFactory.newInstance(color);
        final TransformComponent transform = stone.getComponent(TransformComponent.class);
        if (transform != null) {
            transform.position = position;
        }
        engine.addEntity(stone);
    }
}
