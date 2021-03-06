package com.nhl.dflib;

import com.nhl.dflib.unit.DoubleSeriesAsserts;
import org.junit.jupiter.api.Test;

public class DoubleSeries_SortTest {

    @Test
    public void testSortDouble() {
        DoubleSeries s = DoubleSeries.forDoubles(5., -1., 5., 3., 28., 1.).sortDouble();
        new DoubleSeriesAsserts(s).expectData(-1., 1., 3., 5., 5., 28.);
    }

    @Test
    public void testSort_Comparator() {
        DoubleSeries s = DoubleSeries.forDoubles(5., -1., 5., 3., 28., 1.).sort((d1, d2) -> (int) Math.round(d2 - d1));
        new DoubleSeriesAsserts(s).expectData(28., 5., 5., 3., 1., -1.);
    }

    @Test
    public void testSort_Sorter() {
        DoubleSeries s = DoubleSeries.forDoubles(5., -1., 5., 3., 28., 1.).sort(Exp.$double(0).desc());
        new DoubleSeriesAsserts(s).expectData(28., 5., 5., 3., 1., -1.);
    }
}
