package rendering;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import models.Cylinder;

import models.Vertex3D;
import toolkit.MatRix;
import models.Point2D;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Содержит КЛАССЫ:
 * <p>
 * {@link SystemCoordinate} - Оси координат
 * <p>
 * {@link GeometricTransformations} - геометрические преобразования,
 * <p>
 * {@link Projections} - Виды проекций
 *
 * @author SecuRiTy
 * @version 1.2 22.09.2021
 */
interface DrawWays {

    /**
     * Класс отрисовки системы координат
     */
    class SystemCoordinate {
        /**
         * Метод определен ТОЛЬКО ВНУТРИ своего покета ! Это ОЧЕНЬ ВАЖНО !!!
         * <p>
         * Метод для рисования 2D системы координат
         * Ox - означает что обрисовываем плоскость Ozy
         * Oy - означает что обрисовываем плоскость Oxz
         * Oz - означает что обрисовываем плоскость Oxy
         *
         * @param axis      выбор Оси: Ox, Oy, Oz
         * @param zeroPoint точка начала координат на {@link Canvas}
         */
        static void draw2D(AxisCoordinate axis, Point2D zeroPoint, GraphicsContext graphContext) {
            Canvas canvas = graphContext.getCanvas();
            // цвет осей-линий
            graphContext.setStroke(Color.RED);
            // толщина линий
            graphContext.setLineWidth(1.5);
            // цвет текста: X, Y, Z
            graphContext.setFill(Color.DARKCYAN);
            // размер шрифта
            graphContext.setFont(Font.font(20));
            // Рисуем оси
            // цвет оси Ox Oz
            graphContext.setStroke(Color.BLUE);
            graphContext.strokeLine(zeroPoint.getX(), zeroPoint.getY(),
                    canvas.getWidth(), zeroPoint.getY());
            // цвет оси Oz
            graphContext.setStroke(Color.RED);
            graphContext.strokeLine(zeroPoint.getX(), zeroPoint.getY(),
                    zeroPoint.getX(), -canvas.getHeight()); // -canvas.getHeight() - направляем вверх
            if (axis == AxisCoordinate.Ox) {
                graphContext.fillText("X", zeroPoint.getX(), zeroPoint.getY());
                graphContext.fillText("Z", canvas.getWidth() - 50, zeroPoint.getY());
                graphContext.fillText("Y", zeroPoint.getX(), 50); // расположим вверху
            } else if (axis == AxisCoordinate.Oy) {
                graphContext.fillText("Y", zeroPoint.getX(), zeroPoint.getY());
                graphContext.fillText("X", canvas.getWidth() - 50, zeroPoint.getY());
                graphContext.fillText("Z", zeroPoint.getX(), 50); // расположим вверху
            } else if (axis == AxisCoordinate.Oz) {
                graphContext.fillText("Z", zeroPoint.getX(), zeroPoint.getY());
                graphContext.fillText("X", canvas.getWidth() - 50, zeroPoint.getY());
                graphContext.fillText("Y", zeroPoint.getX(), 50); // расположим вверху
            }
        }

