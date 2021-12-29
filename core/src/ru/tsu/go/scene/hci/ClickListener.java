package ru.tsu.go.scene.hci;

@FunctionalInterface
public interface ClickListener {

    void onClick(final float x, final float y, final float maxX, final float maxY, int button);
}
