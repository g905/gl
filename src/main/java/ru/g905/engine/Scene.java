/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.g905.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.g905.engine.graph.Mesh;

/**
 *
 * @author g905
 */
public class Scene {

    private Map<Mesh, List<GameItem>> meshMap;

    private SkyBox skyBox;

    private SceneLight sceneLight;

    public Scene() {
        meshMap = new HashMap();
    }

    public Map<Mesh, List<GameItem>> getGameMeshes() {
        return meshMap;
    }

    public void setGameItems(ArrayList<GameItem> gameItems) {
        int numGameItems = gameItems != null ? gameItems.size() : 0;
        for (GameItem gameItem : gameItems) {
            Mesh mesh = gameItem.getMesh();
            List<GameItem> list = meshMap.get(mesh);
            if (list == null) {
                list = new ArrayList<>();
                meshMap.put(mesh, list);
            }
            list.add(gameItem);
        }
    }

    public SkyBox getSkyBox() {
        return skyBox;
    }

    public void setSkyBox(SkyBox skyBox) {
        this.skyBox = skyBox;
    }

    public SceneLight getSceneLight() {
        return sceneLight;
    }

    public void setSceneLight(SceneLight sceneLight) {
        this.sceneLight = sceneLight;
    }

}