        /**
         * Метод для рисования 3D системы координат
         * Применим для аксонометрической проекции
         */
        static void draw3DForAxonometricProjection(Point2D zeroPoint, GraphicsContext graphContext,
                                                   double psiAngle, double fiAngle) {
            // Здесь я создаю точки системы координат, Я как бы их аппроксимирую
            Vertex3D[] arrayVertexesAxis = new Vertex3D[]{
                    /*index = 0 Вершина начало */ new Vertex3D(0, 0, 0),
                    /*index = 1 Вершина оси X  */ new Vertex3D(400, 0, 0),
                    /*index = 2 Вершина оси Y  */ new Vertex3D(0, -300, 0),// Важно! -300, чтобы ось была положительна
                    /*index = 3 Вершина оси Z  */ new Vertex3D(0, 0, 400)
            };
            // Беру матрицу преобразований, чтобы к этим точкам применить такие же преобразования
            MatRix matrixAxonometricProjection = MatRix.getMatrixAxonometricProjection(psiAngle, fiAngle);
            for (var vertex : arrayVertexesAxis) {
                // Перевожу вершины в матрицы
                MatRix matrixPoint = new MatRix(new double[][]{
                        {vertex.getX(), vertex.getY(), vertex.getZ(), 1}
                });
                matrixPoint = matrixPoint.multiply(matrixAxonometricProjection);
                vertex.setX(matrixPoint.get(0, 0));
                vertex.setY(matrixPoint.get(0, 1));
                vertex.setZ(matrixPoint.get(0, 2));
            }
            // цвет осей-линий
            graphContext.setStroke(Color.RED);
            // толщина линий
            graphContext.setLineWidth(1.5);
            // цвет текста: X, Y, Z
            graphContext.setFill(Color.DARKCYAN);
            // размер шрифта
            graphContext.setFont(Font.font(20));
            // Рисуем оси
            // цвет оси Ox Oz
            graphContext.setStroke(Color.BLUE);
            // Точка оси Ox + Точка начала координат
            graphContext.strokeLine(arrayVertexesAxis[1].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[1].getY() + zeroPoint.getY(),
                    arrayVertexesAxis[0].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[0].getY() + zeroPoint.getY());
            // Точка оси Oz + Точка начала координат
            graphContext.strokeLine(arrayVertexesAxis[3].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[3].getY() + zeroPoint.getY(),
                    arrayVertexesAxis[0].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[0].getY() + zeroPoint.getY());
            // цвет оси Oy
            graphContext.setStroke(Color.RED);
            // Точка оси Oy + Точка начала координат
            graphContext.strokeLine(arrayVertexesAxis[2].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[2].getY() + zeroPoint.getY(),
                    arrayVertexesAxis[0].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[0].getY() + zeroPoint.getY());
            // рисуем названия
            graphContext.fillText("X", arrayVertexesAxis[1].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[1].getY() + zeroPoint.getY());
            graphContext.fillText("Z", arrayVertexesAxis[3].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[3].getY() + zeroPoint.getY());
            graphContext.fillText("Y", arrayVertexesAxis[2].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[2].getY() + zeroPoint.getY());
        }

        /**
         * Метод для рисования 3D системы координат. Применим для косоугольной проекции
         */
        static void draw3DForObliqueProjection(Point2D zeroPoint, GraphicsContext graphContext,
                                               double alfaAngle, double l) {
            // Здесь я создаю точки системы координат, Я как бы их аппроксимирую
            Vertex3D[] arrayVertexesAxis = new Vertex3D[]{
                    /*index = 0 Вершина начало */ new Vertex3D(0, 0, 0),
                    /*index = 1 Вершина оси X  */ new Vertex3D(400, 0, 0),
                    /*index = 2 Вершина оси Y  */ new Vertex3D(0, -300, 0),// Важно! -30, чтобы ось была положительна
                    /*index = 3 Вершина оси Z  */ new Vertex3D(0, 0, 400)
            };
            MatRix matrixObliqueProjection = MatRix.getMatrixObliqueProjection(alfaAngle, l);
            for (var vertex : arrayVertexesAxis) {
                // Перевожу вершины в матрицы
                MatRix matrixPoint = new MatRix(new double[][]{
                        {vertex.getX(), vertex.getY(), vertex.getZ(), 1}
                });
                matrixPoint = matrixPoint.multiply(matrixObliqueProjection);
                vertex.setX(matrixPoint.get(0, 0));
                vertex.setY(matrixPoint.get(0, 1));
                vertex.setZ(matrixPoint.get(0, 2));
            }
            // цвет осей-линий
            graphContext.setStroke(Color.RED);
            // толщина линий
            graphContext.setLineWidth(1.5);
            // цвет текста: X, Y, Z
            graphContext.setFill(Color.DARKCYAN);
            // размер шрифта
            graphContext.setFont(Font.font(20));
            // Рисуем оси
            // цвет оси Ox Oz
            graphContext.setStroke(Color.BLUE);
            // Точка оси Ox + Точка начала координат
            graphContext.strokeLine(arrayVertexesAxis[1].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[1].getY() + zeroPoint.getY(),
                    arrayVertexesAxis[0].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[0].getY() + zeroPoint.getY());
            // Точка оси Oz + Точка начала координат
            graphContext.strokeLine(arrayVertexesAxis[3].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[3].getY() + zeroPoint.getY(),
                    arrayVertexesAxis[0].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[0].getY() + zeroPoint.getY());
            // цвет оси Oz
            graphContext.setStroke(Color.RED);
            // Точка оси Oy + Точка начала координат
            graphContext.strokeLine(arrayVertexesAxis[2].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[2].getY() + zeroPoint.getY(),
                    arrayVertexesAxis[0].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[0].getY() + zeroPoint.getY());
            // рисуем названия
            graphContext.fillText("X", arrayVertexesAxis[1].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[1].getY() + zeroPoint.getY());
            graphContext.fillText("Z", arrayVertexesAxis[3].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[3].getY() + zeroPoint.getY());
            graphContext.fillText("Y", arrayVertexesAxis[2].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[2].getY() + zeroPoint.getY());
        }

