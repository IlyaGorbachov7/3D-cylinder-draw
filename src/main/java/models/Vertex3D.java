package models;

import java.util.Objects;

public class Vertex3D implements Cloneable{
    private double x;
    private double y;
    private double z;

    public Vertex3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return String.format("Vertex3D{x= %.2f, y= %.2f, z= %.2f}", x, y, z);
    }

    @Override
    public Vertex3D clone() throws CloneNotSupportedException {
        return (Vertex3D) super.clone();
    }
}