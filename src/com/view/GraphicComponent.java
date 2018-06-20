package com.view;

import com.controller.Controller;
import com.model.Graphic;
import com.model.GraphicPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

import java.util.ArrayList;
import java.util.List;

public class GraphicComponent extends Canvas{
    private static final int CTRL_KEY_MASK = 262144;
    private static final int WHEEL_COUNT = 3;
    private static final int STEP_GRID = 100;
    private static final double STEP_SCALE = 0.1;
    private static final double MIN_SCALE = 0.5;
    private static final int STEP_INCREMENT_RESIZE = 300;
    private static final int STEP_REDUCTION_RESIZE = 201;
    private static int shiftX = 0;
    private static int shiftY = 0;
    private Rectangle rectangle = new Rectangle(0, 0, 1000, 1000);
    private org.eclipse.swt.graphics.Point startPoint = new org.eclipse.swt.graphics.Point(0, 0);
    private ScrollBar scrollBarH;
    private ScrollBar scrollBarV;
    private double scale = 1;
    private boolean ctrlIsPress = false;
    private GraphicPoint pointMaxHeight;
    private List<GraphicPoint> pointList;

    GraphicComponent(Display display, Shell shell) {
        super(shell, SWT.DOUBLE_BUFFERED | SWT.NO_REDRAW_RESIZE | SWT.V_SCROLL | SWT.H_SCROLL);
        setBackground(display.getSystemColor(SWT.COLOR_WHITE));
        pointList = new ArrayList<>();
        initListeners();
        initPaintListeners();
    }

