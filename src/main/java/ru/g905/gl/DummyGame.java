/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.g905.gl;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import static org.lwjgl.glfw.GLFW.*;
import ru.g905.engine.IGameLogic;
import ru.g905.engine.MouseInput;
import ru.g905.engine.Scene;
import ru.g905.engine.SceneLight;
import ru.g905.engine.Window;
import ru.g905.engine.graph.Camera;
import ru.g905.engine.graph.Material;
import ru.g905.engine.graph.Mesh;
import ru.g905.engine.graph.ObjLoader;
import ru.g905.engine.graph.Renderer;
import ru.g905.engine.graph.Texture;
import ru.g905.engine.graph.lights.DirectionalLight;
import ru.g905.engine.graph.lights.PointLight;
import ru.g905.engine.graph.lights.SpotLight;
import ru.g905.engine.graph.weather.Fog;
import ru.g905.engine.items.GameItem;
import ru.g905.engine.items.SkyBox;
import ru.g905.engine.items.Terrain;

public class DummyGame implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private Scene scene;

    private Hud hud;

    private float lightAngle;

    private static final float CAMERA_POS_STEP = 0.1f;
    private static final float FREE_FALL = 3.2f;

    private float spotAngle = 0;
    private float y0 = 0;
    private float spotInc = 2f;

    private GameItem g;

    private Vector3f[] l;

    private Terrain terrain;

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

        float reflectance = 0.65f;
        Texture normalMap = new Texture("src/main/resources/textures/rock_normals.png");

        Mesh quadMesh1 = ObjLoader.loadMesh("models/quad.obj");
        Texture texture = new Texture("src/main/resources/textures/rock.png");
        Material quadMaterial = new Material(texture, reflectance);
        quadMesh1.setMaterial(quadMaterial);

        GameItem quadItem = new GameItem(quadMesh1);
        quadItem.setPosition(0, -27, -2f);
        quadItem.setScale(2.0f);
        quadItem.setRotation(0, 90, 0);

        Mesh quadMesh2 = ObjLoader.loadMesh("models/quad.obj");
        Material quMaterial2 = new Material(texture, reflectance);
        quMaterial2.setNormalMap(normalMap);
        quadMesh2.setMaterial(quMaterial2);
        GameItem quadItem2 = new GameItem(quadMesh2);
        quadItem2.setPosition(0, -27, 2f);
        quadItem2.setScale(2.0f);
        quadItem2.setRotation(0, 90, 0);

        float skyBoxScale = 50.0f;
        float terrainScale = 100;
        int terrainSize = 1;
        float minY = -0.1f;
        float maxY = 0.1f;
        int textInc = 10;
        terrain = new Terrain(terrainSize, terrainScale, minY, maxY, "src/main/resources/textures/heightmap3.png", "src/main/resources/textures/terrain7.jpg", textInc);
        scene.setGameItems(terrain.getGameItems());
        scene.setGameItems(new GameItem[]{quadItem, quadItem2});

        SkyBox skyBox = new SkyBox("models/skybox_1.obj", "src/main/resources/textures/skybox7.jpg");
        skyBox.setScale(skyBoxScale);
        scene.setSkyBox(skyBox);

        setupLights();

        scene.setFog(new Fog(true, new Vector3f(0.5f, 0.5f, 0.5f), 0.02f));

        //hud
        hud = new Hud("DEMO");

        camera.getPosition().x = 0.0f;
        camera.getPosition().y = 5.0f;
        camera.getPosition().z = 0.0f;
        camera.getRotation().x = 90;
    }

    private void setupLights() throws Exception {
        SceneLight sceneLight = new SceneLight();
        scene.setSceneLight(sceneLight);

        sceneLight.setAmbientLight(new Vector3f(0.3f, 0.3f, 0.3f));
        sceneLight.setSkyBoxLight(new Vector3f(1f, 1f, 1f));

        //Point light
        Vector3f lightPosition = new Vector3f(0, 0, 1);
        float lightIntensity = 1.0f;

        PointLight pointLight = new PointLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(att);

        sceneLight.setPointLightList(new PointLight[]{pointLight});

        //Spot light
        lightPosition = new Vector3f(10f, 30.0f, 5f);
        PointLight sl_pointLight = new PointLight(new Vector3f(1, 0, 0), lightPosition, 14.0f);
        PointLight sl_pointLight2 = new PointLight(new Vector3f(0, 1, 0), lightPosition, lightIntensity);
        PointLight sl_pointLight3 = new PointLight(new Vector3f(0.922f, 0.204f, 0.537f), new Vector3f(0, -26, 0), 2.0f);

        att = new PointLight.Attenuation(0.0f, 0.0f, 0.001f);
        sl_pointLight.setAttenuation(att);
        Vector3f coneDir = new Vector3f(0, -1, 0);
        float cutoff = (float) Math.cos(Math.toRadians(10));

        SpotLight spotLight = new SpotLight(sl_pointLight, coneDir, (float) 1.9);

        SpotLight sl2 = new SpotLight(sl_pointLight2, coneDir, cutoff);

        SpotLight sl3 = new SpotLight(sl_pointLight3, coneDir, cutoff);

        sceneLight.setSpotLightList(new SpotLight[]{spotLight, new SpotLight(sl2), new SpotLight(sl3)});

        Mesh quadMesh1 = ObjLoader.loadMesh("models/cube.obj");
        //Texture texture = new Texture("src/main/resources/textures/grassblock.png");
        Material quadMaterial = new Material();
        quadMesh1.setMaterial(quadMaterial);

        g = new GameItem(quadMesh1);
        g.setScale(0.3f);
        g.setRotation(sl3.getConeDirection().x, sl3.getConeDirection().y, sl3.getConeDirection().z);

        g.setPosition(sl3.getPointLight().getPosition().x, sl3.getPointLight().getPosition().y, sl3.getPointLight().getPosition().z);

        g.getMesh().getMaterial().setAmbientColor(new Vector4f(0.922f, 0.204f, 0.537f, 1.0f));

        scene.setGameItems(new GameItem[]{g});

        lightPosition = new Vector3f(-1, 0, 0);
        sceneLight.setDirLight(new DirectionalLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity));
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

        SceneLight sl = scene.getSceneLight();
        SpotLight s = sl.getSpotLightList()[2];

        float oldCutoff = s.getCutOff();
        Vector3f p = s.getPointLight().getPosition();
        g.setPosition(p.x, p.y, p.z);
        //g.setRotation(s.getConeDirection().x, s.getConeDirection().y, s.getConeDirection().z);
        if (window.isKeyPressed(GLFW_KEY_L)) {
            s.getPointLight().setPosition(new Vector3f(p.x + 0.1f, 0, 0));
        } else if (window.isKeyPressed(GLFW_KEY_O)) {
            s.getConeDirection().x = s.getConeDirection().x + 0.01f;
        } else if (window.isKeyPressed(GLFW_KEY_T)) {
            s.setCutOff(oldCutoff + 0.1f);
        } else if (window.isKeyPressed(GLFW_KEY_R)) {
            s.setCutOff(oldCutoff - 0.1f);
        }

    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
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

        // Update camera position
        Vector3f prevPos = new Vector3f(camera.getPosition());
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        y0 += (FREE_FALL * FREE_FALL) / 2;
        camera.getPosition().y -= y0 / 600;
        float height = terrain.getHeight(camera.getPosition()) + 1.0f;
        if (camera.getPosition().y <= height) {
            camera.getPosition().y = height;
            y0 = 0;
        }
        hud.setTText("height: " + height + "; y0: " + y0);

        double spotAngleRad = Math.toRadians(spotAngle);
        Vector3f coneDir = scene.getSceneLight().getSpotLightList()[0].getConeDirection();
        Vector3f coneDir2 = scene.getSceneLight().getSpotLightList()[1].getConeDirection();
        coneDir.x = (float) (Math.cos(spotAngleRad) / 7);
        coneDir.z = (float) (Math.sin(spotAngleRad) / 7);

        coneDir2.x = (float) (Math.cos(spotAngleRad - 180) / 7);
        coneDir2.z = (float) (Math.sin(spotAngleRad - 180) / 7);

        Vector3f pos3 = scene.getSceneLight().getSpotLightList()[2].getPointLight().getPosition();
        scene.getSceneLight().getSpotLightList()[2].getPointLight().getPosition().z = (float) (Math.cos(spotAngleRad) * 3);
        scene.getSceneLight().getSpotLightList()[2].getPointLight().getPosition().x = (float) (Math.sin(spotAngleRad) * 3);
        scene.getSceneLight().getSpotLightList()[2].getPointLight().getPosition().y += (float) (Math.sin(spotAngleRad * 2) / 3);

        Vector3f v = scene.getSceneLight().getSpotLightList()[2].getConeDirection();
        scene.getSceneLight().getSpotLightList()[2].getConeDirection().x = (float) Math.cos(spotAngleRad);
        scene.getSceneLight().getSpotLightList()[2].getConeDirection().y = (float) Math.sin(spotAngleRad);

        Vector3f gp = g.getRotation();
        g.setRotation(gp.add(scene.getSceneLight().getSpotLightList()[2].getConeDirection()));

        SceneLight sceneLight = scene.getSceneLight();

        DirectionalLight dirLight = sceneLight.getDirLight();
        sceneLight.getAmbientLight().set(0.6f, 0.6f, 0.6f);
        lightAngle += 0.1f;
        if (lightAngle > 90) {
            dirLight.setIntensity(1f);
            if (lightAngle >= 270) {
                lightAngle = -90;
            }
            sceneLight.getAmbientLight().set(0.2f, 0.2f, 0.2f);
        } else if (lightAngle <= -80 || lightAngle >= 80) {
            float factor = 1 - (float) (Math.abs(lightAngle) - 80) / 10.0f;
            sceneLight.getAmbientLight().set(Math.max(factor, 0.2f), Math.max(factor, 0.2f), Math.max(factor, 0.2f));
            dirLight.setIntensity(Math.max(factor, 0.2f));
            dirLight.getColor().y = Math.max(factor, 0.2f);
            dirLight.getColor().z = Math.max(factor, 0.1f);
        } else {
            sceneLight.getAmbientLight().set(0.6f, 0.6f, 0.6f);
            dirLight.setIntensity(1);
            dirLight.getColor().x = 1;
            dirLight.getColor().y = 1;
            dirLight.getColor().z = 1;
        }
        double angRad = Math.toRadians(lightAngle);
        dirLight.getDirection().x = (float) Math.sin(angRad);
        dirLight.getDirection().y = (float) Math.cos(angRad);
        hud.setStatusText("x: " + (int) camera.getPosition().x + "; y: " + (int) camera.getPosition().y + "; z: " + (int) camera.getPosition().z);

    }

    @Override
    public void render(Window window) {
        hud.updateSize(window);
        renderer.render(window, camera, scene, hud);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        scene.cleanup();
        if (hud != null) {
            hud.cleanup();
        }
    }

}
