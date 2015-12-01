package br.com.marribe.torredehani.interfaces;

/**
 * Created by danielmarcoto on 26/11/15.
 */
public interface OnGameEvent {
    void onStart();
    void onFinish();
    void onNotAllowedMovement();
    void onException(Exception ex);
}
