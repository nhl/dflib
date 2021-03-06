package com.nhl.dflib;

import com.nhl.dflib.unit.SeriesAsserts;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IntSeries_SelectTest {

    @Test
    public void testPositional() {
        Series<Integer> s = IntSeries.forInts(3, 4, 2).select(2, 1);
        new SeriesAsserts(s).expectData(2, 4);
        assertTrue(s instanceof IntSeries);
    }

    @Test
    public void testPositional_Empty() {
        Series<Integer> s = IntSeries.forInts(3, 4, 2).select();
        new SeriesAsserts(s).expectData();
        assertTrue(s instanceof IntSeries);
    }

    @Test
    public void testPositional_OutOfBounds() {
        IntSeries s = IntSeries.forInts(3, 4, 2);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> s.select(0, 3));
    }

    @Test
    public void testPositionalNulls() {
        Series<Integer> s = IntSeries.forInts(3, 4, 2).select(2, 1, -1);
        new SeriesAsserts(s).expectData(2, 4, null);
        assertFalse(s instanceof IntSeries);
    }

    @Test
    public void testBooleanCondition() {
        BooleanSeries condition = BooleanSeries.forBooleans(false, true, true);
        Series<Integer> s = IntSeries.forInts(3, 4, 2).select(condition);
        new SeriesAsserts(s).expectData(4, 2);
        assertTrue(s instanceof IntSeries);
    }

    @Test
    public void testPredicate() {
        Series<Integer> s = IntSeries.forInts(3, 4, 2).select(i -> i > 2);
        new SeriesAsserts(s).expectData(3, 4);
        assertTrue(s instanceof IntSeries);
    }

    @Test
    public void testInt() {
        Series<Integer> s = IntSeries.forInts(3, 4, 2).selectInt(i -> i > 2);
        new SeriesAsserts(s).expectData(3, 4);
        assertTrue(s instanceof IntSeries);
    }
}
