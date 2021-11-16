package ru.tsu.go;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import io.netty.util.AttributeKey;
import lombok.val;
import ru.tsu.go.view.screen.DefaultScreenRepository;
import ru.tsu.go.view.screen.ScreenRepository;

import javax.inject.Inject;

public final class GoContext implements GameContext /*extends ContextWrapper*/ {

    private final ScreenRepository screenRepository;
    private Game game;

    /*public GoContext(final GameContext context, final ScreenRepository screenRepository) {
        //super(context);
        this.screenRepository = screenRepository;
    }*/

    @Inject
    public GoContext(final DefaultScreenRepository screenRepository) {
        this.screenRepository = screenRepository;
        //this.game = game;
    }

    @Override
    public <T extends Screen> void startScreen(final AttributeKey<T> screen) {
        val game = this.game;
        if (game != null) {
            game.setScreen(screenRepository.getScreen(screen));
        }
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void setGame(final Game game) {
        this.game = game;
    }
}
