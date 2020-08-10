/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.g905.engine.loaders.obj;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector3f;
import ru.g905.engine.Utils;
import ru.g905.engine.graph.InstancedMesh;
import ru.g905.engine.graph.Mesh;

/**
 *
 * @author g905
 */
public class ObjLoader {

    public static Mesh loadMesh(String fileName) throws Exception {
        return loadMesh(fileName, 1);
    }

    public static Mesh loadMesh(String filename, int instances) throws Exception {
        List<String> lines = Utils.readAllLines(filename);

        //System.out.println(lines);
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Face> faces = new ArrayList<>();

        for (String line : lines) {
            String[] tokens = line.split("\\s+");
            switch (tokens[0]) {
                case "v":
                    Vector3f vec3f = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]));
                    vertices.add(vec3f);
                    break;
                case "vt":
                    Vector2f vec2f = new Vector2f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]));
                    textures.add(vec2f);
                    break;
                case "vn":
                    Vector3f vec3fNorm = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]));
                    normals.add(vec3fNorm);
                    break;
                case "f":
                    Face face = new Face(tokens[1], tokens[2], tokens[3]);
                    faces.add(face);
                    break;
                default:
                    break;
            }
        }
        return reorderLists(vertices, textures, normals, faces, instances);
    }

    private static Mesh reorderLists(List<Vector3f> posList, List<Vector2f> texCoordList, List<Vector3f> normList, List<Face> facesList, int instances) {
        List<Integer> indices = new ArrayList<>();

        float[] posArr = new float[posList.size() * 3];
        int i = 0;
        for (Vector3f pos : posList) {
            posArr[i * 3] = pos.x;
            posArr[i * 3 + 1] = pos.y;
            posArr[i * 3 + 2] = pos.z;
            i++;
        }
        float[] texCoordArr = new float[posList.size() * 2];
        float[] normArr = new float[posList.size() * 3];

        for (Face face : facesList) {
            IdxGroup[] faceVertexIndices = face.getFaceVertexIndices();
            for (IdxGroup indValue : faceVertexIndices) {
                processFaceVertex(indValue, texCoordList, normList, indices, texCoordArr, normArr);
            }
        }
        int[] indicesArr = Utils.listIntToArray(indices);
        //indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();
        Mesh mesh;
        if (instances > 1) {
            mesh = new InstancedMesh(posArr, texCoordArr, normArr, indicesArr, instances);
        } else {
            mesh = new Mesh(posArr, texCoordArr, normArr, indicesArr);
        }
        return mesh;
    }

    private static void processFaceVertex(IdxGroup indices, List<Vector2f> texCoordList, List<Vector3f> normList, List<Integer> indicesList, float[] texCoordArr, float[] normArr) {
        int posIndex = indices.idxPos;
        indicesList.add(posIndex);

        if (indices.idxTexCoord >= 0) {
            Vector2f texCoord = texCoordList.get(indices.idxTexCoord);
            texCoordArr[posIndex * 2] = texCoord.x;
            texCoordArr[posIndex * 2 + 1] = 1 - texCoord.y;
        }
        if (indices.idxVecNormal >= 0) {
            Vector3f vecNorm = normList.get(indices.idxVecNormal);
            normArr[posIndex * 3] = vecNorm.x;
            normArr[posIndex * 3 + 1] = vecNorm.y;
            normArr[posIndex * 3 + 2] = vecNorm.z;
        }
    }

    protected static class Face {

        private IdxGroup[] idxGroups = new IdxGroup[3];

        public Face(String v1, String v2, String v3) {
            idxGroups = new IdxGroup[3];

            idxGroups[0] = parseLine(v1);
            idxGroups[1] = parseLine(v2);
            idxGroups[2] = parseLine(v3);
        }

        private IdxGroup parseLine(String line) {
            IdxGroup idxGroup = new IdxGroup();

            String[] lineTokens = line.split("/");
            int length = lineTokens.length;
            idxGroup.idxPos = Integer.parseInt(lineTokens[0]) - 1;
            if (length > 1) {
                String texCoord = lineTokens[1];
                idxGroup.idxTexCoord = texCoord.length() > 0 ? Integer.parseInt(texCoord) - 1 : IdxGroup.NO_VALUE;
                if (length > 2) {
                    idxGroup.idxVecNormal = Integer.parseInt(lineTokens[2]) - 1;
                }
            }
            return idxGroup;
        }

        public IdxGroup[] getFaceVertexIndices() {
            return idxGroups;
        }
    }

    protected static class IdxGroup {

        public static final int NO_VALUE = -1;
        public int idxPos;
        public int idxTexCoord;
        public int idxVecNormal;

        public IdxGroup() {
            idxPos = NO_VALUE;
            idxTexCoord = NO_VALUE;
            idxVecNormal = NO_VALUE;
        }
    }
}
