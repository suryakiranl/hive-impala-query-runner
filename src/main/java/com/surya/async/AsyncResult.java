package com.surya.async;

import java.util.List;

public class AsyncResult {
    private List<String> columnNames;
    private long rowCount;

    public AsyncResult(List<String> columnNames, long rowCount) {
        this.columnNames = columnNames;
        this.rowCount = rowCount;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public long getRowCount() {
        return rowCount;
    }

    @Override
    public String toString() {
        return "AsyncResult{" +
            "rowCount=" + rowCount +
            ", columnNames=" + columnNames +
            '}';
    }
}
