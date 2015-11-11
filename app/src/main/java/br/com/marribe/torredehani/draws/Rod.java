package br.com.marribe.torredehani.draws;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Stack;

/**
 * Created by danielmarcoto on 20/10/15.
 */
public class Rod extends GameObject {

    public static final float DEFAULT_WIDTH = 40;
    public static final float DEFAULT_HEIGHT = 700;

    private static final int DEFAULT_COLOR = Color.parseColor("#086788"); // Azul escuro
    private static final int SELECTED_COLOR = Color.parseColor("#DD1C1A"); // Vermelho

    private boolean isSelected;
    private int diskCount;

    private Stack<Disk> disks;

    public Rod(){
        disks = new Stack<>();
    }

    public Disk pop(){
        diskCount--;
        return disks.pop();
    }

    public Disk push(Disk disk){
        disks.push(disk);
        diskCount++;
        return disk;
    }

    public int getDiskCount(){
        return diskCount;
    }

    @Override
    public void draw(Canvas canvas) {

        // Desenha o elemento da torre

        int currentColor = DEFAULT_COLOR;
        if (isSelected){
            currentColor = SELECTED_COLOR;
        }

        Paint paint = new Paint();
        paint.setColor(currentColor);

        canvas.drawRect(x, y, x + width, y + height, paint);

        for (GameObject draw : disks) {
            draw.draw(canvas);
        }
    }

    public boolean isDiskAccepted(Disk disk){
        if (disks.size() == 0) return true;
        Disk current = disks.peek();
        return current.getDiskNumber() < disk.getDiskNumber();
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public float getNextDiskX(){
        return x;
    }

    public float getNextDiskY(){
        return (y + height) - ((Disk.SPACE + Disk.DEFAULT_HEIGHT) * (diskCount + 1));
    }

}
