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
    private int amountOfDisks;

    public void setDestinationRod(Rod destinationRod) {
        this.destinationRod = destinationRod;
    }

    public void setDisk(Disk disk) {
        this.disk = disk;
    }

    private Rod destinationRod;
    private Disk disk;

    private float yInitStage;

    public TowerOfHanoi() throws InvalidStateException {
        this(3);
    }

    public TowerOfHanoi(int amountOfDisks) throws InvalidStateException{
        this.amountOfDisks = amountOfDisks;
        this.selectedRod = null;

        if (amountOfDisks < 3 || amountOfDisks > 6){
            throw new InvalidStateException("O total de discos deve ser entre 3 e 6");
        }
    }

    public Rod getSelectedRod() {
        return selectedRod;
    }

    public void setSelectedRod(Rod selectedRod) {
        this.selectedRod = selectedRod;
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

    public void initialize(){

        //final float rodWidth = 40;
        //final float rodHeight = 700;

        float xStart = (width - (Rod.DEFAULT_WIDTH * 3)) / 4;
        this.yInitStage = height - 200;
        float yInitRod = yInitStage - Rod.DEFAULT_HEIGHT;

        firstRod = new Rod();
        firstRod.setX(xStart);
        firstRod.setY(yInitRod);
        firstRod.setWidth(Rod.DEFAULT_WIDTH);
        firstRod.setHeight(Rod.DEFAULT_HEIGHT);

        secondRod = new Rod();
        secondRod.setX(Rod.DEFAULT_WIDTH + (xStart * 2));
        secondRod.setY(yInitRod);
        secondRod.setWidth(Rod.DEFAULT_WIDTH);
        secondRod.setHeight(Rod.DEFAULT_HEIGHT);

        thirdRod = new Rod();
        thirdRod.setX((Rod.DEFAULT_WIDTH * 2) + (xStart * 3));
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
        float diskWidth = Disk.WIDTH_TO_GREATER;
        //float diskY = this.yInitStage - Disk.SPACE - Disk.DEFAULT_HEIGHT;
        float diskY = firstRod.getNextDiskY();
        float diskX = xStart + (Rod.DEFAULT_WIDTH / 2) - (diskWidth / 2);

        for (int i = amountOfDisks; i > 0; i--){

            Disk disk = new Disk(i + 1);
            disk.setWidth(diskWidth);
            disk.setHeight(Disk.DEFAULT_HEIGHT);
            disk.setX(diskX);
            disk.setY(diskY);

            firstRod.push(disk);

            diskWidth -= (Disk.WIDTH_INCREMENT * 2);
            //diskY -= Disk.DEFAULT_HEIGHT + Disk.SPACE;
            diskX += Disk.WIDTH_INCREMENT;
            diskY = firstRod.getNextDiskY();
            //diskX = firstRod.getNextDiskX();

            Log.i("Log", "disk " + disk.getDiskNumber());
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

    public boolean isMovimentAllowed(Rod rod, Disk disk){


        return false;
    }

    public MovementState selectDisk(Rod destinationRod) {

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
            selectedRod = null;
            return MovementState.Continue;
        }

        if (selectedRod.getDiskCount() == 0){
            return MovementState.NotAllowed;
        }

        Disk disk = selectedRod.pop();

        if (destinationRod.isDiskAccepted(disk)){
            destinationRod.push(disk);
            this.destinationRod = destinationRod;
            this.disk = disk;
            return MovementState.Ok;
        } else {
            selectedRod.push(disk);
            return MovementState.NotAllowed;
        }
    }

    public Rod getDestinationRod() {
        return destinationRod;
    }

    public Disk getDisk() {
        return disk;
    }

    public enum MovementState {
        NotAllowed,
        Ok,
        Continue,
        GameLost,
        GameWin
    }
}
