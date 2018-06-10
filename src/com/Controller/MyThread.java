package com.Controller;

import com.Model.Graphic;
import com.Model.Point;
import com.View.MyCanvas;
import com.View.TableWithValues;
import org.eclipse.swt.widgets.Display;

import java.util.List;

public class MyThread implements Runnable{
    private List<Integer> xList;
    private Graphic graphic;
    private MyCanvas myCanvas;
    private TableWithValues tableWithValues;

    public MyThread(List<Integer> xList, Graphic graphic, MyCanvas myCanvas, TableWithValues tableWithValues) {
        this.xList = xList;
        this.graphic = graphic;
        this.myCanvas = myCanvas;
        this.tableWithValues = tableWithValues;
    }
    @Override
    public void run() {
        double y = 0;
        int a = 2;
        int b = 1;
        double tempY;
        for (Integer x : xList) {
            for (int recursIndex = 1; recursIndex < 100000; recursIndex++) {
                tempY = Math.pow(-1, recursIndex) * Math.sin(recursIndex * (a * x - b)) / recursIndex;
                if (tempY > 0 && tempY <= 0.001) {
                    Point point = new Point(x, y);
                    graphic.addValue(point);
                    this.updateShell();
                    break;
                }
                else {
                    y = y + tempY;
                }
            }
        }
    }

    private void updateShell() {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                myCanvas.updateCanvas();
                tableWithValues.updateTable(graphic);
            }
        });
    }

}
