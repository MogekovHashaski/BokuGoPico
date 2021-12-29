package ru.tsu.go.scene.graphics;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import ru.tsu.go.scene.world.entity.TransformComponent;

public final class CameraSystem extends IteratingSystem {

    final ComponentMapper<CameraComponent> cameraMapper;
    final ComponentMapper<TransformComponent> transformMapper;

    public CameraSystem() {
        this(Family.all(CameraComponent.class).get(), 0);
    }

    public CameraSystem(final Family family) {
        this(family, 0);
    }

    public CameraSystem(final Family family, final int priority) {
        super(family, priority);
        this.cameraMapper = ComponentMapper.getFor(CameraComponent.class);
        this.transformMapper = ComponentMapper.getFor(TransformComponent.class);
    }


    /**
     * This method is called on every entity on every update call of the EntitySystem. Override this to implement your system's
     * specific processing.
     *
     * @param entity    The current Entity being processed
     * @param deltaTime The delta time between the last and current frame
     */
    @Override
    public void processEntity(final Entity entity, final float deltaTime) {
        final CameraComponent cameraComponent = cameraMapper.get(entity);
        if (cameraComponent.target == null) {
            return;
        }

        final TransformComponent transformComponent = transformMapper.get(cameraComponent.target);
        if (transformComponent == null) {
            return;
        }
        cameraComponent.camera.position.x = transformComponent.position.x;
        cameraComponent.camera.position.y = transformComponent.position.y;
    }

    @Override
    public void update(final float deltaTime) {
        super.update(deltaTime);
    }
}
