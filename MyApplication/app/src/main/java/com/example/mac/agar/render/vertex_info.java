package com.example.mac.agar.render;

/**
 * Created by mac on 23.08.15.
 */
public class vertex_info
{
    /** Количество байт занимаемых одним числом. */
    public static final int mBytesPerFloat = 4;

    /** Количество элементов в вершине. */
    public static final int mStrideBytes = 9 * mBytesPerFloat;

    /** Смещение в массиве данных. */
    public static final int mPositionOffset = 0;

    /** Размер массива позиций в элементах. */
    public static final int mPositionDataSize = 3;

    /** Смещение для данных цвета. */
    public static final int mColorOffset = 3;

    /** Размер данных цвета в элементах. */
    public static final int mColorDataSize = 4;

    /** Смещение для данных цвета. */
    public static final int mTexOffset = 7;

    /** Размер данных цвета в элементах. */
    public static final int mTexDataSize = 2;
}
