package br.com.marribe.torredehani.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
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

    public long createGame(GameMatch gameMatch){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.GameEntry.COLUMN_DURATION,
                gameMatch.getDuration());
        contentValues.put(DbContract.GameEntry.COLUMN_DISKS_AMOUNT,
                gameMatch.getDisksAmount());
        contentValues.put(DbContract.GameEntry.COLUMN_MOVEMENTS_AMOUNT,
                gameMatch.getMovementAmount());
        contentValues.put(DbContract.GameEntry.COLUMN_PLAYER_NAME,
                gameMatch.getPlayerName());
        contentValues.put(DbContract.GameEntry.COLUMN_IS_FINISHED, 0);

        // Obtem a data atual
        Calendar calendar = Calendar.getInstance();
        contentValues.put(DbContract.GameEntry.COLUMN_DATE, calendar.getTimeInMillis());

        long id = database.insert(DbContract.GameEntry.TABLE_NAME, null, contentValues);

        Log.i("Log", "BD: Salvar jogo");
        return id;
    }

    public void updateGame(long gameId, GameMatch gameMatch){

        int isFinished = gameMatch.isFinished() ? 1 : 0;

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.GameEntry.COLUMN_IS_FINISHED, isFinished);
        contentValues.put(DbContract.GameEntry.COLUMN_MOVEMENTS_AMOUNT,
                gameMatch.getMovementAmount());
        contentValues.put(DbContract.GameEntry.COLUMN_DURATION, gameMatch.getDuration());

        String[] whereArgs = { String.valueOf(gameId) };

        database.update(DbContract.GameEntry.TABLE_NAME,
                contentValues,
                DbContract.GameEntry.COLUMN_ID + " = ?",
                whereArgs);

        Log.i("Log", "BD: Atualizar jogo");
    }

    public void saveMovement(long gameId, DiskMovement diskMovement){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.MovementEntry.COLUMN_ID_GAME, gameId);
        contentValues.put(DbContract.MovementEntry.COLUMN_DISK_NUMBER,
                diskMovement.getDisk().getDiskNumber());
        contentValues.put(DbContract.MovementEntry.COLUMN_ROD_ORIGIN_NUMBER,
                diskMovement.getCurrent().getRodNumber());
        contentValues.put(DbContract.MovementEntry.COLUMN_ROD_DESTINATION_NUMBER,
                diskMovement.getDestination().getRodNumber());

        long id = database.insert(DbContract.MovementEntry.TABLE_NAME,
                null, contentValues);

        Log.i("Log", "BD: Salvar movimento de jogo: " + id + " / gameid: " + gameId);
    }

    public long getRecentGameId(){

        long gameId = 0;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT MAX(");
        sql.append(DbContract.GameEntry.COLUMN_ID);
        sql.append(") AS Maior FROM ");
        sql.append(DbContract.GameEntry.TABLE_NAME);

        Cursor cursor = database.rawQuery(sql.toString(), null);

        if (cursor != null &&
                cursor.moveToFirst() &&
                !cursor.isNull(0)){
            gameId = cursor.getLong(0);
        }

        if (!cursor.isClosed()){
            cursor.close();
        }

        Log.i("Log", "BD: Obter Id mais recente de jogo: " + gameId);

        return gameId;
    }

    public long getRecentMovementId(){

        long gameId = 0;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT MAX(");
        sql.append(DbContract.MovementEntry.COLUMN_ID);
        sql.append(") AS Maior FROM ");
        sql.append(DbContract.MovementEntry.TABLE_NAME);

        Cursor cursor = database.rawQuery(sql.toString(), null);

        if (cursor != null &&
                cursor.moveToFirst() &&
                !cursor.isNull(0)){
            gameId = cursor.getLong(0);
        }

        if (!cursor.isClosed()){
            cursor.close();
        }

        Log.i("Log", "BD: Obter Id mais recente de movimento: " + gameId);

        return gameId;
    }

    public GameMatch getGame(long gameId){

        GameMatch gameMatch = null;

        String[] columns = {
                DbContract.GameEntry.COLUMN_PLAYER_NAME,
                DbContract.GameEntry.COLUMN_MOVEMENTS_AMOUNT,
                DbContract.GameEntry.COLUMN_DISKS_AMOUNT,
                DbContract.GameEntry.COLUMN_DURATION,
                DbContract.GameEntry.COLUMN_IS_FINISHED
        };

        String[] whereArgs = { String.valueOf(gameId) };

        Cursor cursor = database.query(DbContract.GameEntry.TABLE_NAME,
                columns,
                DbContract.GameEntry.COLUMN_ID + " = ?",
                whereArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()){

            gameMatch = new GameMatch();

            int indexMovements = cursor.getColumnIndexOrThrow(
                    DbContract.GameEntry.COLUMN_MOVEMENTS_AMOUNT
            );
            int indexPlayerName = cursor.getColumnIndexOrThrow(
                    DbContract.GameEntry.COLUMN_PLAYER_NAME
            );
            int indexDiskAmount = cursor.getColumnIndexOrThrow(
                    DbContract.GameEntry.COLUMN_DISKS_AMOUNT
            );
            int indexIsFinished = cursor.getColumnIndexOrThrow(
                    DbContract.GameEntry.COLUMN_IS_FINISHED
            );
            int indexDuration = cursor.getColumnIndex(
                    DbContract.GameEntry.COLUMN_DURATION
            );

            gameMatch.setIsFinished(cursor.getInt(indexIsFinished) == 1);
            gameMatch.setMovementAmount(cursor.getInt(indexMovements));
            gameMatch.setDuration(cursor.getInt(indexDuration));
            gameMatch.setPlayerName(cursor.getString(indexPlayerName));
            gameMatch.setDisksAmount(cursor.getInt(indexDiskAmount));
        }

        if (!cursor.isClosed()){
            cursor.close();
        }

        Log.i("Log", "BD: Obter o jogo por ID");
        Log.i("Log", "finished:" + gameMatch.isFinished());
        Log.i("Log", "disks: " + gameMatch.getDisksAmount());
        Log.i("Log", "movements: " + gameMatch.getDisksAmount());
        Log.i("Log", "playerName: " + gameMatch.getPlayerName());

        return gameMatch;
    }

    public List<GameMatch> getGames(){
        List<GameMatch> list = new ArrayList<>();

        String[] columns = {
                DbContract.GameEntry.COLUMN_PLAYER_NAME,
                DbContract.GameEntry.COLUMN_DURATION,
                DbContract.GameEntry.COLUMN_DISKS_AMOUNT,
                DbContract.GameEntry.COLUMN_MOVEMENTS_AMOUNT,
                DbContract.GameEntry.COLUMN_IS_FINISHED,
                DbContract.GameEntry.COLUMN_ID
        };

        Cursor cursor = database.query(
                DbContract.GameEntry.TABLE_NAME,
                columns,
                null,
                null, null, null, DbContract.GameEntry.COLUMN_ID, "20"
        );

        if (cursor != null && cursor.moveToFirst()){
            do {
                int indexPlayerName = cursor
                        .getColumnIndexOrThrow(DbContract.GameEntry.COLUMN_PLAYER_NAME);
                int indexDuration = cursor
                        .getColumnIndexOrThrow(DbContract.GameEntry.COLUMN_DURATION);
                int indexDisks = cursor
                        .getColumnIndexOrThrow(DbContract.GameEntry.COLUMN_DISKS_AMOUNT);
                int indexMovements = cursor
                        .getColumnIndexOrThrow(DbContract.GameEntry.COLUMN_MOVEMENTS_AMOUNT);
                int indexIsFinished = cursor
                        .getColumnIndexOrThrow(DbContract.GameEntry.COLUMN_IS_FINISHED);
                int indexId = cursor
                        .getColumnIndexOrThrow(DbContract.GameEntry.COLUMN_ID);
                /*int indexDate = cursor
                        .getColumnIndexOrThrow(DbContract.GameEntry.COLUMN_DATE);*/

                GameMatch gameMatch = new GameMatch();

                gameMatch.setPlayerName(cursor.getString(indexPlayerName));
                gameMatch.setDisksAmount(cursor.getInt(indexDisks));
                gameMatch.setDuration(cursor.getInt(indexDuration));
                gameMatch.setMovementAmount(cursor.getInt(indexMovements));
                gameMatch.setIsFinished(cursor.getInt(indexIsFinished) == 1);
                //gameMatch.setDate(cursor.getLong(indexDate));
                gameMatch.setId(cursor.getLong(indexId));

                list.add(gameMatch);

            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed())
            cursor.close();

        return list;
    }

    public List<int[]> getMovements(long gameId){
        List<int[]> list = new ArrayList<>();

        String[] columns = {
                DbContract.MovementEntry.COLUMN_DISK_NUMBER,
                DbContract.MovementEntry.COLUMN_ROD_ORIGIN_NUMBER,
                DbContract.MovementEntry.COLUMN_ROD_DESTINATION_NUMBER
        };

        Log.i("Log", "gameId: " + gameId);

        String[] selectionArgs = { String.valueOf(gameId) };

        Cursor cursor = database.query(
                DbContract.MovementEntry.TABLE_NAME,
                columns,
                DbContract.MovementEntry.COLUMN_ID_GAME + " = ?",
                selectionArgs,
                null,
                null,
                null,
                null);

        Log.i("Log", "O cursor retornou  " + cursor.getCount() + " reg");

        if (cursor.moveToFirst()){
            do {
                int indexDisk = cursor.getColumnIndexOrThrow(
                        DbContract.MovementEntry.COLUMN_DISK_NUMBER
                );
                int indexOrigin = cursor.getColumnIndexOrThrow(
                        DbContract.MovementEntry.COLUMN_ROD_ORIGIN_NUMBER
                );
                int indexDestination = cursor.getColumnIndexOrThrow(
                        DbContract.MovementEntry.COLUMN_ROD_DESTINATION_NUMBER
                );

                int[] items = new int[3];
                items[0] = cursor.getInt(indexDisk);
                items[1] = cursor.getInt(indexOrigin);
                items[2] = cursor.getInt(indexDestination);
                list.add(items);

            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed())
            cursor.close();

        Log.i("Log", "BD: Obter a lista de movimentos de disco por ID");
        for (int[] item: list) {
            Log.i("Log", "disk:" + item[0] + " / ori:" + item[1] + " / des: " + item[2]);
        }

        return list;
    }

    public void deleteMovements(long gameId){
        String[] whereArgs = { String.valueOf(gameId) };

        database.delete(
                DbContract.MovementEntry.TABLE_NAME,
                DbContract.MovementEntry.COLUMN_ID_GAME + " = ?",
                whereArgs);
    }

    public void deleteMovement(long id){
        String[] whereArgs = { String.valueOf(id) };

        database.delete(
                DbContract.MovementEntry.TABLE_NAME,
                DbContract.MovementEntry.COLUMN_ID + " = ?",
                whereArgs);
    }

    public void deleteGame(long gameId){

        String[] whereArgs = { String.valueOf(gameId) };

        database.delete(
                DbContract.GameEntry.TABLE_NAME,
                DbContract.GameEntry.COLUMN_ID + " = ?",
                whereArgs);
    }
}
