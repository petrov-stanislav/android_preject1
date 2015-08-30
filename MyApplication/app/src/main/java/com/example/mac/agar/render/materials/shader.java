package com.example.mac.agar.render.materials;

import android.opengl.GLES20;

/**
 * Created by mac on 17.08.15.
 */
public class shader
{
    int vHandle, pHandle;
    final String vShd, pShd;

    public int programHandle;

    public shader(String newVertShader, String newPixShader)
    {
        vShd = newVertShader;
        pShd = newPixShader;

        Load();
    }

    void Load()
    {
        // Загрузка шейдера.
        vHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        pHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        if (vHandle != 0)
        {
            // Передаем в наш шейдер программу.
            GLES20.glShaderSource(vHandle, vShd);

            // Компиляция шейреда
            GLES20.glCompileShader(vHandle);

            // Получаем результат процесса компиляции
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(vHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // Если компиляция не удалась, удаляем шейдер.
            if (compileStatus[0] == 0)
            {
                GLES20.glDeleteShader(vHandle);
                vHandle = 0;
            }
        }

        if (vHandle == 0)
        {
            throw new RuntimeException("Error creating shader.");
        }

        if (pHandle != 0)
        {
            // Передаем в наш шейдер программу.
            GLES20.glShaderSource(pHandle, pShd);

            // Компиляция шейреда
            GLES20.glCompileShader(pHandle);

            // Получаем результат процесса компиляции
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(pHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // Если компиляция не удалась, удаляем шейдер.
            if (compileStatus[0] == 0)
            {
                GLES20.glDeleteShader(pHandle);
                pHandle = 0;
            }
        }

        if (pHandle == 0)
        {
            throw new RuntimeException("Error creating shader.");
        }

        Union();
    }

    void Union()
    {
        // Создаем объект программы вместе со ссылкой на нее.
        programHandle = GLES20.glCreateProgram();

        if (programHandle != 0)
        {
            // Подключаем вершинный шейдер к программе.
            GLES20.glAttachShader(programHandle, vHandle);

            // Подключаем фрагментный шейдер к программе.
            GLES20.glAttachShader(programHandle, pHandle);

            // Подключаем атрибуты цвета и положения
            GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
            GLES20.glBindAttribLocation(programHandle, 1, "a_Color");
            GLES20.glBindAttribLocation(programHandle, 2, "a_TexCoord");

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
    }
}
