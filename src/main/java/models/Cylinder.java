package models;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Класс представляет Цилиндр с вырезом.
 *
 * @author SecuRiTy
 * @version 1.2 20.09.2021
 */
public class Cylinder implements Cloneable {
    private int N;
    private double R1;
    private double R2;
    private double H;

    private double dAngle;
    private double dRad;
    private boolean isApproximation;

    private ArrayList<ArrayList<Vertex3D>> vertexes;

    private ArrayList<ArrayList<Surface>> surfaces;

    public Cylinder(int n, double r1, double r2, double H) {
        this.N = n;
        R1 = r1;
        R2 = r2;
        this.H = H;
        dAngle = 360 / (double) N;
        dRad = Math.toRadians(dAngle);

        vertexes = approximationVertex();

        surfaces = initSurfacesCylinderClockWise();

    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public double getR1() {
        return R1;
    }

    public void setR1(double r1) {
        R1 = r1;
    }

    public double getR2() {
        return R2;
    }

    public void setR2(double r2) {
        R2 = r2;
    }

    public double getH() {
        return H;
    }

    public void setH(double height) {
        this.H = height;
    }

    public ArrayList<ArrayList<Vertex3D>> getVertexes() {
        return vertexes;
    }

    public ArrayList<ArrayList<Surface>> getSurfaces() {
        return surfaces;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Cylinder other) {
            return R1 == other.R1 &&
                    R2 == other.R2 &&
                    H == other.H;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(N, R1, R2, H);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()
                + "{\nlevelApprox = " + N
                + ",\n R1 = " + R1
                + "\n R2 = " + R2
                + ",\n height = " + H
                + ",\n Array Vertex:\n" +vertexes
                + ",\n Array Facet:\n" + surfaces
                + "}";
    }

    @Override
    public Cylinder clone() throws CloneNotSupportedException {
        Cylinder copyCylinder = (Cylinder) super.clone();
        copyCylinder.vertexes = new ArrayList<>();
        int row = 0;
        for (var arrVertex : this.vertexes) {
            copyCylinder.vertexes.add((ArrayList<Vertex3D>) arrVertex.clone());
            int column = 0;
            for (var vertex : arrVertex) {
                copyCylinder.vertexes.get(row).set(column, vertex.clone());
                ++column;
            }
            ++row;
        }
        // Обязательно нужно инициализировать ребра и грани именно таким способам !!!.
        copyCylinder.surfaces = copyCylinder.initSurfacesCylinderClockWise();
        return copyCylinder;
    }

    /**
     * Метод аппроксимирует точки.
     * Аппроксимация видеться по
     * <p>
     * n - степень аппроксимации,
     * <p>
     * R1,R2 - радиусам окружностей
     * <p>
     * H - высота цилиндра,
     */
    private ArrayList<ArrayList<Vertex3D>> approximationVertex() {
        if (this.isApproximation) return vertexes;

        final int numberCircles = 4;
        final int numberVertexOneCircle = N;
        double radIncrease = 0;

        // Здесь же производим инициализацию ArrayList
        ArrayList<ArrayList<Vertex3D>> vertexList = new ArrayList<>(numberCircles);
        for (int index = 0; index < numberCircles; ++index) {
            vertexList.add(new ArrayList<>(numberVertexOneCircle));
        }

        for (int column = 0; column < numberVertexOneCircle; ++column) {
            // Аппроксимируем нижний круг цилиндра R1
            vertexList.get(0).add(new Vertex3D(R1 * Math.cos(radIncrease), H / 2, R1 * Math.sin(radIncrease)));
            // Аппроксимируем верхний круг цилиндра R1
            vertexList.get(1).add(new Vertex3D(R1 * Math.cos(radIncrease), -H / 2, R1 * Math.sin(radIncrease)));
            // Аппроксимируем нижний круг цилиндра R2
            vertexList.get(2).add(new Vertex3D(R2 * Math.cos(radIncrease), H / 2, R2 * Math.sin(radIncrease)));
            // Аппроксимируем верхнего круг цилиндра R2
            vertexList.get(3).add(new Vertex3D(R2 * Math.cos(radIncrease), -H / 2, R2 * Math.sin(radIncrease)));
            radIncrease -= dRad;
        }
        this.isApproximation = true;
        return vertexList;
    }

    /**
     * Метод инициализирует поверхности по уже созданным вершинам.
     */
    private ArrayList<ArrayList<Surface>> initSurfacesCylinderClockWise() {
        if (!isApproximation) {
            vertexes = approximationVertex();
        }
        // 2-е плоскости (нижний и верхний круг) + 2-е боковые плоскости (внутренняя R1 и внешняя R2) = 4
        final int numberSurface = 4;
        // количество граней в одном отдельно взятой плоскости цилиндра
        final int numberFacesOnOneSurface = N;

        // Производим инициализацию ArrayList
        ArrayList<ArrayList<Surface>> surfacesList = new ArrayList<>(4);
        for (int i = 0; i < numberSurface; ++i) {
            surfacesList.add(new ArrayList<>(numberFacesOnOneSurface));
        }

        for (int i = 0; i < numberFacesOnOneSurface; ++i) {
            // Боковая поверхность цилиндра с радиусом R1
            surfacesList.get(0).add(new Surface(Arrays.asList(
                    vertexes.get(0).get(i),
                    vertexes.get(0).get((i != numberFacesOnOneSurface - 1) ? i + 1 : 0),
                    vertexes.get(1).get((i != numberFacesOnOneSurface - 1) ? i + 1 : 0),
                    vertexes.get(1).get(i)), Color.CORAL));
            // Боковая поверхность цилиндра с радиусом R2
            surfacesList.get(1).add(new Surface(Arrays.asList(
                    vertexes.get(2).get((i != numberFacesOnOneSurface - 1) ? i + 1 : 0),
                    vertexes.get(2).get(i),
                    vertexes.get(3).get(i),
                    vertexes.get(3).get((i != numberFacesOnOneSurface - 1) ? i + 1 : 0)
            ), Color.CORAL));
            // Нижняя поверхность цилиндра
            surfacesList.get(2).add(new Surface(Arrays.asList(
                    vertexes.get(0).get((i != numberFacesOnOneSurface - 1) ? i + 1 : 0),
                    vertexes.get(0).get(i),
                    vertexes.get(2).get(i),
                    vertexes.get(2).get((i != numberFacesOnOneSurface - 1) ? i + 1 : 0)
            ), Color.CORAL));
            // Верхняя поверхность цилиндра
            surfacesList.get(3).add(new Surface(Arrays.asList(
                    vertexes.get(1).get(i),
                    vertexes.get(1).get((i != numberFacesOnOneSurface - 1) ? i + 1 : 0),
                    vertexes.get(3).get((i != numberFacesOnOneSurface - 1) ? i + 1 : 0),
                    vertexes.get(3).get(i)), Color.CORAL));
        }
        return surfacesList;
    }
}