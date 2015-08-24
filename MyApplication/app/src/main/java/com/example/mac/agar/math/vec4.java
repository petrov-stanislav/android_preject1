package com.example.mac.agar.math;

/* 4 floats class */
public class vec4
{
    float X, Y, Z, W;

    /* vec4 class constructor
     * ARGUMENTS:
     *   - New coords:
     *       float NewX, NewY, NewZ, NewW;
     */
    vec4( float NewX, float NewY, float NewZ, float NewW )
    {
        X = NewX;
        Y = NewY;
        Z = NewZ;
        W = NewW;
    } /* End of 'vec4' function */

    /* vec4 class constructor
     * ARGUMENTS: None.
     */
    vec4()
    {
        X = 0;
        Y = 0;
        Z = 0;
        W = 0;
    } /* End of 'vec4' function */
} /* End of 'vec4' class */
