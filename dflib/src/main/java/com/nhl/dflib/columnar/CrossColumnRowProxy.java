package com.nhl.dflib.columnar;

import com.nhl.dflib.Index;
import com.nhl.dflib.row.RowBuilder;
import com.nhl.dflib.row.RowProxy;

public class CrossColumnRowProxy implements RowProxy {

    private Index columns;
    private Series[] data;
    private int rowIndex;
    private int height;

    public CrossColumnRowProxy(Index columns, Series[] data, int height) {
        this.columns = columns;
        this.data = data;
        this.height = height;
    }

    @Override
    public Index getIndex() {
        return columns;
    }

    @Override
    public Object get(int columnPos) {
        return data[columnPos].get(rowIndex);
    }

    @Override
    public Object get(String columnName) {
        return data[columns.position(columnName).ordinal()].get(rowIndex);
    }

    @Override
    public void copyRange(RowBuilder to, int fromOffset, int toOffset, int len) {
        // row can be missing in joins...
        if (rowIndex >= 0) {
            for (int i = 0; i < columns.size(); i++) {
                to.set(i + toOffset, data[i].get(rowIndex));
            }
        }
    }

    public boolean hasNext() {
        return rowIndex < height;
    }

    public CrossColumnRowProxy rewind() {
        this.rowIndex++;
        return this;
    }
}
