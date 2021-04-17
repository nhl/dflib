package com.nhl.dflib.exp.num;

import com.nhl.dflib.BooleanSeries;
import com.nhl.dflib.IntSeries;
import com.nhl.dflib.Series;
import com.nhl.dflib.exp.Exp;
import com.nhl.dflib.exp.condition.BinaryCondition;

import java.util.function.BiFunction;

/**
 * @since 0.11
 */
public class IntBinaryCondition extends BinaryCondition<Integer, Integer> {

    private final BiFunction<IntSeries, IntSeries, BooleanSeries> primitiveOp;

    public IntBinaryCondition(
            String name,
            Exp<Integer> left,
            Exp<Integer> right,
            BiFunction<Series<Integer>, Series<Integer>, BooleanSeries> op,
            BiFunction<IntSeries, IntSeries, BooleanSeries> primitiveOp) {

        super(name, left, right, op);
        this.primitiveOp = primitiveOp;
    }

    @Override
    protected BooleanSeries eval(Series<Integer> ls, Series<Integer> rs) {
        return (ls instanceof IntSeries && rs instanceof IntSeries)
                ? primitiveOp.apply((IntSeries) ls, (IntSeries) rs)
                : super.eval(ls, rs);
    }
}
