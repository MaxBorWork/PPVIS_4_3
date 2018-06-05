package com.View;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import java.util.ArrayList;
import java.util.List;

public class MainWindow {
    private Display display = new Display();
    private Shell shell;
    private TableWithValues tableWithValues;
    private Canvas mainCanvas;
    List<Integer> xList = new ArrayList<>();
    List<Double> yList = new ArrayList<>();

    public MainWindow() {
        shell = new Shell(display);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        shell.setLayout(gridLayout);

        tableWithValues = new TableWithValues();
        tableWithValues.createTable(shell);

        initCanvas(shell);

        Button inputBtn = new Button(shell, SWT.PUSH);
        inputBtn.setText("Введите границы массива");
        inputBtn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                inputBtnShell(gridLayout);
            }
        });

        shell.setSize(1000 , 700 );
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    public void initCanvas(Shell shell) {
        mainCanvas = new Canvas(shell, SWT.NONE);
        mainCanvas.setLayoutData(new GridData(500, 500));
        mainCanvas.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent paintEvent) {
                paintEvent.gc.drawLine(250, 450, 250, 50);
                paintEvent.gc.drawArc(250, 50, 10, 10, -90, -90);
                paintEvent.gc.drawArc(240, 50, 10, 10, 0, -90);
                paintEvent.gc.drawText("Y", 230, 45);
                paintEvent.gc.drawLine(50, 250, 450, 250);
                paintEvent.gc.drawArc(440, 250, 10, 10, -180, -90);
                paintEvent.gc.drawArc(440, 240, 10, 10, -90, -90);
                paintEvent.gc.drawText("X", 440, 255);

                for (int index = 0; index < xList.size(); index++) {
                    //paintEvent.gc.drawPoint(xList.get(index), yList.get(index));
                }

            }
        });
    }

    private void inputBtnShell(GridLayout gridLayout) {
        Shell inputBorderDialog = new Shell(display, SWT.DIALOG_TRIM);
        RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
        inputBorderDialog.setLayout(rowLayout);

        Composite bottomBord = new Composite(inputBorderDialog, SWT.NONE);
        bottomBord.setLayout(gridLayout);

        Label inputBottomBordLabel = new Label(bottomBord, SWT.NONE);
        inputBottomBordLabel.setText("Введите нижнюю границу: ");

        Text inputBottomBordText = new Text(bottomBord, SWT.NONE);

        Composite topBord = new Composite(inputBorderDialog, SWT.NONE);
        topBord.setLayout(gridLayout);

        Label inputTopBordLabel = new Label(topBord, SWT.NONE);
        inputTopBordLabel.setText("Введите нижнюю границу: ");

        Text inputTopBordText = new Text(topBord, SWT.NONE);

        Button inputOkButton = new Button(inputBorderDialog, SWT.PUSH);
        inputOkButton.setText("Задать границы");

        inputOkButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                for (int xMassIndex = (Integer.parseInt(inputBottomBordText.getText())); xMassIndex <= Integer.parseInt(inputTopBordText.getText()); xMassIndex++) {
                    xList.add(xMassIndex);
                }
                calcFunction(xList);
                tableWithValues.updateTable(tableWithValues.getTableWithValues(), xList, yList);
            }
        });

        inputBorderDialog.setSize(400, 200);
        inputBorderDialog.open();
    }

    private void calcFunction(List<Integer> xList) {
        double y = 0;
        int a = 2;
        int b = 1;
        double tempY;
        for (Integer x : xList) {
            for (int recursIndex = 1; recursIndex < 100000; recursIndex++) {
                tempY = Math.pow(-1, recursIndex) * Math.sin(recursIndex * (a * x - b)) / recursIndex;
                if (tempY > 0 && tempY <= 0.001) {
                    yList.add(y);
                    System.out.println("Y:" + y);
                    break;
                }
                else {
                    y = y + tempY;
                }
            }
        }

    }
}
