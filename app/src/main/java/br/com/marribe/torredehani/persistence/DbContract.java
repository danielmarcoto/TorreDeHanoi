package br.com.marribe.torredehani.persistence;

/**
 * Created by danielmarcoto on 01/12/15.
 */
public final class DbContract {
    private DbContract(){}

    public static abstract class GameEntry {
        public static final String TABLE_NAME = "game";
        public static final String COLUMN_ID = "id_game";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_PLAYER_NAME = "playerName";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_DISKS_AMOUNT = "diskAmount";
        public static final String COLUMN_MOVEMENTS_AMOUNT = "movementsAmount";
        public static final String COLUMN_IS_FINISHED = "isFinished";

        public static final String CREATE_TABLE_SCRIPT = "CREATE TABLE " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_DATE + " INTEGER NOT NULL," +
                COLUMN_PLAYER_NAME + " VARCHAR(200) NOT NULL," +
                COLUMN_DURATION + " INTEGER NOT NULL," +
                COLUMN_DISKS_AMOUNT + " INTEGER NOT NULL," +
                COLUMN_MOVEMENTS_AMOUNT + " INTEGER NOT NULL," +
                COLUMN_IS_FINISHED + " INTEGER NOT NULL" +
                ");";

        public static final String DROP_TABLE_SCRIPT = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class MovementEntry {
        public static final String TABLE_NAME = "movement";
        public static final String COLUMN_ID = "id_movement";
        public static final String COLUMN_ID_GAME = "id_game";
        public static final String COLUMN_DISK_NUMBER = "disk_number";
        public static final String COLUMN_ROD_ORIGIN_NUMBER = "rod_origin_number";
        public static final String COLUMN_ROD_DESTINATION_NUMBER = "rod_destination_number";

        public static final String CREATE_TABLE_SCRIPT = "CREATE TABLE " +
                TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_ID_GAME + " INTEGER NOT NULL," +
                COLUMN_DISK_NUMBER + " INTEGER NOT NULL," +
                COLUMN_ROD_ORIGIN_NUMBER + " INTEGER NOT NULL," +
                COLUMN_ROD_DESTINATION_NUMBER + " INTEGER NOT NULL" +
                ");";

        public static final String DROP_TABLE_SCRIPT = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
