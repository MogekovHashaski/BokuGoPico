package ru.tsu.go.scene.hci;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.tsu.go.scene.graphics.TextureComponent;
import ru.tsu.go.scene.world.WorldUnits;
import ru.tsu.go.scene.world.entity.TransformComponent;
import ru.tsu.go.scene.world.entity.util.ZComparator;

import javax.swing.text.View;
import java.util.Comparator;

public class InputHandlerSystem extends SortedIteratingSystem {

    private final WorldUnits worldUnits;
    private final Array<Entity> inputQueue;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private final Comparator<Entity> comparator;
    private final ComponentMapper<ClickComponent> clickComponentMapper;
    private final ComponentMapper<TextureComponent> textureComponentMapper;
    private final ComponentMapper<TransformComponent> transformComponentMapper;

    public InputHandlerSystem(final WorldUnits worldUnits, final Viewport viewport, final OrthographicCamera camera) {
        this(worldUnits, viewport, camera, Family.all(ClickComponent.class, TextureComponent.class, TransformComponent.class).get(), new ZComparator(), 0);
    }

    /*public InputHandlerSystem(final OrthographicCamera camera, final Comparator<Entity> comparator) {
        this(camera, Family.all(ClickComponent.class, TextureComponent.class, TransformComponent.class).get(), comparator, 0);
    }

    *//**
     * Instantiates a system that will iterate over the entities described by the Family.
     *
     * @param family     The family of entities iterated over in this System
     * @param comparator The comparator to sort the entities
     *//*
    public InputHandlerSystem(final OrthographicCamera camera, final Family family, final Comparator<Entity> comparator) {
        this(camera, family, comparator, 0);
    }*/

    /**
     * Instantiates a system that will iterate over the entities described by the Family, with a specific priority.
     *
     * @param family     The family of entities iterated over in this System
     * @param comparator The comparator to sort the entities
     * @param priority   The priority to execute this system with (lower means higher priority)
     */
    public InputHandlerSystem(final WorldUnits worldUnits, final Viewport viewport, final OrthographicCamera camera, final Family family, final Comparator<Entity> comparator, final int priority) {
        super(family, comparator, priority);
        this.worldUnits = worldUnits;
        this.viewport = viewport;
        this.camera = camera;
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
        /*final ClickComponent click = clickComponentMapper.get(entity);
        final TransformComponent transform = transformComponentMapper.get(entity);
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !transform.isHidden) {
        }*/
    }

    Vector3 getMousePosInGameWorld() {
        return viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }

    @Override
    public void update(final float deltaTime) {
        super.update(deltaTime);
        if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            inputQueue.clear();
            return;
        }
        final float xx = Gdx.input.getX();
        final float yy = Gdx.input.getY();
        final Vector3 clickPosition = getMousePosInGameWorld();//viewport.unproject(new Vector3(xx, yy,0));
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
            /*final Vector3 vecBound = new Vector3(posX - originX, posY - originY, 0);
            final Vector3 vecEnd = new Vector3(posX + originX, posY + originY, 0);*/
            final Vector3 origin = new Vector3(worldUnits.toWorldUnits(transformComponent.scale.x * originX), worldUnits.toWorldUnits(transformComponent.scale.y * originY), 0);
            final Vector3 start = new Vector3(posX - origin.x, posY - origin.y, 0);
            final Vector3 end = new Vector3(posX + origin.x, posY + origin.y, 0);
            final Rectangle bounds = new Rectangle(posX - origin.x, posY - origin.y, origin.x * 2, origin.y * 2);
            if (bounds.contains(clickPosition.x, clickPosition.y)) {
                //System.out.println(String.format("x %.02f; y %.02f", clickPosition.x, clickPosition.y));
                clickComponent.listener.onClick(clickPosition.x - (posX - origin.x), clickPosition.y - (posY - origin.y),
                    origin.x * 2, origin.y * 2, 0);
            }
            /*final Vector3 vecBoundNew = viewport.unproject(vecBound);
            final Vector3 vecEndNew = viewport.unproject(vecEnd);
            final Rectangle bounds2 = new Rectangle(vecBoundNew.x, vecBoundNew.y, vecEndNew.x, vecEndNew.y);*/
            //if (bounds.contains(clickPosition.x, clickPosition.y)){
                //System.out.println("wooo!" + xx + " " + yy + " p" + posX + " " + posY + " lol " + originX + " " + width + " " + " now "  +clickPosition.x);
                /*System.out.println(String.format("Original: [clickX: %.02f, clickY: %.02f], Unprojected: " +
                    "[unprojX: %.02f, unprojY: %.02f], Origin: [x: %.02f, y: %.02f], Bounds: [%.02f, %.02f, %.02f, %.02f], Unprojected bounds: [%.02f, %.02f, %.02f, %.02f",
                    xx, yy, clickPosition.x, clickPosition.y, originX, originY, posX - originX, posY - originY, posX + originX, posY + originY, vecBoundNew.x, vecBoundNew.y, vecEndNew.x, vecEndNew.y));
                clickComponent.listener.onClick((int) posX, (int) posY, 0);*/
            //}
            /*spriteBatch.draw(tex.region,
                t.position.x - originX, t.position.y - originY,
                originX, originY,
                width, height,
                PixelsToMeters(t.scale.x), PixelsToMeters(t.scale.y),
                t.rotation);*/
        }
        inputQueue.clear();
    }
}
