package com.nhl.dflib.series;

import com.nhl.dflib.*;
import com.nhl.dflib.accumulator.*;
import com.nhl.dflib.concat.SeriesConcat;
import com.nhl.dflib.groupby.SeriesGrouper;
import com.nhl.dflib.map.Mapper;
import com.nhl.dflib.sample.Sampler;
import com.nhl.dflib.sort.SeriesSorter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Random;

/**
 * @since 0.6
 */
public abstract class LongBaseSeries implements LongSeries {

    @Override
    public <V> Series<V> map(ValueMapper<Long, V> mapper) {
        return new ColumnMappedSeries<>(this, mapper);
    }

    @Override
    public DataFrame map(Index resultColumns, ValueToRowMapper<Long> mapper) {
        return Mapper.map(this, resultColumns, mapper);
    }

    @Override
    public Series<Long> rangeOpenClosed(int fromInclusive, int toExclusive) {
        return rangeOpenClosedLong(fromInclusive, toExclusive);
    }

    @Override
    public Series<Long> select(IntSeries positions) {

        int h = positions.size();

        long[] data = new long[h];

        for (int i = 0; i < h; i++) {
            int index = positions.getInt(i);

            // "index < 0" (often found in outer joins) indicate nulls.
            // If a null is encountered, we can no longer maintain primitive and have to change to Series<Long>...
            if (index < 0) {
                return selectAsObjectSeries(positions);
            }

            data[i] = getLong(index);
        }

        return new LongArraySeries(data);
    }

    @Override
    public Series<Long> select(Condition condition) {
        return selectLong(condition);
    }

    @Override
    public Series<Long> select(ValuePredicate<Long> p) {
        return selectLong(p::test);
    }

    @Override
    public LongSeries selectLong(Condition condition) {
        return selectLong(condition.eval(this));
    }

    @Override
    public LongSeries selectLong(LongPredicate p) {
        LongAccumulator filtered = new LongAccumulator();

        int len = size();

        for (int i = 0; i < len; i++) {
            long v = getLong(i);
            if (p.test(v)) {
                filtered.addLong(v);
            }
        }

        return filtered.toSeries();
    }

    @Override
    public LongSeries selectLong(BooleanSeries positions) {
        int s = size();
        int ps = positions.size();

        if (s != ps) {
            throw new IllegalArgumentException("Positions size " + ps + " is not the same as this size " + s);
        }

        LongAccumulator data = new LongAccumulator();

        for (int i = 0; i < size(); i++) {
            if (positions.getBoolean(i)) {
                data.addLong(getLong(i));
            }
        }

        return data.toSeries();
    }

    @Override
    public Series<Long> select(BooleanSeries positions) {
        return selectLong(positions);
    }

    @Override
    public LongSeries sortLong() {
        int size = size();
        long[] sorted = new long[size];
        copyToLong(sorted, 0, 0, size);

        // TODO: use "parallelSort" ?
        Arrays.sort(sorted);

        return new LongArraySeries(sorted);
    }

    @Override
    public LongSeries sort(Sorter... sorters) {
        return selectAsLongSeries(new SeriesSorter<>(this).sortIndex(sorters));
    }

    // TODO: implement 'sortLong(LongComparator)' similar to how IntBaseSeries does "sortInt(IntComparator)"
    //   Reimplement this method to delegate to 'sortLong'
    @Override
    public LongSeries sort(Comparator<? super Long> comparator) {
        return selectAsLongSeries(new SeriesSorter<>(this).sortIndex(comparator));
    }

    private LongSeries selectAsLongSeries(IntSeries positions) {

        int h = positions.size();

        long[] data = new long[h];

        for (int i = 0; i < h; i++) {
            // unlike SelectSeries, we do not expect negative ints in the index.
            // So if it happens, let it fall through to "getLong()" and fail there
            int index = positions.getInt(i);
            data[i] = getLong(index);
        }

        return new LongArraySeries(data);
    }

