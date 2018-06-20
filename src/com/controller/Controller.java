package com.controller;

import com.model.Graphic;
import com.model.Point;
import com.view.MainWindow;
import org.eclipse.swt.widgets.Display;

import java.util.ArrayList;
import java.util.List;

public class Controller implements Runnable{
    private List<Integer> xList;
    private Graphic graphic;
    private MainWindow mainWindow;
    private Point addingPoint;

    public Controller(MainWindow mainWindow) {
        graphic = new Graphic();
        this.mainWindow = mainWindow;
        xList = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            for (Integer x : xList) {
                addPoint(calculateY(x));
                Thread.sleep(100);
            }
        }
        catch (InterruptedException ignored) {
        }
    }

    private Point calculateY(int x) {
        double y = 0;
        int a = 2;
        int b = 1;
        double tempY;
        for (int recursIndex = 1; recursIndex < 100000; recursIndex++) {
            tempY = Math.pow(-1, recursIndex) * Math.sin(recursIndex * (a * x - b)) / recursIndex;
            if (tempY > 0 && tempY <= 0.001) {
                addingPoint = new Point(x, y);
                break;
            } else {
                y = y + tempY;
            }
        }
        return addingPoint;
    }

    public void addPoint(Point point) {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                int index = graphic.addValue(point);
                mainWindow.updateShell(index);
            }
        });
    }

    public List<Integer> getxList() {
        return xList;
    }

    public void addValToXList(int x) {
        xList.add(x);
    }

    public Graphic getGraphic() {
        return graphic;
    }

    public Point getPointFromGraphic(int index) {
        return graphic.getPoint(index);
    }
}
