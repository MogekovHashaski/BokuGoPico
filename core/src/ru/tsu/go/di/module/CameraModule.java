package ru.tsu.go.di.module;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dagger.Module;
import dagger.Provides;
import ru.tsu.go.di.scope.GoScreenScope;
import ru.tsu.go.scene.graphics.CameraComponent;

import javax.annotation.Nullable;


@Module
public class CameraModule {



    /*@Provides
    @GoScreenScope
    CameraComponent provideCameraComponent(final OrthographicCamera camera, final Viewport viewport) {
        final CameraComponent cameraComponent = new CameraComponent();
        cameraComponent.camera = camera;
        cameraComponent.viewport = viewport;
        return cameraComponent;
    }*/

    /*@Provides
    @GoScreenScope
    Viewport provideViewport(OrthographicCamera camera) {
       final float PPM = 32.0f;
        final float FRUSTUM_WIDTH = Gdx.graphics.getWidth()/PPM;
        final float FRUSTUM_HEIGHT = Gdx.graphics.getHeight()/PPM;
        final float PIXELS_TO_METRES = 1.0f / PPM;
        //FRUSTUM_WIDTH / 2f, FRUSTUM_HEIGHT / 2f
        final Viewport viewport = new StretchViewport(200, 200, camera);
        viewport.apply();
        return viewport;
    }*/
}
