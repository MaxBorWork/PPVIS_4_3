package com.model;

import java.util.ArrayList;
import java.util.List;

public class Graphic {
    private List<Point> graphic;

    public Graphic() {
        graphic = new ArrayList<>();
    }

    public int addValue(Point value) {
        synchronized (graphic) {
            graphic.add(value);
            return graphic.size()-1;
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
