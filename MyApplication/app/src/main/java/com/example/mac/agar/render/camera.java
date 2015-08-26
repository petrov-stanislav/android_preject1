package com.example.mac.agar.render;

import android.opengl.Matrix;

import com.example.mac.agar.math.vec3;

/**
 * Created by mac on 23.08.15.
 */
/* Camera class */
public class camera
{
    // Положение глаза, точки наблюдения в пространстве.
    private vec3 eye;

    // На какое расстояние мы можем видеть вперед. Ограничивающая плоскость обзора.
    private vec3 look;

    // Положение где наша голова находилась бы если бы мы держали камеру.
    private vec3 up;

    /**
     * Определяем матрицу ВИДА. Её можно рассматривать как камеру. Эта матрица описывает пространство;
     * она задает положение предметов относительно нашего глаза.
     */
    public float[] mViewMatrix = new float[16];

    camera( vec3 NewEye, vec3 NewLook, vec3 NewUp )
    {
        eye = NewEye;
        look = NewLook;
        up = NewUp;

        UpdateMatrix();
    } /* End of 'camera' function */

    public void SetEye( vec3 NewEye )
    {
        eye = NewEye;
        UpdateMatrix();
    }

    public void SetLook( vec3 NewLook )
    {
        look = NewLook;
        UpdateMatrix();
    }

    public void SetUp( vec3 NewUp )
    {
        up = NewUp;
        UpdateMatrix();
    }

    public vec3 GetEye()
    {
        return eye;
    }

    public vec3 GetLook()
    {
        return look;
    }

    public vec3 GetUp()
    {
        return up;
    }

    private void UpdateMatrix()
    {
        // Устанавливаем матрицу ВИДА. Она описывает положение камеры.
        // Примечание: В OpenGL 1, матрица ModelView используется как комбинация матрицы МОДЕЛИ
        // и матрицы ВИДА. В OpenGL 2, мы можем работать с этими матрицами отдельно по выбору.
        Matrix.setLookAtM(mViewMatrix, 0, eye.X, eye.Y, eye.Z, look.X, look.Y, look.Z, up.X, up.Y, up.Z);
    }
} /* End of 'camera' class */
