package com.view;

import com.model.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

import java.util.ArrayList;
import java.util.List;

public class GraphicWindow extends Canvas{
    private static final int ctrlKeyCode = 262144;
    private double scale = 1;
    private static final int step = 100;
    private static int shiftX = 0;
    private static int shiftY = 0;
    private static final double scaleStep = 0.1;
    private static final double scaleMinimum = 0.2;
    private static final int increaseSizeStep = 300;
    private static final int decreaseSizeStep = 200;
    private Rectangle rectangle = new Rectangle(0, 0, 1000, 1000);
    private org.eclipse.swt.graphics.Point startPoint = new org.eclipse.swt.graphics.Point(0, 0);
    private boolean ctrlIsPressed = false;
    private ScrollBar scrollBarH;
    private ScrollBar scrollBarV;
    private Point pointMaxHeight;
    private List<Point> pointViewList;
    private Label zoomRatio;

    GraphicWindow(Display display, Shell shell, Label zoomRatio) {
        super(shell, SWT.DOUBLE_BUFFERED | SWT.NO_REDRAW_RESIZE | SWT.V_SCROLL | SWT.H_SCROLL);
        setBackground(display.getSystemColor(SWT.COLOR_WHITE));
        this.zoomRatio = zoomRatio;
        zoomRatio.setText("Кратность масштабирования: " + scale);
        pointViewList = new ArrayList<>();
        initListeners();
        initPaintListeners();
    }

    private void initListeners() {
        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseScrolled(MouseEvent mouseEvent) {
                if (ctrlIsPressed) {
                    if (mouseEvent.count == 3) {
                        scale += scaleStep;
                        while (updateScrollSize()) ;
                    } else if (scale - scaleStep > scaleMinimum) {
                        scale -= scaleStep;
                        while (updateScrollSize()) ;
                    }
                    redraw();
                    zoomRatio.setText("Кратность масштабирования: " + scale);
                }
            }
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.keyCode == ctrlKeyCode)
                    ctrlIsPressed = true;
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                if (keyEvent.keyCode == 262144)
                    ctrlIsPressed = false;
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
                addScroll();
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

    public void addPointToPointViewList(Point point) {
        if (pointViewList.size() == 0) {
            pointMaxHeight = point;
            pointViewList.add(point);
        } else {
            if (point.getY() > pointMaxHeight.getY()) {
                pointMaxHeight = point;
            }
            pointViewList.add(point);
        }
        updateScrollSize();
        redraw();
    }

