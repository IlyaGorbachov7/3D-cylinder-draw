package com.example.demo2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import models.Cylinder;
import models.Light;
import models.Point2D;
import rendering.DrawerCylinder;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Класс, который связывает
 * fxml файл с кодом java
 * Нам же надо как-то обрабатывать действия,
 * <p>
 * Всем привет. Хотел сказать, что об этом
 * Я задумывался еще в детстве как это код взаимодействует с
 * интерфейсом. И вот я это изучаю. Я счастлив.
 *
 * @author SecuRiTy
 * @version 1.3 18.09.2021
 */
public class HelloController implements Initializable {

    private static final Alert MSG_ERROR = new Alert(Alert.AlertType.ERROR);
    private GraphicsContext GC;
    private Point2D ZERO_POINT;
    private DrawerCylinder drawerCylinder;

    //                      FXML переменные
    @FXML
    public Canvas canvasFxml;
    @FXML
    public AnchorPane panelCanvas;
    // Начальные условия
    @FXML
    private TextField nFxml;
    @FXML
    private TextField r1Fxml;
    @FXML
    private TextField r2Fxml;
    @FXML
    private TextField hFxml;

    // Кнопки
    @FXML
    public Button buttonAccept;
    @FXML
    public Button buttonMove;
    @FXML
    public Button buttonRotate;
    @FXML
    public Button buttonScale;
    @FXML
    public Button buttonRefresh;

    // Геометрические преобразования
    // Двигать
    @FXML
    public TextField dxFxml;
    @FXML
    public TextField dyFxml;
    @FXML
    public TextField dzFxml;
    // Масштабировать
    @FXML
    public TextField sxFxml;
    @FXML
    public TextField syFxml;
    @FXML
    public TextField szFxml;
    // Вращать
    @FXML
    public TextField rxFxml;
    @FXML
    public TextField ryFxml;
    @FXML
    public TextField rzFxml;

    // Углы проекций
    // для аксонометрической
    @FXML
    public TextField psiAngleAxonometricFxml;
    @FXML
    public TextField fiAngleAxonometricFxml;
    // для косоугольной
    @FXML
    public TextField alfaAngleObliqueFxml;
    @FXML
    public TextField lObliqueFxml;
    // для перспективной
    @FXML
    public TextField pPerspectiveFxml;
    @FXML
    public TextField dPerspectiveFxml;
    @FXML
    public TextField teteAnglePerspectiveFxml;
    @FXML
    public TextField fiAnglePerspectiveFxml;

    // Переключатели-радиоприемники принадлежащие одной группе
    // группа
    @FXML
    public ToggleGroup groupToggleProjection;
    // переключатели
    @FXML
    public RadioButton radioButFrontal;
    @FXML
    public RadioButton radioButProfile;
    @FXML
    public RadioButton radioButHorizontal;
    @FXML
    public RadioButton radioButAxonometric;
    @FXML
    public RadioButton radioButOblique;
    @FXML
    public RadioButton radioButPerspective;

    // Флажок на удаление невидимых линий
    @FXML
    public CheckBox checkButDelHiddenLines;
    // Флажок на удаление контура
    @FXML
    public CheckBox checkButDelContour;
    // Флажок на добавление света
    @FXML
    public CheckBox checkButAddLight;
    // для Света
    @FXML
    public TextField ilFxml;
    @FXML
    public TextField kdFxml;
    @FXML
    public TextField xCordLightFxml;
    @FXML
    public TextField yCordLightFxml;
    @FXML
    public TextField zCordLightFxml;

