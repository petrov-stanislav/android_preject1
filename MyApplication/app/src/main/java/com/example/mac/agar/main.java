package com.example.mac.agar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.mac.agar.game.base;

/**
 * This is main function of 'Agar' project.
 **/

/* Main agar preject class */
public class main extends Activity
{
    /* Global base */
    base Base;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Base = new base(this);
        setContentView(Base.AgarSurface);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Base.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Base.onPause();
    }
} /* End of 'main' class */
