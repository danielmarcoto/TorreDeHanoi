package br.com.marribe.torredehani;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.marribe.torredehani.draws.Disk;
import br.com.marribe.torredehani.draws.DiskMovement;
import br.com.marribe.torredehani.draws.GameAutosolve;
import br.com.marribe.torredehani.draws.GamePreferences;
import br.com.marribe.torredehani.draws.GameView;
import br.com.marribe.torredehani.draws.Rod;
import br.com.marribe.torredehani.draws.TowerOfHanoi;
import br.com.marribe.torredehani.interfaces.GameAutomation;
import br.com.marribe.torredehani.interfaces.OnGameEvent;
import br.com.marribe.torredehani.persistence.DbService;
import br.com.marribe.torredehani.persistence.GameMatch;
import br.com.marribe.torredehani.util.ActivityUtil;
import br.com.marribe.torredehani.util.GameDbTrace;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;
    private FloatingActionButton fab;
    private TowerOfHanoi game;
    private DbService dbService;
    private GameMatch gameMatch;
    private long currentGameId;
    private GamePreferences gamePreferences;

    private boolean isNewGame;
    private boolean isAutoSolutionRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Carregar estado inicial das preferências do usuário
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        gamePreferences = GamePreferences.getInstance();
        gamePreferences.setPlayerName(
                sharedPref.getString(
                        SettingsActivity.preferenceName,
                        getString(R.string.pref_default_player_name)
                )
        );
        gamePreferences.setDiskAmount(
                sharedPref.getString(
                        SettingsActivity.preferenceDisksAmount, "3"
                )
        );
        gamePreferences.setSpeedMovement(
                sharedPref.getString(
                        SettingsActivity.preferenceSpeedEffect, "100"
                )
        );

        // Verifica se há um id de Jogo
        if (savedInstanceState != null){
            currentGameId = savedInstanceState.getLong(ActivityUtil.ID_GAME_BUNDLE, 0);

            List<int[]> movements = dbService.getMovements(currentGameId);

            //List<DiskMovement> list = toDiskMovementList(movements.);

            //GameAutomation gameAutomation = new GameDbTrace(game, list);

            //gameView.startSolution(gameAutomation);

            Log.i("Log", "currentGameId: " + currentGameId);
        }

        gameView = (GameView)findViewById(R.id.gameStage);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        dbService = new DbService(getApplicationContext());
        isNewGame = true;

        // Verifica se existe um estado de jogo anterior
        long lastGameId = dbService.getRecentGameId();
        if (lastGameId > 0){
            gameMatch = dbService.getGame(lastGameId);
            if (!gameMatch.isFinished()){
                currentGameId = lastGameId;
                isNewGame = false;
            }
        }

        // Declarar os eventos
        gameView.setOnGameEvent(new OnGameEvent() {
            @Override
            public void onStart() {
                Log.i("Log", "OnStart");

                fab.setVisibility(View.INVISIBLE);

                if (!isAutoSolutionRunning)
                    currentGameId = dbService.createGame(gameMatch);
            }

            @Override
            public void onFinish() {
                Log.i("Log", "OnFinish");

                showMessage(getString(R.string.msg_you_won));

                fab.setVisibility(View.VISIBLE);

                if (!isAutoSolutionRunning) {
                    gameMatch.setIsFinished(true);
                    dbService.updateGame(currentGameId, gameMatch);
                }

                currentGameId = 0;
                isAutoSolutionRunning = false;
            }

            @Override
            public void onInitialize() {
                Log.i("Log", "OnInitialize");

                fab.setVisibility(View.VISIBLE);

                game = gameView.getGame();

                if (isNewGame) {
                    isNewGame = false;
                    restart();
                    return;
                }

                // Detecta se o jogo anterior não foi ganho
                if (!gameMatch.isFinished()) {

                    List<int[]> diskMovements = dbService.getMovements(currentGameId);

                    // Debug game state
                    Log.i("Log", "Estados das Rods");
                    Log.i("Log", "Rod 1 com " + game.getFirstRod().getDisks().size() + " discos");
                    Log.i("Log", "Rod 2 com " + game.getSecondRod().getDisks().size() + " discos");
                    Log.i("Log", "Rod 3 com " + game.getThirdRod().getDisks().size() + " discos");

                    game.setGameState(diskMovements);

                    fab.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNotAllowedMovement() {
                showMessage(getString(R.string.msg_wrong_movement));
            }

            @Override
            public void onDiskMoves(DiskMovement diskMovement, boolean isUndo) {
                Log.i("Log", "OnDiskMoves");

                if (isAutoSolutionRunning) return;

                if (!isUndo) {
                    gameMatch.setMovementAmount(game.getAmountOfMovements());
                    dbService.updateGame(currentGameId, gameMatch);
                    dbService.saveMovement(currentGameId, diskMovement);
                } else {
                    long id = dbService.getRecentMovementId();
                    dbService.deleteMovement(id);
                }
            }

            @Override
            public void onException(Exception ex) {
                showMessage(ex.getMessage());
            }
        });

        // Botão play
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage(getString(R.string.msg_start_auto_resolution));

                startAutoSolution();

                fab.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        gameView.stop();
    }

    @Override
    protected void onStart() {
        super.onStart();

        gameView.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (isAutoSolutionRunning) return false;

        switch (id){
           case R.id.action_new_game:
                Log.i("Hanoi", "Novo Jogo");

                isNewGame = true;

                dbService.deleteMovements(currentGameId);
                dbService.deleteGame(currentGameId);

                restart();

                gameView.initialize();
                break;
            case R.id.action_undo:
                Log.i("Hanoi", "Desfazer");
                if (!gameView.undo()){
                    showMessage(getString(R.string.msg_impossible_undo));
                }
                break;
            case R.id.action_solve:
                Log.i("Hanoi", "Resolver");
                startAutoSolution();
                break;
            case R.id.action_history:
                Log.i("Hanoi", "Histórico");

                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivityForResult(intent, ActivityUtil.HISTORY_ACTIVITY);

                break;
            case R.id.action_settings:
                Log.i("Hanoi", "Configurações");

                Intent intentSetting = new Intent(this, SettingsActivity.class);
                startActivityForResult(intentSetting, ActivityUtil.SETTINGS_ACTIVITY);
                break;
        }

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }

    private void startAutoSolution(){
        if (game.isHasStarted()){
            showMessage(getString(R.string.msg_cannot_autosolve));
        } else {
            gameView.startSolution(new GameAutosolve(game));
            isAutoSolutionRunning = true;
        }
    }

    private void showMessage(String msg){
        Snackbar.make(gameView, msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();
    }

    private List<DiskMovement> toDiskMovementList(List<int[]> diskMovements){

        List<DiskMovement> list = new ArrayList<>();

        // Converte um array de inteiros para um movimento de disco
        for (int i = 0; i < diskMovements.size(); i++) {
            int[] items = diskMovements.get(i);

            Rod rodCurrent = game.getRodByNumber(items[1]);
            Rod rodDestination = game.getRodByNumber(items[2]);

            HashMap<Integer, Disk> disks = rodCurrent.getDisks();
            Disk disk = disks.get(items[0]);

            DiskMovement diskMovement = new DiskMovement();
            diskMovement.setCurrent(rodCurrent);
            diskMovement.setDestination(rodDestination);
            diskMovement.setDisk(disk);
            list.add(diskMovement);

            /*
            game.selectDestinationRod(rodCurrent);
            game.selectDestinationRod(rodDestination);
            game.moveDisk(disk, rodDestination); */
        }

        return list;
    }

    private void restart(){
        gameMatch = new GameMatch();
        gameMatch.setPlayerName(gamePreferences.getPlayerName());
        gameMatch.setDisksAmount(game.getAmountOfDisks());
        gameMatch.setMovementAmount(0);
        gameMatch.setIsFinished(false);
    }
}
