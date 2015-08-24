package com.example.mac.agar.game;

import com.example.mac.agar.vec2;

import java.util.Vector;

/**
 * Created by mac on 13.08.15.
 */

/* Global preject map class */
public class map
{
    /* Food in map class */
    public class food
    {
        /* Coordinats of food */
        vec2 Coord = new vec2();

        /* Food class constructor
         * ARGUMENTS:
         *   - New coordinats of food.
         *       m.vec2 NewXY;
         */
        food( vec2 NewXY )
        {
            Coord = NewXY;
        } /* End of 'food' function */

        /* Food class constructor with random coords.
         * ARGUMENTS: None.
         */
        food()
        {
            Coord.X = (float)Math.random();
            Coord.Y = (float)Math.random();
        } /* End of 'food' function */

        /* Set new random coordinates of food function
         * ARGUMENTS: None.
         * RETURNS: None.
         */
        void SetNewCoord()
        {
            Coord.X = (float)Math.random();
            Coord.Y = (float)Math.random();
        } /* End of 'SetNewCoord' function */
    } /* End of 'food' class */

    /* Stock of food in map */
    Vector<food> Food = new Vector<food>();

    /* Main map class constructor.
     * ARGUMENTS:
     *   - Max food in map constant:
     *       int MaxFood;
     */
    map( int MaxFood )
    {
        Food.setSize(MaxFood);

        for (int i = 0; i < MaxFood; i++)
        {
            Food.set(i, new food());
        }
    } /* End of 'map' function */
} /* End of 'map' class */

