package ru.tsu.go.di.component;

import dagger.BindsInstance;
import dagger.Component;
import ru.tsu.go.GoContext;
import ru.tsu.go.GoGame;
import ru.tsu.go.di.PixelWorldUnitRatio;
import ru.tsu.go.di.WorldHeight;
import ru.tsu.go.di.WorldWidth;
import ru.tsu.go.di.module.GameModule;
import ru.tsu.go.di.scope.GoScreenScope;
import ru.tsu.go.view.screen.ScreenFactory;
import ru.tsu.go.view.screen.game.GoScreen;

import javax.inject.Singleton;

@GoScreenScope
@Component(modules = {GameModule.class})
public interface GameContextComponent {

    GoContext gameContext();
    GoGame game();
    ScreenFactory<GoScreen> screenFactory();

    @Component.Builder
    interface Builder {
        /*@BindsInstance
        Builder withInitialWidth(@InitialWidth final int initialWidth);
        @BindsInstance
        Builder withInitialHeight(@InitialHeight final int initialHeight);*/
        @BindsInstance
        Builder withWorldWidth(@WorldWidth final float worldWidth);
        @BindsInstance
        Builder withWorldHeight(@WorldHeight final float worldHeight);
        @BindsInstance
        Builder withPixelToWorldUnitRatio(@PixelWorldUnitRatio final float pixelToWorldUnitRatio);

        GameContextComponent build();
    }
}
