/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.g905.engine;

import org.joml.Vector3f;
import ru.g905.engine.graph.DirectionalLight;
import ru.g905.engine.graph.PointLight;
import ru.g905.engine.graph.SpotLight;

/**
 *
 * @author zharnikov
 */
public class SceneLight {

    private Vector3f ambientLight;

    private PointLight[] pointLightList;

    private SpotLight[] spotLightList;

    private DirectionalLight dirLight;

    public Vector3f getAmbientLight() {
        return ambientLight;
    }

    public void setAmbientLight(Vector3f ambientLight) {
        this.ambientLight = ambientLight;
    }

    public DirectionalLight getDirLight() {
        return dirLight;
    }

    public void setDirLight(DirectionalLight dirLight) {
        this.dirLight = dirLight;
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
}