    private void initPaintListeners() {
        this.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent event) {

                event.gc.drawLine(getClientArea().width /2 + startPoint.x + shiftX, getClientArea().height /2 + rectangle.height + startPoint.y + shiftY, getClientArea().width /2 + startPoint.x + shiftX, getClientArea().height /2 + startPoint.y - rectangle.height + shiftY);
                event.gc.drawArc(getClientArea().width /2 + startPoint.x + shiftX, getClientArea().height /2 + startPoint.y - rectangle.height + shiftY, 15, 15, -90, -90);
                event.gc.drawArc(getClientArea().width /2 + startPoint.x - 15 + shiftX, getClientArea().height /2 + startPoint.y - rectangle.height + shiftY, 15, 15, 0, -90);
                event.gc.drawText("Y", getClientArea().width /2 + startPoint.x - 20 + shiftX, getClientArea().height /2 + startPoint.y - rectangle.height + shiftY);

                event.gc.drawLine(getClientArea().width /2 + startPoint.x - rectangle.width + shiftX, getClientArea().height /2 + startPoint.y + shiftY, getClientArea().width /2 + startPoint.x + rectangle.width + shiftX, getClientArea().height /2 + startPoint.y + shiftY);
                event.gc.drawArc(getClientArea().width /2 + startPoint.x + rectangle.width - 10 + shiftX, getClientArea().height /2 + startPoint.y + shiftY, 15, 15, -180, -90);
                event.gc.drawArc(getClientArea().width /2 + startPoint.x + rectangle.width - 10 + shiftX, getClientArea().height /2 + startPoint.y - 15 + shiftY, 15, 15, -90, -90);
                event.gc.drawText("X", getClientArea().width /2 + startPoint.x + rectangle.width + shiftX, getClientArea().height /2 + startPoint.y + 20 + shiftY);

                event.gc.drawText("0", getClientArea().width /2 + startPoint.x - 10 + shiftX, getClientArea().height /2 + startPoint.y + 5 + shiftY);

                for (int i = 0; i < (rectangle.width) / (scale * step) - 1; i++) {
                    event.gc.drawLine((int) (scale * step * (i + 1)) + startPoint.x + getClientArea().width /2 +  shiftX, getClientArea().height /2 + startPoint.y - 10 + shiftY, (int) (scale * step * (i + 1)) + startPoint.x + getClientArea().width /2 + shiftX, getClientArea().height /2 + startPoint.y + 10 + shiftY);

                }
                event.gc.drawText("1", (int) (scale * step) + startPoint.x + getClientArea().width /2 + shiftX,
                                    getClientArea().height /2 + startPoint.y +15 + shiftY, true);

                for (int i = 0; i >  - (rectangle.width) / (scale * step); i--) {
                    event.gc.drawLine((int) (scale * step * (i - 1)) + startPoint.x + getClientArea().width /2 + shiftX, getClientArea().height /2 + startPoint.y - 10 + shiftY, (int) (scale * step * (i - 1)) + startPoint.x + getClientArea().width /2 + shiftX, getClientArea().height /2 + startPoint.y + 10 + shiftY);
                }
                event.gc.drawText("-1", (int) (scale * step * (- 1)) + startPoint.x + getClientArea().width /2 + shiftX,
                                    getClientArea().height /2 + startPoint.y +15 + shiftY, true);


                for (int i = 0; i < (rectangle.height) / (scale * step) - 1; i++) {
                    event.gc.drawLine(getClientArea().width /2 + startPoint.x + 10 + shiftX, getClientArea().height /2 + startPoint.y - (int) (scale * step * (i + 1)) + shiftY,  getClientArea().width /2 + startPoint.x - 10 + shiftX, getClientArea().height /2 + startPoint.y - (int) (scale * step * (i + 1)) + shiftY);
                }
                event.gc.drawText("1", getClientArea().width /2 + startPoint.x - 30 + shiftX,
                                    getClientArea().height /2 + startPoint.y - (int) (scale * step) + shiftY, true);


                for (int i = 0; i > - (rectangle.height) / (scale * step); i--) {
                    event.gc.drawLine(getClientArea().width /2 + startPoint.x + 10 + shiftX, getClientArea().height /2 + startPoint.y - (int) (scale * step * (i - 1)) + shiftY,  getClientArea().width /2 + startPoint.x - 10 + shiftX, getClientArea().height /2 + startPoint.y - (int) (scale * step * (i - 1)) + shiftY);
                }
                event.gc.drawText("-1", getClientArea().width /2 + startPoint.x - 30 + shiftX,
                                    getClientArea().height /2 + startPoint.y - (int) (scale * step * (- 1)) + shiftY, true);


                if (pointViewList.size() != 0) {

                    for (int drawingPoint = 0; drawingPoint < pointViewList.size() - 1; drawingPoint++){
                        event.gc.drawLine((int) (pointViewList.get(drawingPoint).getX() * step * scale) + startPoint.x + getClientArea().width /2 + shiftX,
                                getClientArea().height /2 + startPoint.y - (int) (pointViewList.get(drawingPoint).getY() * step * scale) + shiftY,
                                (int) (pointViewList.get(drawingPoint + 1).getX() * step * scale) + startPoint.x + getClientArea().width /2 + shiftX,
                                getClientArea().height /2 + startPoint.y - (int) (pointViewList.get(drawingPoint + 1).getY() * step * scale) + shiftY);
                    }

                }
            }
        });
        redraw();
    }

    private boolean updateScrollSize() {
        boolean isUpdatedWindowSize = false;
        if (pointMaxHeight == null || pointViewList.size() == 0) return false;
        int x = rectangle.width - (int) (getClientArea().width / 2 + pointViewList.get(pointViewList.size() - 1).getX() * step * scale);
        boolean incrementWidth = x - 200 * scale < 0;
        if (incrementWidth) {
            rectangle.width += (int) ((increaseSizeStep - x) * scale);
            isUpdatedWindowSize = true;
        } else {
            boolean reductionWidth = x > (500 * scale) && rectangle.width != getClientArea().width;
            if (reductionWidth) {
                rectangle.width -= (int) (x - decreaseSizeStep * scale);
                if (rectangle.width < getClientArea().width)
                    rectangle.width = getClientArea().width;
                isUpdatedWindowSize = true;
            }
        }
        addScroll();
        return isUpdatedWindowSize;
    }

    private void addScroll() {
        Rectangle client = getClientArea();
        scrollBarH.setMaximum(rectangle.width);
        scrollBarV.setMaximum(rectangle.height);
        scrollBarH.setMinimum(-rectangle.width);
        scrollBarV.setMinimum(-rectangle.height);
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
        pointViewList.clear();
        rectangle.height = getClientArea().height;
        rectangle.width = getClientArea().width;
        pointMaxHeight = null;
        startPoint = new org.eclipse.swt.graphics.Point(0, 0);
        addScroll();
        redraw();
    }

    public double getScale() {
        return scale;
    }
}
