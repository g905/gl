/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.g905.engine;

import org.joml.Vector3f;
import ru.g905.engine.graph.lights.DirectionalLight;
import ru.g905.engine.graph.lights.PointLight;
import ru.g905.engine.graph.lights.SpotLight;

/**
 *
 * @author zharnikov
 */
public class SceneLight {

    private Vector3f ambientLight;

    private Vector3f skyBoxLight;

    private PointLight[] pointLightList;

    private SpotLight[] spotLightList;

    private DirectionalLight dirLight;

    public Vector3f getAmbientLight() {
        return ambientLight;
    }

    public void setAmbientLight(Vector3f ambientLight) {
        this.ambientLight = ambientLight;
    }

    public PointLight[] getPointLightList() {
        return pointLightList;
    }

    public void setPointLightList(PointLight[] pointLightList) {
        this.pointLightList = pointLightList;
    }

    public SpotLight[] getSpotLightList() {
        return spotLightList;
    }

    public void setSpotLightList(SpotLight[] spotLightList) {
        this.spotLightList = spotLightList;
    }

    public DirectionalLight getDirLight() {
        return dirLight;
    }

    public void setDirLight(DirectionalLight directionalLight) {
        this.dirLight = directionalLight;
    }

    public Vector3f getSkyBoxLight() {
        return skyBoxLight;
    }

    public void setSkyBoxLight(Vector3f skyBoxLight) {
        this.skyBoxLight = skyBoxLight;
    }

}
