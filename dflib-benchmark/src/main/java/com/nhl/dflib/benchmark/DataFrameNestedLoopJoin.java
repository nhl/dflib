package com.nhl.dflib.benchmark;

import com.nhl.dflib.DataFrame;
import com.nhl.dflib.benchmark.data.RowByRowSequence;
import com.nhl.dflib.join.JoinPredicate;
import com.nhl.dflib.join.JoinType;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(2)
@State(Scope.Thread)
public class DataFrameNestedLoopJoin {

    @Param("5000")
    public int rows;

    private DataFrame df1;
    private DataFrame df2;

    @Setup
    public void setUp() {
        df1 = RowByRowSequence.dfWithMixedData(rows);
        df2 = RowByRowSequence.dfWithMixedData(rows);
    }

    @Benchmark
    public Object leftJoin() {
        return df1
                .join(df2, JoinPredicate.on("c2", "c0"), JoinType.left)
                .materialize().iterator();
    }

    @Benchmark
    public Object rightJoin() {
        return df1
                .join(df2, JoinPredicate.on("c2", "c0"), JoinType.right)
                .materialize().iterator();
    }

    @Benchmark
    public Object innerJoin() {
        return df1
                .join(df2, JoinPredicate.on("c2", "c0"), JoinType.inner)
                .materialize().iterator();
    }

    @Benchmark
    public Object fullJoin() {
        return df1
                .join(df2, JoinPredicate.on("c2", "c0"), JoinType.inner)
                .materialize().iterator();
    }

}
