package toolkit;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import models.Cylinder;
import models.Light;
import models.Surface;
import models.Vertex3D;
import models.Point2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Класс: Инструмент заполнения поверхности
 */
public class ToolsFillingSurfacesCylinder {
    private final GraphicsContext graphContext;
    private final Point2D zeroPoint;
    private Light light;
    private boolean isRemoveHiddenLine;
    private boolean isRemoveContour;
    private boolean isAddLight;

    public ToolsFillingSurfacesCylinder(GraphicsContext graphContext, Point2D zeroPointCoordinate) {
        this.graphContext = Objects.requireNonNull(graphContext);
        this.zeroPoint = Objects.requireNonNull(zeroPointCoordinate);
        isRemoveHiddenLine = isRemoveContour = false;
    }

    /**
     * Установка света.
     */
    public void setLight(Light light) {
        this.light = Objects.requireNonNull(light);
    }

    public Light getLight() {
        return light;
    }

    /**
     * @param removeHiddenLine флаг на удаление невидимых линий
     */
    public void setRemoveHiddenLine(boolean removeHiddenLine) {
        isRemoveHiddenLine = removeHiddenLine;
    }

    public boolean isRemoveHiddenLine() {
        return isRemoveHiddenLine;
    }

    /**
     * @param removeContour флаг на удаление контура
     */
    public void setRemoveContour(boolean removeContour) {
        isRemoveContour = removeContour;
    }

    public boolean isRemoveContour() {
        return isRemoveContour;
    }

    /**
     * @param addLight флаг на добавление света
     */
    public void setAddLight(boolean addLight) {
        isAddLight = addLight;
    }

    public boolean isAddLight() {
        return isAddLight;
    }

    /**
     * Закраска всего цилиндр.
     */
    public void fillSurfaceCylinder(Cylinder cylinder, Vertex3D vectorView) {
        ArrayList<ArrayList<Surface>> surfacesList = cylinder.getSurfaces();

        // Удаляем из цилиндра поверхности, которых не видны.
        if (isRemoveHiddenLine) deleteAllUnvisitableSurfaces(surfacesList, vectorView);
        // Закрашиваем с учетом света
        for (int indexSurface = 0; indexSurface < surfacesList.size(); ++indexSurface) {
            for (var facetSurface : surfacesList.get(indexSurface)) {
                if (isRemoveHiddenLine) startFillingSurface(facetSurface);
                if (!isRemoveContour) startDrawingLineBySurface(facetSurface, indexSurface);
            }
        }
    }

    /**
     * Удаление невидимых поверхностей
     */
    private void deleteAllUnvisitableSurfaces(List<ArrayList<Surface>> surfacesList, Vertex3D vertexView) {
        Objects.requireNonNull(surfacesList);
        Objects.requireNonNull(vertexView);
        for (var surfaces : surfacesList) {
            surfaces.removeIf(surface -> {
                Vertex3D vectorView = getVectorDirection(vertexView, getVertexCenterSurface(surface));
                return getScalarProductVectors(getVectorNarmalToSurface(surface), vectorView) < 0;
            });
        }
    }

