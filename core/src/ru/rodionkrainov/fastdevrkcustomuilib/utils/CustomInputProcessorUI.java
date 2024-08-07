package ru.rodionkrainov.fastdevrkcustomuilib.utils;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.Arrays;

import ru.rodionkrainov.fastdevrkcustomuilib.FastDevRKCustomUILib;

public final class CustomInputProcessorUI implements InputProcessor {
    private final boolean isDesktop;

    private int numIterationsToResetDownVPos = -1; // only for mobile (android and ios)

    private final boolean[][] pointersStates      = new boolean[4][3]; // [][0] -> is down; [][1] -> is up; [][2] -> is dragged
    private final Vector2 vPointerDownPosition    = new Vector2(-1, -1);
    private final Vector2 vPointerUpPosition      = new Vector2(-1, -1);
    private final Vector2 vPointerDraggedPosition = new Vector2(-1, -1);
    private final Vector2 vPointerMovePosition    = new Vector2(-1, -1);

    public CustomInputProcessorUI(boolean _isDesktop) {
        isDesktop = _isDesktop;
    }

    public static Vector3 unproject(float _x, float _y) {
        if (FastDevRKCustomUILib.isLibInit()) return FastDevRKCustomUILib.getStage().getCamera().unproject(new Vector3(_x, _y, 0));
        else return new Vector3(-1, -1, -1);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int _screenX, int _screenY, int _pointer, int button) {
        Vector3 unprojectV = unproject(_screenX, _screenY);
        vPointerDownPosition.set(unprojectV.x, unprojectV.y);

        vPointerUpPosition.set(-1, -1);
        vPointerDraggedPosition.set(-1, -1);

        if (_pointer < pointersStates.length) pointersStates[_pointer][0] = true; // is down

        return false;
    }

    @Override
    public boolean touchUp(int _screenX, int _screenY, int _pointer, int button) {
        Vector3 unprojectV = unproject(_screenX, _screenY);
        vPointerUpPosition.set(unprojectV.x, unprojectV.y);

        if (isDesktop) vPointerDownPosition.set(-1, -1);
        else numIterationsToResetDownVPos = 1;
        vPointerDraggedPosition.set(-1, -1);

        if (_pointer < pointersStates.length) pointersStates[_pointer][1] = true; // is up

        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int _screenX, int _screenY, int _pointer) {
        Vector3 unprojectV = unproject(_screenX, _screenY);
        vPointerDraggedPosition.set(unprojectV.x, unprojectV.y);

        vPointerUpPosition.set(-1, -1);
        vPointerMovePosition.set(vPointerDraggedPosition);

        if (_pointer < pointersStates.length) pointersStates[_pointer][2] = true; // is dragged

        return false;
    }

    @Override
    public boolean mouseMoved(int _screenX, int _screenY) {
        Vector3 unprojectV = unproject(_screenX, _screenY);
        if (isDesktop && vPointerDraggedPosition.x == -1 && vPointerDraggedPosition.y == -1) vPointerMovePosition.set(unprojectV.x, unprojectV.y);
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
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
