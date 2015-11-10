package br.com.marribe.torredehani.draws;

import android.content.Context;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by danielmarcoto on 20/10/15.
 */
public class AnimateView extends View {

    private TowerOfHanoi game;
    private boolean isRunning;
    private boolean isInitialized;

    private float xTouch;
    private float yTouch;

    public AnimateView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);

        isInitialized = false;
    }

    public AnimateView(Context context){
        super(context);
    }

    public void initialize(){

        try {
            game = new TowerOfHanoi(6);
            game.setX(0);
            game.setY(0);
            game.setWidth(getWidth());
            game.setHeight(getHeight());

            game.initialize();

            // Criar eventos para o toque
            setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {

                        xTouch = event.getX();
                        yTouch = event.getY();

                        if (isInitialized) {
                            // TODO: Interações do Jogo
                            TowerOfHanoi.MovementState movementState = TowerOfHanoi.MovementState.Continue;

                            if (game.getFirstRod().intercept(xTouch, yTouch)) {
                                movementState = game.selectDisk(game.getFirstRod());
                            }

                            if (game.getSecondRod().intercept(xTouch, yTouch)) {
                                movementState = game.selectDisk(game.getSecondRod());
                            }

                            if (game.getThirdRod().intercept(xTouch, yTouch)) {
                                movementState = game.selectDisk(game.getThirdRod());
                            }

                            Log.i("Log", "RETORNO: " + movementState.toString());
                        }
                        //Log.i("Log", "x: " + xTouch + " / y:" + yTouch);
                    }

                    return true;
                }
            });
        } catch (InvalidStateException ex){
            // TODO: notificar um problema
            Snackbar.make(this, ex.getMessage(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isInitialized){
            initialize();

            isInitialized = true;
        }

        game.draw(canvas);
    }

    public void start(){
        isRunning = true;

        AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                while (isRunning){
                    try {
                        //
                        if (game.getDestinationRod() != null &&
                                game.getDisk() != null){

                            Disk disk = game.getDisk();
                            Rod rod = game.getDestinationRod();

                            disk.setX(disk.getX() + 10);

                            if (disk.getWidth() >= 500){
                                game.setSelectedRod(null);
                                game.setDisk(null);
                            }
                        }

                        publishProgress();
                        Thread.sleep(10);
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);

                invalidate();
            }
        };
        asyncTask.execute();
    }

    public void stop(){
        isRunning = false;
    }
}
