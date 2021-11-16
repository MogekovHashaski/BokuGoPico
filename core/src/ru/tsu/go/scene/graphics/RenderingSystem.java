package ru.tsu.go.scene.graphics;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ru.tsu.go.scene.world.WorldUnits;
import ru.tsu.go.scene.world.entity.TransformComponent;
import ru.tsu.go.scene.world.entity.util.ZComparator;

import java.util.Comparator;


public class RenderingSystem extends SortedIteratingSystem {
    //static final float PPM = 32.0f; // sets the amount of pixels each metre of box2d objects contains

    // this gets the height and width of our camera frustrum based off the width and height of the screen and our pixel per meter ratio
    /*static final float FRUSTUM_WIDTH = Gdx.graphics.getWidth()/PPM;
    static final float FRUSTUM_HEIGHT = Gdx.graphics.getHeight()/PPM;

    public static final float PIXELS_TO_METRES = 1.0f / PPM;

    private static Vector2 meterDimensions = new Vector2();
    private static Vector2 pixelDimensions = new Vector2();
    public static Vector2 getScreenSizeInMeters(){
        meterDimensions.set(Gdx.graphics.getWidth()*PIXELS_TO_METRES,
            Gdx.graphics.getHeight()*PIXELS_TO_METRES);
        return meterDimensions;
    }

    // static method to get screen size in pixels
    public static Vector2 getScreenSizeInPixesl() {
        pixelDimensions.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return pixelDimensions;
    }

    // convenience method to convert pixels to meters
    public static float PixelsToMeters(float pixelValue){
        return pixelValue * PIXELS_TO_METRES;
    }*/


    private final WorldUnits worldUnits;
    private final Comparator<Entity> comparator;
    private final SpriteBatch spriteBatch;
    private final OrthographicCamera camera;
    private final Array<Entity> renderQueue;
    private final ComponentMapper<TextureComponent> textureComponentMapper;
    private final ComponentMapper<TransformComponent> transformComponentMapper;

    public RenderingSystem(final WorldUnits worldUnits, final SpriteBatch spriteBatch, final OrthographicCamera camera) {
        this(worldUnits, spriteBatch, camera, Family.all(TransformComponent.class, TextureComponent.class).get(), new ZComparator(), 0);
    }

    /**
     * Instantiates a system that will iterate over the entities described by the Family.
     *
     * @param family     The family of entities iterated over in this System
     * @param comparator The comparator to sort the entities
     */
    public RenderingSystem(final WorldUnits worldUnits, final SpriteBatch spriteBatch, final OrthographicCamera camera, final Family family, final Comparator<Entity> comparator) {
        this(worldUnits, spriteBatch, camera, family, comparator, 0);
    }

    /**
     * Instantiates a system that will iterate over the entities described by the Family, with a specific priority.
     *
     * @param family     The family of entities iterated over in this System
     * @param comparator The comparator to sort the entities
     * @param priority   The priority to execute this system with (lower means higher priority)
     */
    public RenderingSystem(final WorldUnits worldUnits, final SpriteBatch spriteBatch, final OrthographicCamera camera, final Family family, final Comparator<Entity> comparator, final int priority) {
        super(family, comparator, priority);
        this.worldUnits = worldUnits;
        this.comparator = comparator;
        this.spriteBatch = spriteBatch;
        this.camera = camera;
        this.renderQueue = new Array<>();
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
        renderQueue.add(entity);
    }

    @Override
    public void update(final float deltaTime) {
        super.update(deltaTime);
        renderQueue.sort(comparator);
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.enableBlending();
        spriteBatch.begin();

        // loop through each entity in our render queue
        for (Entity entity : renderQueue) {
            TextureComponent tex = textureComponentMapper.get(entity);
            TransformComponent t = transformComponentMapper.get(entity);

            if (tex.region == null || t.isHidden) {
                continue;
            }

            final float width = tex.region.getRegionWidth();
            final float height = tex.region.getRegionHeight();

            final float originX = width/2f;
            final float originY = height/2f;

            spriteBatch.draw(tex.region,
                t.position.x - originX, t.position.y - originY,
                originX, originY,
                width, height,
                worldUnits.toWorldUnits(t.scale.x), worldUnits.toWorldUnits(t.scale.y),
                t.rotation);
        }
        spriteBatch.end();
        renderQueue.clear();
    }
}
