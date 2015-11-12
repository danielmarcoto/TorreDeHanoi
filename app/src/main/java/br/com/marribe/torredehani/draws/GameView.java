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
public class GameView extends View {

    private TowerOfHanoi game;
    private boolean isRunning;
    private boolean isInitialized;

    private float xTouch;
    private float yTouch;

    private Context contextActity;

    public GameView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);

        isInitialized = false;
        contextActity = context;
    }

    public GameView(Context context){
        super(context);

        contextActity = context;
    }

    public void initialize(){

        try {
            game = new TowerOfHanoi(3);
            game.setX(0);
            game.setY(0);
            game.setWidth(getWidth());
            game.setHeight(getHeight());

            game.initialize();

            final View current = this;

            // Criar eventos para o toque
            setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {

                        xTouch = event.getX();
                        yTouch = event.getY();

                        if (isInitialized) {
                            // TODO: Interações do Jogo
                            TowerOfHanoi.MovementState movementState = TowerOfHanoi.MovementState.Nothing;

                            if (game.getFirstRod().intercept(xTouch, yTouch)) {
                                movementState = game.selectDestinationRod(game.getFirstRod());
                            }

                            if (game.getSecondRod().intercept(xTouch, yTouch)) {
                                movementState = game.selectDestinationRod(game.getSecondRod());
                            }

                            if (game.getThirdRod().intercept(xTouch, yTouch)) {
                                movementState = game.selectDestinationRod(game.getThirdRod());
                            }

                            Log.i("Log", "RETORNO: " + movementState.toString());

                            if (movementState.equals(TowerOfHanoi.MovementState.NotAllowed)) {
                                Snackbar.make(current, "Movimento não permitido", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }

                            movementState = null;
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

        final View current = this;

        AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                while (isRunning){
                    try {
                        //
                        if (game.getDestinationRod() != null &&
                                game.getDisk() != null){

                            game.moveDisk(game.getDisk(), game.getDestinationRod());

                            if (game.getAmountOfDisks() ==
                                    game.getThirdRod().getDiskCount()){

                                Snackbar.make(current, "Você venceu!!!", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                                isInitialized = false;
                            }

                            game.setDisk(null);
                            game.setDestinationRod(null);
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
