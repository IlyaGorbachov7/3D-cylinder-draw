package toolkit;

import models.Vertex3D;

import java.util.Arrays;
import java.util.Objects;

/**
 * Класс Матрица.
 * Содержит все необходимые методы, а
 * матрицы геометрических преобразований, матриц всех проекций
 */
public class MatRix {
    private double[][] matrix;
    int row;
    int column;

    public MatRix(double[][] matrix) {
        this.row = matrix.length;
        this.column = matrix[0].length;

        this.matrix = new double[this.row][];

        for (int i = 0; i < this.row; ++i) {
            this.matrix[i] = Arrays.copyOf(matrix[i], this.column);
        }


    }

    public MatRix(MatRix srsMatrix) {
        this.row = srsMatrix.row;
        this.column = srsMatrix.column;

        this.matrix = new double[this.row][];
        for (int i = 0; i < this.row; ++i) {
            this.matrix[i] = Arrays.copyOf(srsMatrix.matrix[i], this.column);
        }

    }

    public double get(int rowPos, int columnPos) {
        if (rowPos > this.row || rowPos < 0 || columnPos > this.column || columnPos < 0) {
            System.out.println("Out Side");
        }

        return this.matrix[rowPos][columnPos];
    }

    public void set(int rowPos, int columnPos, double value) {
        if (rowPos > this.row || rowPos < 0 || columnPos > this.column || columnPos < 0) {
            System.out.println("OutSide Matrix !!! ERROR (getEntry)");
        }
        matrix[rowPos][columnPos] = value;
    }

    /**
     * Метод по перемножению матриц
     */
    public MatRix multiply(MatRix mutRix) {
        if (this.column != mutRix.row) {
            return null;
        }
        double[][] newMatrixArray = new double[this.row][mutRix.column];

        for (int i = 0; i < this.row; ++i) {
            for (int j = 0; j < mutRix.column; ++j) {
                for (int k = 0; k < this.column; ++k) {
                    newMatrixArray[i][j] += this.matrix[i][k] * mutRix.matrix[k][j];
                }
            }
        }

        return new MatRix(newMatrixArray);

    }

    @Override
    public String toString() {
        StringBuilder outString = new StringBuilder(100);
        outString.append("Matrix[").append(this.row).append("][").append(this.column).append("]");
        for (double[] array : this.matrix) {
            outString.append(Arrays.toString(array));
        }
        return outString.toString();
    }

    public static MatRix getMatrixMOVE(Vertex3D Dv) {
        Objects.requireNonNull(Dv);
        return new MatRix(new double[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {Dv.getX(), Dv.getY(), Dv.getZ(), 1}
        });
    }

    public static MatRix getMatrixSCALE(Vertex3D Sv) {
        Objects.requireNonNull(Sv);
        return new MatRix(new double[][]{
                {Sv.getX(), 0, 0, 0},
                {0, Sv.getY(), 0, 0},
                {0, 0, Sv.getZ(), 0},
                {0, 0, 0, 1}
        });
    }

    public static MatRix getMatrixROTATE(CoordinateAxis axis, double angle) {
        angle = Math.toRadians(angle);
        double sinAngle = Math.sin(angle);
        double cosAngle = Math.cos(angle);
        MatRix returnMatrixRotate = null;
        if (axis == CoordinateAxis.Oz) {
            //Вращение по оси Oz
            returnMatrixRotate = new MatRix(new double[][]{
                    {cosAngle, sinAngle, 0, 0},
                    {-sinAngle, cosAngle, 0, 0},
                    {0, 0, 1, 0},
                    {0, 0, 0, 1}
            });
        } else if (axis == CoordinateAxis.Oy) {
            // Вращение по оси Oy
            returnMatrixRotate = new MatRix(new double[][]{
                    {cosAngle, 0, -sinAngle, 0},
                    {0, 1, 0, 0},
                    {sinAngle, 0, cosAngle, 0},
                    {0, 0, 0, 1}
            });
        } else if (axis == CoordinateAxis.Ox) {
            // Вращение по оси Ox
            returnMatrixRotate = new MatRix(new double[][]{
                    {1, 0, 0, 0},
                    {0, cosAngle, sinAngle, 0},
                    {0, -sinAngle, cosAngle, 0},
                    {0, 0, 0, 1}
            });
        }
        return returnMatrixRotate;
    }

    public static MatRix getMatrixFrontalProjection() {
        return new MatRix(new double[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 1}
        });
    }

    public static MatRix getMatrixProfileProjection() {
        return new MatRix(new double[][]{
                {0, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
    }

    public static MatRix getMatrixHorizontalProjection() {
        return new MatRix(new double[][]{
                {1, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
    }

    public static MatRix getMatrixAxonometricProjection(double psiAngle, double fiAngle) {
        psiAngle = Math.toRadians(psiAngle);
        fiAngle = Math.toRadians(fiAngle);
        double sinPsiAngle = Math.sin(psiAngle),
                cosPsiAngle = Math.cos(psiAngle),
                sinFiAngle = Math.sin(fiAngle),
                cosFiAngle = Math.cos(fiAngle);
        return new MatRix(new double[][]{
                {cosPsiAngle, sinFiAngle * sinPsiAngle, 0, 0},
                {0, cosFiAngle, 0, 0},
                {sinPsiAngle, -sinFiAngle * cosPsiAngle, 0, 0},
                {0, 0, 0, 1}
        });
    }

    public static MatRix getMatrixObliqueProjection(double alfaAngle, double l) {
        alfaAngle = Math.toRadians(alfaAngle);
        double cosAlfaAngle = Math.cos(alfaAngle),
                sinAlfaAngle = Math.sin(alfaAngle);
        return new MatRix(new double[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {l * cosAlfaAngle, l * sinAlfaAngle, 0, 0},
                {0, 0, 0, 1}
        });
    }

    public static MatRix getMatrixPerspectiveProjection(double d) {
        return new MatRix(new double[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 1 / d},
                {0, 0, 0, 0}
        });
    }

    public static MatRix getMatrixViewTransformation(double p, double teteAngle, double fiAngle) {
        teteAngle = Math.toRadians(teteAngle);
        fiAngle = Math.toRadians(fiAngle);
        double sinTeteAngle = Math.sin(teteAngle),
                sinFiAngle = Math.sin(fiAngle),
                cosTeteAngle = Math.cos(teteAngle),
                cosFiAngle = Math.cos(fiAngle);
        return new MatRix(new double[][]{
                {-sinTeteAngle, -cosFiAngle * cosTeteAngle, -sinFiAngle * cosTeteAngle, 0},
                {cosTeteAngle, -cosFiAngle * sinTeteAngle, -sinFiAngle * sinTeteAngle, 0},
                {0, sinFiAngle, -cosFiAngle, 0},
                {0, 0, p, 1}
        });
    }

    /**
     * Ось координат,
     * <p>
     * Ox - вокруг оси Ox
     * <p>
     * Oy - вокруг оси Oy
     * <p>
     * Oz - вокруг оси Oz
     */
    public enum CoordinateAxis {
        Ox, Oy, Oz
    }
}
