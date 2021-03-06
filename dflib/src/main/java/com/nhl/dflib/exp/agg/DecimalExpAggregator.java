package com.nhl.dflib.exp.agg;

import com.nhl.dflib.*;
import com.nhl.dflib.series.SingleValueSeries;

import java.math.BigDecimal;
import java.util.function.Function;

/**
 * @since 0.11
 */
public class DecimalExpAggregator implements DecimalExp {

    private final Exp<BigDecimal> exp;
    private final Function<Series<BigDecimal>, BigDecimal> aggregator;

    public DecimalExpAggregator(Exp<BigDecimal> exp, Function<Series<BigDecimal>, BigDecimal> aggregator) {
        this.exp = exp;
        this.aggregator = aggregator;
    }

    @Override
    public String toString() {
        return toQL();
    }

    @Override
    public Class<BigDecimal> getType() {
        return BigDecimal.class;
    }

    @Override
    public Series<BigDecimal> eval(DataFrame df) {
        BigDecimal val = aggregator.apply(exp.eval(df));
        return new SingleValueSeries<>(val, 1);
    }

    @Override
    public Series<BigDecimal> eval(Series<?> s) {
        BigDecimal val = aggregator.apply(exp.eval(s));
        return new SingleValueSeries<>(val, 1);
    }

    @Override
    public String toQL() {
        return exp.toQL();
    }

    @Override
    public String toQL(DataFrame df) {
        return exp.toQL(df);
    }
}
