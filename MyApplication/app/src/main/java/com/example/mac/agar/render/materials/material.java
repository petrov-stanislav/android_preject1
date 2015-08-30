package com.example.mac.agar.render.materials;

import android.content.Context;

import com.example.mac.agar.R;

/**
 * Created by mac on 30.08.15.
 */
public class material
{
    private final int sizeOfShd = 1;
    public enum SHD
    {
        DEFAULT,
        UNDEFINE
    }
    public SHD prevShd = SHD.UNDEFINE;

    private final int sizeOfTxt = 3;
    public enum TXT
    {
        YOBA1,
        YOBA2,
        SMILE,
        UNDEFINE
    }
    public TXT prevTxt = TXT.UNDEFINE;

    static public int toInt( SHD Shd )
    {
        if (Shd == SHD.DEFAULT)
            return 0;
        else
            return -1;
    }

    static public int toInt( TXT Txt )
    {
        if (Txt == TXT.YOBA1)
            return 0;
        else if (Txt == TXT.YOBA2)
            return 1;
        else if (Txt == TXT.SMILE)
            return 2;
        else
            return -1;
    }

    public shader[] Shd = new shader[1];
    public texture[] Txt = new texture[3];

    public material(final Context context)
    {
        for (int i = 0; i < sizeOfTxt; i++)
            Txt[i] = new texture();

        Txt[0].Load(context, R.mipmap.avatar);
        Txt[1].Load(context, R.mipmap.ioba);
        Txt[2].Load(context, R.mipmap.smile);

        Shd[0] = new shader(context.getString(R.string.vertex_def),
                            context.getString(R.string.pixel_def));
    }
}
