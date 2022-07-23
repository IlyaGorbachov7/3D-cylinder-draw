package models;

import javafx.scene.paint.Color;

import java.util.List;
import java.util.Objects;

/**
 * Класс описывает грань.
 */
public class Surface {
    private List<Vertex3D> vertexes;
    private Color color;

    public Surface(List<Vertex3D> vertexes) {
        this.vertexes = vertexes;
        this.color = Color.BLACK;
    }
    public Surface(List<Vertex3D> vertexes,Color color) {
        this.vertexes = vertexes;
        this.color = color;
    }

    public List<Vertex3D> getVertexes() {
        return vertexes;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setVertexes(List<Vertex3D> vertexes) {
        this.vertexes = vertexes;
    }

    @Override
    public String toString() {
        StringBuilder outputString = new StringBuilder();
        outputString.append("Surface{" + "vertexes=");
        for (Vertex3D vertex : vertexes) {
            outputString.append("\n\t\t\t").append(vertex);
        }
        return outputString.toString();
    }
}
