package com.controller;

import com.model.Graphic;
import com.model.GraphicPoint;
import com.model.Point;
import com.view.MainWindow;
import org.eclipse.swt.widgets.Display;

import java.util.List;

public class Controller{
    private List<Integer> xList;
    private Graphic graphic;
    private MainWindow mainWindow;
    private CalculateFunction calcFunction;

    public Controller(MainWindow mainWindow) {
        graphic = new Graphic();
        this.mainWindow = mainWindow;
        calcFunction = new CalculateFunction(this);
    }

    public void addPoint(Point point) {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                int index = graphic.addValue(point);
                mainWindow.updateData(index);
            }
        });
    }

    private void updateShell() {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                //myCanvas.updateCanvas();
                //tableWithValues.updateTable(graphic);
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
}
