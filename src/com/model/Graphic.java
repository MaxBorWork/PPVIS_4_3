package com.model;

import java.util.ArrayList;
import java.util.List;

public class Graphic {
    private List<Point> graphic;

    public Graphic() {
        graphic = new ArrayList<>();
    }

    public void addValue(Point value) {
        synchronized (graphic) {
            graphic.add(value);
        }
    }

    public List<Point> getGraphic() {
        synchronized (graphic) {
            return graphic;
        }
    }

    public int getGraphicSize() {
        synchronized (graphic) {
            return graphic.size();
        }
    }

    public Point getPoint(int index) {
        synchronized (graphic) {
            return graphic.get(index);
        }
    }
}
