package com.nhl.dflib;

import com.nhl.dflib.unit.DataFrameAsserts;
import org.junit.jupiter.api.Test;

public class ColumnDataFrameTest {

    @Test
    public void testConstructor() {
        Index i = Index.forLabels("a", "b");
        ColumnDataFrame df = new ColumnDataFrame(i,
                IntSeries.forInts(1, 2),
                IntSeries.forInts(3, 4));

        new DataFrameAsserts(df, i).expectHeight(2);
    }

    @Test
    public void testConstructor_NoData() {
        Index i = Index.forLabels("a", "b");
        ColumnDataFrame df = new ColumnDataFrame(i);

        new DataFrameAsserts(df, i).expectHeight(0);
    }
}
