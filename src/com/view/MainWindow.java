package com.view;

import com.controller.Controller;
import com.model.Graphic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import java.util.ArrayList;
import java.util.List;

import static org.eclipse.swt.SWT.SHELL_TRIM;

public class MainWindow {
    private Display display = new Display();
    private TableWithValues tableWithValues;
    Controller controller;

    public MainWindow() {
        Shell shell = new Shell(display, SHELL_TRIM );
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        shell.setLayout(gridLayout);
        shell.setSize(1300 , 800 );

        Composite composite =new Composite(shell, SWT.NONE);
        RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
        composite.setLayout(rowLayout);

        tableWithValues = new TableWithValues();
        tableWithValues.createTable(composite);

        controller = new Controller(this);

        GraphicComponent myCanvas = new GraphicComponent(display, shell, controller);
        myCanvas.setLayoutData(new GridData(800, 700));

        Button inputBtn = new Button(composite, SWT.PUSH);
        inputBtn.setText("Введите границы массива");
        inputBtn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                inputBtnShell(gridLayout);
            }
        });

        Button drawGraphic = new Button(composite, SWT.PUSH);
        drawGraphic.setText("График");
        drawGraphic.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                /*Controller myThread = new Controller(xList, graphic, myCanvas, tableWithValues);
                new Thread(myThread).start();*/
            }
        });

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
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
                if (!inputBottomBordText.getText().equals("") && !inputTopBordText.getText().equals("")) {
                    for (int xMassIndex = (Integer.parseInt(inputBottomBordText.getText())); xMassIndex <= Integer.parseInt(inputTopBordText.getText()); xMassIndex++) {
                        controller.addValToXList(xMassIndex);
                    }
                    inputBorderDialog.close();
                }
                else {
                    MessageBox messageBox = new MessageBox(inputBorderDialog, SWT.APPLICATION_MODAL);
                    messageBox.setMessage("Введите обе границы!");
                    messageBox.open();
                }
            }
        });

        inputBorderDialog.setSize(400, 200);
        inputBorderDialog.open();
    }
}