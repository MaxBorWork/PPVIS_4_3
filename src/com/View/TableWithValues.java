package com.View;

import com.Model.Graphic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.*;

import java.util.List;

public class TableWithValues {
    private Table tableWithValues;

    public void createTable(Composite shell) {
        tableWithValues = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
        tableWithValues.setHeaderVisible(true);
        tableWithValues.setLinesVisible(true);
        tableWithValues.setLayoutData(new RowData(220, 500));
        TableColumn columnWithInputX = new TableColumn(tableWithValues, SWT.NONE);
        columnWithInputX.setText("x");
        columnWithInputX.setWidth(50);

        TableColumn columnWithResult = new TableColumn(tableWithValues, SWT.NONE);
        columnWithResult.setText("F(x)");
        columnWithResult.setWidth(150);

        tableWithValues.setSize(220, 500);
    }

    public void updateTable(Graphic graphic) {
        getTableWithValues().removeAll();
        for (int tableItemIndex = 0; tableItemIndex < graphic.getGraphicSize(); tableItemIndex++) {
            TableItem tableItem = new TableItem(tableWithValues, SWT.NONE);
            tableItem.setText(0, String.valueOf(graphic.getPoint(tableItemIndex).getX()));
            tableItem.setText(1, String.valueOf(graphic.getPoint(tableItemIndex).getY()));
        }
    }

    public Table getTableWithValues() {
        return tableWithValues;
    }
}
