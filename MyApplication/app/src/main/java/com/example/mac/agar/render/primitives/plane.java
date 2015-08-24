package com.example.mac.agar.render.primitives;

import android.content.Context;

import com.example.mac.agar.render.primitive;
import com.example.mac.agar.render.texture;
import com.example.mac.agar.render.vertex_info;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by mac on 24.08.15.
 */
public class plane extends primitive {
    texture Tex = new texture();

    public plane() {
        final float[] planeVerticesData =
                {
                        // X, Y, Z,
                        // R, G, B, A
                        -0.5f, -0.5f, 0.0f,
                        0.9f, 0.9f, 0.5f, 0.9f,
                        0, 0,

                        0.5f, -0.5f, 0.0f,
                        0.9f, 0.9f, 0.5f, 0.9f,
                        1, 0,

                        -0.5f, 0.5f, 0.0f,
                        0.9f, 0.9f, 0.5f, 0.5f,
                        0, 1,

                        0.5f, 0.5f, 0.0f,
                        0.9f, 0.9f, 0.5f, 0.5f,
                        1, 1
                };

        // Инициализируем буфер.
        VertexBuffer = ByteBuffer.allocateDirect(planeVerticesData.length * vertex_info.mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        VertexBuffer.put(planeVerticesData).position(0);
    }

    public void LoadTex(final Context context, final int resourceId)
    {
        Tex.Load(context, resourceId);
    }
}