        /**
         * Метод для рисования 3D системы координат. Применяем для перспективной проекции
         */
        static void draw3DForPerspectiveProjection(Point2D zeroPoint, GraphicsContext graphContext,
                                                   double d, double p, double teteAngle, double fiAngle) {
            // Здесь я создаю точки системы координат, Я как бы их аппроксимирую
            Vertex3D[] arrayVertexesAxis = new Vertex3D[]{
                    /*index = 0 Вершина начало */ new Vertex3D(0, 0, 0),
                    /*index = 1 Вершина оси X  */ new Vertex3D(400, 0, 0),
                    /*index = 2 Вершина оси Y  */ new Vertex3D(0, -300, 0),// Важно! -30, чтобы ось была положительна
                    /*index = 3 Вершина оси Z  */ new Vertex3D(0, 0, 400)
            };
            // Беру матрицу преобразований, чтобы к этим точкам применить такие же преобразования
            MatRix matrixViewTransformation = MatRix.getMatrixViewTransformation(p, teteAngle, fiAngle);
            MatRix matrixPerspectiveProjection = MatRix.getMatrixPerspectiveProjection(d);
            // Перевожу вершины в матрицы
            for (var vertex : arrayVertexesAxis) {
                MatRix matrixPoint = new MatRix(new double[][]{
                        {vertex.getX(), vertex.getY(), vertex.getZ(), 1}
                });
                matrixPoint = matrixPoint.multiply(matrixViewTransformation) // видовое преобразование (точки зрения)
                        .multiply(matrixPerspectiveProjection); // И сразу перспективное проекция
                if (matrixPoint.get(0, 3) == 0) {
                    matrixPoint.set(0, 3, 0.1);
                }
                vertex.setX(matrixPoint.get(0, 0) / matrixPoint.get(0, 3));
                vertex.setY(matrixPoint.get(0, 1) / matrixPoint.get(0, 3));
                vertex.setZ(matrixPoint.get(0, 2) / matrixPoint.get(0, 3));

            }
            // толщина линий
            graphContext.setLineWidth(1.5);
            // цвет текста: X, Y, Z
            graphContext.setFill(Color.DARKCYAN);
            // размер шрифта
            graphContext.setFont(Font.font(20));
            // Рисуем оси
            // цвет оси Ox Oz
            graphContext.setStroke(Color.BLUE);
            // Точка оси Ox + Точка начала координат
            graphContext.strokeLine(arrayVertexesAxis[1].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[1].getY() + zeroPoint.getY(),
                    arrayVertexesAxis[0].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[0].getY() + zeroPoint.getY());
            // Точка оси Oz + Точка начала координат
            graphContext.strokeLine(arrayVertexesAxis[2].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[2].getY() + zeroPoint.getY(),
                    arrayVertexesAxis[0].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[0].getY() + zeroPoint.getY());
            // цвет оси Oz
            graphContext.setStroke(Color.RED);
            // Точка оси Oy + Точка начала координат
            graphContext.strokeLine(arrayVertexesAxis[3].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[3].getY() + zeroPoint.getY(),
                    arrayVertexesAxis[0].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[0].getY() + zeroPoint.getY());
            // рисуем названия
            graphContext.fillText("X", arrayVertexesAxis[1].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[1].getY() + zeroPoint.getY());
            graphContext.fillText("Z", arrayVertexesAxis[3].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[3].getY() + zeroPoint.getY());
            graphContext.fillText("Y", arrayVertexesAxis[2].getX() + zeroPoint.getX(),
                    arrayVertexesAxis[2].getY() + zeroPoint.getY());
        }

