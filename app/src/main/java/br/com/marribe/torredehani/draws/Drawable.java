package br.com.marribe.torredehani.draws;

import android.graphics.Canvas;
import android.util.Log;

import java.util.Objects;

/**
 * Created by danielmarcoto on 20/10/15.
 */
public abstract class Drawable {

    protected float x;
    protected float y;

    protected float width;
    protected float height;

    public abstract void draw(Canvas canvas);

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public boolean intercept( float x, float y ){
        return ((x >= this.x) && (y >= this.y)) &&
                ((x < (this.x + width)) && (y < (this.y + height)));
    }

    @Override
    public boolean equals(Object o){
        Drawable drawable = (Drawable)o;
        return drawable.x == x && drawable.y == y &&
                drawable.width == width && drawable.height == height;
    }
}
