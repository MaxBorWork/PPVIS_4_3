package com.View;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import java.util.List;

public class TableWithValues {
    private Table tableWithValues;

    public void createTable(Shell shell) {
        tableWithValues = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
        tableWithValues.setHeaderVisible(true);
        tableWithValues.setLinesVisible(true);
        tableWithValues.setLayoutData(new GridData(220, 500));
        TableColumn columnWithInputX = new TableColumn(tableWithValues, SWT.NONE);
        columnWithInputX.setText("x");
        columnWithInputX.setWidth(50);

        TableColumn columnWithResult = new TableColumn(tableWithValues, SWT.NONE);
        columnWithResult.setText("F(x)");
        columnWithResult.setWidth(150);

        tableWithValues.setSize(220, 500);
    }

    public void updateTable(Table tableWithValues, List<Integer> xList, List<Double> yList) {
        this.tableWithValues = tableWithValues;
        for (int tableItemIndex = 0; tableItemIndex < xList.size(); tableItemIndex++) {
            TableItem tableItem = new TableItem(tableWithValues, SWT.NONE);
            tableItem.setText(0, String.valueOf(xList.get(tableItemIndex)));
            tableItem.setText(1, String.valueOf(yList.get(tableItemIndex)));
        }
    }

    public Table getTableWithValues() {
        return tableWithValues;
    }
}
