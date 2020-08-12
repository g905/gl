/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.g905.engine.graph;

import java.nio.FloatBuffer;
import java.util.List;
import org.joml.Matrix4f;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;
import org.lwjgl.system.MemoryUtil;
import ru.g905.engine.items.GameItem;

/**
 *
 * @author zharnikov
 */
public class InstancedMesh extends Mesh {

    private static final int FLOAT_SIZE_BYTES = 4;

    private static final int VECTOR4F_SIZE_BYTES = 4 * InstancedMesh.FLOAT_SIZE_BYTES;

    private static final int MATRIX_SIZE_FLOATS = 4 * 4;

    private static final int MATRIX_SIZE_BYTES = InstancedMesh.MATRIX_SIZE_FLOATS * InstancedMesh.FLOAT_SIZE_BYTES;

    private static final int INSTANCE_SIZE_BYTES = InstancedMesh.MATRIX_SIZE_BYTES + InstancedMesh.FLOAT_SIZE_BYTES * 2 + InstancedMesh.FLOAT_SIZE_BYTES;

    private static final int INSTANCE_SIZE_FLOATS = InstancedMesh.MATRIX_SIZE_FLOATS + 3;

    private final int numInstances;

    private final int instanceDataVBO;

    private FloatBuffer instanceDataBuffer;

    public InstancedMesh(float[] positions, float[] textCoords, float[] normals, int[] indices, int numInstances) {
        super(positions, textCoords, normals, indices, Mesh.createEmptyIntArray(Mesh.MAX_WEIGHTS * positions.length / 3, 0), Mesh.createEmptyFloatArray(Mesh.MAX_WEIGHTS * positions.length / 3, 0));

        this.numInstances = numInstances;

        glBindVertexArray(vaoId);

        // Model View Matrix
        instanceDataVBO = glGenBuffers();
        vboIdList.add(instanceDataVBO);
        instanceDataBuffer = MemoryUtil.memAllocFloat(numInstances * InstancedMesh.INSTANCE_SIZE_FLOATS);
        glBindBuffer(GL_ARRAY_BUFFER, instanceDataVBO);
        int start = 5;
        int strideStart = 0;

        for (int i = 0; i < 4; i++) {
            glVertexAttribPointer(start, 4, GL_FLOAT, false, InstancedMesh.INSTANCE_SIZE_BYTES, strideStart);
            glVertexAttribDivisor(start, 1);
            glEnableVertexAttribArray(start);
            start++;
            strideStart += InstancedMesh.VECTOR4F_SIZE_BYTES;
        }

        // Texture offsets
        glVertexAttribPointer(start, 2, GL_FLOAT, false, InstancedMesh.INSTANCE_SIZE_BYTES, strideStart);
        glVertexAttribDivisor(start, 1);
        glEnableVertexAttribArray(start);
        strideStart += InstancedMesh.FLOAT_SIZE_BYTES * 2;
        start++;

        //Selection or scaling
        glVertexAttribPointer(start, 1, GL_FLOAT, false, InstancedMesh.INSTANCE_SIZE_BYTES, strideStart);
        glVertexAttribDivisor(start, 1);
        glEnableVertexAttribArray(start);
        start++;

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    @Override
    public void cleanUp() {
        super.cleanUp();
        if (this.instanceDataBuffer != null) {
            MemoryUtil.memFree(this.instanceDataBuffer);
            this.instanceDataBuffer = null;
        }
    }

    public void renderListInstanced(List<GameItem> gameItems, Transformation transformation, Matrix4f viewMatrix) {
        renderListInstanced(gameItems, false, transformation, viewMatrix);
    }

    public void renderListInstanced(List<GameItem> gameItems, boolean billBoard, Transformation transformation, Matrix4f viewMatrix) {
        initRender();

        int chunkSize = numInstances;
        int length = gameItems.size();
        for (int i = 0; i < length; i += chunkSize) {
            int end = Math.min(length, i + chunkSize);
            List<GameItem> subList = gameItems.subList(i, end);
            renderChunkInstanced(subList, billBoard, transformation, viewMatrix);
        }

        endRender();
    }

    private void renderChunkInstanced(List<GameItem> gameItems, boolean billBoard, Transformation transformation, Matrix4f viewMatrix) {
        this.instanceDataBuffer.clear();

        int i = 0;

        Texture text = getMaterial().getTexture();
        for (GameItem gameItem : gameItems) {
            Matrix4f modelMatrix = transformation.buildModelMatrix(gameItem);
            if (viewMatrix != null && billBoard) {
                viewMatrix.transpose3x3(modelMatrix);
            }

            modelMatrix.get(InstancedMesh.INSTANCE_SIZE_FLOATS * i, instanceDataBuffer);
            if (text != null) {
                int col = gameItem.getTextPos() % text.getNumCols();
                int row = gameItem.getTextPos() / text.getNumCols();
                float textXOffset = (float) col / text.getNumCols();
                float textYOffset = (float) row / text.getNumRows();
                int buffPos = InstancedMesh.INSTANCE_SIZE_FLOATS * i + InstancedMesh.MATRIX_SIZE_FLOATS;
                this.instanceDataBuffer.put(buffPos, textXOffset);
                this.instanceDataBuffer.put(buffPos + 1, textYOffset);
            }

            int buffPos = InstancedMesh.INSTANCE_SIZE_FLOATS * i + InstancedMesh.MATRIX_SIZE_FLOATS + 2;
            this.instanceDataBuffer.put(buffPos, billBoard ? gameItem.getScale() : gameItem.isSelected() ? 1 : 0);

            i++;
        }

        glBindBuffer(GL_ARRAY_BUFFER, instanceDataVBO);
        glBufferData(GL_ARRAY_BUFFER, instanceDataBuffer, GL_DYNAMIC_READ);

        glDrawElementsInstanced(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0, gameItems.size());

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

}
