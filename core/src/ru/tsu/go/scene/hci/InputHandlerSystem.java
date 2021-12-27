package ru.tsu.go.scene.hci;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.tsu.go.scene.graphics.TextureComponent;
import ru.tsu.go.scene.world.WorldUnits;
import ru.tsu.go.scene.world.entity.TransformComponent;
import ru.tsu.go.scene.world.entity.util.ZComparator;

import java.util.Comparator;

public class InputHandlerSystem extends SortedIteratingSystem {

    private final WorldUnits worldUnits;
    private final Array<Entity> inputQueue;
    private final Viewport viewport;
    private final Comparator<Entity> comparator;
    private final ComponentMapper<ClickComponent> clickComponentMapper;
    private final ComponentMapper<TextureComponent> textureComponentMapper;
    private final ComponentMapper<TransformComponent> transformComponentMapper;

    public InputHandlerSystem(final WorldUnits worldUnits, final Viewport viewport) {
        this(worldUnits, viewport, Family.all(ClickComponent.class, TextureComponent.class, TransformComponent.class).get(), new ZComparator(), 0);
    }

    /**
     * Instantiates a system that will iterate over the entities described by the Family, with a specific priority.
     *
     * @param family     The family of entities iterated over in this System
     * @param comparator The comparator to sort the entities
     * @param priority   The priority to execute this system with (lower means higher priority)
     */
    public InputHandlerSystem(final WorldUnits worldUnits, final Viewport viewport, final Family family, final Comparator<Entity> comparator, final int priority) {
        super(family, comparator, priority);
        this.worldUnits = worldUnits;
        this.viewport = viewport;
        this.inputQueue = new Array<>();
        this.comparator = comparator;
        this.clickComponentMapper = ComponentMapper.getFor(ClickComponent.class);
        this.textureComponentMapper = ComponentMapper.getFor(TextureComponent.class);
        this.transformComponentMapper = ComponentMapper.getFor(TransformComponent.class);
    }

    /**
     * This method is called on every entity on every update call of the EntitySystem. Override this to implement your system's
     * specific processing.
     *
     * @param entity    The current Entity being processed
     * @param deltaTime The delta time between the last and current frame
     */
    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        inputQueue.add(entity);
    }

    Vector3 getMousePosInGameWorld() {
        return viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }

    @Override
    public void update(final float deltaTime) {
        super.update(deltaTime);
        if (!Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            inputQueue.clear();
            return;
        }
        final Vector3 clickPosition = getMousePosInGameWorld();
        inputQueue.sort(comparator);
        for (final Entity entity : inputQueue) {
            ClickComponent clickComponent = clickComponentMapper.get(entity);
            TextureComponent textureComponent = textureComponentMapper.get(entity);
            TransformComponent transformComponent = transformComponentMapper.get(entity);

            if (textureComponent.region == null || transformComponent.isHidden) {
                continue;
            }


            final float width = textureComponent.region.getRegionWidth();
            final float height = textureComponent.region.getRegionHeight();

            final float posX = transformComponent.position.x;
            final float posY = transformComponent.position.y;
            final float originX = width / 2f;
            final float originY = height / 2f;
            final Vector3 origin = new Vector3(worldUnits.toWorldUnits(transformComponent.scale.x * originX), worldUnits.toWorldUnits(transformComponent.scale.y * originY), 0);
            final Rectangle bounds = new Rectangle(posX - origin.x, posY - origin.y, origin.x * 2, origin.y * 2);
            if (bounds.contains(clickPosition.x, clickPosition.y)) {
                clickComponent.listener.onClick(clickPosition.x - (posX - origin.x), clickPosition.y - (posY - origin.y),
                    origin.x * 2, origin.y * 2, 0);
            }
        }
        inputQueue.clear();
    }
}