    private Series<Long> selectAsObjectSeries(IntSeries positions) {

        int h = positions.size();
        Long[] data = new Long[h];

        for (int i = 0; i < h; i++) {
            int index = positions.getInt(i);
            data[i] = index < 0 ? null : getLong(index);
        }

        return new ArraySeries<>(data);
    }

    @Override
    public LongSeries concatLong(LongSeries... other) {
        if (other.length == 0) {
            return this;
        }

        // TODO: use SeriesConcat

        int size = size();
        int h = size;
        for (LongSeries s : other) {
            h += s.size();
        }

        long[] data = new long[h];
        copyToLong(data, 0, 0, size);

        int offset = size;
        for (LongSeries s : other) {
            int len = s.size();
            s.copyToLong(data, 0, offset, len);
            offset += len;
        }

        return new LongArraySeries(data);
    }

    @Override
    public Series<Long> fillNulls(Long value) {
        // primitive series has no nulls
        return this;
    }

    @Override
    public Series<Long> fillNullsFromSeries(Series<? extends Long> values) {
        // primitive series has no nulls
        return this;
    }

    @Override
    public Series<Long> fillNullsBackwards() {
        // primitive series has no nulls
        return this;
    }

    @Override
    public Series<Long> fillNullsForward() {
        // primitive series has no nulls
        return this;
    }

    @Override
    public Series<Long> head(int len) {
        return headLong(len);
    }

    @Override
    public Series<Long> tail(int len) {
        return tailLong(len);
    }

    @SafeVarargs
    @Override
    public final Series<Long> concat(Series<? extends Long>... other) {
        // concatenating as Double... to concat as DoubleSeries, "concatDouble" should be used
        if (other.length == 0) {
            return this;
        }

        // TODO: use SeriesConcat

        Series<Long>[] combined = new Series[other.length + 1];
        combined[0] = this;
        System.arraycopy(other, 0, combined, 1, other.length);

        return SeriesConcat.concat(combined);
    }

    @Override
    public Series<Long> materialize() {
        return materializeLong();
    }

    @Override
    public Long get(int index) {
        return getLong(index);
    }

    @Override
    public void copyTo(Object[] to, int fromOffset, int toOffset, int len) {
        for (int i = 0; i < len; i++) {
            to[toOffset + i] = getLong(i);
        }
    }

    @Override
    public IntSeries indexLong(LongPredicate predicate) {
        IntAccumulator index = new IntAccumulator();

        int len = size();

        for (int i = 0; i < len; i++) {
            if (predicate.test(getLong(i))) {
                index.addInt(i);
            }
        }

        return index.toSeries();
    }

    @Override
    public IntSeries index(ValuePredicate<Long> predicate) {
        return indexLong(predicate::test);
    }

    @Override
    public BooleanSeries locateLong(LongPredicate predicate) {
        int len = size();

        BooleanAccumulator matches = new BooleanAccumulator(len);

        for (int i = 0; i < len; i++) {
            matches.addBoolean(predicate.test(getLong(i)));
        }

        return matches.toSeries();
    }

    @Override
    public BooleanSeries locate(ValuePredicate<Long> predicate) {
        return locateLong(predicate::test);
    }

    @Override
    public Series<Long> replace(BooleanSeries condition, Long with) {
        return with != null
                ? replaceLong(condition, with)
                : nullify(condition);
    }

    @Override
    public Series<Long> replaceNoMatch(BooleanSeries condition, Long with) {
        return with != null
                ? replaceNoMatchLong(condition, with)
                : nullifyNoMatch(condition);
    }

    // TODO: make long versions of 'replace' public?

    private LongSeries replaceLong(BooleanSeries condition, long with) {
        int s = size();
        int r = Math.min(s, condition.size());
        LongAccumulator longs = new LongAccumulator(s);

        for (int i = 0; i < r; i++) {
            longs.addLong(condition.getBoolean(i) ? with : getLong(i));
        }

        for (int i = r; i < s; i++) {
            longs.addLong(getLong(i));
        }

        return longs.toSeries();
    }

