package br.com.marribe.torredehani.draws;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by danielmarcoto on 24/11/15.
 */
public class GameAutosolve {
    private TowerOfHanoi towerOfHanoi;

    private List<Rod> rodsOrigin;
    private List<Rod> rodsDestination;

    private int currentPosition;

    public GameAutosolve(TowerOfHanoi towerOfHanoi) {
        this.towerOfHanoi = towerOfHanoi;

        // Solução estática para 3 discos
        if (towerOfHanoi.getAmountOfDisks() == 3){
            rodsOrigin = new ArrayList<>(7);
            rodsDestination = new ArrayList<>(7);
            currentPosition = 0;

            rodsOrigin.add(0, towerOfHanoi.getFirstRod());
            rodsOrigin.add(1, towerOfHanoi.getFirstRod());
            rodsOrigin.add(2, towerOfHanoi.getThirdRod());
            rodsOrigin.add(3, towerOfHanoi.getFirstRod());
            rodsOrigin.add(4, towerOfHanoi.getSecondRod());
            rodsOrigin.add(5, towerOfHanoi.getSecondRod());
            rodsOrigin.add(6, towerOfHanoi.getFirstRod());

            rodsDestination.add(0, towerOfHanoi.getThirdRod());
            rodsDestination.add(1, towerOfHanoi.getSecondRod());
            rodsDestination.add(2, towerOfHanoi.getSecondRod());
            rodsDestination.add(3, towerOfHanoi.getThirdRod());
            rodsDestination.add(4, towerOfHanoi.getFirstRod());
            rodsDestination.add(5, towerOfHanoi.getThirdRod());
            rodsDestination.add(6, towerOfHanoi.getThirdRod());
        }
    }

    public Rod[] getDisksPairOriginDestination(){
        Rod[] disks = {
                rodsOrigin.get(currentPosition),
                rodsDestination.get(currentPosition)
        };
        currentPosition++;
        return disks;
    }
}
