package com.controller;

import com.model.Point;

public class CalculateFunction implements Runnable {
    Point addingPoint;
    Controller controller;

    CalculateFunction(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            for (Integer x : controller.getxList()) {
                calculateY(x);

                Thread.sleep(500);
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
}
