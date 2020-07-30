/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.g905.gl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;
import ru.g905.engine.GameItem;
import ru.g905.engine.IGameLogic;
import ru.g905.engine.MouseInput;
import ru.g905.engine.Scene;
import ru.g905.engine.SceneLight;
import ru.g905.engine.SkyBox;
import ru.g905.engine.Window;
import ru.g905.engine.graph.Camera;
import ru.g905.engine.graph.DirectionalLight;
import ru.g905.engine.graph.Material;
import ru.g905.engine.graph.Mesh;
import ru.g905.engine.graph.ObjLoader;
import ru.g905.engine.graph.PointLight;
import ru.g905.engine.graph.Renderer;
import ru.g905.engine.graph.SpotLight;
import ru.g905.engine.graph.Texture;

public class DummyGame implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private Scene scene;

    private Hud hud;

    private float lightAngle;

    private static final float CAMERA_POS_STEP = 0.05f;

    private float spotAngle = 0;
    private float spotInc = 2f;

    public DummyGame() {
        renderer = new Renderer();

        camera = new Camera();
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
        lightAngle = -90;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        scene = new Scene();

        float reflectance = 1f;
        //Mesh mesh = OBJLoader.loadMesh("/models/bunny.obj");
        //Material material = new Material(new Vector3f(0.2f, 0.5f, 0.5f), reflectance);

        Mesh mesh = ObjLoader.loadMesh("models/cube.obj");
        Texture texture = new Texture("src/main/resources/textures/grassblock.png");
        Material material = new Material(texture, reflectance);
        mesh.setMaterial(material);

        float blockScale = 0.5f;
        float skyBoxScale = 150.0f;
        float extension = 2.0f;

        float startx = extension * (-skyBoxScale + blockScale);
        float startz = extension * (skyBoxScale - blockScale);
        float starty = -1.0f;
        float incv = blockScale * 2;

        float posx = startx;
        float posz = startz;
        float incy = 0.0f;
        int NUM_ROWS = (int) (extension * skyBoxScale * 2 / incv);
        int NUM_COLS = (int) (extension * skyBoxScale * 2 / incv);
        ArrayList<GameItem> gameItems = new ArrayList<>();

        float z = 0;
        float inc = 0.1f;
        float inc2 = 0.1f;
        for (int i = 0; i < 100; ++i) {
            if (i < 50) {
                z += inc;
            } else {
                z -= inc;
            }
            for (int j = 0; j < 100; ++j) {
                if (j < 50) {
                    z += inc2;
                } else {
                    z -= inc2;
                }
                GameItem gamei = new GameItem(mesh);
                gamei.setPosition(i, z * z, j);
                gamei.setScale(0.5f);
                gameItems.add(gamei);
            }
        }
        /*
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                GameItem gameItem = new GameItem(mesh);
                gameItem.setScale(blockScale);
                incy = Math.random() > 0.9f ? blockScale * 2 : 0f;
                gameItem.setPosition(posx, starty + incy, posz);
                gameItems[i * NUM_COLS + j] = gameItem;

                posx += inc;
            }
            posx = startx;
            posz -= inc;
        }*/
        scene.setGameItems(gameItems);

        SkyBox skyBox = new SkyBox("models/skybox.obj", "src/main/resources/textures/skybox.png");
        skyBox.setScale(skyBoxScale);
        scene.setSkyBox(skyBox);

        setupLights();
        /*GameItem gameItem = new GameItem(mesh);
        gameItem.setScale(0.5f);
        gameItem.setPosition(0, 0, -2);
        gameItems.add(gameItem);
        gameItems = new GameItem[]{gameItem};*/

        //hud
        hud = new Hud("DEMO");

        camera.getPosition().x = 0.65f;
        camera.getPosition().y = 1.15f;
        camera.getPosition().z = 4.34f;
    }

    private void setupLights() {
        SceneLight sceneLight = new SceneLight();

        sceneLight.setAmbientLight(new Vector3f(1f, 1f, 1f));

        //Point light
        Vector3f lightPosition = new Vector3f(0, 0, 1);
        float lightIntensity = 5.0f;

        PointLight pointLight = new PointLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(att);

        sceneLight.setPointLightList(new PointLight[]{pointLight});

        //Spot light
        lightPosition = new Vector3f(10f, 30.0f, 5f);
        PointLight sl_pointLight = new PointLight(new Vector3f(1, 0, 0), lightPosition, 14.0f);
        PointLight sl_pointLight2 = new PointLight(new Vector3f(0, 1, 0), lightPosition, lightIntensity);
        att = new PointLight.Attenuation(0.0f, 0.0f, 0.001f);
        sl_pointLight.setAttenuation(att);
        Vector3f coneDir = new Vector3f(0, -1, 0);
        float cutoff = (float) Math.cos(Math.toRadians(10));

        SpotLight spotLight = new SpotLight(sl_pointLight, coneDir, (float) 1.9);

        SpotLight sl2 = new SpotLight(sl_pointLight2, coneDir, cutoff);

        sceneLight.setSpotLightList(new SpotLight[]{spotLight, new SpotLight(sl2)});

        lightPosition = new Vector3f(-1, 0, 0);
        sceneLight.setDirLight(new DirectionalLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity));
        scene.setSceneLight(sceneLight);
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 1;
        }

        SceneLight sceneLight = scene.getSceneLight();

        Vector3f lightPos = sceneLight.getSpotLightList()[0].getPointLight().getPosition();
        if (window.isKeyPressed(GLFW_KEY_N)) {
            sceneLight.getSpotLightList()[0].getPointLight().getPosition().z = lightPos.z + 0.1f;
        } else if (window.isKeyPressed(GLFW_KEY_M)) {
            sceneLight.getSpotLightList()[0].getPointLight().getPosition().z = lightPos.z - 0.1f;
        } else if (window.isKeyPressed(GLFW_KEY_J)) {
            sceneLight.getSpotLightList()[0].getPointLight().getPosition().y = lightPos.y + 0.1f;
        } else if (window.isKeyPressed(GLFW_KEY_H)) {
            sceneLight.getSpotLightList()[0].getPointLight().getPosition().y = lightPos.y - 0.1f;
        } else if (window.isKeyPressed(GLFW_KEY_U)) {
            sceneLight.getSpotLightList()[0].getPointLight().getPosition().x = lightPos.x + 0.1f;
        } else if (window.isKeyPressed(GLFW_KEY_Y)) {
            sceneLight.getSpotLightList()[0].getPointLight().getPosition().x = lightPos.x - 0.1f;
        }
        /*
        float plLightPos = pointLight.getPosition().z;
        if (window.isKeyPressed(GLFW_KEY_K)) {
            this.pointLight.getPosition().z = plLightPos + 0.1f;
        } else if (window.isKeyPressed(GLFW_KEY_L)) {
            this.pointLight.getPosition().z = plLightPos - 0.1f;
        }

        Vector3f lightPos = spotLight.getPointLight().getPosition();
        if (window.isKeyPressed(GLFW_KEY_N)) {
            this.spotLight.getPointLight().getPosition().z = lightPos.z + 0.1f;
        } else if (window.isKeyPressed(GLFW_KEY_M)) {
            this.spotLight.getPointLight().getPosition().z = lightPos.z - 0.1f;
        } else if (window.isKeyPressed(GLFW_KEY_J)) {
            this.spotLight.getPointLight().getPosition().y = lightPos.y + 0.1f;
        } else if (window.isKeyPressed(GLFW_KEY_H)) {
            this.spotLight.getPointLight().getPosition().y = lightPos.y - 0.1f;
        } else if (window.isKeyPressed(GLFW_KEY_U)) {
            this.spotLight.getPointLight().getPosition().x = lightPos.x + 0.1f;
        } else if (window.isKeyPressed(GLFW_KEY_Y)) {
            this.spotLight.getPointLight().getPosition().x = lightPos.x - 0.1f;
        }

        Vector3f coneDir = spotLight.getConeDirection();
        if (window.isKeyPressed(GLFW_KEY_R)) {
            this.spotLight.getConeDirection().x = coneDir.x + 0.01f;
        } else if (window.isKeyPressed(GLFW_KEY_T)) {
            this.spotLight.getConeDirection().x = coneDir.x - 0.01f;
        } else if (window.isKeyPressed(GLFW_KEY_F)) {
            this.spotLight.getConeDirection().y = coneDir.y + 0.01f;
        } else if (window.isKeyPressed(GLFW_KEY_G)) {
            this.spotLight.getConeDirection().y = coneDir.y - 0.01f;
        } else if (window.isKeyPressed(GLFW_KEY_C)) {
            this.spotLight.getConeDirection().z = coneDir.z + 0.01f;
        } else if (window.isKeyPressed(GLFW_KEY_V)) {
            this.spotLight.getConeDirection().z = coneDir.z - 0.01f;
        }

        float currCutoff = spotLight.getCutOff();
        if (window.isKeyPressed(GLFW_KEY_Q)) {
            this.spotLight.setCutOff(0.01f + currCutoff);
        } else if (window.isKeyPressed(GLFW_KEY_E)) {
            this.spotLight.setCutOff(currCutoff - 0.01f);
        }
         */

        Vector3f sl = sceneLight.getSpotLightList()[0].getPointLight().getPosition();
        if (window.isKeyPressed(GLFW_KEY_R)) {
            sceneLight.getSpotLightList()[0].getPointLight().getPosition().x = sl.x + 0.1f;
        } else if (window.isKeyPressed(GLFW_KEY_T)) {
            sceneLight.getSpotLightList()[0].getPointLight().getPosition().x = sl.x - 0.1f;
        } else if (window.isKeyPressed(GLFW_KEY_Y)) {
            sceneLight.getSpotLightList()[0].getPointLight().getPosition().z = sl.z + 0.1f;
        } else if (window.isKeyPressed(GLFW_KEY_U)) {
            sceneLight.getSpotLightList()[0].getPointLight().getPosition().z = sl.z - 0.1f;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        // Update camera based on mouse
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        hud.rotateCompass(camera.getRotation().y);

        spotAngle += spotInc;
        if (spotAngle > 360) {
            spotAngle = 0;
        }

        double spotAngleRad = Math.toRadians(spotAngle);
        Vector3f coneDir = scene.getSceneLight().getSpotLightList()[0].getConeDirection();
        Vector3f coneDir2 = scene.getSceneLight().getSpotLightList()[1].getConeDirection();
        coneDir.x = (float) (Math.cos(spotAngleRad) / 7);
        coneDir.z = (float) (Math.sin(spotAngleRad) / 7);

        coneDir2.x = (float) (Math.cos(spotAngleRad - 180) / 7);
        coneDir2.z = (float) (Math.sin(spotAngleRad - 180) / 7);

        DirectionalLight dirLight = scene.getSceneLight().getDirLight();
        lightAngle += 0.03f;
        if (lightAngle > 90) {
            dirLight.setIntensity(0);
            if (lightAngle >= 360) {
                lightAngle = -90;
            }
        } else if (lightAngle <= -80 || lightAngle >= 80) {
            float factor = 1 - (float) (Math.abs(lightAngle) - 80) / 10.0f;
            dirLight.setIntensity(factor);
            dirLight.getColor().y = Math.max(factor, 0.2f);
            dirLight.getColor().z = Math.max(factor, 0.1f);
        } else {
            dirLight.setIntensity(1);
            dirLight.getColor().x = 1;
            dirLight.getColor().y = 1;
            dirLight.getColor().z = 1;
        }
        double angRad = Math.toRadians(lightAngle);
        dirLight.getDirection().x = (float) Math.sin(angRad);
        dirLight.getDirection().y = (float) Math.cos(angRad);
        hud.setStatusText("cone cutoff: " + scene.getSceneLight().getSpotLightList()[0].getCutOff());

    }

    @Override
    public void render(Window window) {
        hud.updateSize(window);
        renderer.render(window, camera, scene, hud);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        Map<Mesh, List<GameItem>> mapMeshes = scene.getGameMeshes();
        for (Mesh mesh : mapMeshes.keySet()) {
            mesh.cleanUp();
        }
        hud.cleanup();
    }

}
