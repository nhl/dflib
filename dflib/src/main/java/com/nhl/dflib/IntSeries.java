package com.nhl.dflib;

import com.nhl.dflib.accumulator.BooleanAccumulator;
import com.nhl.dflib.series.IntArraySeries;
import com.nhl.dflib.accumulator.IntAccumulator;
import com.nhl.dflib.sort.IntComparator;

import java.util.Comparator;
import java.util.Random;

/**
 * A Series optimized to store and access primitive int values without <code>java.lang.Integer</code> wrapper. Can also
 * pose as "Series&lt;Integer>", although this is not the most efficient way of using it.
 *
 * @since 0.6
 */
public interface IntSeries extends Series<Integer> {

    static IntSeries forInts(int... ints) {
        return new IntArraySeries(ints);
    }

    /**
     * @since 0.7
     */
    static <V> IntSeries forSeries(Series<V> series, IntValueMapper<? super V> converter) {
        int len = series.size();
        IntAccumulator a = new IntAccumulator(len);
        for (int i = 0; i < len; i++) {
            a.addInt(converter.map(series.get(i)));
        }

        return a.toSeries();
    }

    @Override
    default Class<Integer> getNominalType() {
        return Integer.TYPE;
    }

    @Override
    default Class<?> getInferredType() {
        return Integer.TYPE;
    }

    int getInt(int index);

    void copyToInt(int[] to, int fromOffset, int toOffset, int len);

    IntSeries materializeInt();

    IntSeries concatInt(IntSeries... other);

    IntSeries rangeOpenClosedInt(int fromInclusive, int toExclusive);

    IntSeries headInt(int len);

    IntSeries tailInt(int len);

    /**
     * @since 0.11
     */
    IntSeries selectInt(Condition condition);

    /**
     * @since 0.11
     */
    IntSeries selectInt(IntPredicate p);

    /**
     * @since 0.11
     */
    IntSeries selectInt(BooleanSeries positions);

    /**
     * @deprecated since 0.11 in favor of {@link #selectInt(IntPredicate)}
     */
    @Deprecated
    default IntSeries filterInt(IntPredicate p) {
        return selectInt(p);
    }

    /**
     * @deprecated since 0.11 in favor of {@link #selectInt(BooleanSeries)}
     */
    default IntSeries filterInt(BooleanSeries positions) {
        return selectInt(positions);
    }

    @Override
    IntSeries sort(Sorter... sorters);

    @Override
    IntSeries sort(Comparator<? super Integer> comparator);

    IntSeries sortInt();

    IntSeries sortInt(IntComparator comparator);

    /**
     * @since 0.8
     */
    IntSeries sortIndexInt();

    /**
     * @since 0.8
     */
    IntSeries sortIndexInt(IntComparator comparator);

    /**
     * Returns an IntSeries that represents positions in the Series that match the predicate. The returned value can be
     * used to "select" data from this Series or from DataFrame containing this Series. Same as {@link #index(ValuePredicate)},
     * only usually much faster.
     *
     * @param predicate match condition
     * @return an IntSeries that represents positions in the Series that match the predicate. Negative values denote
     * null values.
     */
    IntSeries indexInt(IntPredicate predicate);

    BooleanSeries locateInt(IntPredicate predicate);

    /**
     * @return a IntSeries that contains non-repeating values from this Series.
     */
    IntSeries uniqueInt();

    /**
     * @since 0.7
     */
    @Override
    IntSeries sample(int size);

    /**
     * @since 0.7
     */
    @Override
    IntSeries sample(int size, Random random);

    /**
     * @since 0.7
     */
    default int[] toIntArray() {
        int len = size();
        int[] copy = new int[len];
        copyToInt(copy, 0, 0, len);
        return copy;
    }

    /**
     * @since 0.7
     */
    int max();

    /**
     * @since 0.7
     */
    int min();

    /**
     * @since 0.7
     */
    long sum();

    /**
     * @deprecated since 0.11 in favor of {@link #avg()}.
     */
    @Deprecated
    default double average() {
        return avg();
    }

    /**
     * @since 0.11
     */
    double avg();

    /**
     * @since 0.7
     */
    double median();

    /**
     * @since 0.11
     */
    default IntSeries add(IntSeries s) {
        int len = size();
        IntAccumulator accumulator = new IntAccumulator(len);

        for (int i = 0; i < len; i++) {
            accumulator.addInt(this.getInt(i) + s.getInt(i));
        }

        return accumulator.toSeries();
    }

    /**
     * Performs subtraction operation between this and another IntSeries.
     *
     * @since 0.11
     */
    default IntSeries sub(IntSeries s) {
        int len = size();
        IntAccumulator accumulator = new IntAccumulator(len);

        for (int i = 0; i < len; i++) {
            accumulator.addInt(this.getInt(i) - s.getInt(i));
        }

        return accumulator.toSeries();
    }

    /**
     * Performs multiplication operation between this and another IntSeries.
     *
     * @since 0.11
     */
    default IntSeries mul(IntSeries s) {
        int len = size();
        IntAccumulator accumulator = new IntAccumulator(len);

        for (int i = 0; i < len; i++) {
            accumulator.addInt(this.getInt(i) * s.getInt(i));
        }

        return accumulator.toSeries();
    }

    /**
     * Performs division operation between this and another IntSeries.
     *
     * @since 0.11
     */
    default IntSeries div(IntSeries s) {
        int len = size();
        IntAccumulator accumulator = new IntAccumulator(len);

        for (int i = 0; i < len; i++) {
            accumulator.addInt(this.getInt(i) / s.getInt(i));
        }

        return accumulator.toSeries();
    }

    /**
     * Performs modulo operation between this and another IntSeries.
     *
     * @since 0.11
     */
    default IntSeries mod(IntSeries s) {
        int len = size();
        IntAccumulator accumulator = new IntAccumulator(len);

        for (int i = 0; i < len; i++) {
            accumulator.addInt(this.getInt(i) % s.getInt(i));
        }

        return accumulator.toSeries();
    }

    /**
     * @since 0.11
     */
    default BooleanSeries lt(IntSeries s) {
        int len = size();
        BooleanAccumulator accumulator = new BooleanAccumulator(len);

        for (int i = 0; i < len; i++) {
            accumulator.addBoolean(this.getInt(i) < s.getInt(i));
        }

        return accumulator.toSeries();
    }

    /**
     * @since 0.11
     */
    default BooleanSeries le(IntSeries s) {
        int len = size();
        BooleanAccumulator accumulator = new BooleanAccumulator(len);

        for (int i = 0; i < len; i++) {
            accumulator.addBoolean(this.getInt(i) <= s.getInt(i));
        }

        return accumulator.toSeries();
    }

    /**
     * @since 0.11
     */
    default BooleanSeries gt(IntSeries s) {
        int len = size();
        BooleanAccumulator accumulator = new BooleanAccumulator(len);

        for (int i = 0; i < len; i++) {
            accumulator.addBoolean(this.getInt(i) > s.getInt(i));
        }

        return accumulator.toSeries();
    }

    /**
     * @since 0.11
     */
    default BooleanSeries ge(IntSeries s) {
        int len = size();
        BooleanAccumulator accumulator = new BooleanAccumulator(len);

        for (int i = 0; i < len; i++) {
            accumulator.addBoolean(this.getInt(i) >= s.getInt(i));
        }

        return accumulator.toSeries();
    }
}