    private void initListeners() {
        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseScrolled(MouseEvent mouseEvent) {
                if (ctrlIsPress) {
                    if (mouseEvent.count == WHEEL_COUNT) {
                        scale += STEP_SCALE;
                        while (updateSize()) ;
                    } else if (scale - STEP_SCALE > MIN_SCALE) {
                        scale -= STEP_SCALE;
                        while (updateSize()) ;
                    }
                    redraw();
                }
            }
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.keyCode == CTRL_KEY_MASK)
                    ctrlIsPress = true;
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                if (keyEvent.keyCode == 262144)
                    ctrlIsPress = false;
            }
        });

        scrollBarH = getHorizontalBar();

        scrollBarH.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                int hSelection = scrollBarH.getSelection();
                int destX = -hSelection - startPoint.x;
                scroll(destX, 0, 0, 0, rectangle.width, rectangle.height, false);
                startPoint.x = -hSelection;
            }
        });

        scrollBarV = getVerticalBar();

        scrollBarV.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                int vSelection = scrollBarV.getSelection();
                int destY = -vSelection - startPoint.y;
                scroll(0, destY, 0, 0, rectangle.width, rectangle.height, false);
                startPoint.y = -vSelection;
            }
        });

        addListener(SWT.Resize, new Listener() {
            public void handleEvent(Event e) {
                rectangle = new Rectangle(0, 0, getClientArea().width, getClientArea().height);
                resizeEvent();
                redraw();
            }
        });

        this.addMouseListener(new MouseListener() {
            int placeX = 0, placeY = 0;
            int xLast = 0, yLast = 0;

            @Override
            public void mouseDoubleClick(MouseEvent arg0) {
            }

            @Override
            public void mouseDown(MouseEvent arg0) {
                placeX = arg0.x;
                placeY = arg0.y;
            }

            @Override
            public void mouseUp(MouseEvent arg0) {
                xLast = arg0.x;
                yLast = arg0.y;
                shiftY = yLast - placeY;
                shiftX = xLast - placeX;
                redraw();
            }
        });

    }

    public void addData(GraphicPoint point) {
        if (pointList.size() == 0) {
            pointMaxHeight = point;
            pointList.add(point);
        } else {
            if (point.getY() > pointMaxHeight.getY()) {
                pointMaxHeight = point;
            }
            pointList.add(point);
        }
        updateGraphic();
        redraw();
    }

    private void initPaintListeners() {
        this.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent event) {

                event.gc.drawLine(startPoint.x + shiftX, rectangle.height + startPoint.y + shiftY, startPoint.x + shiftX, startPoint.y - rectangle.height + shiftY);
                event.gc.drawArc(startPoint.x + 1 + shiftX, startPoint.y - rectangle.height + 10 + shiftY, 10, 10, -90, -90);
                event.gc.drawArc(startPoint.x - 10 + shiftX, startPoint.y - rectangle.height + 10 + shiftY, 10, 10, 0, -90);
                event.gc.drawText("Y", startPoint.x - 20 + shiftX, startPoint.y - rectangle.height + shiftY);

                event.gc.drawLine(startPoint.x - rectangle.width + shiftX, startPoint.y + shiftY, startPoint.x + rectangle.width + shiftX, startPoint.y + shiftY);
                event.gc.drawArc(startPoint.x + rectangle.width - 20 + shiftX, startPoint.y + 1 + shiftY, 10, 10, -180, -90);
                event.gc.drawArc(startPoint.x + rectangle.width - 20 + shiftX, startPoint.y - 10 + shiftY, 10, 10, -90, -90);
                event.gc.drawText("X", startPoint.x + rectangle.width + shiftX, startPoint.y + 20 + shiftY);

                event.gc.drawText("0", startPoint.x - 10 + shiftX, startPoint.y + 5 + shiftY);

                for (int i = 0; i < (rectangle.width) / (scale * STEP_GRID); i++) {
                    event.gc.drawLine((int) (scale * STEP_GRID * (i + 1)) + startPoint.x + shiftX, startPoint.y - 10 + shiftY, (int) (scale * STEP_GRID * (i + 1)) + startPoint.x + shiftX, startPoint.y + 10 + shiftY);
                    event.gc.drawText(String.valueOf(i + 1), (int) (scale * STEP_GRID * (i + 1)) + startPoint.x + shiftX, startPoint.y +15 + shiftY, true);
                }

                for (int i = 0; i >  - (rectangle.width) / (scale * STEP_GRID); i--) {
                    event.gc.drawLine((int) (scale * STEP_GRID * (i - 1)) + startPoint.x + shiftX, startPoint.y - 10 + shiftY, (int) (scale * STEP_GRID * (i - 1)) + startPoint.x + shiftX, startPoint.y + 10 + shiftY);
                    event.gc.drawText(String.valueOf(i - 1), (int) (scale * STEP_GRID * (i - 1)) + startPoint.x + shiftX, startPoint.y +15 + shiftY, true);
                }

                for (int i = 0; i < (rectangle.height) / (scale * STEP_GRID); i++) {
                    event.gc.drawLine(startPoint.x + 10 + shiftX, startPoint.y - (int) (scale * STEP_GRID * (i + 1)) + shiftY,  startPoint.x - 10 + shiftX, startPoint.y - (int) (scale * STEP_GRID * (i + 1)) + shiftY);
                    event.gc.drawText(String.valueOf(i + 1), startPoint.x - 30 + shiftX, startPoint.y - (int) (scale * STEP_GRID * (i + 1)) + shiftY, true);
                }

                for (int i = 0; i > - (rectangle.height) / (scale * STEP_GRID); i--) {
                    event.gc.drawLine(startPoint.x + 10 + shiftX, startPoint.y - (int) (scale * STEP_GRID * (i - 1)) + shiftY,  startPoint.x - 10 + shiftX, startPoint.y - (int) (scale * STEP_GRID * (i - 1)) + shiftY);
                    event.gc.drawText(String.valueOf(i - 1), startPoint.x - 30 + shiftX, startPoint.y - (int) (scale * STEP_GRID * (i - 1)) + shiftY, true);
                }

                if (pointList.size() != 0) {

                    for (int drawingPoint = 0; drawingPoint < pointList.size() - 2; drawingPoint++){
                        event.gc.drawLine((int) (pointList.get(drawingPoint).getX() * STEP_GRID * scale) + startPoint.x + shiftX,
                                startPoint.y - (int) (pointList.get(drawingPoint).getY() * STEP_GRID * scale) + shiftY,
                                (int) (pointList.get(drawingPoint + 1).getX() * STEP_GRID * scale) + startPoint.x + shiftX,
                                startPoint.y - (int) (pointList.get(drawingPoint + 1).getY() * STEP_GRID * scale) + shiftY);
                    }

                }
            }
        });
        redraw();
    }

    private boolean updateSize() {
        boolean isUpdateSize = false;
        if (pointMaxHeight == null || pointList.size() == 0) return false;
        int x = rectangle.width - (int) (pointList.get(pointList.size() - 1).getX() * STEP_GRID * scale);
        int y = rectangle.height - (int) ((pointMaxHeight.getY() * STEP_GRID * scale));
        boolean condition_increment_size_width = x - 200 * scale < 0;
        if (condition_increment_size_width) {
            rectangle.width += (int) ((STEP_INCREMENT_RESIZE - x) * scale);
            isUpdateSize = true;
        } else {
            boolean condition_reduction_size_width = x > (500 * scale) && rectangle.width != getClientArea().width;
            if (condition_reduction_size_width) {
                rectangle.width -= (int) (x - STEP_REDUCTION_RESIZE * scale);
                if (rectangle.width < getClientArea().width)
                    rectangle.width = getClientArea().width;
                isUpdateSize = true;
            }
        }
        boolean condition_increment_size_height = y - 200 * scale < 0;
        if (condition_increment_size_height) {
            rectangle.height += (int) ((STEP_INCREMENT_RESIZE - y) * scale);
            isUpdateSize = true;
        } else {
            boolean condition_reduction_size_height = y > (500 * scale) && rectangle.height != getClientArea().height;
            if (condition_reduction_size_height) {
                rectangle.height -= (int) (y - STEP_REDUCTION_RESIZE * scale);
                if (rectangle.height < getClientArea().height) {
                    rectangle.height = getClientArea().height;
                }
                isUpdateSize = true;
            }
        }
        resizeEvent();
        return isUpdateSize;
    }

    private void updateGraphic() {
        int x = rectangle.width - (int) (pointList.get(pointList.size() - 1).getX() * STEP_GRID * scale);
        int y = rectangle.height - (int) ((pointMaxHeight.getY() * STEP_GRID * scale));
        boolean resizeScroll = x - 200 * scale < 0 || y - 200 * scale < 0;
        boolean changeScale = resizeScroll && scale - STEP_SCALE > MIN_SCALE;
        if (changeScale) {
            while (scale - STEP_SCALE > MIN_SCALE) {
                scale -= STEP_SCALE;
                x = rectangle.width - (int) (pointList.get(pointList.size() - 1).getX() * STEP_GRID * scale);
                y = rectangle.height - (int) ((pointMaxHeight.getY() * STEP_GRID * scale));
                resizeScroll = x - 200 * scale < 0 || y - 200 * scale < 0;
                if (!resizeScroll) return;
            }
        }
        updateSize();
    }

    private void resizeEvent() {
        Rectangle client = getClientArea();
        scrollBarH.setMaximum(rectangle.width);
        scrollBarV.setMaximum(rectangle.height);
        scrollBarH.setThumb(Math.min(rectangle.width, client.width));
        scrollBarV.setThumb(Math.min(rectangle.height, client.height));
        int hPage = rectangle.width - client.width;
        int vPage = rectangle.height - client.height;
        int hSelection = scrollBarH.getSelection();
        int vSelection = scrollBarV.getSelection();
        if (hSelection >= hPage) {
            if (hPage <= 0)
                hSelection = 0;
            startPoint.x = -hSelection;
        }
        if (vSelection >= vPage) {
            if (vPage <= 0)
                vSelection = 0;
            startPoint.y = -vSelection;
        }
    }

    public void removeAll() {
        pointList.clear();
        rectangle.height = getClientArea().height;
        rectangle.width = getClientArea().width;
        pointMaxHeight = null;
        startPoint = new org.eclipse.swt.graphics.Point(0, 0);
        resizeEvent();
        redraw();
    }

    public void updateCanvas() {
        redraw();
    }
}
