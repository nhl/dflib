package com.nhl.dflib.series;

import com.nhl.dflib.DataFrame;
import com.nhl.dflib.Series;
import com.nhl.dflib.unit.SeriesAsserts;
import org.junit.jupiter.api.Test;

public class ByRowSeries_TailTest {

    private ByRowSeries createSeries(Object... data) {
        return new ByRowSeries(DataFrame.newFrame("a", "b").foldByRow(data));
    }

    @Test
    public void test() {
        Series<Object> s = createSeries("a", "b", "c", "d").tail(2);
        new SeriesAsserts(s).expectData("c", "d");
    }

    @Test
    public void test_Zero() {
        Series<Object> s = createSeries("a", "b", "c", "d").tail(0);
        new SeriesAsserts(s).expectData();
    }

    @Test
    public void test_OutOfBounds() {
        Series<Object> s = createSeries("a", "b").tail(3);
        new SeriesAsserts(s).expectData("a", "b");
    }
}
