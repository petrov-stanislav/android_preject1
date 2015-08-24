package com.example.mac.agar.render.primitives;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.example.mac.agar.math.vec3;
import com.example.mac.agar.render.drawable;
import com.example.mac.agar.render.vertex_info;

import java.nio.FloatBuffer;

/**
 * Created by mac on 23.08.15.
 */
public class primitive
{
    /**
     * Матрица моделей. Она используется для перемещения моделей со своим пространством  (где каждая модель рассматривается
     * относительно центра системы координат нашего мира) в пространстве мира.
     */
    public float[] mModelMatrix = new float[16];
    protected FloatBuffer VertexBuffer;

    public primitive FreeMatix()
    {
        Matrix.setIdentityM(mModelMatrix, 0);
        return this;
    }

    public void Scale( vec3 Scale )
    {
        Matrix.scaleM(mModelMatrix, 0, Scale.X, Scale.Y, Scale.Z);
    }

    public void Rotate( float AngleInDegree, vec3 Rotate )
    {
        Matrix.rotateM(mModelMatrix, 0, AngleInDegree, Rotate.X, Rotate.Y, Rotate.Z);
    }

    public void Translate( vec3 Translate )
    {
        Matrix.translateM(mModelMatrix, 0, Translate.X, Translate.Y, Translate.Z);
    }

    public void Draw( drawable Rnd )
    {
        // Передаем значения о расположении.
        VertexBuffer.position(vertex_info.mPositionOffset);
        GLES20.glVertexAttribPointer(Rnd.mPositionHandle, vertex_info.mPositionDataSize, GLES20.GL_FLOAT, false,
                vertex_info.mStrideBytes, VertexBuffer);

        GLES20.glEnableVertexAttribArray(Rnd.mPositionHandle);

        // Передаем значения о цвете.
        VertexBuffer.position(vertex_info.mColorOffset);
        GLES20.glVertexAttribPointer(Rnd.mColorHandle, vertex_info.mColorDataSize, GLES20.GL_FLOAT, false,
                vertex_info.mStrideBytes, VertexBuffer);

        GLES20.glEnableVertexAttribArray(Rnd.mColorHandle);

        // Передаем значения о текстуре.
        VertexBuffer.position(vertex_info.mTexOffset);
        GLES20.glVertexAttribPointer(Rnd.mTexHandle, vertex_info.mTexDataSize, GLES20.GL_FLOAT, false,
                vertex_info.mStrideBytes, VertexBuffer);

        GLES20.glEnableVertexAttribArray(Rnd.mTexHandle);

        // Перемножаем матрицу ВИДА на матрицу МОДЕЛИ, и сохраняем результат в матрицу MVP
        // (которая теперь содержит модель*вид).
        Matrix.multiplyMM(Rnd.mMVPMatrix, 0, Rnd.Camera.mViewMatrix, 0, mModelMatrix, 0);

        // Перемножаем матрицу модели*вида на матрицу проекции, сохраняем в MVP матрицу.
        // (которая теперь содержит модель*вид*проекцию).
        Matrix.multiplyMM(Rnd.mMVPMatrix, 0, Rnd.mProjectionMatrix, 0, Rnd.mMVPMatrix, 0);

        GLES20.glUniformMatrix4fv(Rnd.mMVPMatrixHandle, 1, false, Rnd.mMVPMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}

