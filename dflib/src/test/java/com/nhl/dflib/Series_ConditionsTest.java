package com.nhl.dflib;

import com.nhl.dflib.unit.BooleanSeriesAsserts;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class Series_ConditionsTest {

    @Test
    public void testEq1() {

        Series<String> s1 = Series.forData("a", "b", "n", "c");
        Series<String> s2 = Series.forData("a", "b", "n", "c");

        BooleanSeries cond = s1.eq(s2);
        new BooleanSeriesAsserts(cond).expectData(true, true, true, true);
    }

    @Test
    public void testEq2() {

        Series<String> s1 = Series.forData("a", "b", "n", "c");
        Series<String> s2 = Series.forData("a ", "b", "N", "c");

        BooleanSeries cond = s1.eq(s2);
        new BooleanSeriesAsserts(cond).expectData(false, true, false, true);
    }

    @Test
    public void testEq_SizeMismatch() {
        Series<String> s1 = Series.forData("a", "b", "n", "c");
        Series<String> s2 = Series.forData("a", "b", "n");

        assertThrows(IllegalArgumentException.class, () -> s1.eq(s2));
    }

    @Test
    public void testNe1() {

        Series<String> s1 = Series.forData("a", "b", "n", "c");
        Series<String> s2 = Series.forData("a", "b", "n", "c");

        BooleanSeries cond = s1.ne(s2);
        new BooleanSeriesAsserts(cond).expectData(false, false, false, false);
    }

    @Test
    public void testNe2() {

        Series<String> s1 = Series.forData("a", "b", "n", "c");
        Series<String> s2 = Series.forData("a ", "b", "N", "c");

        BooleanSeries cond = s1.ne(s2);
        new BooleanSeriesAsserts(cond).expectData(true, false, true, false);
    }

}
