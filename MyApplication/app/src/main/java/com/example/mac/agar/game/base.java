package com.example.mac.agar.game;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;

import com.example.mac.agar.render.drawable;

/**
 * Created by mac on 13.08.15.
 */

/* Base animation class of 'agar' preject. */
public class base
{
    /* Drawable surface */
    public GLSurfaceView AgarSurface;

    /* Main base class constructor.
     * ARGUMENTS:
     *   - Drawable activity:
     *       Activity MainActivity;
     */
    public base( Activity MainActivity )
    {
        AgarSurface = new GLSurfaceView(MainActivity);

        // Проверяем поддереживается ли OpenGL ES 2.0.
        final ActivityManager activityManager = (ActivityManager)MainActivity.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportsEs2)
        {
            // Запрос OpenGL ES 2.0 для установки контекста.
            AgarSurface.setEGLContextClientVersion(2);

            // Устанавливаем рендеринг, создаем экземпляр класса, он будет описан ниже.
            AgarSurface.setRenderer(new drawable(MainActivity));
        }
        else
        {
            // Устройство поддерживает только OpenGL ES 1.x
            // опишите реализацию рендеринга здесь, для поддержку двух систем ES 1 and ES 2.
            return;
        }

        MainActivity.setContentView(AgarSurface);
    } /* End of 'base' function */

    /* onResume of agar surface function.
     * ARGUMENTS: None.
     * RETURNS: None.
     */
    public void onResume()
    {
        AgarSurface.onResume();
    } /* End of 'onResume' function */

    /* onPause of agar surface function.
     * ARGUMENTS: None.
     * RETURNS: None.
     */
    public void onPause()
    {
        AgarSurface.onPause();
    } /* End of 'onPause' function */
} /* End of 'base' class */
