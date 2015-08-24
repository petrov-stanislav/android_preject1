package com.example.mac.agar.render;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.example.mac.agar.R;
import com.example.mac.agar.camera;
import com.example.mac.agar.render.shader;
import com.example.mac.agar.vec3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by mac on 13.08.15.
 */
/* Main drawable class */
public class drawable implements GLSurfaceView.Renderer
{
    private final FloatBuffer mTriangle1Vertices;

    int programHandle;

    private shader vShader;
    private shader fShader;

    camera Camera;

    /**
     * Сохраняем матрицу моделей. Она используется для перемещения моделей со своим пространством  (где каждая модель рассматривается
     * относительно центра системы координат нашего мира) в пространстве мира.
     */
    private float[] mModelMatrix = new float[16];

    /** Выделяем массив для хранения объединеной матрицы. Она будет передана в программу шейдера. */
    private float[] mMVPMatrix = new float[16];

    /** Сохраняем матрицу проекции.Она используется для преобразования трехмерной сцены в 2D изображение. */
    private float[] mProjectionMatrix = new float[16];

    /** Используется для передачи в матрицу преобразований. */
    private int mMVPMatrixHandle;

    /** Используется для передачи информации о положении модели. */
    private int mPositionHandle;

    /** Используется для передачи информации о цвете модели. */
    private int mColorHandle;

    public drawable( Activity MainActivity )
    {
        // Треугольники красный, зеленый, и синий.
        final float[] triangle1VerticesData = {
                // X, Y, Z,
                // R, G, B, A
                -0.5f, -0.25f, 0.0f,
                1.0f, 0.0f, 0.0f, 1.0f,

                0.5f, -0.25f, 0.0f,
                0.0f, 0.0f, 1.0f, 1.0f,

                0.0f, 0.559016994f, 0.0f,
                0.0f, 1.0f, 0.0f, 1.0f};

        // Инициализируем буфер.
        mTriangle1Vertices = ByteBuffer.allocateDirect(triangle1VerticesData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        mTriangle1Vertices.put(triangle1VerticesData).position(0);

        vShader = new shader(MainActivity.getString(R.string.vertex_def));
        fShader = new shader(MainActivity.getString(R.string.pixel_def));
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        // Устанавливаем цвет фона светло серый.
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

        Camera = new camera(new vec3(0.0f, 0.0f, 1.5f),
                            new vec3(0.0f, 0.0f, -10.0f),
                            new vec3(0.0f, 1.0f, 0.0f));

        // Загрузка вершинного шейдера.
        vShader.Load(shader.SHD_TYPE.VERTEX);

        // Загрузка фрагментного шейдера.
        fShader.Load(shader.SHD_TYPE.PIXEL);

        // Создаем объект программы вместе со ссылкой на нее.
        programHandle = shader.Union(vShader, fShader);

        // Установить настройки вручную. Это будет позже использовано для передачи значений в программу.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");

        // Сообщить OpenGL чтобы использовал эту программу при рендеринге.
        GLES20.glUseProgram(programHandle);
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

    /**
     * Рисуем треугольники из массива данных вершин.
     *
     * @param aTriangleBuffer Буфер содержащий данные о вершинах.
     */
    private void drawTriangle(final FloatBuffer aTriangleBuffer)
    {
        // Передаем значения о расположении.
        aTriangleBuffer.position(mPositionOffset);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
                mStrideBytes, aTriangleBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Передаем значения о цвете.
        aTriangleBuffer.position(mColorOffset);
        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,
                mStrideBytes, aTriangleBuffer);

        GLES20.glEnableVertexAttribArray(mColorHandle);

        // Перемножаем матрицу ВИДА на матрицу МОДЕЛИ, и сохраняем результат в матрицу MVP
        // (которая теперь содержит модель*вид).
        Matrix.multiplyMM(mMVPMatrix, 0, Camera.mViewMatrix, 0, mModelMatrix, 0);

        // Перемножаем матрицу модели*вида на матрицу проекции, сохраняем в MVP матрицу.
        // (которая теперь содержит модель*вид*проекцию).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        // Делаем полный оборот при вращении за 10 секунд.
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);

        // Рисуем треугольники плоскостью к нам.
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.3f, 0.5f, 0.7f);
        drawTriangle(mTriangle1Vertices);
    }

} /* End of 'map' class */
