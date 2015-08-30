package com.example.mac.agar.render;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.example.mac.agar.game.player;
import com.example.mac.agar.math.vec3;
import com.example.mac.agar.render.materials.material;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by mac on 13.08.15.
 */
/* Main drawable class */
public class drawable implements GLSurfaceView.Renderer
{
    Context programContext;
    player Player;
    public material Mtl;

    public camera Camera;

    public  float GlobalTime;
    /** Выделяем массив для хранения объединеной матрицы. Она будет передана в программу шейдера. */
    public float[] mMVPMatrix = new float[16];

    /** Сохраняем матрицу проекции.Она используется для преобразования трехмерной сцены в 2D изображение. */
    public float[] mProjectionMatrix = new float[16];

    public drawable( Context context )
    {
        programContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config)
    {
        Player = new player(programContext);
        Mtl = new material(programContext);

        // Устанавливаем цвет фона светло серый.
        GLES20.glClearColor(0.3f, 0.5f, 0.7f, 0.5f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        Camera = new camera(new vec3(0.0f, 0.0f, 1.5f),
                            new vec3(0.0f, 0.0f, -10.0f),
                            new vec3(0.0f, 1.0f, 0.0f));
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height)
    {
        // Устанавливаем OpenGL окно просмотра того же размера что и поверхность экрана.
        GLES20.glViewport(0, 0, width, height);

        // Создаем новую матрицу проекции. Высота остается та же,
        // а ширина будет изменяться в соответствии с соотношением сторон.
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 10.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 glUnused)
    {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GlobalTime = SystemClock.uptimeMillis() / 10000.0f;

        Player.Update(this);
        Player.Draw(this);
    }
} /* End of 'drawable' class */

