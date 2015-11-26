package br.com.marribe.torredehani.draws;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Created by danielmarcoto on 20/10/15.
 */
public class Rod extends GameObject {

    public static final float DEFAULT_WIDTH = 40;
    public static final float DEFAULT_HEIGHT = 700;

    private static final int DEFAULT_COLOR = Color.parseColor("#086788"); // Azul escuro
    private static final int SELECTED_COLOR = Color.parseColor("#DD1C1A"); // Vermelho

    private int rodNumber;
    private boolean isSelected;
    private int diskCount;

    private Stack<Disk> disks;

    public Rod(int rodNumber){
        this.rodNumber = rodNumber;
        disks = new Stack<>();
    }

    public int getRodNumber() {
        return rodNumber;
    }

    public Disk pop(){
        diskCount--;
        Disk disk = disks.pop();
        disk.setRod(null);
        return disk;
    }

    public Disk push(Disk disk){
        disk.setRod(this);
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
        return current.getDiskNumber() > disk.getDiskNumber();
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public float getXCenter(){
        return x + (width / 2);
    }

    public float getNextDiskY(){
        return (y + height) - ((Disk.SPACE + Disk.DEFAULT_HEIGHT) * (disks.size()));
    }

    public HashMap<Integer, Disk> getDisks(){
        HashMap<Integer, Disk> diskHashMap = new HashMap<>();

        for (Disk disk : disks){
            diskHashMap.put(disk.getDiskNumber(), disk);
        }

        return diskHashMap;
    }

    @Override
    public boolean intercept(float x, float y) {
        if (disks.size() == 0) return super.intercept(x, y);

        Disk disk = disks.peek();

        return super.intercept(x, y) || disk.intercept(x, y);
    }
}