    /**
     * Рисует контур грани поверхности
     *
     * @param surface      Грань поверхности цилиндра.
     * @param indexSurface Индекс поверхности. То есть у цилиндра с вырезом 4-и поверхности.<p>
     *                     0 - индекс боковой поверхности R1.<p>
     *                     1 - индекс боковой поверхности R2.<p>
     *                     2 - индекс нижней поверхности цилиндра.<p>
     *                     3 - индекс верхней поверхности цилиндра.<p>
     */
    private void startDrawingLineBySurface(Surface surface, int indexSurface) {
        if (indexSurface < 0 || indexSurface > 4) throw new IndexOutOfBoundsException("indexSurface: < 0 || > 4");
        Objects.requireNonNull(surface);

        List<Vertex3D> vertexes = surface.getVertexes();
        graphContext.setStroke(Color.BLACK);

        if (indexSurface == 2 || indexSurface == 3) {
            // Рисуем контур основание цилиндра под индексом indexSurface = 2, indexSurface == 3
            graphContext.strokeLine(
                    vertexes.get(0).getX() + zeroPoint.getX(), vertexes.get(0).getY() + zeroPoint.getY(),
                    vertexes.get(1).getX() + zeroPoint.getX(), vertexes.get(1).getY() + zeroPoint.getY());
            graphContext.strokeLine(
                    vertexes.get(2).getX() + zeroPoint.getX(), vertexes.get(2).getY() + zeroPoint.getY(),
                    vertexes.get(3).getX() + zeroPoint.getX(), vertexes.get(3).getY() + zeroPoint.getY());

        } else if (indexSurface == 0 || indexSurface == 1) {
            // Рисуем контур боковых поверхностей цилиндра под индексом indexSurface = 0, indexSurface = 1
            graphContext.strokeLine(
                    vertexes.get(0).getX() + zeroPoint.getX(), vertexes.get(0).getY() + zeroPoint.getY(),
                    vertexes.get(1).getX() + zeroPoint.getX(), vertexes.get(1).getY() + zeroPoint.getY());
            graphContext.strokeLine(
                    vertexes.get(2).getX() + zeroPoint.getX(), vertexes.get(2).getY() + zeroPoint.getY(),
                    vertexes.get(3).getX() + zeroPoint.getX(), vertexes.get(3).getY() + zeroPoint.getY());
            graphContext.strokeLine(
                    vertexes.get(0).getX() + zeroPoint.getX(), vertexes.get(0).getY() + zeroPoint.getY(),
                    vertexes.get(3).getX() + zeroPoint.getX(), vertexes.get(3).getY() + zeroPoint.getY());
            graphContext.strokeLine(
                    vertexes.get(1).getX() + zeroPoint.getX(), vertexes.get(1).getY() + zeroPoint.getY(),
                    vertexes.get(2).getX() + zeroPoint.getX(), vertexes.get(2).getY() + zeroPoint.getY());
        }
    }

    /**
     * Закрашивание грани.
     */
    private void startFillingSurface(Surface surface) {
        Objects.requireNonNull(surface);

        List<Vertex3D> vertexes = surface.getVertexes();

        if (isAddLight) {
            graphContext.setFill(getBrightness(surface, light));
        } else graphContext.setFill(surface.getColor());

        graphContext.fillPolygon(new double[]{
                vertexes.get(0).getX() + zeroPoint.getX(),
                vertexes.get(1).getX() + zeroPoint.getX(),
                vertexes.get(2).getX() + zeroPoint.getX(),
                vertexes.get(3).getX() + zeroPoint.getX(),
        }, new double[]{
                vertexes.get(0).getY() + zeroPoint.getY(),
                vertexes.get(1).getY() + zeroPoint.getY(),
                vertexes.get(2).getY() + zeroPoint.getY(),
                vertexes.get(3).getY() + zeroPoint.getY(),
        }, 4);
    }

    /**
     * Освещенность
     */
    private static Color getBrightness(Surface surface, Light light) {

        Vertex3D vectorNarmal = getVectorNarmalToSurface(surface);
        Vertex3D vectorLight = getVectorDirection(light.getPovPoint(), getVertexCenterSurface(surface));

        double cos = getCosAngleBetweenVectors(vectorNarmal, vectorLight);
        double distance = getVectorModule(vectorLight);

        double iLightIntensity = (light.getIl() * light.getKd() * cos) / Math.sqrt(distance);

        Color startingColor = surface.getColor();
        double r = startingColor.getRed() * 255d;
        double g = startingColor.getGreen() * 255d;
        double b = startingColor.getBlue() * 255d;

        r += iLightIntensity;
        g += iLightIntensity;
        b += iLightIntensity;

        r = (r > 255) ? 255.0 : r;
        g = (g > 255) ? 255.0 : g;
        b = (b > 255) ? 255.0 : b;

        r = (r < 0) ? 0 : r;
        g = (g < 0) ? 0 : g;
        b = (b < 0) ? 0 : b;
        return Color.rgb((int) r, (int) g, (int) b);
    }