    private LongSeries replaceNoMatchLong(BooleanSeries condition, long with) {

        int s = size();
        int r = Math.min(s, condition.size());
        LongAccumulator longs = new LongAccumulator(s);

        for (int i = 0; i < r; i++) {
            longs.addLong(condition.getBoolean(i) ? getLong(i) : with);
        }

        if (s > r) {
            longs.fill(r, s, with);
        }

        return longs.toSeries();
    }

    private Series<Long> nullify(BooleanSeries condition) {
        int s = size();
        int r = Math.min(s, condition.size());
        ObjectAccumulator<Long> values = new ObjectAccumulator<>(s);

        for (int i = 0; i < r; i++) {
            values.add(condition.getBoolean(i) ? null : getLong(i));
        }

        for (int i = r; i < s; i++) {
            values.add(getLong(i));
        }

        return values.toSeries();
    }

    private Series<Long> nullifyNoMatch(BooleanSeries condition) {
        int s = size();
        int r = Math.min(s, condition.size());
        ObjectAccumulator<Long> values = new ObjectAccumulator<>(s);

        for (int i = 0; i < r; i++) {
            values.add(condition.getBoolean(i) ? getLong(i) : null);
        }

        if (s > r) {
            values.fill(r, s, null);
        }

        return values.toSeries();
    }

    @Override
    public BooleanSeries eq(Series<?> another) {
        int s = size();
        int as = another.size();

        if (s != as) {
            throw new IllegalArgumentException("Another Series size " + as + " is not the same as this size " + s);
        }

        BooleanAccumulator bools = new BooleanAccumulator(s);

        if (another instanceof LongSeries) {
            LongSeries anotherLong = (LongSeries) another;

            for (int i = 0; i < s; i++) {
                bools.addBoolean(getLong(i) == anotherLong.getLong(i));
            }
        } else {
            for (int i = 0; i < s; i++) {
                bools.addBoolean(Objects.equals(get(i), another.get(i)));
            }
        }

        return bools.toSeries();
    }

    @Override
    public BooleanSeries ne(Series<?> another) {
        int s = size();
        int as = another.size();

        if (s != as) {
            throw new IllegalArgumentException("Another Series size " + as + " is not the same as this size " + s);
        }

        BooleanAccumulator bools = new BooleanAccumulator(s);
        if (another instanceof LongSeries) {
            LongSeries anotherLong = (LongSeries) another;

            for (int i = 0; i < s; i++) {
                bools.addBoolean(getLong(i) != anotherLong.getLong(i));
            }
        } else {
            for (int i = 0; i < s; i++) {
                bools.addBoolean(!Objects.equals(get(i), another.get(i)));
            }
        }

        return bools.toSeries();
    }

    @Override
    public BooleanSeries isNull() {
        return new FalseSeries(size());
    }

    @Override
    public BooleanSeries isNotNull() {
        return new TrueSeries(size());
    }

    @Override
    public Series<Long> unique() {
        return uniqueLong();
    }

    @Override
    public LongSeries uniqueLong() {
        int size = size();
        if (size < 2) {
            return this;
        }

        LongAccumulator unique = new UniqueLongAccumulator();
        for (int i = 0; i < size; i++) {
            unique.add(get(i));
        }

        return unique.size() < size() ? unique.toSeries() : this;
    }

    @Override
    public DataFrame valueCounts() {
        return ValueCounts.valueCountsNoNulls(this);
    }

    // TODO: some optimized version of "primitive" group by ...

    @Override
    public SeriesGroupBy<Long> group() {
        return group(l -> l);
    }

    @Override
    public SeriesGroupBy<Long> group(ValueMapper<Long, ?> by) {
        return new SeriesGrouper<>(by).group(this);
    }

    /**
     * @since 0.7
     */
    @Override
    public LongSeries sample(int size) {
        return selectAsLongSeries(Sampler.sampleIndex(size, size()));
    }

    /**
     * @since 0.7
     */
    @Override
    public LongSeries sample(int size, Random random) {
        return selectAsLongSeries(Sampler.sampleIndex(size, size(), random));
    }

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}
