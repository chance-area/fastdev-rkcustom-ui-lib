package ru.rodionkrainov.libgdxrkcustomuilib.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.Arrays;

public class CustomClickListener extends ClickListener {
    private final boolean isDesktop;

    private int numIterationsToResetDownVPos = -1; // only for mobile (android and ios)

    private final boolean[][] pointersStates      = new boolean[4][3]; // [][0] -> is down; [][1] -> is up; [][2] -> is dragged
    private final Vector2 vPointerDownPosition    = new Vector2(-1, -1);
    private final Vector2 vPointerUpPosition      = new Vector2(-1, -1);
    private final Vector2 vPointerDraggedPosition = new Vector2(-1, -1);
    private final Vector2 vPointerMovePosition    = new Vector2(-1, -1);

    public CustomClickListener(boolean _isDesktop) {
        isDesktop = _isDesktop;
    }

    @Override
    public boolean touchDown(InputEvent _event, float _x, float _y, int _pointer, int _button) {
        vPointerDownPosition.set(_x, _y);

        vPointerUpPosition.set(-1, -1);
        vPointerDraggedPosition.set(-1, -1);

        if (_pointer < pointersStates.length) pointersStates[_pointer][0] = true; // is down

        return super.touchDown(_event, _x, _y, _pointer, _button);
    }

    @Override
    public void touchUp(InputEvent _event, float _x, float _y, int _pointer, int _button) {
        vPointerUpPosition.set(_x, _y);

        if (isDesktop) vPointerDownPosition.set(-1, -1);
        else numIterationsToResetDownVPos = 1;
        vPointerDraggedPosition.set(-1, -1);

        if (_pointer < pointersStates.length) pointersStates[_pointer][1] = true; // is up

        super.touchUp(_event, _x, _y, _pointer, _button);
    }

    @Override
    public void touchDragged(InputEvent _event, float _x, float _y, int _pointer) {
        vPointerDraggedPosition.set(_x, _y);

        vPointerUpPosition.set(-1, -1);
        vPointerMovePosition.set(vPointerDraggedPosition);

        if (_pointer < pointersStates.length) pointersStates[_pointer][2] = true; // is dragged

        super.touchDragged(_event, _x, _y, _pointer);
    }

    @Override
    public boolean mouseMoved(InputEvent _event, float _x, float _y) {
        if (isDesktop && vPointerDraggedPosition.x == -1 && vPointerDraggedPosition.y == -1) vPointerMovePosition.set(_x, _y);
        return super.mouseMoved(_event, _x, _y);
    }


    /* ---------------------------------------------------------
    -------------------------- GETTERS -------------------------
    ------------------------------------------------------------ */

    public Vector2 getVecPointerDownPosition() {
        return vPointerDownPosition;
    }

    public Vector2 getVecPointerUpPosition() {
        return vPointerUpPosition;
    }

    public Vector2 getVecPointerDraggedPosition() {
        return vPointerDraggedPosition;
    }

    public Vector2 getVecPointerMovePosition() {
        return vPointerMovePosition;
    }

    public boolean[][] getPointersStates(boolean _isReset) {
        boolean[][] tpmStates = new boolean[ pointersStates.length ][ pointersStates[0].length ];
        for (int i = 0; i < tpmStates.length; i++) System.arraycopy(pointersStates[i], 0, tpmStates[i], 0, tpmStates[i].length);

        if (_isReset) resetPointersStates();

        return tpmStates;
    }
    public boolean[][] getPointersStates() {
        return getPointersStates(true);
    }


    /* ---------------------------------------------------------
    --------------------------- OTHER --------------------------
    ------------------------------------------------------------ */

    public void update() {
        // on mobile systems don't work event 'mouseMoved'
        if (!isDesktop) {
            vPointerMovePosition.set(vPointerDownPosition);

            if (numIterationsToResetDownVPos == 0) {
                numIterationsToResetDownVPos = -1;
                vPointerDownPosition.set(-1, -1);
            } else numIterationsToResetDownVPos--;
        }
    }

    public void resetPointersStates() {
        for (boolean[] pointerState : pointersStates) Arrays.fill(pointerState, false);
    }
}
