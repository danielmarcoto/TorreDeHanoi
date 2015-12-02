package br.com.marribe.torredehani.draws;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import br.com.marribe.torredehani.interfaces.GameAutomation;

/**
 * Created by danielmarcoto on 24/11/15.
 */

public class GameAutosolve extends GameAutomation {

    private int currentPosition;

    public GameAutosolve(TowerOfHanoi towerOfHanoi) {
        super(towerOfHanoi);
    }

    @Override
    public void initialize(){

        HashMap<Integer, Disk> disks = towerOfHanoi.getFirstRod().getDisks();
        HashMap<Disk, int[]> initialMovementHash = new HashMap<>();

        int positionAux = 1;

        for (int i = 1; i <= totalDisks; i++){
            Disk disk = disks.get(i);
            initialMovementHash.put(disk, new int[]{positionAux, positionAux * 2});
            positionAux *= 2;
        }

        HashMap<Integer, Rod> rodsCurrentState = new HashMap<>();
        for (int i=1; i <= totalDisks; i++){
            rodsCurrentState.put(i, towerOfHanoi.getFirstRod());
        }

        for (Map.Entry<Disk, int[]> item : initialMovementHash.entrySet()) {

            Disk disk = item.getKey();

            int initialPosition = item.getValue()[0];
            int increment = item.getValue()[1];

            for (int i = initialPosition - 1; i < getTotalMovements(); i+= increment){
                DiskMovement diskMovement = new DiskMovement();
                diskMovement.setDisk(disk);

                int diskNumber = disk.getDiskNumber();

                Rod current = rodsCurrentState.get(diskNumber);
                diskMovement.setCurrent(current);

                // Detecta se o disco é par ou ímpar
                if ((totalDisks % 2 == 1 && diskNumber % 2 == 1) ||
                        (totalDisks % 2 == 0 && diskNumber % 2 == 0)){
                    diskMovement.setDestination(getNextAntiClockwise(current));
                } else {
                    diskMovement.setDestination(getNextClockwise(current));
                }

                rodsCurrentState.put(diskNumber, diskMovement.getDestination());

                Log.i("Log", "Disco " + diskNumber +
                        " / Origem: " + current.getRodNumber() +
                        " / Destino: " + diskMovement.getDestination().getRodNumber());

                movements[i] = diskMovement;
            }
        }
    }

    @Override
    public DiskMovement getNextDiskMovement() {
        return movements[currentPosition++];
    }

    private Rod getNextClockwise(Rod rod){
        int currentNumber = rod.getRodNumber();
        int newNumber = currentNumber + 1;

        if (newNumber > 3){
            return towerOfHanoi.getRodByNumber(1);
        }
        return towerOfHanoi.getRodByNumber(newNumber);
    }

    private Rod getNextAntiClockwise(Rod rod){
        int currentNumber = rod.getRodNumber();
        int newNumber = currentNumber - 1;

        if (newNumber == 0){
            return towerOfHanoi.getRodByNumber(3);
        }
        return towerOfHanoi.getRodByNumber(newNumber);
    }
}

/*
public class GameAutosolve {
    private TowerOfHanoi towerOfHanoi;
    private int totalDisks;
    private double totalMovements;

    private DiskMovement[] movements;

    private int currentPosition;

    public GameAutosolve(TowerOfHanoi towerOfHanoi) {
        this.towerOfHanoi = towerOfHanoi;

        totalDisks = towerOfHanoi.getAmountOfDisks();
        totalMovements = Math.pow(2, totalDisks) - 1;

        movements = new DiskMovement[(int)totalMovements];
    }

    public void initialize(){

        HashMap<Integer, Disk> disks = towerOfHanoi.getFirstRod().getDisks();
        HashMap<Disk, int[]> initialMovementHash = new HashMap<>();

        int positionAux = 1;

        for (int i = 1; i <= totalDisks; i++){
            Disk disk = disks.get(i);
            initialMovementHash.put(disk, new int[]{positionAux, positionAux * 2});
            positionAux *= 2;
        }

        HashMap<Integer, Rod> rodsCurrentState = new HashMap<>();
        for (int i=1; i <= totalDisks; i++){
            rodsCurrentState.put(i, towerOfHanoi.getFirstRod());
        }

        for (Map.Entry<Disk, int[]> item : initialMovementHash.entrySet()) {

            Disk disk = item.getKey();

            int initialPosition = item.getValue()[0];
            int increment = item.getValue()[1];

            for (int i = initialPosition - 1; i < totalMovements; i+= increment){
                DiskMovement diskMovement = new DiskMovement();
                diskMovement.setDisk(disk);

                int diskNumber = disk.getDiskNumber();

                Rod current = rodsCurrentState.get(diskNumber);
                diskMovement.setCurrent(current);

                // Detecta se o disco é par ou ímpar
                if ((totalDisks % 2 == 1 && diskNumber % 2 == 1) ||
                        (totalDisks % 2 == 0 && diskNumber % 2 == 0)){
                    diskMovement.setDestination(getNextAntiClockwise(current));
                } else {
                    diskMovement.setDestination(getNextClockwise(current));
                }

                rodsCurrentState.put(diskNumber, diskMovement.getDestination());

                Log.i("Log", "Disco " + diskNumber +
                        " / Origem: " + current.getRodNumber() +
                        " / Destino: " + diskMovement.getDestination().getRodNumber());

                movements[i] = diskMovement;
            }
        }
    }

    public DiskMovement getNextDiskMovement() {
        return movements[currentPosition++];
    }

    protected Rod getNextClockwise(Rod rod){
        int currentNumber = rod.getRodNumber();
        int newNumber = currentNumber + 1;

        if (newNumber > 3){
            return towerOfHanoi.getRodByNumber(1);
        }
        return towerOfHanoi.getRodByNumber(newNumber);
    }

    protected Rod getNextAntiClockwise(Rod rod){
        int currentNumber = rod.getRodNumber();
        int newNumber = currentNumber - 1;

        if (newNumber == 0){
            return towerOfHanoi.getRodByNumber(3);
        }
        return towerOfHanoi.getRodByNumber(newNumber);
    }
}
*/