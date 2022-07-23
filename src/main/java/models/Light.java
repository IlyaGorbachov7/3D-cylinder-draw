package models;

import models.Vertex3D;

/**
 * Класс "Свет"
 */
public class Light {
    private final double il;
    private final double kd;
    private final Vertex3D povPoint;

    public Light(double x, double y, double z, double  il, double kd) {
        this.il = il;
        this.kd = kd;
        povPoint = new Vertex3D(x,y,z);
    }

    public double getIl() {
        return il;
    }

    public double getKd() {
        return kd;
    }

    public Vertex3D getPovPoint() {
        return povPoint;
    }

    @Override
    public String toString() {
        return "Intensity = " + il +
                ", kd = " + kd +
                ", position is " + povPoint +
                '}';
    }
}
