package br.com.marribe.torredehani.draws;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by danielmarcoto on 20/10/15.
 */
public class Disk extends GameObject {

    public static final float SPACE = 20;
    public static final float WIDTH_INCREMENT = 30;
    public static final float DEFAULT_HEIGHT = 60;
    public static final float WIDTH_TO_GREATER = 300;

    private static final int COLOR = Color.parseColor("#F0C808"); // Amarelo
    private static final int TEXT_COLOR = Color.parseColor("#000000");

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
        textPaint.setTextSize(40);
        textPaint.setAntiAlias(true);
        textPaint.setColor(TEXT_COLOR);
        textPaint.setTextAlign(Paint.Align.CENTER);

        canvas.drawRect(x, y, x + width, y + height, paint);
        canvas.drawText(String.valueOf(diskNumber), x + (width / 2), y + 40, textPaint);
    }
}
