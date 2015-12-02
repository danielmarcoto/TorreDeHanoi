package br.com.marribe.torredehani.interfaces;

import br.com.marribe.torredehani.draws.DiskMovement;

/**
 * Created by danielmarcoto on 26/11/15.
 */
public interface OnGameEvent {
    void onStart();
    void onFinish();
    void onInitialize();
    void onNotAllowedMovement();
    void onDiskMoves(DiskMovement diskMovement, boolean isUndo);
    void onException(Exception ex);
}
