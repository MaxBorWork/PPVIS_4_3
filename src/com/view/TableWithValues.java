package com.view;

import com.model.Graphic;
import com.model.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.*;

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

    public void updateTable(Point point) {
            TableItem tableItem = new TableItem(tableWithValues, SWT.NONE);
            tableItem.setText(0, String.valueOf(point.getX()));
            tableItem.setText(1, String.valueOf(point.getY()));
    }

    public void removeAll() {
        tableWithValues.removeAll();
    }

    public Table getTableWithValues() {
        return tableWithValues;
    }
}
