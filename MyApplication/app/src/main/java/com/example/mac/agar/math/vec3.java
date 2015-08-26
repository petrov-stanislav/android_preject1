package com.example.mac.agar.math;

/**
 * Created by mac on 23.08.15.
 */ /* 3 floats class */
public class vec3
{
    public float X, Y, Z;

    /* vec3 class constructor
     * ARGUMENTS:
     *   - New coords:
     *       float NewX, NewY, NewZ;
     */
    public vec3( float NewX, float NewY, float NewZ )
    {
        X = NewX;
        Y = NewY;
        Z = NewZ;
    } /* End of 'vec3' function */

    /* vec3 class constructor
     * ARGUMENTS: None.
     */
    public vec3()
    {
        X = 0;
        Y = 0;
        Z = 0;
    } /* End of 'vec3' function */
} /* End of 'vec3' class */
