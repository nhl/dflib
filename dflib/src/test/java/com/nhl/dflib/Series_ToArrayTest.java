package com.nhl.dflib;

import com.nhl.dflib.unit.SeriesAsserts;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class Series_ToArrayTest {

    @ParameterizedTest
    @EnumSource(SeriesType.class)
    public void testContents(SeriesType type) {
        String[] a = type.createSeries("a", "b", "c", "d", "e").toArray(new String[0]);
        assertArrayEquals(new Object[]{"a", "b", "c", "d", "e"}, a);
    }

    @ParameterizedTest
    @EnumSource(SeriesType.class)
    public void testMutability(SeriesType type) {
        Series<String> s = type.createSeries("a", "b");
        String[] a = s.toArray(new String[0]);
        a[0] = "c";

        new SeriesAsserts(s).expectData("a", "b");
    }
}
