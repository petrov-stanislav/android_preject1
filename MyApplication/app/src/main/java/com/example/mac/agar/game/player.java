package com.example.mac.agar.game;

import android.content.Context;

import com.example.mac.agar.math.vec3;
import com.example.mac.agar.render.drawable;
import com.example.mac.agar.render.primitives.plane;

/**
 * Created by mac on 13.08.15.
 */
public class player {
    plane Plane1, Plane2, Plane3;

    public player( Context context )
    {
        Plane1 = new plane(context);
        Plane2 = new plane(context);
        Plane3 = new plane(context);
    }

    public void Update( drawable Rnd )
    {
        float angleInDegrees = 360.0f * Rnd.GlobalTime;

        Rnd.Camera.SetEye(new vec3((float)Math.sin(Rnd.GlobalTime * 3) * 1,
                                   (float)Math.cos(Rnd.GlobalTime * 3) * 1,
                                   1.5f));

        Plane1.FreeMatix().Rotate(angleInDegrees, new vec3(0, 0, 1));
        Plane2.FreeMatix().Rotate(angleInDegrees, new vec3(0, 1, 0));
        Plane3.FreeMatix().Rotate(angleInDegrees, new vec3(1, 0, 0));
    }

    public void Draw( drawable Rnd )
    {
        Plane1.Draw(Rnd);
        Plane2.Draw(Rnd);
        Plane3.Draw(Rnd);
    }
} /* End of 'map' class */
