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

    private static final Vector4f DEFAULT_COLOR = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

    private Vector4f ambientColor;

    private Vector4f diffuseСolor;

    private Vector4f specularСolor;

    private float reflectance;

    private Texture texture;

    private Texture normalMap;

    public Material() {
        this.ambientColor = DEFAULT_COLOR;
        this.diffuseСolor = DEFAULT_COLOR;
        this.specularСolor = DEFAULT_COLOR;
        this.texture = null;
        this.reflectance = 0;
    }

    public Material(Vector4f color, float reflectance) {
        this(color, color, color, null, reflectance);
    }

    public Material(Texture texture) {
        this(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR, texture, 0);
    }

    public Material(Texture texture, float reflectance) {
        this(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR, texture, reflectance);
    }

    public Material(Vector4f ambientСolor, Vector4f diffuseСolor, Vector4f specularСolor, Texture texture, float reflectance) {
        this.ambientColor = ambientСolor;
        this.diffuseСolor = diffuseСolor;
        this.specularСolor = specularСolor;
        this.texture = texture;
        this.reflectance = reflectance;
    }

    public Vector4f getAmbientColor() {
        return ambientColor;
    }

    public void setAmbientColor(Vector4f ambientСolor) {
        this.ambientColor = ambientСolor;
    }

    public Vector4f getDiffuseColor() {
        return diffuseСolor;
    }

    public void setDiffuseColor(Vector4f diffuseСolor) {
        this.diffuseСolor = diffuseСolor;
    }

    public Vector4f getSpecularColor() {
        return specularСolor;
    }

    public void setSpecularColor(Vector4f specularСolor) {
        this.specularСolor = specularСolor;
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

    public boolean hasNormalMap() {
        return this.normalMap != null;
    }

    public Texture getNormalMap() {
        return normalMap;
    }

    public void setNormalMap(Texture normalMap) {
        this.normalMap = normalMap;
    }

}
