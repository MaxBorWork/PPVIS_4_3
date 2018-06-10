package com.Model;

import java.util.ArrayList;
import java.util.List;

public class Graphic {
    private List<Point> graphic = new ArrayList<>();

    public void addValue(Point value) {
        graphic.add(value);
    }

    public List<Point> getGraphic() {
        return graphic;
    }

    public int getGraphicSize() {
        return graphic.size();
    }

    public Point getPoint(int index) {
        return graphic.get(index);
    }
}
