/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.g905.engine.graph;

/**
 *
 * @author g905
 */
import org.joml.Vector4f;

public class Material {

    private static final Vector4f DEFAULT_color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

    private Vector4f ambientcolor;

    private Vector4f diffusecolor;

    private Vector4f specularcolor;

    private float reflectance;

    private Texture texture;

    public Material() {
        this.ambientcolor = DEFAULT_color;
        this.diffusecolor = DEFAULT_color;
        this.specularcolor = DEFAULT_color;
        this.texture = null;
        this.reflectance = 0;
    }

    public Material(Vector4f color, float reflectance) {
        this(color, color, color, null, reflectance);
    }

    public Material(Texture texture) {
        this(DEFAULT_color, DEFAULT_color, DEFAULT_color, texture, 0);
    }

    public Material(Texture texture, float reflectance) {
        this(DEFAULT_color, DEFAULT_color, DEFAULT_color, texture, reflectance);
    }

    public Material(Vector4f ambientcolor, Vector4f diffusecolor, Vector4f specularcolor, Texture texture, float reflectance) {
        this.ambientcolor = ambientcolor;
        this.diffusecolor = diffusecolor;
        this.specularcolor = specularcolor;
        this.texture = texture;
        this.reflectance = reflectance;
    }

    public Vector4f getAmbientcolor() {
        return ambientcolor;
    }

    public void setAmbientcolor(Vector4f ambientcolor) {
        this.ambientcolor = ambientcolor;
    }

    public Vector4f getDiffusecolor() {
        return diffusecolor;
    }

    public void setDiffusecolor(Vector4f diffusecolor) {
        this.diffusecolor = diffusecolor;
    }

    public Vector4f getSpecularcolor() {
        return specularcolor;
    }

    public void setSpecularcolor(Vector4f specularcolor) {
        this.specularcolor = specularcolor;
    }

    public float getReflectance() {
        return reflectance;
    }

    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }

    public boolean isTextured() {
        return this.texture != null;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

}
