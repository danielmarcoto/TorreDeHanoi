package br.com.marribe.torredehani.interfaces;

import br.com.marribe.torredehani.draws.DiskMovement;
import br.com.marribe.torredehani.draws.TowerOfHanoi;

/**
 * Created by danielmarcoto on 01/12/15.
 */
public abstract class GameAutomation {

    protected TowerOfHanoi towerOfHanoi;
    protected int totalDisks;
    //protected double totalMovements;

    protected DiskMovement[] movements;

    public GameAutomation(TowerOfHanoi towerOfHanoi) {
        this.towerOfHanoi = towerOfHanoi;

        totalDisks = towerOfHanoi.getAmountOfDisks();
        //totalMovements = Math.pow(2, totalDisks) - 1;

        movements = new DiskMovement[(int)getTotalMovements()];
    }

    protected double getTotalMovements(){
        return Math.pow(2, totalDisks) - 1;
    }

    public abstract void initialize();

    public abstract DiskMovement getNextDiskMovement();
}
