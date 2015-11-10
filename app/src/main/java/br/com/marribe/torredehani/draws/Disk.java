package br.com.marribe.torredehani.draws;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by danielmarcoto on 20/10/15.
 */
public class Disk extends GameObject {

    private static final int COLOR = Color.parseColor("#F0C808"); // Amarelo
    private static final int TEXT_COLOR = Color.parseColor("#FFF1D0");

    private int diskNumber;

    public Disk(int diskNumber){
        this.diskNumber = diskNumber;
    }

    public int getDiskNumber() {
        return diskNumber;
    }

    @Override
    public void draw(Canvas canvas) {

        // Desenhar o disk com o tamanho proporcional ao número
        Paint paint = new Paint();
        paint.setColor(COLOR);

        Paint textPaint = new Paint();
        textPaint.setColor(TEXT_COLOR);

        canvas.drawRect(x, y, x + width, y + height, paint);
        canvas.drawText(String.valueOf(diskNumber), (x + width) / 2, y + height +  (height/2), textPaint);
    }
}
