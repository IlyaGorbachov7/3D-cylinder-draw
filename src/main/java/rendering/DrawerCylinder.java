package rendering;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import models.Cylinder;
import models.Point2D;
import models.Vertex3D;
import toolkit.ToolsFillingSurfacesCylinder;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Клас "Рисовальщик Цилиндра с вырезом"
 */
public class DrawerCylinder implements DrawWays {
    private Point2D zeroPoint;
    private Cylinder cylinder;
    private final GraphicsContext graphContext;
    private final ToolsFillingSurfacesCylinder toolFilling;

    public DrawerCylinder(Cylinder cylinder, GraphicsContext gc, Point2D zeroPoint) {
        this.cylinder = Objects.requireNonNull(cylinder);
        this.graphContext = Objects.requireNonNull(gc);
        this.zeroPoint = Objects.requireNonNull(zeroPoint);
        toolFilling = new ToolsFillingSurfacesCylinder(graphContext, zeroPoint);
    }

    public Point2D getZeroPoint() {
        return zeroPoint;
    }

    public void setZeroPoint(Point2D zeroPoint) {
        this.zeroPoint = zeroPoint;
    }

    public Cylinder getCylinder() {
        return cylinder;
    }

    public void setCylinder(Cylinder cylinder) {
        this.cylinder = cylinder;
    }

    public ToolsFillingSurfacesCylinder getToolFilling() {
        return toolFilling;
    }

    /**
     * Геометрические преобразования
     */
    public void makeMoveCylinder(double dx, double dy, double dz) {
        Vertex3D Dv = new Vertex3D(dx, dy, dz);
        DrawWays.GeometricTransformations.moveCylinder(cylinder, Dv);
    }

    public void makeScaleCylinder(double sx, double sy, double sz) {
        Vertex3D Sv = new Vertex3D(sx, sy, sz);
        DrawWays.GeometricTransformations.scaleCylinder(cylinder, Sv);
    }

    public void makeRotateCylinder(double rx, double ry, double rz) {
        Vertex3D Rv = new Vertex3D(rx, ry, rz);
        DrawWays.GeometricTransformations.rotateCylinder(cylinder, Rv);
    }

    /**
     * Отрисовка Проекций
     */
    public void drawFrontalProjectionCylinder() {
        redraw();
        SystemCoordinate.draw2D(SystemCoordinate.AxisCoordinate.Oz, zeroPoint, graphContext);
        try {
            Cylinder tmpCylinder = cylinder.clone();
            DrawWays.Projections.makeFrontalProjectionCylinder(tmpCylinder);
            toolFilling.fillSurfaceCylinder(tmpCylinder, new Vertex3D(0, 0, 10_000));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void drawProfileProjectionCylinder() {
        redraw();
        SystemCoordinate.draw2D(SystemCoordinate.AxisCoordinate.Ox, zeroPoint, graphContext);
        try {
            Cylinder tmpCylinder = cylinder.clone();
            DrawWays.Projections.makeProfileProjectionCylinder(tmpCylinder);
            toolFilling.fillSurfaceCylinder(tmpCylinder, new Vertex3D(0, 0, 10_000));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void drawHorizontalProjectionCylinder() {
        redraw();
        SystemCoordinate.draw2D(SystemCoordinate.AxisCoordinate.Oy, zeroPoint, graphContext);
        try {
            Cylinder tmpCylinder = cylinder.clone();
            DrawWays.Projections.makeHorizontalProjectionCylinder(tmpCylinder);
            toolFilling.fillSurfaceCylinder(tmpCylinder, new Vertex3D(0, 0, 10_000));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void drawAxonometricProjectionCylinder(double psiAngle, double fiAngle) {
        redraw();
        SystemCoordinate.draw3DForAxonometricProjection(zeroPoint, graphContext, psiAngle, fiAngle);
        try {
            Cylinder tmpCylinder = cylinder.clone();
            Projections.makeAxonometricProjectionCylinder(tmpCylinder, psiAngle, fiAngle);
            toolFilling.fillSurfaceCylinder(tmpCylinder, new Vertex3D(0, 0, 10_000));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void drawObliqueProjectionCylinder(double alfaAngle, double l) {
        redraw();
        SystemCoordinate.draw3DForObliqueProjection(zeroPoint, graphContext, alfaAngle, l);
        try {
            Cylinder tmpCylinder = cylinder.clone();
            Projections.makeObliqueProjectionCylinder(tmpCylinder, alfaAngle, l);
            toolFilling.fillSurfaceCylinder(tmpCylinder, new Vertex3D(0, 0, 10_000));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void drawPerspectiveProjectionCylinder(double p, double d, double teteAngle, double fiAngle) {
        redraw();
        SystemCoordinate.draw3DForPerspectiveProjection(zeroPoint, graphContext, d, p, teteAngle, fiAngle);
        try {
            Cylinder tmpCylinder = cylinder.clone();
            Projections.makePerspectiveProjectionCylinder(tmpCylinder, p, d, teteAngle, fiAngle);
            toolFilling.fillSurfaceCylinder(tmpCylinder, new Vertex3D(0, 0, -10_000));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Очистить canvas
     */
    private void redraw() {
        Canvas canvas = graphContext.getCanvas();
        graphContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphContext.setFill(Color.WHITE);
        graphContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
