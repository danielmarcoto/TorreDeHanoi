package br.com.marribe.torredehani.persistence;

/**
 * Created by danielmarcoto on 01/12/15.
 */
public class GameMatch {
    private String playerName;
    private int duration;
    private int disksAmount;
    private int movementAmount;
    private boolean isFinished;

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDisksAmount() {
        return disksAmount;
    }

    public void setDisksAmount(int disksAmount) {
        this.disksAmount = disksAmount;
    }

    public int getMovementAmount() {
        return movementAmount;
    }

    public void setMovementAmount(int movementAmount) {
        this.movementAmount = movementAmount;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }
}
