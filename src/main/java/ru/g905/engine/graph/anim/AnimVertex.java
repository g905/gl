/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.g905.engine.graph.anim;

import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 *
 * @author g905
 */
public class AnimVertex {

    public Vector3f position;
    public Vector2f textCoords;
    public Vector3f normal;
    public float[] weights;
    public int[] jointIndices;

    public AnimVertex() {
        super();
        normal = new Vector3f();
    }
}
