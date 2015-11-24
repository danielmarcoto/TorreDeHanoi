package br.com.marribe.torredehani;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import br.com.marribe.torredehani.draws.GameView;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gameView = (GameView)findViewById(R.id.gameStage);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Iniciar auto-resolução do jogo", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                gameView.startSolution();
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
                break;
            case R.id.action_undo:
                Log.i("Hanoi", "Desfazer");
                break;
            case R.id.action_solve:
                Log.i("Hanoi", "Resolver");
                break;
            case R.id.action_settings:
                Log.i("Hanoi", "Configurações");
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