    /**
     * @return {@link Vertex3D} координаты центра поверхности
     */
    private static Vertex3D getVertexCenterSurface(Surface surface) {
        Objects.requireNonNull(surface);
        List<Vertex3D> vertexesSurface = surface.getVertexes();
        double xCoordinateCenter = 0, yCoordinateCenter = 0, zCoordinateCenter = 0;
        for (var vertex : vertexesSurface) {
            xCoordinateCenter += vertex.getX();
            yCoordinateCenter += vertex.getY();
            zCoordinateCenter += vertex.getZ();
        }
        xCoordinateCenter /= vertexesSurface.size();
        yCoordinateCenter /= vertexesSurface.size();
        zCoordinateCenter /= vertexesSurface.size();
        return new Vertex3D(xCoordinateCenter, yCoordinateCenter, zCoordinateCenter);
    }

    /**
     * Для проверки:
     * https://ru.onlinemschool.com/math/assistance/cartesian_coordinate/plane/
     *
     * @return {@link Vertex3D} ВЕКТОР-НОРМАЛИ
     */
    static private Vertex3D getVectorNarmalToSurface(Surface surface) {
        Objects.requireNonNull(surface);
        List<Vertex3D> vertexesSurface = surface.getVertexes();
        Vertex3D v1 = vertexesSurface.get(0), v2 = vertexesSurface.get(1), v3 = vertexesSurface.get(2);
        return new Vertex3D(
                v1.getY() * v2.getZ() + v2.getY() * v3.getZ() + v3.getY() * v1.getZ()
                        - v2.getY() * v1.getZ() - v3.getY() * v2.getZ() - v1.getY() * v3.getZ(),
                v1.getZ() * v2.getX() + v2.getZ() * v3.getX() + v3.getZ() * v1.getX()
                        - v2.getZ() * v1.getX() - v3.getZ() * v2.getX() - v1.getZ() * v3.getX(),
                v1.getX() * v2.getY() + v2.getX() * v3.getY() + v3.getX() * v1.getY()
                        - v2.getX() * v1.getY() - v3.getX() * v2.getY() - v1.getX() * v3.getY());
    }

    /**
     * @return {@link Vertex3D} ВЕКТОР.
     */
    private static Vertex3D getVectorDirection(Vertex3D vertex1, Vertex3D vertex2) {
        return new Vertex3D(
                vertex1.getX() - vertex2.getX(),
                vertex1.getY() - vertex2.getY(),
                vertex1.getZ() - vertex2.getZ());
    }

    /**
     * @return Модуль вектора
     */
    private static double getVectorModule(Vertex3D vector) {
        return Math.sqrt(Math.pow(vector.getX(), 2) +
                Math.pow(vector.getY(), 2) +
                Math.pow(vector.getZ(), 2));
    }

    /**
     * @return Скалярное произведение векторов
     */
    private static double getScalarProductVectors(Vertex3D vector1, Vertex3D vector2) {
        return vector1.getX() * vector2.getX()
                + vector1.getY() * vector2.getY()
                + vector1.getZ() * vector2.getZ();
    }

    /**
     * Для проверки:
     * https://www.webmath.ru/poleznoe/formules_13_9.php
     *
     * @return значение косинуса скалярного произведения векторов
     */
    private static double getCosAngleBetweenVectors(Vertex3D vector1, Vertex3D vector2) {
        double result = getScalarProductVectors(vector1, vector2);
        result /= Math.sqrt(Math.pow(vector1.getX(), 2) + Math.pow(vector1.getY(), 2) + Math.pow(vector1.getZ(), 2))
                * Math.sqrt(Math.pow(vector2.getX(), 2) + Math.pow(vector2.getY(), 2) + Math.pow(vector2.getZ(), 2));
        return result;
    }
}
