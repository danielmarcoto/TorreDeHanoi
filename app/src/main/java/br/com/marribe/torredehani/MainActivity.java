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

import br.com.marribe.torredehani.draws.GamePreferences;
import br.com.marribe.torredehani.draws.GameView;
import br.com.marribe.torredehani.interfaces.OnGameEvent;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Carregar estado inicial das preferências
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        GamePreferences gamePreferences = GamePreferences.getInstance();
        gamePreferences.setPlayerName(
                sharedPref.getString(
                        SettingsActivity.preferenceName, getString(R.string.pref_player_name)
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

        // Declarar os eventos
        gameView.setOnGameEvent(new OnGameEvent() {
            @Override
            public void onStart() {
                fab.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFinish() {
                Snackbar.make(gameView, getString(R.string.msg_you_won), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                fab.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNotAllowedMovement() {
                Snackbar.make(gameView, getString(R.string.msg_wrong_movement), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
                break;
            case R.id.action_solve:
                Log.i("Hanoi", "Resolver");
                gameView.startSolution();
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
