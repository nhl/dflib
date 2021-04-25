package com.nhl.dflib.benchmark.speed;

import com.nhl.dflib.DataFrame;
import com.nhl.dflib.Series;
import com.nhl.dflib.benchmark.ValueMaker;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(2)
@State(Scope.Thread)
public class DataFrameConcat {

    @Param("1000000")
    public int rows;

    private DataFrame df1;
    private DataFrame df2;

    @Setup
    public void setUp() {
        String string =
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis vulputate sollicitudin ligula sit amet ornare.";

        Series<Integer> c10 = ValueMaker.intSeq().series(rows + 5);
        Series<String> c11 = ValueMaker.stringSeq().series(rows + 5);
        Series<Integer> c12 = ValueMaker.randomIntSeq((rows + 5) / 2).series(rows + 5);
        Series<String> c13 = ValueMaker.constStringSeq(string).series(rows + 5);

        df1 = DataFrame.newFrame("c0", "c1", "c2", "c3")
                .columns(c10, c11, c12, c13);

        Series<Integer> c20 = ValueMaker.intSeq().series(rows - 5);
        Series<String> c21 = ValueMaker.stringSeq().series(rows - 5);
        Series<Integer> c22 = ValueMaker.randomIntSeq((rows - 5) / 2).series(rows - 5);
        Series<String> c23 = ValueMaker.constStringSeq(string).series(rows - 5);

        df2 = DataFrame.newFrame("c0", "c1", "c2", "c3")
                .columns(c20, c21, c22, c23);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public Object hConcat() {
        return df1
                .hConcat(df2)
                .materialize().iterator();
    }

    @Benchmark
    public Object vConcat() {
        return df1
                .vConcat(df2)
                .materialize().iterator();
    }
}
