package com.example.dimgion.parkinson;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "demographics")
public class Demographics {

    @PrimaryKey(autoGenerate = true)
    private int key;

    @ColumnInfo(name = "time")
    private double time;

    @ColumnInfo(name = "leftFoot")
    private double leftFoot;

    @ColumnInfo(name = "rightFoot")
    private double rightFoot;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getLeftFoot() {
        return leftFoot;
    }

    public void setLeftFoot(double leftFoot) {
        this.leftFoot = leftFoot;
    }

    public double getRightFoot() {
        return rightFoot;
    }

    public void setRightFoot(double rightFoot) {
        this.rightFoot = rightFoot;
    }
}
