package br.com.marribe.torredehani.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import br.com.marribe.torredehani.draws.DiskMovement;

/**
 * Created by danielmarcoto on 01/12/15.
 */
public class DbService {
    private SQLiteDatabase database;

    public DbService(Context context){
        GameOpenHelper gameOpenHelper = new GameOpenHelper(context);
        database = gameOpenHelper.getWritableDatabase();
    }

    public int createGame(GameMatch gameMatch){
        // TODO: salvar o jogo

        Log.i("Log", "BD: Salvar jogo");
        return 1;
    }

    public void updateGame(int gameId, GameMatch gameMatch){
        // TODO: salvar a quantidade de movimentos

        Log.i("Log", "BD: Atualizar jogo");
    }

    public void saveMovement(int gameId, DiskMovement diskMovement){
        // TODO: salvar o movimento

        Log.i("Log", "BD: Salvar movimento de jogo");
    }

    public int getRecentGameId(){
        // TODO: recuperar o jogo mais recente

        Log.i("Log", "BD: Obter Id mais recente de jogo");

        return 0;
    }

    public GameMatch getGame(int gameId){
        // TODO: recuperar o jogo pelo Id

        Log.i("Log", "BD: Obter o jogo por ID");

        return null;
    }

    public List<DiskMovement> getMovements(int gameId){
        // TODO: recuperar os movimentos de um jogo

        Log.i("Log", "BD: Obter a lista de movimentos de disco por ID");

        return null;
    }
}
