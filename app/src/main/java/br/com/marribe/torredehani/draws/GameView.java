package br.com.marribe.torredehani.draws;

import android.content.Context;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;
import java.util.Stack;

import br.com.marribe.torredehani.interfaces.OnGameEvent;

/**
 * Created by danielmarcoto on 20/10/15.
 */
public class GameView extends View {

    private Stack<DiskMovement> diskMovements;
    private TowerOfHanoi game;
    private GameAutosolve solution;
    private GamePreferences gamePreferences;
    private OnGameEvent onGameEvent;

    private boolean isRunning;
    private boolean isInitialized;

    private float xTouch;
    private float yTouch;

    private Disk diskToFadeIn;
    private Disk diskToFadeOut;
    private Rod rodDestination;

    private boolean canPlay;
    private boolean canNotifyGameInit;
    private boolean canNotifyGameFinished;

    public GameView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);

        isInitialized = false;
        gamePreferences = GamePreferences.getInstance();
    }

    public GameView(Context context){
        super(context);
    }

    public TowerOfHanoi getGame() {
        return game;
    }

    public OnGameEvent getOnGameEvent() {
        return onGameEvent;
    }

    public boolean isAutoSolutionRunning(){
        return solution != null;
    }

    public void setOnGameEvent(OnGameEvent onGameEvent) {
        this.onGameEvent = onGameEvent;
    }

    public void startTrace(List<DiskMovement> diskMovements){
        // TODO: Fazer meio de reproduzir um jogo já registado no banco de dados
    }

    public void startSolution(){
        canPlay = false;

        solution = new GameAutosolve(game);
        solution.initialize();

        // Primeiro movimento
        DiskMovement movement = solution.getNextDiskMovement();
        game.selectDestinationRod(movement.getCurrent());
        game.selectDestinationRod(movement.getDestination());

        diskToFadeOut = game.getDisk();
        rodDestination = game.getDestinationRod();

        Log.i("Log", "Iniciar auto-solução!!!");
    }

    public void initialize(){

        try {
            diskMovements = new Stack<>();

            final int diskAmount = Integer.parseInt(gamePreferences.getDiskAmount());

            game = new TowerOfHanoi(diskAmount);
            game.setX(0);
            game.setY(0);
            game.setWidth(getWidth());
            game.setHeight(getHeight());

            game.initialize();

            canPlay = true;

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

                                    if (onGameEvent != null){
                                        DiskMovement diskMovement = new DiskMovement();
                                        diskMovement.setDestination(game.getDestinationRod());
                                        diskMovement.setDisk(game.getDisk());
                                        diskMovement.setCurrent(game.getSelectedRod());
                                        diskMovements.push(diskMovement);

                                        onGameEvent.onDiskMoves(diskMovement);
                                    }

                                    diskToFadeOut = game.getDisk();
                                    rodDestination = game.getDestinationRod();
                                    canPlay = false;
                                    break;
                                case NotAllowed:
                                    if (onGameEvent != null)
                                        onGameEvent.onNotAllowedMovement();
                                    break;
                            }
                        }
                    }

                    return true;
                }
            });
        } catch (InvalidStateException ex){
            if (onGameEvent != null)
                onGameEvent.onException(ex);
        }

        if (onGameEvent != null)
            onGameEvent.onInitialize();
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
                        final int diskIncrement = 51; //17

                        if (diskToFadeOut != null){

                            int alpha = diskToFadeOut.getAlpha();
                            diskToFadeOut.setAlpha(alpha - diskIncrement);

                            if (alpha == 0) {
                                game.moveDisk(diskToFadeOut, rodDestination);
                                diskToFadeIn = diskToFadeOut;
                                diskToFadeOut = null;

                                if (game.getAmountOfMovements() == 1){
                                    canNotifyGameInit = true;
                                }

                                Log.i("Log", "acabou o fade out");
                            }
                        }

                        if (diskToFadeIn != null){
                            int alpha = diskToFadeIn.getAlpha();

                            if (alpha == 255) {

                                // Detecta quando o jogo acabou, jogador venceu
                                if (game.getAmountOfDisks() ==
                                        game.getThirdRod().getDiskCount()){

                                    canNotifyGameFinished = true;
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
                                    DiskMovement movement = solution.getNextDiskMovement();
                                    game.selectDestinationRod(movement.getCurrent());
                                    game.selectDestinationRod(movement.getDestination());

                                    diskToFadeOut = game.getDisk();
                                    rodDestination = game.getDestinationRod();

                                }
                            } else {
                                diskToFadeIn.setAlpha(alpha + diskIncrement);
                            }
                        }

                        publishProgress();

                        int speedMovement = Integer.parseInt(gamePreferences.getSpeedMovement());
                        Thread.sleep(speedMovement);
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

                if (canNotifyGameInit && onGameEvent != null){
                    canNotifyGameInit = false;
                    onGameEvent.onStart();
                }

                if (canNotifyGameFinished && onGameEvent != null){
                    canNotifyGameFinished = false;
                    onGameEvent.onFinish();
                }

                invalidate();
            }
        };
        asyncTask.execute();
    }

    public void stop(){
        isRunning = false;
    }

    public boolean undo(){
        if (!isInitialized || diskMovements.size() == 0) return false;

        DiskMovement diskMovement = diskMovements.pop();

        // Realiza o movimento invertendo a origem e o destino
        game.selectDestinationRod(diskMovement.getDestination());
        game.selectDestinationRod(diskMovement.getCurrent());

        diskToFadeOut = game.getDisk();
        rodDestination = game.getDestinationRod();
        return true;
    }
}