        /**
         * `
         * Использую для выбора отрисовки системы координат.
         * Так проще !
         * Ox - означает что обрисовываем плоскость Ozy
         * Oy - означает что обрисовываем плоскость Oxz
         * Oz - означает что обрисовываем плоскость Oxy
         */
        public enum AxisCoordinate {
            Ox, Oy, Oz
        }
    }

    /**
     * Класс геометрических преобразований
     */
    class GeometricTransformations {

        static void moveCylinder(Cylinder cylinder, Vertex3D Dv) {
            Objects.requireNonNull(cylinder);
            Objects.requireNonNull(Dv);

            MatRix moveMatrix = MatRix.getMatrixMOVE(Dv);
            ArrayList<ArrayList<Vertex3D>> listVertexCylinder = cylinder.getVertexes();

            for (var arrVertex : listVertexCylinder) {
                for (var vertex : arrVertex) {
                    MatRix matrixPoint = new MatRix(new double[][]{
                            {vertex.getX(), vertex.getY(), vertex.getZ(), 1}
                    });

                    matrixPoint = matrixPoint.multiply(moveMatrix);

                    vertex.setX(matrixPoint.get(0, 0));
                    vertex.setY(matrixPoint.get(0, 1));
                    vertex.setZ(matrixPoint.get(0, 2));
                }
            }
        }

        static void scaleCylinder(Cylinder cylinder, Vertex3D Sv) {
            Objects.requireNonNull(cylinder);
            Objects.requireNonNull(Sv);

            MatRix moveMatrix = MatRix.getMatrixSCALE(Sv);
            ArrayList<ArrayList<Vertex3D>> listVertexCylinder = cylinder.getVertexes();

            for (var arrVertex : listVertexCylinder) {
                for (var vertex : arrVertex) {
                    MatRix matrixVertex = new MatRix(new double[][]{
                            {vertex.getX(), vertex.getY(), vertex.getZ(), 1}
                    });

                    matrixVertex = matrixVertex.multiply(moveMatrix);

                    vertex.setX(matrixVertex.get(0, 0));
                    vertex.setY(matrixVertex.get(0, 1));
                    vertex.setZ(matrixVertex.get(0, 2));
                }
            }
        }

        static void rotateCylinder(Cylinder cylinder, Vertex3D Rv) {
            Objects.requireNonNull(cylinder);
            Objects.requireNonNull(Rv);
            if (Rv.getX() != 0) {
                // Вращение по оси Ox
                rotateByAxis(SystemCoordinate.AxisCoordinate.Ox, cylinder, Rv.getX());
            }
            if (Rv.getY() != 0) {
                // Вращение по оси Oy
                rotateByAxis(SystemCoordinate.AxisCoordinate.Oy, cylinder, Rv.getY());
            }
            if (Rv.getZ() != 0) {
                // Вращение по оси Oz
                rotateByAxis(SystemCoordinate.AxisCoordinate.Oz, cylinder, Rv.getZ());
            }
        }

