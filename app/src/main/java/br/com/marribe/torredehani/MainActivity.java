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

import java.util.List;

import br.com.marribe.torredehani.draws.DiskMovement;
import br.com.marribe.torredehani.draws.GamePreferences;
import br.com.marribe.torredehani.draws.GameView;
import br.com.marribe.torredehani.draws.TowerOfHanoi;
import br.com.marribe.torredehani.interfaces.OnGameEvent;
import br.com.marribe.torredehani.persistence.DbService;
import br.com.marribe.torredehani.persistence.GameMatch;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;
    private FloatingActionButton fab;
    private TowerOfHanoi game;
    private DbService dbService;
    private GameMatch gameMatch;
    private int currentGameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Carregar estado inicial das preferências do usuário
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final GamePreferences gamePreferences = GamePreferences.getInstance();
        gamePreferences.setPlayerName(
                sharedPref.getString(
                        SettingsActivity.preferenceName, getString(R.string.pref_default_player_name)
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

        gameView = (GameView)findViewById(R.id.gameStage);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        dbService = new DbService(getApplicationContext());

        // Declarar os eventos
        gameView.setOnGameEvent(new OnGameEvent() {
            @Override
            public void onStart() {
                fab.setVisibility(View.INVISIBLE);

                if (!gameView.isAutoSolutionRunning())
                    currentGameId = dbService.createGame(gameMatch);
            }

            @Override
            public void onFinish() {
                Snackbar.make(gameView, getString(R.string.msg_you_won), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                fab.setVisibility(View.VISIBLE);

                if (currentGameId > 0) {
                    gameMatch.setIsFinished(true);
                    dbService.updateGame(currentGameId, gameMatch);

                    currentGameId = 0;
                }
            }

            @Override
            public void onInitialize() {
                fab.setVisibility(View.VISIBLE);
                game = gameView.getGame();

                // Verifica se existe um estado de jogo anterior
                int lastGameId = dbService.getRecentGameId();
                if (lastGameId > 0){
                    gameMatch = dbService.getGame(lastGameId);

                    // Detecta se o jogo anterior não foi ganho
                    if (!gameMatch.isFinished()){
                        List<DiskMovement> diskMovements = dbService.getMovements(currentGameId);

                        game.runDisksMovements((DiskMovement[])diskMovements.toArray());
                    }
                } else{
                    gameMatch = new GameMatch();
                    gameMatch.setPlayerName(gamePreferences.getPlayerName());
                    gameMatch.setDisksAmount(game.getAmountOfDisks());
                    gameMatch.setMovementAmount(0);
                    gameMatch.setIsFinished(false);
                }
            }

            @Override
            public void onNotAllowedMovement() {
                Snackbar.make(gameView, getString(R.string.msg_wrong_movement), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

            @Override
            public void onDiskMoves(DiskMovement diskMovement) {
                if (currentGameId > 0){
                    gameMatch.setMovementAmount(game.getAmountOfMovements());
                    dbService.updateGame(currentGameId, gameMatch);
                    dbService.saveMovement(currentGameId, diskMovement);
                }
            }

            @Override
            public void onException(Exception ex) {
                Snackbar.make(gameView, ex.getMessage(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Botão play
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,
                        getString(R.string.msg_start_auto_resolution),
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                gameView.startSolution();
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

        switch (id){
            case R.id.action_new_game:
                Log.i("Hanoi", "Novo Jogo");
                gameView.initialize();
                break;
            case R.id.action_undo:
                Log.i("Hanoi", "Desfazer");
                if (!gameView.undo()){
                    Snackbar.make(gameView,
                            getString(R.string.msg_impossible_undo),
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                break;
            case R.id.action_solve:
                Log.i("Hanoi", "Resolver");
                gameView.startSolution();
                break;
            case R.id.action_history:
                Log.i("Hanoi", "Histórico");
                // TODO: Carregar activity com lista de jogos anteriores
                break;
            case R.id.action_settings:
                Log.i("Hanoi", "Configurações");

                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
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
}
