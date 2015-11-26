package br.com.marribe.torredehani.draws;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by danielmarcoto on 20/10/15.
 */
public class TowerOfHanoi extends GameObject {

    private static final int STAGE_COLOR = Color.parseColor("#06AED5"); // Azul claro
    private static final int BACK_COLOR = Color.parseColor("#FFF1D0"); // Bege

    private Rod firstRod;
    private Rod secondRod;
    private Rod thirdRod;

    private Rod selectedRod;
    private Rod destinationRod;
    private Disk disk;

    private float yInitStage;
    private int amountOfDisks;

    public TowerOfHanoi(int amountOfDisks) throws InvalidStateException{
        this.amountOfDisks = amountOfDisks;
        this.selectedRod = null;
        this.disk = null;

        if (amountOfDisks < 2 || amountOfDisks > 6){
            throw new InvalidStateException("O total de discos deve ser entre 3 e 6");
        }
    }

    public Rod getSelectedRod() {
        return selectedRod;
    }

    public void setSelectedRod(Rod selectedRod) {
        this.selectedRod = selectedRod;
    }

    public int getAmountOfDisks() {
        return amountOfDisks;
    }

    public void setDestinationRod(Rod destinationRod) {
        this.destinationRod = destinationRod;
    }

    public void setDisk(Disk disk) {
        this.disk = disk;
    }


    public Rod getFirstRod() {
        return firstRod;
    }

    public Rod getSecondRod() {
        return secondRod;
    }

    public Rod getThirdRod() {
        return thirdRod;
    }

    public Rod getRodByNumber(int number){
        switch (number){
            case 1:
                return firstRod;
            case 2:
                return secondRod;
            case 3:
                return thirdRod;
        }
        return null;
    }

    public void initialize(){

        float xStart = (width - (Rod.DEFAULT_WIDTH * 3)) / 6;
        this.yInitStage = height - 200;
        float yInitRod = yInitStage - Rod.DEFAULT_HEIGHT;

        firstRod = new Rod(1);
        firstRod.setX(xStart);
        firstRod.setY(yInitRod);
        firstRod.setWidth(Rod.DEFAULT_WIDTH);
        firstRod.setHeight(Rod.DEFAULT_HEIGHT);

        secondRod = new Rod(2);
        secondRod.setX(Rod.DEFAULT_WIDTH + (xStart * 3));
        secondRod.setY(yInitRod);
        secondRod.setWidth(Rod.DEFAULT_WIDTH);
        secondRod.setHeight(Rod.DEFAULT_HEIGHT);

        thirdRod = new Rod(3);
        thirdRod.setX((Rod.DEFAULT_WIDTH * 2) + (xStart * 5));
        thirdRod.setY(yInitRod);
        thirdRod.setWidth(Rod.DEFAULT_WIDTH);
        thirdRod.setHeight(Rod.DEFAULT_HEIGHT);

        //Log.i("Log", "Disk " + i);
        //Log.i("Log", "x: " + diskX + " / y: " + diskY);
        //Log.i("Log", "width: " + diskWidth + " / height: " + diskHeight);

        // Inicializar os discos
        //final float diskSpace = 20;
        //final float diskWidthInc = 30;
        //final float diskHeight = 60;

        float diskWidth = (width - (Disk.SPACE * 4)) / 3;

        Log.i("Log", "Largura do disk maior: " + diskWidth);
        float diskY = this.yInitStage - Disk.SPACE - Disk.DEFAULT_HEIGHT;
        //float diskY = firstRod.getNextDiskY();
        float diskX = xStart + (Rod.DEFAULT_WIDTH / 2) - (diskWidth / 2);

        for (int i = amountOfDisks; i > 0; i--){

            Disk disk = new Disk(i);
            disk.setWidth(diskWidth);
            disk.setHeight(Disk.DEFAULT_HEIGHT);
            disk.setX(diskX);
            disk.setY(diskY);

            firstRod.push(disk);

            diskWidth -= (Disk.WIDTH_INCREMENT * 2);

            diskX += Disk.WIDTH_INCREMENT;
            diskY -= Disk.DEFAULT_HEIGHT + Disk.SPACE;
            //diskY = firstRod.getNextDiskY();
            //diskX = firstRod.getNextDiskX();

            Log.i("Log", "disk " + disk.getDiskNumber() + " / Width: " + disk.getWidth());
        }
    }

    @Override
    public void draw(Canvas canvas) {

        // Desenhar o fundo
        Paint paintBack = new Paint();
        paintBack.setColor(BACK_COLOR);

        canvas.drawRect(x, y, x + width, y + height, paintBack);

        // Desenhar a base
        Paint paintStage = new Paint();
        paintStage.setColor(STAGE_COLOR);

        canvas.drawRect(0, yInitStage, width, height, paintStage);

        firstRod.draw(canvas);
        secondRod.draw(canvas);
        thirdRod.draw(canvas);
    }

    public MovementState selectDestinationRod(Rod destinationRod) {

        if (selectedRod == null) {
            selectedRod = destinationRod;
            selectedRod.setIsSelected(true);
            return MovementState.Continue;
        }
        selectedRod.setIsSelected(false);

        this.disk = null;
        this.destinationRod = null;

        // Continua caso haja uma torre selecionada
        if (selectedRod.equals(destinationRod)){
            this.selectedRod = null;
            return MovementState.Continue;
        }

        if (selectedRod.getDiskCount() == 0){
            this.selectedRod = null;
            return MovementState.NotAllowed;
        }

        Disk disk = selectedRod.pop();

        if (destinationRod.isDiskAccepted(disk)){
            destinationRod.push(disk);
            this.selectedRod = null;
            this.destinationRod = destinationRod;
            this.disk = disk;
            return MovementState.Ok;
        } else {
            selectedRod.push(disk);
            this.selectedRod = null;
            return MovementState.NotAllowed;
        }
    }

    public void moveDisk(Disk disk, Rod destinationRod){
        float newX = destinationRod.getXCenter() - (disk.width / 2);
        float newY = destinationRod.getNextDiskY();

        Log.i("Log", "Disk X:" + newX + ", Y " + newY);

        disk.setX(newX);
        disk.setY(newY);
    }

    public Rod getDestinationRod() {
        return destinationRod;
    }

    public Disk getDisk() {
        return disk;
    }

    public enum MovementState {
        Nothing,
        NotAllowed,
        Ok,
        Continue
    }
}
