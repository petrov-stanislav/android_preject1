package com.example.mac.agar;

import android.opengl.GLES20;

/**
 * Created by mac on 17.08.15.
 */
public class shader
{
    int Handle;
    final String Shd;

    shader( String newShader )
    {
        Shd = newShader;
    }

    void Load()
    {
        // Загрузка вершинного шейдера.
        Handle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

        if (Handle != 0)
        {
            // Передаем в наш шейдер программу.
            GLES20.glShaderSource(Handle, Shd);

            // Компиляция шейреда
            GLES20.glCompileShader(Handle);

            // Получаем результат процесса компиляции
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(Handle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // Если компиляция не удалась, удаляем шейдер.
            if (compileStatus[0] == 0)
            {
                GLES20.glDeleteShader(Handle);
                Handle = 0;
            }
        }

        if (Handle == 0)
        {
            throw new RuntimeException("Error creating vertex shader.");
        }
    }

    static int Union( shader vShader, shader fShader )
    {
        // Создаем объект программы вместе со ссылкой на нее.
        int programHandle = GLES20.glCreateProgram();

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

        return programHandle;
    }
}
