package com.example.mac.agar;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;

/**
 * Created by mac on 13.08.15.
 */

/* Base animation class of 'agar' preject. */
public class base
{
    final int MAX_FOOD = 1000;

    /* Drawable surface */
    public GLSurfaceView AgarSurface;

    /* Global map of preject */
    map Map = new map(MAX_FOOD);

    /* It is you :) */
    player Player;

    /* Main base class constructor.
     * ARGUMENTS:
     *   - Drawable activity:
     *       Activity MainActivity;
     */
    base( Activity MainActivity )
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
            AgarSurface.setRenderer(new drawable());
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

    /* Main update function.
     * ARGUMENTS: None.
     * RETURNS: None.
     */
    public void Update()
    {
    } /* End of 'Update' function */

    /* Main draw function.
     * ARGUMENTS: None.
     * RETURNS: None.
     */
    public void Draw()
    {
    } /* End of 'Draw' function */
} /* End of 'base' class */
