package com.nhl.dflib;

import com.nhl.dflib.unit.SeriesAsserts;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DoubleSeries_SelectTest {

    @Test
    public void test() {
        Series<Double> s = DoubleSeries.forDoubles(3, 4, 2).select(2, 1);
        new SeriesAsserts(s).expectData(2., 4.);
        assertTrue(s instanceof DoubleSeries);
    }

    @Test
    public void test_Empty() {
        Series<Double> s = DoubleSeries.forDoubles(3, 4, 2).select();
        new SeriesAsserts(s).expectData();
        assertTrue(s instanceof DoubleSeries);
    }

    @Test
    public void test_OutOfBounds() {
        DoubleSeries s =  DoubleSeries.forDoubles(3, 4, 2);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> s.select(0, 3));
    }

    @Test
    public void testNulls() {
        Series<Double> s = DoubleSeries.forDoubles(3, 4, 2).select(2, 1, -1);
        new SeriesAsserts(s).expectData(2., 4., null);
        assertFalse(s instanceof DoubleSeries);
    }

    @Test
    public void testBooleanCondition() {
        BooleanSeries condition = BooleanSeries.forBooleans(false, true, true);
        Series<Double> s = DoubleSeries.forDoubles(3, 4, 2).select(condition);
        new SeriesAsserts(s).expectData(4., 2.);
        assertTrue(s instanceof DoubleSeries);
    }
}