    /**
     * Переопределили метод интерфейса {@link Initializable}
     * <p>
     * Он здесь нужен, чтобы заранее инициализировать
     * необходимые переменные. Это очень меня спасло !
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GC = canvasFxml.getGraphicsContext2D();
        ZERO_POINT = new Point2D(canvasFxml.getWidth() / 2,
                canvasFxml.getHeight() / 2);

        canvasFxml.heightProperty().bind(panelCanvas.heightProperty());
        canvasFxml.widthProperty().bind(panelCanvas.widthProperty());

        canvasFxml.widthProperty().addListener((observable) -> {
            if (Objects.isNull(drawerCylinder)) return;
            ZERO_POINT.setX(canvasFxml.getWidth() / 2);
            ZERO_POINT.setY(canvasFxml.getHeight() / 2);
            selectedDrawingOption(new ActionEvent());

        });
        canvasFxml.heightProperty().addListener(observable -> {
            if (Objects.isNull(drawerCylinder)) return;
            ZERO_POINT.setX(canvasFxml.getWidth() / 2);
            ZERO_POINT.setY(canvasFxml.getHeight() / 2);
            selectedDrawingOption(new ActionEvent());
        });
    }

    //-------------------------- Геометрические преобразования
    @FXML
    public void buttonAcceptClicked(ActionEvent actionEvent) {
        int n;
        double r1, r2, h;
        try {
            n = Integer.parseInt(nFxml.getText());
            r1 = Double.parseDouble(r1Fxml.getText());
            r2 = Double.parseDouble(r2Fxml.getText());
            h = Double.parseDouble(hFxml.getText());
            if (Double.compare(r1, r2) > 0) {
                double buff = r1;
                r1 = r2;
                r2 = buff;
            }
            if (r1 == r2) r2 += 0.01;

            if (n <= 0 || h <= 0 || r1 <= 0 || r2 <= 0) throw new IllegalArgumentException(" < 0");
            if (n > 1_000) throw new IllegalArgumentException("Параметр: N - слишком велик");

        } catch (IllegalArgumentException exception) {
            MSG_ERROR.setContentText("В \"НАЧАЛЬНЫХ УЛОВИЯХ\"" +
                    "\nНе верно введено значение: " + exception.getMessage());
            MSG_ERROR.show();
            return;
        }
        drawerCylinder = new DrawerCylinder(new Cylinder(n, r1, r2, h), GC, ZERO_POINT);
        selectedDrawingOption(actionEvent);
    }

    @FXML
    public void buttonMoveClicked(ActionEvent actionEvent) {
        if (!checkedOnAcceptInitialCondition()) return;
        double dx, dy, dz;
        try {
            dx = Double.parseDouble(dxFxml.getText());
            dy = -Double.parseDouble(dyFxml.getText());
            dz = Double.parseDouble(dzFxml.getText());
        } catch (IllegalArgumentException exception) {
            MSG_ERROR.setContentText("В \"ГЕОМЕТРИЧЕСКИХ ПРЕОБРАЗОВАНИЯХ\"" +
                    "\nНе верно введено значение: " + exception.getMessage());
            MSG_ERROR.show();
            return;
        }
        drawerCylinder.makeMoveCylinder(dx, dy, dz);
        selectedDrawingOption(actionEvent);
    }

    @FXML
    public void buttonScaleClicked(ActionEvent actionEvent) {
        if (!checkedOnAcceptInitialCondition()) return;
        double sx, sy, sz;

        try {
            sx = Double.parseDouble(sxFxml.getText());
            sy = Double.parseDouble(syFxml.getText());
            sz = Double.parseDouble(szFxml.getText());
            if (sx <= 0 || sy <= 0 || sz <= 0) throw
                    new IllegalArgumentException("<=0");
        } catch (IllegalArgumentException exception) {
            MSG_ERROR.setContentText("В \"ГЕОМЕТРИЧЕСКИХ ПРЕОБРАЗОВАНИЯХ\"" +
                    "\nНе верно введено значение: " + exception.getMessage());
            MSG_ERROR.show();
            return;
        }
        drawerCylinder.makeScaleCylinder(sx, sy, sz);
        selectedDrawingOption(actionEvent);
    }

    @FXML
    public void buttonRotateClicked(ActionEvent actionEvent) {
        if (!checkedOnAcceptInitialCondition()) return;
        double rx, ry, rz;

        try {
            rx = Double.parseDouble(rxFxml.getText());
            ry = Double.parseDouble(ryFxml.getText());
            rz = Double.parseDouble(rzFxml.getText());

        } catch (IllegalArgumentException exception) {
            MSG_ERROR.setContentText("В \"ГЕОМЕТРИЧЕСКИХ ПРЕОБРАЗОВАНИЯХ\"" +
                    "\nНе верно введено значение: " + exception.getMessage());
            MSG_ERROR.show();
            return;
        }
        drawerCylinder.makeRotateCylinder(rx, ry, rz);
        selectedDrawingOption(actionEvent);
    }


    //-------------------------- Проекции
    @FXML
    public void buttonRefreshClicked(ActionEvent actionEvent) {
        selectedDrawingOption(actionEvent);
    }

    @FXML
    public void toggleFrontalSelected(ActionEvent actionEvent) {
        if (!checkedOnAcceptInitialCondition()) return;
        checkButtonAddLightClicked(actionEvent);
        checkButtonRemoveHiddenLinesClicked(actionEvent);
        checkButtonRemoveContourClicked(actionEvent);

        drawerCylinder.drawFrontalProjectionCylinder();
    }

    @FXML
    public void toggleProfileSelected(ActionEvent actionEvent) {
        if (!checkedOnAcceptInitialCondition()) return;
        checkButtonAddLightClicked(actionEvent);
        checkButtonRemoveHiddenLinesClicked(actionEvent);
        checkButtonRemoveContourClicked(actionEvent);

        drawerCylinder.drawProfileProjectionCylinder();
    }

    @FXML
    public void toggleHorizontalSelected(ActionEvent actionEvent) {
        if (!checkedOnAcceptInitialCondition()) return;
        checkButtonAddLightClicked(actionEvent);
        checkButtonRemoveHiddenLinesClicked(actionEvent);
        checkButtonRemoveContourClicked(actionEvent);

        drawerCylinder.drawHorizontalProjectionCylinder();
    }

    @FXML
    public void toggleAxonometricSelected(ActionEvent actionEvent) {
        if (!checkedOnAcceptInitialCondition()) return;
        checkButtonAddLightClicked(actionEvent);
        checkButtonRemoveHiddenLinesClicked(actionEvent);
        checkButtonRemoveContourClicked(actionEvent);

        double psiAngle;
        double fiAngle;
        try {
            psiAngle = Double.parseDouble(psiAngleAxonometricFxml.getText());
            fiAngle = Double.parseDouble(fiAngleAxonometricFxml.getText());
        } catch (IllegalArgumentException exception) {
            MSG_ERROR.setContentText("В \"Аксонометрической проекции\"" +
                    "\nНе верно введено значение: " + exception.getMessage());
            MSG_ERROR.show();
            return;
        }
        drawerCylinder.drawAxonometricProjectionCylinder(psiAngle, fiAngle);
    }

    @FXML
    public void toggleObliqueSelected(ActionEvent actionEvent) {
        if (!checkedOnAcceptInitialCondition()) return;
        checkButtonAddLightClicked(actionEvent);
        checkButtonRemoveHiddenLinesClicked(actionEvent);
        checkButtonRemoveContourClicked(actionEvent);

        double alfaAngle;
        double l;
        try {
            alfaAngle = Double.parseDouble(alfaAngleObliqueFxml.getText());
            l = Double.parseDouble(lObliqueFxml.getText());
        } catch (IllegalArgumentException exception) {
            MSG_ERROR.setContentText("В \"Косоугольной проекции\"" +
                    "\nНе верно введено значение: " + exception.getMessage());
            MSG_ERROR.show();
            return;
        }
        drawerCylinder.drawObliqueProjectionCylinder(alfaAngle, l);
    }

    @FXML
    public void togglePerspectiveSelected(ActionEvent actionEvent) {
        if (!checkedOnAcceptInitialCondition()) return;
        checkButtonAddLightClicked(actionEvent);
        checkButtonRemoveHiddenLinesClicked(actionEvent);
        checkButtonRemoveContourClicked(actionEvent);

        double p, d, teteAngle, fiAngle;
        try {
            p = Double.parseDouble(pPerspectiveFxml.getText());
            d = Double.parseDouble(dPerspectiveFxml.getText());
            teteAngle = Double.parseDouble(teteAnglePerspectiveFxml.getText());
            fiAngle = Double.parseDouble(fiAnglePerspectiveFxml.getText());
        } catch (IllegalArgumentException exception) {
            MSG_ERROR.setContentText("В \"Перспективной проекции\"" +
                    "\nНе верно введено значение: " + exception.getMessage());
            MSG_ERROR.show();
            return;
        }
        drawerCylinder.drawPerspectiveProjectionCylinder(p, d, teteAngle, fiAngle);
    }

    /**
     * Слушатель флага на удаление невидимых линий
     */
    @FXML
    public void checkButtonRemoveHiddenLinesClicked(ActionEvent actionEvent) {
        if (Objects.isNull(drawerCylinder)) return;
        drawerCylinder.getToolFilling().setRemoveHiddenLine(checkButDelHiddenLines.isSelected());
    }