        private static void rotateByAxis(SystemCoordinate.AxisCoordinate axis, Cylinder cylinder,
                                         double angle) {
            Objects.requireNonNull(cylinder);
            MatRix matrixRotateByAxis = null;
            // Выбираем по какой оси будет поворот.
            if (axis == SystemCoordinate.AxisCoordinate.Ox) {
                matrixRotateByAxis = MatRix.getMatrixROTATE(MatRix.CoordinateAxis.Ox, angle);
            } else if (axis == SystemCoordinate.AxisCoordinate.Oy) {
                matrixRotateByAxis = MatRix.getMatrixROTATE(MatRix.CoordinateAxis.Oy, angle);
            } else if (axis == SystemCoordinate.AxisCoordinate.Oz) {
                matrixRotateByAxis = MatRix.getMatrixROTATE(MatRix.CoordinateAxis.Oz, angle);
            }
            ArrayList<ArrayList<Vertex3D>> listVertexCylinder = cylinder.getVertexes();
            for (var arrVertex : listVertexCylinder) {
                for (var vertex : arrVertex) {
                    MatRix matrixPoint = new MatRix(new double[][]{
                            {vertex.getX(), vertex.getY(), vertex.getZ(), 1}
                    });
                    matrixPoint = matrixPoint.multiply(matrixRotateByAxis);

                    vertex.setX(matrixPoint.get(0, 0));
                    vertex.setY(matrixPoint.get(0, 1));
                    vertex.setZ(matrixPoint.get(0, 2));
                }
            }
        }
    }

    /**
     * Класс всех проекций
     */
    class Projections {

        static void makeFrontalProjectionCylinder(Cylinder cylinder) {
            Objects.requireNonNull(cylinder);

            MatRix matrixFrontalProjection = MatRix.getMatrixFrontalProjection();
            ArrayList<ArrayList<Vertex3D>> listVertexesCylinder = cylinder.getVertexes();

            for (var arrVertexes : listVertexesCylinder) {
                for (var vertex : arrVertexes) {
                    MatRix matrixPoint = new MatRix(new double[][]{
                            {vertex.getX(), vertex.getY(), vertex.getZ(), 1}
                    });
                    matrixPoint = matrixPoint.multiply(matrixFrontalProjection);

                    vertex.setX(matrixPoint.get(0, 0));
                    vertex.setY(matrixPoint.get(0, 1));
                }
            }
        }

        static void makeProfileProjectionCylinder(Cylinder cylinder) {
            Objects.requireNonNull(cylinder);

            MatRix matrixProfileProjection = MatRix.getMatrixProfileProjection();
            ArrayList<ArrayList<Vertex3D>> listVertexesCylinder = cylinder.getVertexes();

            for (var arrVertexes : listVertexesCylinder) {
                for (var vertex : arrVertexes) {
                    MatRix matrixPoint = new MatRix(new double[][]{
                            {vertex.getX(), vertex.getY(), vertex.getZ(), 1}
                    });
                    matrixPoint = matrixPoint.multiply(matrixProfileProjection);

                    vertex.setY(matrixPoint.get(0, 1));
                    vertex.setZ(matrixPoint.get(0, 2));
                    vertex.setX(vertex.getZ()); // Делаем так, обрисовывать ВСЕ ЧЕРЕЗ X Y
                }
            }
        }

        static void makeHorizontalProjectionCylinder(Cylinder cylinder) {
            Objects.requireNonNull(cylinder);

            MatRix matrixHorizontalProjection = MatRix.getMatrixHorizontalProjection();
            ArrayList<ArrayList<Vertex3D>> listVertexesCylinder = cylinder.getVertexes();

            for (var arrVertexes : listVertexesCylinder) {
                for (var vertex : arrVertexes) {
                    MatRix matrixPoint = new MatRix(new double[][]{
                            {vertex.getX(), vertex.getY(), vertex.getZ(), 1}
                    });
                    matrixPoint = matrixPoint.multiply(matrixHorizontalProjection);

                    vertex.setX(matrixPoint.get(0, 0));
                    vertex.setZ(matrixPoint.get(0, 2));
                    vertex.setY(-vertex.getZ());    // Делаем так, чтобы обрисовывать ВСЕ ЧЕРЕЗ X Y
                }
            }
        }

