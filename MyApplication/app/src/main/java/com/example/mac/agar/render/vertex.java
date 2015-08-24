package com.example.mac.agar.render;

import com.example.mac.agar.vec3;
import com.example.mac.agar.vec4;

/**
 * Created by mac on 23.08.15.
 */
class vertex_info
{
    /** Количество байт занимаемых одним числом. */
    private final int mBytesPerFloat = 4;

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
}

class vertex
{
    vec3 Pos;
    vec4 Color;
}