    /**
     * Слушатель флага на удаление контура
     */
    @FXML
    public void checkButtonRemoveContourClicked(ActionEvent actionEvent) {
        if (Objects.isNull(drawerCylinder)) return;
        drawerCylinder.getToolFilling().setRemoveContour(checkButDelContour.isSelected());
    }

    /**
     * Слушатель флага на добавление света.
     */
    @FXML
    public void checkButtonAddLightClicked(ActionEvent actionEvent) {
        if (Objects.isNull(drawerCylinder)) return;
        if (checkButAddLight.isSelected()) {
            double il, kd, x, y, z;
            try {
                il = Double.parseDouble(ilFxml.getText());
                kd = Double.parseDouble(kdFxml.getText());
                x = Double.parseDouble(xCordLightFxml.getText());
                y = -Double.parseDouble(yCordLightFxml.getText());
                z = Double.parseDouble(zCordLightFxml.getText());
            } catch (IllegalArgumentException exception) {
                checkButAddLight.setSelected(false);
                MSG_ERROR.setContentText("В \"ПАРАМЕТРАХ СВЕТА\"" +
                        "\nНе верно введено значение: " + exception.getMessage());
                MSG_ERROR.show();
                return;
            }
            drawerCylinder.getToolFilling().setLight(new Light(x, y, z, il, kd));
        }
        drawerCylinder.getToolFilling().setAddLight(checkButAddLight.isSelected());
    }

    @FXML
    public void listenerClickedOnAllTextField(MouseEvent mouseEvent) {
        TextField g = (TextField) mouseEvent.getSource();
        g.selectAll();
    }

    // ------------------------- Вспомогательные методы (то есть не определенные в FXML) --------------------
    private boolean checkedOnAcceptInitialCondition() {
        if (Objects.isNull(drawerCylinder)) {
            MSG_ERROR.setContentText("Примите \"НАЧАЛЬНЫЕ УСЛОВИЯ\"");
            MSG_ERROR.show();
            return false;
        }
        return true;
    }

    private void selectedDrawingOption(ActionEvent actionEvent) {
        if (radioButFrontal.isSelected()) {
            toggleFrontalSelected(actionEvent);
        } else if (radioButProfile.isSelected()) {
            toggleProfileSelected(actionEvent);
        } else if (radioButHorizontal.isSelected()) {
            toggleHorizontalSelected(actionEvent);
        } else if (radioButAxonometric.isSelected()) {
            toggleAxonometricSelected(actionEvent);
        } else if (radioButOblique.isSelected()) {
            toggleObliqueSelected(actionEvent);
        } else if (radioButPerspective.isSelected()) {
            togglePerspectiveSelected(new ActionEvent());
        }
    }
}