package ru.tsu.go;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@RequiredArgsConstructor
public abstract class ContextWrapper implements GameContext {

    @Delegate(types = GameContext.class)
    private final GameContext context;
}
