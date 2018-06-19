package com.view;

import com.controller.Controller;
import com.model.Graphic;
import com.model.GraphicPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;

import java.util.ArrayList;
import java.util.List;

public class GraphicComponent extends Canvas{
    public static final int CTRL_KEY_MASK = 262144;
    public static final int WHEEL_COUNT = 3;
    public static final int STEP_GRID = 100;
    public static final double STEP_SCALE = 0.1;
    public static final double MIN_SCALE = 0.2;
    public static final int OFFSET_START_GRAPHIC = 50;
    public static final int DIAMETER_POINT = 10;
    public static final int LINE_WIDTH = 3;
    public static final int STEP_INCREMENT_RESIZE = 300;
    public static final int STEP_REDUCTION_RESIZE = 201;
    private Rectangle rectangle = new Rectangle(0, 0, 1000, 1000);
    private org.eclipse.swt.graphics.Point bias = new org.eclipse.swt.graphics.Point(0, 0);
    private ScrollBar scrollBarH;
    private ScrollBar scrollBarV;
    private double scale = 1;
    private boolean ctrlIsPress = false;
    private GraphicPoint pointMaxHeight;
    private List<GraphicPoint> pointList;
    //private static float scale = 1;
    private static int shiftX = 0;
    private static int shiftY = 0;
    private static int valOfDivX = 1;
    private static double valOfDivY = 0.1;

