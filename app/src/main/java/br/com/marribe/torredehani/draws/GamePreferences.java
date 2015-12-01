package br.com.marribe.torredehani.draws;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by danielmarcoto on 30/11/15.
 */
public class GamePreferences {
    private static GamePreferences instance;

    private String playerName;
    private String diskAmount;
    private String speedMovement;

    private GamePreferences(){}

    public static GamePreferences getInstance(){
        if (instance == null) {
            instance = new GamePreferences();
        }
        return instance;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getDiskAmount() {
        return diskAmount;
    }

    public void setDiskAmount(String diskAmount) {
        this.diskAmount = diskAmount;
    }

    public String getSpeedMovement() {
        return speedMovement;
    }

    public void setSpeedMovement(String speedMovement) {
        this.speedMovement = speedMovement;
    }
}