        static void makeAxonometricProjectionCylinder(Cylinder cylinder, double psiAngle, double fiAngle) {
            Objects.requireNonNull(cylinder);

            MatRix matrixAxonometricProjection = MatRix.getMatrixAxonometricProjection(psiAngle, fiAngle);
            ArrayList<ArrayList<Vertex3D>> listVertexesCylinder = cylinder.getVertexes();

            for (var arrVertexes : listVertexesCylinder) {
                for (var vertex : arrVertexes) {
                    MatRix matrixPoint = new MatRix(new double[][]{
                            {vertex.getX(), vertex.getY(), vertex.getZ(), 1}
                    });
                    matrixPoint = matrixPoint.multiply(matrixAxonometricProjection);

                    vertex.setX(matrixPoint.get(0, 0));
                    vertex.setY(matrixPoint.get(0, 1));
                }
            }
        }

        static void makeObliqueProjectionCylinder(Cylinder cylinder, double alfaAngle, double l) {
            Objects.requireNonNull(cylinder);

            MatRix matrixObliqueProjection = MatRix.getMatrixObliqueProjection(alfaAngle, l);
            ArrayList<ArrayList<Vertex3D>> listVertexesCylinder = cylinder.getVertexes();

            for (var arrVertexes : listVertexesCylinder) {
                for (var vertex : arrVertexes) {
                    MatRix matrixPoint = new MatRix(new double[][]{
                            {vertex.getX(), vertex.getY(), vertex.getZ(), 1}
                    });
                    matrixPoint = matrixPoint.multiply(matrixObliqueProjection);

                    vertex.setX(matrixPoint.get(0, 0));
                    vertex.setY(matrixPoint.get(0, 1));
                }
            }
        }

        static void makePerspectiveProjectionCylinder(Cylinder cylinder, double p, double d,
                                                      double teteAngle, double fiAngle) {
            Objects.requireNonNull(cylinder);

            MatRix matrixPerspectiveProjection = MatRix.getMatrixPerspectiveProjection(d);
            MatRix matrixViewTransformation = MatRix.getMatrixViewTransformation(p, teteAngle, fiAngle);
            ArrayList<ArrayList<Vertex3D>> listVertexesCylinder = cylinder.getVertexes();

            for (var arrVertexes : listVertexesCylinder) {
                for (var vertex : arrVertexes) {
                    // Обязательное условие.
                    double z = vertex.getZ();
                    if (z >= -0.1 && z < 0) {
                        vertex.setZ(-0.1);
                    } else if (z >= 0 && z <= 0.1) {
                        vertex.setZ(0.1);
                    }

                    MatRix matrixPoint = new MatRix(new double[][]{
                            {vertex.getX(), vertex.getY(), vertex.getZ(), 1}
                    });
                    matrixPoint = matrixPoint.multiply(matrixViewTransformation) // видовое преобразование
                            .multiply(matrixPerspectiveProjection);              // перспективная проекция

                    matrixPoint.set(0, 0,
                            matrixPoint.get(0, 0) / matrixPoint.get(0, 3));
                    matrixPoint.set(0, 1,
                            matrixPoint.get(0, 1) / matrixPoint.get(0, 3));
                    matrixPoint.set(0, 2,
                            matrixPoint.get(0, 2) / matrixPoint.get(0, 3));
                    matrixPoint.set(0, 3,
                            matrixPoint.get(0, 3) / matrixPoint.get(0, 3));

                    vertex.setX(matrixPoint.get(0, 0));
                    vertex.setY(matrixPoint.get(0, 1));
                }
            }
        }
    }
}
