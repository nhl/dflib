package com.nhl.dflib;

import com.nhl.dflib.unit.SeriesAsserts;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class Series_ShiftTest {

    @ParameterizedTest
    @EnumSource(SeriesType.class)
    public void testDefaultNull(SeriesType type) {
        Series<String> s = type.createSeries("a", "b", "c", "d").shift(2);
        new SeriesAsserts(s).expectData(null, null, "a", "b");
    }

    @ParameterizedTest
    @EnumSource(SeriesType.class)
    public void testPositive(SeriesType type) {
        Series<String> s = type.createSeries("a", "b", "c", "d").shift(2, "X");
        new SeriesAsserts(s).expectData("X", "X", "a", "b");
    }

    @ParameterizedTest
    @EnumSource(SeriesType.class)
    public void testNegative(SeriesType type) {
        Series<String> s = type.createSeries("a", "b", "c", "d").shift(-2, "X");
        new SeriesAsserts(s).expectData("c", "d", "X", "X");
    }

    @ParameterizedTest
    @EnumSource(SeriesType.class)
    public void testZero(SeriesType type) {
        Series<String> s = type.createSeries("a", "b", "c", "d").shift(0, "X");
        new SeriesAsserts(s).expectData("a", "b", "c", "d");
    }
}
