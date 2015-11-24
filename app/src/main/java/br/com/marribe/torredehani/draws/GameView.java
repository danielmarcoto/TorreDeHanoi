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
    private GameAutosolve solution;

    private boolean isRunning;
    private boolean isInitialized;

    private float xTouch;
    private float yTouch;

    private Disk diskToFadeIn;
    private Disk diskToFadeOut;

    private Rod rodDestination;

    private boolean canPlay;

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

    public void startSolution(){
        canPlay = false;

        solution = new GameAutosolve(game);

        // Primeiro movimento
        Rod[] movement = solution.getDisksPairOriginDestination();
        game.selectDestinationRod(movement[0]);
        game.selectDestinationRod(movement[1]);

        diskToFadeOut = game.getDisk();
        rodDestination = game.getDestinationRod();

        Log.i("Log", "Iniciar auto-solução!!!");
    }

    public void initialize(){

        try {
            game = new TowerOfHanoi(3);
            game.setX(0);
            game.setY(0);
            game.setWidth(getWidth());
            game.setHeight(getHeight());

            game.initialize();

            canPlay = true;

            final View current = this;

            // Criar eventos para o toque
            setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (!canPlay) return false;

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {

                        xTouch = event.getX();
                        yTouch = event.getY();

                        if (isInitialized) {

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

                            switch (movementState){
                                case Ok:
                                    diskToFadeOut = game.getDisk();
                                    rodDestination = game.getDestinationRod();
                                    canPlay = false;
                                    break;
                                case NotAllowed:
                                    Snackbar.make(current,
                                            "Movimento não permitido",
                                            Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    break;
                            }
                        }
                    }

                    return true;
                }
            });
        } catch (InvalidStateException ex){
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
                        final int diskIncrement = 51;

                        if (diskToFadeOut != null){
                            int alpha = diskToFadeOut.getAlpha();
                            diskToFadeOut.setAlpha(alpha - diskIncrement);

                            if (alpha == 0) {
                                game.moveDisk(diskToFadeOut, rodDestination);
                                diskToFadeIn = diskToFadeOut;
                                diskToFadeOut = null;

                                Log.i("Log", "acabou o fade out");
                            }
                        }

                        if (diskToFadeIn != null){
                            int alpha = diskToFadeIn.getAlpha();

                            if (alpha == 255) {

                                // Detecta quando o jogo acabou, jogador venceu
                                if (game.getAmountOfDisks() ==
                                        game.getThirdRod().getDiskCount()){

                                    Snackbar.make(current, "Você venceu!!!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();

                                    isInitialized = false;
                                    solution = null;
                                }

                                canPlay = true;
                                diskToFadeIn = null;
                                diskToFadeOut = null;
                                game.setDisk(null);
                                game.setDestinationRod(null);

                                Log.i("Log", "acabou o fade in");

                                // Detecta se há auto-solução em andamento
                                if (solution != null) {
                                    Rod[] firstMovement = solution.getDisksPairOriginDestination();
                                    game.selectDestinationRod(firstMovement[0]);
                                    game.selectDestinationRod(firstMovement[1]);

                                    diskToFadeOut = game.getDisk();
                                    rodDestination = game.getDestinationRod();

                                }
                            } else {
                                diskToFadeIn.setAlpha(alpha + diskIncrement);
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
