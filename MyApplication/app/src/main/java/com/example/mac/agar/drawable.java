package com.example.mac.agar;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

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

    /** Количество байт занимаемых одним числом. */
    private final int mBytesPerFloat = 4;
    int programHandle;

    shader vShader = new shader(
            "uniform mat4 u_MVPMatrix;        \n"     // Константа отвечающая за комбинацию матриц МОДЕЛЬ/ВИД/ПРОЕКЦИЯ.
            + "attribute vec4 a_Position;     \n"     // Информация о положении вершин.
            + "attribute vec4 a_Color;        \n"     // Информация о цвете вершин.

            + "varying vec4 v_Color;          \n"     // Это будет передано в фрагментный шейдер.

            + "void main()                    \n"     // Начало программы вершинного шейдера.
            + "{                              \n"
            + "   v_Color = a_Color;          \n"     // Передаем цвет для фрагментного шейдера.
            // Он будет интерполирован для всего треугольника.
            + "   gl_Position = u_MVPMatrix   \n"     // gl_Position специальные переменные используемые для хранения конечного положения.
            + "               * a_Position;   \n"     // Умножаем вершины на матрицу для получения конечного положения
            + "}                              \n");    // в нормированных координатах экрана.);

    shader fShader = new shader(
            "precision mediump float;       \n"     // Устанавливаем по умолчанию среднюю точность для переменных. Максимальная точность
            // в фрагментном шейдере не нужна.
            + "varying vec4 v_Color;          \n"     // Цвет вершинного шейдера преобразованного
            // для фрагмента треугольников.
            + "void main()                    \n"     // Точка входа для фрагментного шейдера.
            + "{                              \n"
            + "   gl_FragColor = v_Color;     \n"     // Передаем значения цветов.
            + "}                              \n");

    public drawable()
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
    }

    /**
     * Определяем матрицу ВИДА. Её можно рассматривать как камеру. Эта матрица описывает пространство;
     * она задает положение предметов относительно нашего глаза.
     */
    private float[] mViewMatrix = new float[16];

    /** Используется для передачи в матрицу преобразований. */
    private int mMVPMatrixHandle;

    /** Используется для передачи информации о положении модели. */
    private int mPositionHandle;

    /** Используется для передачи информации о цвете модели. */
    private int mColorHandle;

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        // Устанавливаем цвет фона светло серый.
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

        // Положение глаза, точки наблюдения в пространстве.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 1.5f;

        // На какое расстояние мы можем видеть вперед. Ограничивающая плоскость обзора.
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        // Устанавливаем вектор. Положение где наша голова находилась бы если бы мы держали камеру.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Загрузка вершинного шейдера.
        vShader.Handle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        if (vShader.Handle != 0)
        {
            // Передаем в наш шейдер программу.
            GLES20.glShaderSource(vShader.Handle, vShader.Shd);

            // Компиляция шейреда
            GLES20.glCompileShader(vShader.Handle);

            // Получаем результат процесса компиляции
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(vShader.Handle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // Если компиляция не удалась, удаляем шейдер.
            if (compileStatus[0] == 0)
            {
                GLES20.glDeleteShader(vShader.Handle);
                vShader.Handle = 0;
            }
        }
        if (vShader.Handle == 0)
        {
            throw new RuntimeException("Error creating shader.");
        }


        // Загрузка фрагментного шейдера.
        fShader.Handle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        if (fShader.Handle != 0)
        {
            // Передаем в наш шейдер программу.
            GLES20.glShaderSource(fShader.Handle, fShader.Shd);

            // Компиляция шейреда
            GLES20.glCompileShader(fShader.Handle);

            // Получаем результат процесса компиляции
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(fShader.Handle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // Если компиляция не удалась, удаляем шейдер.
            if (compileStatus[0] == 0)
            {
                GLES20.glDeleteShader(fShader.Handle);
                fShader.Handle = 0;
            }
        }
        if (fShader.Handle == 0)
        {
            throw new RuntimeException("Error creating shader.");
        }

        // Создаем объект программы вместе со ссылкой на нее.
        programHandle = GLES20.glCreateProgram();
        if (programHandle != 0)
        {
            // Подключаем вершинный шейдер к программе.
            GLES20.glAttachShader(programHandle, vShader.Handle);

            // Подключаем фрагментный шейдер к программе.
            GLES20.glAttachShader(programHandle, fShader.Handle);

            // Подключаем атрибуты цвета и положения
            GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
            GLES20.glBindAttribLocation(programHandle, 1, "a_Color");

            // Объединяем оба шейдера в программе.
            GLES20.glLinkProgram(programHandle);

            // Получаем ссылку на программу.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // Если ссылку не удалось получить, удаляем программу.
            if (linkStatus[0] == 0)
            {
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }
        if (programHandle == 0)
        {
            throw new RuntimeException("Error creating program.");
        }

        // Устанавливаем матрицу ВИДА. Она описывает положение камеры.
        // Примечание: В OpenGL 1, матрица ModelView используется как комбинация матрицы МОДЕЛИ
        // и матрицы ВИДА. В OpenGL 2, мы можем работать с этими матрицами отдельно по выбору.
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        // Установить настройки вручную. Это будет позже использовано для передачи значений в программу.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");

        // Сообщить OpenGL чтобы использовал эту программу при рендеринге.
        GLES20.glUseProgram(programHandle);
    }

    /** Сохраняем матрицу проекции.Она используется для преобразования трехмерной сцены в 2D изображение. */
    private float[] mProjectionMatrix = new float[16];

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
     * Сохраняем матрицу моделей. Она используется для перемещения моделей со своим пространством  (где каждая модель рассматривается
     * относительно центра системы координат нашего мира) в пространстве мира.
     */
    private float[] mModelMatrix = new float[16];

    /** Выделяем массив для хранения объединеной матрицы. Она будет передана в программу шейдера. */
    private float[] mMVPMatrix = new float[16];

    /** Количество элементов в вершине. */
    private final int mStrideBytes = 7 * mBytesPerFloat;

    /** Смещение в массиве данных. */
    private final int mPositionOffset = 0;

    /** Размер массива позиций в элементах. */
    private final int mPositionDataSize = 3;

    /** Смещение для данных цвета. */
    private final int mColorOffset = 3;

    /** Размер данных цвета в элементах. */
    private final int mColorDataSize = 4;

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
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

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
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);
        drawTriangle(mTriangle1Vertices);
    }

} /* End of 'map' class */
