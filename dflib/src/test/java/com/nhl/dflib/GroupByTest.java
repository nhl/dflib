package com.nhl.dflib;

import com.nhl.dflib.unit.DataFrameAsserts;
import com.nhl.dflib.unit.IntSeriesAsserts;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GroupByTest {

    @Test
    public void testGroup() {
        DataFrame df = DataFrame.newFrame("a", "b").foldByRow(
                1, "x",
                2, "y",
                1, "z",
                0, "a",
                1, "x");

        GroupBy gb = df.group(Hasher.forColumn("a"));
        assertNotNull(gb);

        assertEquals(3, gb.size());
        assertEquals(new HashSet<>(asList(0, 1, 2)), new HashSet<>(gb.getGroups()));

        new DataFrameAsserts(gb.getGroup(0), "a", "b")
                .expectHeight(1)
                .expectRow(0, 0, "a");

        new DataFrameAsserts(gb.getGroup(1), "a", "b")
                .expectHeight(3)
                .expectRow(0, 1, "x")
                .expectRow(1, 1, "z")
                .expectRow(2, 1, "x");

        new DataFrameAsserts(gb.getGroup(2), "a", "b")
                .expectHeight(1)
                .expectRow(0, 2, "y");
    }

    @Test
    public void testGroup_Empty() {
        DataFrame df = DataFrame.newFrame("a", "b").empty();

        GroupBy gb = df.group(Hasher.forColumn("a"));
        assertNotNull(gb);

        assertEquals(0, gb.size());
        assertEquals(Collections.emptySet(), new HashSet<>(gb.getGroups()));
    }

    @Test
    public void testGroup_NullKeysIgnored() {
        DataFrame df = DataFrame.newFrame("a", "b").foldByRow(
                1, "x",
                2, "y",
                1, "z",
                null, "a",
                1, "x");

        GroupBy gb = df.group(Hasher.forColumn("a"));
        assertNotNull(gb);

        assertEquals(2, gb.size());
        assertEquals(new HashSet<>(asList(1, 2)), new HashSet<>(gb.getGroups()));

        new DataFrameAsserts(gb.getGroup(1), "a", "b")
                .expectHeight(3)
                .expectRow(0, 1, "x")
                .expectRow(1, 1, "z")
                .expectRow(2, 1, "x");

        new DataFrameAsserts(gb.getGroup(2), "a", "b")
                .expectHeight(1)
                .expectRow(0, 2, "y");
    }

    @Test
    public void testGroup_Agg() {
        DataFrame df1 = DataFrame.newFrame("a", "b").foldByRow(
                1, "x",
                2, "y",
                1, "z",
                0, "a",
                1, "x");

        DataFrame df = df1.group("a").agg(Aggregator.sumLong("a"), Aggregator.concat("b", ";"));

        new DataFrameAsserts(df, "a", "b")
                .expectHeight(3)
                .expectRow(0, 3L, "x;z;x")
                .expectRow(1, 2L, "y")
                .expectRow(2, 0L, "a");
    }

    @Test
    public void testGroup_Agg_MultipleAggregationsForKey() {
        DataFrame df1 = DataFrame.newFrame("a", "b").foldByRow(
                1, "x",
                2, "y",
                1, "y",
                0, "a",
                1, "x");

        DataFrame df = df1
                .group("b")
                .agg(Aggregator.first("b"), Aggregator.sumLong("a"), Aggregator.medianDouble("a"));

        new DataFrameAsserts(df, "b", "a", "a_")
                .expectHeight(3)
                .expectRow(0, "x", 2L, 1.)
                .expectRow(1, "y", 3L, 1.5)
                .expectRow(2, "a", 0L, 0.);
    }

    @Test
    public void testGroup_toDataFrame() {
        DataFrame df1 = DataFrame.newFrame("a", "b").foldByRow(
                1, "x",
                2, "y",
                1, "y",
                0, "a",
                1, "x");

        DataFrame df = df1.group("a").toDataFrame();

        // must be sorted by groups in the order they are encountered
        new DataFrameAsserts(df, "a", "b")
                .expectHeight(5)
                .expectRow(0, 1, "x")
                .expectRow(1, 1, "y")
                .expectRow(2, 1, "x")
                .expectRow(3, 2, "y")
                .expectRow(4, 0, "a");
    }

    @Test
    public void testGroup_Head_toDataFrame() {
        DataFrame df1 = DataFrame.newFrame("a", "b").foldByRow(
                1, "x",
                2, "y",
                1, "y",
                0, "a",
                1, "x");

        DataFrame df2 = df1.group("a")
                .head(2)
                .toDataFrame();

        new DataFrameAsserts(df2, "a", "b")
                .expectHeight(4)
                .expectRow(0, 1, "x")
                .expectRow(1, 1, "y")
                .expectRow(2, 2, "y")
                .expectRow(3, 0, "a");

        DataFrame df3 = df1.group("a")
                .head(1)
                .toDataFrame();

        new DataFrameAsserts(df3, "a", "b")
                .expectHeight(3)
                .expectRow(0, 1, "x")
                .expectRow(1, 2, "y")
                .expectRow(2, 0, "a");
    }

    @Test
    public void testGroup_Head_Sort_toDataFrame() {
        DataFrame df1 = DataFrame.newFrame("a", "b").foldByRow(
                1, "x",
                2, "y",
                1, "y",
                0, "a",
                1, "x");

        DataFrame df2 = df1.group("a")
                .sort("b", false)
                .head(2)
                .toDataFrame();

        new DataFrameAsserts(df2, "a", "b")
                .expectHeight(4)
                .expectRow(0, 1, "y")
                .expectRow(1, 1, "x")
                .expectRow(2, 2, "y")
                .expectRow(3, 0, "a");
    }

    @Test
    public void testGroup_Tail_toDataFrame() {
        DataFrame df1 = DataFrame.newFrame("a", "b").foldByRow(
                1, "x",
                2, "y",
                1, "y",
                0, "a",
                1, "z");

        DataFrame df2 = df1.group("a")
                .tail(2)
                .toDataFrame();

        new DataFrameAsserts(df2, "a", "b")
                .expectHeight(4)
                .expectRow(0, 1, "y")
                .expectRow(1, 1, "z")
                .expectRow(2, 2, "y")
                .expectRow(3, 0, "a");

        DataFrame df3 = df1.group("a")
                .tail(1)
                .toDataFrame();

        new DataFrameAsserts(df3, "a", "b")
                .expectHeight(3)
                .expectRow(0, 1, "z")
                .expectRow(1, 2, "y")
                .expectRow(2, 0, "a");
    }

    @Test
    public void testGroup_Agg_Named() {
        DataFrame df1 = DataFrame.newFrame("a", "b").foldByRow(
                1, "x",
                2, "y",
                1, "y",
                0, "a",
                1, "x");

        DataFrame df = df1.group("b").agg(
                Aggregator.first("b"),
                Aggregator.sumLong("a").named("a_sum"),
                Aggregator.medianDouble("a").named("a_median"));

        new DataFrameAsserts(df, "b", "a_sum", "a_median")
                .expectHeight(3)
                .expectRow(0, "x", 2L, 1.)
                .expectRow(1, "y", 3L, 1.5)
                .expectRow(2, "a", 0L, 0.);
    }

    @Test
    public void testGroup_Sort_Pos() {
        DataFrame df1 = DataFrame.newFrame("a", "b").foldByRow(
                1, "x",
                2, "b",
                2, "a",
                1, "z",
                0, "n",
                1, "y");

        DataFrame df2 = df1.group("a")
                .sort(1, true)
                .toDataFrame();

        new DataFrameAsserts(df2, "a", "b")
                .expectHeight(6)
                .expectRow(0, 1, "x")
                .expectRow(1, 1, "y")
                .expectRow(2, 1, "z")
                .expectRow(3, 2, "a")
                .expectRow(4, 2, "b")
                .expectRow(5, 0, "n");
    }

    @Test
    public void testGroup_Sort_Name() {
        DataFrame df1 = DataFrame.newFrame("a", "b").foldByRow(
                1, "x",
                2, "b",
                2, "a",
                1, "z",
                0, "n",
                1, "y");

        DataFrame df2 = df1.group("a")
                .sort("b", true)
                .toDataFrame();

        new DataFrameAsserts(df2, "a", "b")
                .expectHeight(6)
                .expectRow(0, 1, "x")
                .expectRow(1, 1, "y")
                .expectRow(2, 1, "z")
                .expectRow(3, 2, "a")
                .expectRow(4, 2, "b")
                .expectRow(5, 0, "n");
    }

    @Test
    public void testGroup_Sort_Names() {
        DataFrame df1 = DataFrame.newFrame("a", "b", "c").foldByRow(
                1, "x", 2,
                2, "b", 1,
                2, "a", 2,
                1, "z", -1,
                0, "n", 5,
                1, "x", 1);

        DataFrame df2 = df1.group("a")
                .sort(new String[]{"b", "c"}, new boolean[]{true, true})
                .toDataFrame();

        new DataFrameAsserts(df2, "a", "b", "c")
                .expectHeight(6)
                .expectRow(0, 1, "x", 1)
                .expectRow(1, 1, "x", 2)
                .expectRow(2, 1, "z", -1)
                .expectRow(3, 2, "a", 2)
                .expectRow(4, 2, "b", 1)
                .expectRow(5, 0, "n", 5);
    }

    @Test
    public void testRank_NoSort() {
        DataFrame df = DataFrame.newFrame("a", "b").foldByRow(
                1, "x",
                2, "y",
                1, "z",
                0, "a",
                1, "x");

        IntSeries rn1 = df.group("a").rank();
        new IntSeriesAsserts(rn1).expectData(1, 1, 1, 1, 1);

        IntSeries rn2 = df.group("b").rank();
        new IntSeriesAsserts(rn2).expectData(1, 1, 1, 1, 1);
    }

    @Test
    public void testSort_Rank() {
        DataFrame df = DataFrame.newFrame("a", "b").foldByRow(
                1, "x",
                2, "y",
                1, "z",
                0, "a",
                1, "x");

        IntSeries rn1 = df.group("a").sort("b", true).rank();
        new IntSeriesAsserts(rn1).expectData(1, 1, 3, 1, 1);

        IntSeries rn2 = df.group("b").sort("a", true).rank();
        new IntSeriesAsserts(rn2).expectData(1, 1, 1, 1, 1);
    }
}
