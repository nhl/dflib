package com.nhl.dflib.exp.num;

import com.nhl.dflib.*;
import com.nhl.dflib.exp.agg.DoubleAggregators;
import com.nhl.dflib.exp.agg.DoubleExpAggregator;

import java.math.BigDecimal;

/**
 * @since 0.11
 */
public class DoubleExpFactory extends NumericExpFactory {

    protected static Exp<Double> cast(Exp<?> exp) {

        // TODO: a map of casting converters

        Class<?> t = exp.getType();
        if (t.equals(Double.class)) {
            return (Exp<Double>) exp;
        }

        if (Number.class.isAssignableFrom(t)) {
            Exp<Number> nExp = (Exp<Number>) exp;
            return DoubleExp1.mapVal("castAsDouble", nExp, Number::doubleValue);
        }

        if (t.equals(String.class)) {
            Exp<String> sExp = (Exp<String>) exp;
            return DoubleExp1.mapVal("castAsDouble", sExp, Double::parseDouble);
        }

        throw new IllegalArgumentException("Expression type '" + t.getName() + "' can't be converted to Double");
    }

    @Override
    public NumExp<?> add(Exp<? extends Number> left, Exp<? extends Number> right) {
        return DoubleExp2.mapVal("+",
                cast(left),
                cast(right),
                (n1, n2) -> n1 + n2,
                DoubleSeries::add);
    }

    @Override
    public NumExp<?> sub(Exp<? extends Number> left, Exp<? extends Number> right) {
        return DoubleExp2.mapVal("-",
                cast(left),
                cast(right),
                (n1, n2) -> n1 - n2,
                DoubleSeries::sub);
    }

    @Override
    public NumExp<?> mul(Exp<? extends Number> left, Exp<? extends Number> right) {
        return DoubleExp2.mapVal("*",
                cast(left),
                cast(right),
                (n1, n2) -> n1 * n2,
                DoubleSeries::mul);
    }

    @Override
    public NumExp<?> div(Exp<? extends Number> left, Exp<? extends Number> right) {
        return DoubleExp2.mapVal("/",
                cast(left),
                cast(right),
                (n1, n2) -> n1 / n2,
                DoubleSeries::div);
    }

    @Override
    public NumExp<?> mod(Exp<? extends Number> left, Exp<? extends Number> right) {
        return DoubleExp2.mapVal("%",
                cast(left),
                cast(right),
                (n1, n2) -> n1 % n2,
                DoubleSeries::mod);
    }

    @Override
    public NumExp<?> abs(Exp<? extends Number> exp) {
        return DoubleExp1.mapVal("abs", cast(exp), Math::abs);
    }

    @Override
    public DecimalExp castAsDecimal(NumExp<?> exp) {
        return DecimalExp1.mapVal("castAsDecimal", cast(exp), BigDecimal::valueOf);
    }

    @Override
    public NumExp<Double> sum(Exp<? extends Number> exp) {
        return new DoubleExpAggregator<>("sum", exp, DoubleAggregators::sum);
    }

    @Override
    public NumExp<?> min(Exp<? extends Number> exp) {
        return new DoubleExpAggregator<>("min", exp, DoubleAggregators::min);
    }

    @Override
    public NumExp<?> max(Exp<? extends Number> exp) {
        return new DoubleExpAggregator<>("max", exp, DoubleAggregators::max);
    }

    @Override
    public NumExp<?> avg(Exp<? extends Number> exp) {
        return new DoubleExpAggregator<>("avg", exp, DoubleAggregators::avg);
    }

    @Override
    public NumExp<?> median(Exp<? extends Number> exp) {
        return new DoubleExpAggregator<>("median", exp, DoubleAggregators::median);
    }

    @Override
    public Condition eq(Exp<? extends Number> left, Exp<? extends Number> right) {
        return DoubleCondition2.mapVal("=", cast(left), cast(right), Double::equals, DoubleSeries::eq);
    }

    @Override
    public Condition ne(Exp<? extends Number> left, Exp<? extends Number> right) {
        return DoubleCondition2.mapVal("!=", cast(left), cast(right), (n1, n2) -> !n1.equals(n2), DoubleSeries::ne);
    }

    @Override
    public Condition lt(Exp<? extends Number> left, Exp<? extends Number> right) {
        return DoubleCondition2.mapVal("<", cast(left), cast(right), (n1, n2) -> n1 < n2, DoubleSeries::lt);
    }

    @Override
    public Condition le(Exp<? extends Number> left, Exp<? extends Number> right) {
        return DoubleCondition2.mapVal("<=", cast(left), cast(right), (n1, n2) -> n1 <= n2, DoubleSeries::le);
    }

    @Override
    public Condition gt(Exp<? extends Number> left, Exp<? extends Number> right) {
        return DoubleCondition2.mapVal(">", cast(left), cast(right), (n1, n2) -> n1 > n2, DoubleSeries::gt);
    }

    @Override
    public Condition ge(Exp<? extends Number> left, Exp<? extends Number> right) {
        return DoubleCondition2.mapVal(">=", cast(left), cast(right), (n1, n2) -> n1 >= n2, DoubleSeries::ge);
    }
}
