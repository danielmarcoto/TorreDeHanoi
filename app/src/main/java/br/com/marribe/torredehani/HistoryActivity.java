package br.com.marribe.torredehani;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import br.com.marribe.torredehani.persistence.DbService;
import br.com.marribe.torredehani.persistence.GameMatch;
import br.com.marribe.torredehani.util.ActivityUtil;

public class HistoryActivity extends AppCompatActivity {

    private ListView historyListView;
    private DbService dbService;
    private List<GameMatch> gameMatchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setupActionBar();

        dbService = new DbService(getApplicationContext());

        gameMatchList = dbService.getGames();

        historyListView = (ListView)findViewById(R.id.historyListView);

        setTitle(getString(R.string.history) + " (" + gameMatchList.size() + ")");

        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                mainIntent.putExtra(ActivityUtil.ID_GAME_BUNDLE, id);
                setResult(ActivityUtil.MAIN_ACTIVITY, mainIntent);
                finish();
            }
        });

        historyListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return gameMatchList.size();
            }

            @Override
            public Object getItem(int position) {
                return gameMatchList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return gameMatchList.get(position).getId();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                GameMatch gameMatch = gameMatchList.get(position);

                View view = getLayoutInflater().inflate(R.layout.list_item_history_layout, null);
                TextView playerEditTextView = (TextView) view.findViewById(R.id.playerNameTextView);
                TextView dateEditTextView = (TextView) view.findViewById(R.id.dateTextView);
                TextView infoEditTextView = (TextView) view.findViewById(R.id.infoTextView);
                playerEditTextView.setText(gameMatch.getPlayerName());

                if (gameMatch.isFinished()){
                    dateEditTextView.setText(getString(R.string.victory));
                } else {
                    dateEditTextView.setText(getString(R.string.ongoing));
                }

                // TODO: Retirar
                int limitMovevements = (int) Math.pow(2, gameMatch.getDisksAmount()) - 1;

                infoEditTextView.setText(
                        //String.format(getString(R.string.second_line_table_history),
                        String.format("Discos: %d / Movimentos: %d de %d",
                                gameMatch.getDisksAmount(),
                                gameMatch.getMovementAmount(),
                                limitMovevements));

                return view;
            }
        });
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
