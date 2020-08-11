/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.g905.gl;

import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector3f;
import org.joml.Vector4f;
import ru.g905.engine.Window;
import ru.g905.engine.graph.Camera;
import ru.g905.engine.items.GameItem;

/**
 *
 * @author zharnikov
 */
public class MouseBoxSelectionDetector extends CameraBoxSelectionDetector {

    private final Matrix4f invProjectionMatrix;

    private final Matrix4f invViewMatrix;

    private final Vector3f mouseDir;

    private final Vector4f tmpVec;

    public MouseBoxSelectionDetector() {
        super();
        this.invProjectionMatrix = new Matrix4f();
        this.invViewMatrix = new Matrix4f();
        this.mouseDir = new Vector3f();
        this.tmpVec = new Vector4f();
    }

    public boolean selectGameItem(GameItem[] gameItems, Window window, Vector2d mousePos, Camera camera) {
        int wdwWidth = window.getWidth();
        int wdwHeight = window.getHeight();

        float x = (float) (2 * mousePos.x) / (float) wdwWidth - 1.0f;
        float y = 1.0f - (float) (2 * mousePos.y) / (float) wdwHeight;
        float z = -1.0f;

        invProjectionMatrix.set(window.getProjectionMatrix());
        invProjectionMatrix.invert();

        tmpVec.set(x, y, z, 1.0f);
        tmpVec.mul(invProjectionMatrix);
        tmpVec.z = -1.0f;
        tmpVec.w = 0.0f;

        Matrix4f viewMatrix = camera.getViewMatrix();
        invViewMatrix.set(viewMatrix);
        invViewMatrix.invert();
        tmpVec.mul(invViewMatrix);

        mouseDir.set(tmpVec.x, tmpVec.y, tmpVec.z);

        return selectGameItem(gameItems, camera.getPosition(), mouseDir);
    }

}
