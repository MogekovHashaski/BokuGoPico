package ru.tsu.go.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.tsu.go.GameContext;
import ru.tsu.go.GoGame;
import ru.tsu.go.di.component.DaggerGameContextComponent;
import ru.tsu.go.di.component.GameContextComponent;

public final class DesktopLauncher {

    public static void main(final String[] arg) {
        final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        final GameContextComponent component = DaggerGameContextComponent.builder().withPixelToWorldUnitRatio(32).withWorldWidth(5).withWorldHeight(5).build();
        final GameContext context = component.gameContext();
        final GoGame game = component.game();
        context.setGame(game);
        game.setScreenFactory(component.screenFactory());
       // final TestScreen screen = component.testScreen();
        new LwjglApplication(game, config);
    }
}
