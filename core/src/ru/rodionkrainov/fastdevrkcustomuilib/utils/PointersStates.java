package ru.rodionkrainov.fastdevrkcustomuilib.utils;

public class PointersStates {
    private final boolean isDown;
    private final boolean isUp;
    private final boolean isDragged;

    public PointersStates(boolean _isDown, boolean _isUp, boolean _isDragged) {
        isDown    = _isDown;
        isUp      = _isUp;
        isDragged = _isDragged;
    }

    public boolean isDown() {
        return isDown;
    }

    public boolean isUp() {
        return isUp;
    }

    boolean isDragged() {
        return isDragged;
    }
}
