/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.g905.gl;

import org.joml.Intersectionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import ru.g905.engine.graph.Camera;
import ru.g905.engine.items.GameItem;

/**
 *
 * @author zharnikov
 */
public class CameraBoxSelectionDetector {

    private final Vector3f max;

    private final Vector3f min;

    private final Vector2f nearFar;

    private Vector3f dir;

    public CameraBoxSelectionDetector() {
        dir = new Vector3f();
        this.max = new Vector3f();
        this.min = new Vector3f();
        this.nearFar = new Vector2f();
    }

    public void selectGameItem(GameItem[] gameItems, Camera camera) {
        GameItem selectedGameItem = null;
        float closestDistance = Float.POSITIVE_INFINITY;

        dir = camera.getViewMatrix().positiveZ(dir).negate();
        for (GameItem gameItem : gameItems) {
            gameItem.setSelected(false);
            min.set(gameItem.getPosition());
            max.set(gameItem.getPosition());
            min.add(-gameItem.getScale(), -gameItem.getScale(), -gameItem.getScale());
            max.add(gameItem.getScale(), gameItem.getScale(), gameItem.getScale());
            if (Intersectionf.intersectRayAab(camera.getPosition(), dir, min, max, nearFar) && nearFar.x < closestDistance) {
                closestDistance = nearFar.x;
                selectedGameItem = gameItem;
            }
        }

        if (selectedGameItem != null) {
            selectedGameItem.setSelected(true);
        }
    }
}
