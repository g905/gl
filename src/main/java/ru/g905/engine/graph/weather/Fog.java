/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.g905.engine.graph.weather;

import org.joml.Vector3f;

/**
 *
 * @author zharnikov
 */
public class Fog {

    private boolean fogActive;
    private Vector3f color;
    private float density;

    public static Fog NOFOG = new Fog();

    public Fog() {
        fogActive = false;
        this.color = new Vector3f();
        this.density = 0;
    }

    public Fog(boolean fogActive, Vector3f color, float density) {
        this.fogActive = fogActive;
        this.color = color;
        this.density = density;
    }

    public boolean isFogActive() {
        return fogActive;
    }

    public void setFogActive(boolean fogActive) {
        this.fogActive = fogActive;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }
}
