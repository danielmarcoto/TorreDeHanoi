package br.com.marribe.torredehani.util;

import java.util.List;

import br.com.marribe.torredehani.draws.DiskMovement;
import br.com.marribe.torredehani.draws.TowerOfHanoi;
import br.com.marribe.torredehani.interfaces.GameAutomation;

/**
 * Created by danielmarcoto on 01/12/15.
 */
public class GameDbTrace extends GameAutomation {

    List<DiskMovement> diskMovementList;
    private int currentItem;

    public GameDbTrace(TowerOfHanoi towerOfHanoi, List<DiskMovement> diskMovementList) {
        super(towerOfHanoi);

        this.diskMovementList = diskMovementList;
    }

    @Override
    public void initialize() {
        currentItem = 0;
    }

    @Override
    public DiskMovement getNextDiskMovement() {
        return diskMovementList.get(currentItem++);
    }
}
