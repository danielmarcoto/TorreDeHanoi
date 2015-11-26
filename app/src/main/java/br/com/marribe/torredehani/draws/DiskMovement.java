package br.com.marribe.torredehani.draws;

/**
 * Created by danielmarcoto on 25/11/15.
 */
public class DiskMovement {
    private Disk disk;
    private Rod current;
    private Rod destination;

    public Disk getDisk() {
        return disk;
    }

    public void setDisk(Disk disk) {
        this.disk = disk;
    }

    public Rod getCurrent() {
        return current;
    }

    public void setCurrent(Rod current) {
        this.current = current;
    }

    public Rod getDestination() {
        return destination;
    }

    public void setDestination(Rod destination) {
        this.destination = destination;
    }
}
