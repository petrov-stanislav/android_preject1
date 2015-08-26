package com.example.mac.agar.render.primitives;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.example.mac.agar.R;
import com.example.mac.agar.math.vec3;
import com.example.mac.agar.render.drawable;
import com.example.mac.agar.render.shader;
import com.example.mac.agar.render.texture;
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

    texture Txt;
    shader Shd;

    public primitive( Context context )
    {
        Txt = new texture();
        Txt.Load(context, R.mipmap.smile);

        Shd = new shader(context.getString(R.string.vertex_def),
                         context.getString(R.string.pixel_def));
    }

    public primitive FreeMatix()
    {
        Matrix.setIdentityM(mModelMatrix, 0);
        return this;
    }

    public primitive Scale( vec3 Scale )
    {
        Matrix.scaleM(mModelMatrix, 0, Scale.X, Scale.Y, Scale.Z);
        return this;
    }

    public primitive Rotate( float AngleInDegree, vec3 Rotate )
    {
        Matrix.rotateM(mModelMatrix, 0, AngleInDegree, Rotate.X, Rotate.Y, Rotate.Z);
        return this;
    }

    public primitive Translate( vec3 Translate )
    {
        Matrix.translateM(mModelMatrix, 0, Translate.X, Translate.Y, Translate.Z);
        return this;
    }

    public void Draw( drawable Rnd )
    {
        GLES20.glUseProgram(Shd.programHandle);

        // Установить настройки вручную. Это будет позже использовано для передачи значений в программу.
        int mMVPMatrixHandle = GLES20.glGetUniformLocation(Shd.programHandle, "u_MVPMatrix");
        int mPositionHandle = GLES20.glGetAttribLocation(Shd.programHandle, "a_Position");
        int mColorHandle = GLES20.glGetAttribLocation(Shd.programHandle, "a_Color");
        int mTexHandle = GLES20.glGetAttribLocation(Shd.programHandle, "a_TexCoord");

        // Передаем значения о расположении.
        VertexBuffer.position(vertex_info.mPositionOffset);
        GLES20.glVertexAttribPointer(mPositionHandle, vertex_info.mPositionDataSize, GLES20.GL_FLOAT, false,
                vertex_info.mStrideBytes, VertexBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Передаем значения о цвете.
        VertexBuffer.position(vertex_info.mColorOffset);
        GLES20.glVertexAttribPointer(mColorHandle, vertex_info.mColorDataSize, GLES20.GL_FLOAT, false,
                vertex_info.mStrideBytes, VertexBuffer);

        GLES20.glEnableVertexAttribArray(mColorHandle);

        // Передаем значения о текстуре.
        VertexBuffer.position(vertex_info.mTexOffset);
        GLES20.glVertexAttribPointer(mTexHandle, vertex_info.mTexDataSize, GLES20.GL_FLOAT, false,
                vertex_info.mStrideBytes, VertexBuffer);

        GLES20.glEnableVertexAttribArray(mTexHandle);

        // Перемножаем матрицу ВИДА на матрицу МОДЕЛИ, и сохраняем результат в матрицу MVP
        // (которая теперь содержит модель*вид).
        Matrix.multiplyMM(Rnd.mMVPMatrix, 0, Rnd.Camera.mViewMatrix, 0, mModelMatrix, 0);

        // Перемножаем матрицу модели*вида на матрицу проекции, сохраняем в MVP матрицу.
        // (которая теперь содержит модель*вид*проекцию).
        Matrix.multiplyMM(Rnd.mMVPMatrix, 0, Rnd.mProjectionMatrix, 0, Rnd.mMVPMatrix, 0);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, Rnd.mMVPMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}