    GraphicComponent(Display display, Shell shell, Controller controller) {
        super(shell, SWT.DOUBLE_BUFFERED | SWT.NO_REDRAW_RESIZE | SWT.V_SCROLL | SWT.H_SCROLL);
        setBackground(display.getSystemColor(SWT.COLOR_WHITE));
        pointList = new ArrayList<>();
        initListeners();
        initPaintListeners(controller.getGraphic());
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
                int destX = -hSelection - bias.x;
                scroll(destX, 0, 0, 0, rectangle.width, rectangle.height, false);
                bias.x = -hSelection;
            }
        });

        scrollBarV = getVerticalBar();

        scrollBarV.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                int vSelection = scrollBarV.getSelection();
                int destY = -vSelection - bias.y;
                scroll(0, destY, 0, 0, rectangle.width, rectangle.height, false);
                bias.y = -vSelection;
            }
        });

        addListener(SWT.Resize, new Listener() {
            public void handleEvent(Event e) {
                rectangle = new Rectangle(0, 0, getClientArea().width, getClientArea().height);
                resizeEvent();
                redraw();
            }
        });

        /*this.addMouseListener(new MouseListener() {
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

        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseScrolled(MouseEvent mouseEvent) {
                if (mouseEvent.count > 0)
                    scale += .4f;
                else
                    scale -= .4f;
                scale = Math.max(scale, 0);
                redraw();
            }
        });*/
    }

    private void initPaintListeners(Graphic graphic) {
        this.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent event) {
                /*event.gc.drawLine((int) ((350 * scale) + shiftX), (int) ((650 * scale) + shiftY), (int) ((350 * scale) + shiftX), (int) ((50 * scale) + shiftY));
                event.gc.drawArc((int) ((350 * scale) + shiftX), (int) ((50 * scale) + shiftY), 10, 10, -90, -90);
                event.gc.drawArc((int) ((340 * scale) + shiftX), (int) ((50 * scale) + shiftY), 10, 10, 0, -90);
                event.gc.drawText("Y", (int) ((330 * scale) + shiftX), (int) ((45 * scale) + shiftY));
                event.gc.drawLine((int) ((50 * scale) + shiftX), (int) ((350 * scale) + shiftY), (int) ((650 * scale) + shiftX), (int) ((350 * scale) + shiftY));
                event.gc.drawArc((int) ((640 * scale) + shiftX), (int) ((350 * scale) + shiftY), 10, 10, -180, -90);
                event.gc.drawArc((int) ((640 * scale) + shiftX), (int) ((340 * scale) + shiftY), 10, 10, -90, -90);
                event.gc.drawText("X", (int) ((640 * scale) + shiftX), (int) ((355 * scale) + shiftY));*/

                event.gc.drawLine((int) ((scale) + shiftX), (int) ((-500 * scale) + shiftY), (int) ((scale) + shiftX), (int) ((500 * scale) + shiftY));
                event.gc.drawArc((int) ((1 * scale) + shiftX), (int) ((-490 * scale) + shiftY), 10, 10, -90, -90);
                event.gc.drawArc((int) ((-10 * scale) + shiftX), (int) ((-490 * scale) + shiftY), 10, 10, 0, -90);
                event.gc.drawText("Y", (int) ((-20 * scale) + shiftX), (int) ((-480 * scale) + shiftY));
                event.gc.drawLine((int) ((-500 * scale) + shiftX), (int) ((scale) + shiftY), (int) ((500 * scale) + shiftX), (int) ((scale) + shiftY));
                event.gc.drawArc((int) ((480 * scale) + shiftX), (int) ((1 * scale) + shiftY), 10, 10, -180, -90);
                event.gc.drawArc((int) ((480 * scale) + shiftX), (int) ((-10 * scale) + shiftY), 10, 10, -90, -90);
                event.gc.drawText("X", (int) ((480 * scale) + shiftX), (int) ((20 * scale) + shiftY));
                event.gc.drawText("0", (int)(-10 * scale + shiftX), (int) (5 * scale + shiftY));

                if (graphic.getGraphicSize() != 0) {

                    int numberOfDelX = (500 / 10) * valOfDivX;
                    int numberOfDelY = (int) ((500 / 10) * valOfDivY);
                    for (int delX = 0; delX < (10 / valOfDivX); delX++) {

                        if (delX == 1) {
                            event.gc.drawText("10",
                                    (int) (((numberOfDelX * (delX))) * scale) + shiftX,
                                    (int) (0 * scale) + shiftY, true);
                        }
                        event.gc.drawLine((int) (numberOfDelX * delX * scale) + shiftX,
                                (int) (-5 * scale) + shiftY, (int) (numberOfDelX * delX * scale) + shiftX,
                                (int) (5 * scale) + shiftY);
                    }
                    for (int delX = 0; delX > - (10 / valOfDivX); delX--) {
                        event.gc.drawLine((int) (numberOfDelX * delX * scale) + shiftX,
                                (int) (-5 * scale) + shiftY, (int) (numberOfDelX * delX * scale) + shiftX,
                                (int) (5 * scale) + shiftY);
                    }

                    for (int delY = 0; delY <= (10 / valOfDivY); delY++) {
                        if (delY == 1) {
                            event.gc.drawText(String.valueOf(valOfDivY), (int) (10 * scale) + shiftX,
                                    (int) (- (3 * numberOfDelY * (delY)) * scale) + shiftY, true);
                        }
                        event.gc.drawLine((int) (-5 * scale) + shiftX,
                                (int) ((0 - (numberOfDelY * delY)) * scale) + shiftY, (int) (5 * scale) + shiftX,
                                (int) ((0 - (numberOfDelY * delY)) * scale) + shiftY);
                    }
                    for (int delY = 0; delY > - (10 / valOfDivY); delY--) {
                        event.gc.drawLine((int) (-5 * scale) + shiftX,
                                (int) ((0 - (numberOfDelY * delY)) * scale) + shiftY, (int) (5 * scale) + shiftX,
                                (int) ((0 - (numberOfDelY * delY)) * scale) + shiftY);
                    }

                    for (int drawingPoint = 0; drawingPoint < graphic.getGraphicSize() - 2; drawingPoint++){
                        event.gc.drawLine((int) ((graphic.getPoint(drawingPoint).getX()*10*scale) + shiftX),
                                (int) ((graphic.getPoint(drawingPoint).getY())*100*scale * (-1) + shiftY),
                                (int) ((graphic.getPoint(drawingPoint + 1).getX()*10*scale) + shiftX),
                                (int) ((graphic.getPoint(drawingPoint+1).getY())*100*scale * (-1)) + shiftY);
                    }

                    /*for (int drawingPoint = 0; drawingPoint < graphic.getGraphicSize() - 2; drawingPoint++){
                        event.gc.drawLine((int) ((graphic.getPoint(drawingPoint).getX()*10*scale) + shiftX),
                                                (int) (700+(graphic.getPoint(drawingPoint).getY()*100*scale) + shiftY),
                                                    (int) ((graphic.getPoint(drawingPoint + 1).getX()*10*scale) + shiftX),
                                                (int) (700+(graphic.getPoint(drawingPoint+1).getY()*100*scale) + shiftY));
                    }*/
                }
            }
        });
    }

    private boolean updateSize() {
        boolean isUpdateSize = false;
        if (pointMaxHeight == null || pointList.size() == 0) return false;
        int x = rectangle.width - (int) (pointList.get(pointList.size() - 1).getX() * STEP_GRID * scale + OFFSET_START_GRAPHIC);
        int y = rectangle.height - (int) ((pointMaxHeight.getY() * STEP_GRID * scale) + OFFSET_START_GRAPHIC);
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
        int x = rectangle.width - (int) (pointList.get(pointList.size() - 1).getX() * STEP_GRID * scale + OFFSET_START_GRAPHIC);
        int y = rectangle.height - (int) ((pointMaxHeight.getY() * STEP_GRID * scale) + OFFSET_START_GRAPHIC);
        boolean resizeScroll = x - 200 * scale < 0 || y - 200 * scale < 0;
        boolean changeScale = resizeScroll && scale - STEP_SCALE > MIN_SCALE;
        if (changeScale) {
            while (scale - STEP_SCALE > MIN_SCALE) {
                scale -= STEP_SCALE;
                x = rectangle.width - (int) (pointList.get(pointList.size() - 1).getX() * STEP_GRID * scale + OFFSET_START_GRAPHIC);
                y = rectangle.height - (int) ((pointMaxHeight.getY() * STEP_GRID * scale) + OFFSET_START_GRAPHIC);
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
            bias.x = -hSelection;
        }
        if (vSelection >= vPage) {
            if (vPage <= 0)
                vSelection = 0;
            bias.y = -vSelection;
        }
    }

    public void removeAll() {
        pointList.clear();
        rectangle.height = getClientArea().height;
        rectangle.width = getClientArea().width;
        pointMaxHeight = null;
        bias = new org.eclipse.swt.graphics.Point(0, 0);
        resizeEvent();
        redraw();
    }

    public void updateCanvas() {
        redraw();
    }
}
