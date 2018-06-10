package com.View;

import com.Model.Graphic;
import com.Model.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;

public class MyCanvas extends Canvas{
    private static float scale = 1;
    private static int shiftX = 0;
    private static int shiftY = 0;
    public static GC gc1;
    private static Font font;
    private int valX;
    private int valY;

    MyCanvas(Display display, Shell shell, Graphic graphic) {
        super(shell, SWT.NONE);
        setBackground(display.getSystemColor(SWT.COLOR_WHITE));
        this.setLayoutData(new GridData(700, 700));
        initCanvas(display, graphic);
    }

    private void initCanvas(Display display, Graphic graphic) {
        this.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent event) {
                event.gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
                event.gc.drawLine(250, 450, 250, 50);
                event.gc.drawArc(250, 50, 10, 10, -90, -90);
                event.gc.drawArc(240, 50, 10, 10, 0, -90);
                event.gc.drawText("Y", 230, 45);
                event.gc.drawLine(50, 250, 450, 250);
                event.gc.drawArc(440, 250, 10, 10, -180, -90);
                event.gc.drawArc(440, 240, 10, 10, -90, -90);
                event.gc.drawText("X", 440, 255);

                if (graphic.getGraphicSize() != 0) {
                    for (int drawingPoint = 0; drawingPoint < graphic.getGraphicSize(); drawingPoint++) {
                        event.gc.drawLine(graphic.getPoint(drawingPoint).getX(), (int) graphic.getPoint(drawingPoint).getY(),
                                graphic.getPoint(drawingPoint+1).getX(), (int) graphic.getPoint(drawingPoint+1).getY());
                    }
                }
            }
        });
    }

    public void updateCanvas() {
        redraw();
    }
}
