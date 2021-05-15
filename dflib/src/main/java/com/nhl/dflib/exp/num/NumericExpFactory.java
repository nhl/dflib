package com.nhl.dflib.exp.num;

import com.nhl.dflib.Condition;
import com.nhl.dflib.DecimalExp;
import com.nhl.dflib.Exp;
import com.nhl.dflib.NumericExp;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * An abstraction over various numeric types allowing to mix and match different types in a given operation.
 *
 * @since 0.11
 */
public abstract class NumericExpFactory {

    protected static final Map<Class<? extends Number>, Integer> typeConversionRank;
    protected static final Map<Class<? extends Number>, NumericExpFactory> factories;
    protected static final DecimalExpFactory decimalFactory;

    static {

        decimalFactory = new DecimalExpFactory();

        typeConversionRank = new HashMap<>();

        typeConversionRank.put(BigDecimal.class, 0);

        typeConversionRank.put(Double.class, 1);
        typeConversionRank.put(Double.TYPE, 1);

        typeConversionRank.put(Float.class, 2);
        typeConversionRank.put(Float.TYPE, 2);

        typeConversionRank.put(Long.class, 3);
        typeConversionRank.put(Long.TYPE, 3);

        typeConversionRank.put(Integer.class, 4);
        typeConversionRank.put(Integer.TYPE, 4);

        // we don't have factories for these yet
        // typeConversionRank.put(Short.class, 5);
        // typeConversionRank.put(Byte.class, 6);

        factories = new HashMap<>();

        factories.put(BigDecimal.class, decimalFactory);

        factories.put(Double.class, new DoubleExpFactory());
        factories.put(Double.TYPE, factories.get(Double.class));

        factories.put(Integer.class, new IntExpFactory());
        factories.put(Integer.TYPE, factories.get(Integer.class));

        factories.put(Long.class, new LongExpFactory());
        factories.put(Long.TYPE, factories.get(Long.class));
    }

    /**
     * Provides direct access to the DecimalExpFactory, that can be used to return {@link DecimalExp} instead of
     * {@link NumericExp}.
     */
    public static DecimalExpFactory decimalFactory() {
        return decimalFactory;
    }

    public static NumericExpFactory factory(Exp<? extends Number> exp) {
        return factory(exp.getType());
    }

    public static NumericExpFactory factory(Class<? extends Number> type) {

        NumericExpFactory factory = factories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Unsupported arithmetic type: " + type);
        }

        return factory;
    }

    public static NumericExpFactory factory(Exp<? extends Number> left, Exp<? extends Number> right) {
        return factory(left.getType(), right.getType());
    }

    public static NumericExpFactory factory(Class<? extends Number> left, Class<? extends Number> right) {

        Class<? extends Number> type = factoryType(left, right);

        NumericExpFactory factory = factories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Unsupported arithmetic type: " + type);
        }

        return factory;
    }

    protected static Class<? extends Number> factoryType(Class<? extends Number> left, Class<? extends Number> right) {

        Integer lr = typeConversionRank.get(left);
        if (lr == null) {
            throw new IllegalArgumentException("Unsupported numeric type: " + left);
        }

        Integer rr = typeConversionRank.get(right);
        if (rr == null) {
            throw new IllegalArgumentException("Unsupported numeric type: " + right);
        }

        // widening conversion that matches standard Java primitive arithmetics
        return lr.compareTo(rr) < 0 ? left : right;
    }

    public abstract NumericExp<?> add(Exp<? extends Number> left, Exp<? extends Number> right);

    public abstract NumericExp<?> sub(Exp<? extends Number> left, Exp<? extends Number> right);

    public abstract NumericExp<?> mul(Exp<? extends Number> left, Exp<? extends Number> right);

    public abstract NumericExp<?> div(Exp<? extends Number> left, Exp<? extends Number> right);

    public abstract NumericExp<?> mod(Exp<? extends Number> left, Exp<? extends Number> right);

    public abstract NumericExp<?> sum(Exp<? extends Number> exp);

    public abstract NumericExp<?> min(Exp<? extends Number> exp);

    public abstract NumericExp<?> max(Exp<? extends Number> exp);

    public abstract NumericExp<?> avg(Exp<? extends Number> exp);

    public abstract NumericExp<?> median(Exp<? extends Number> exp);

    public abstract DecimalExp castAsDecimal(NumericExp<?> exp);

    public abstract Condition eq(Exp<? extends Number> left, Exp<? extends Number> right);

    public abstract Condition ne(Exp<? extends Number> left, Exp<? extends Number> right);

    public abstract Condition lt(Exp<? extends Number> left, Exp<? extends Number> right);

    public abstract Condition le(Exp<? extends Number> left, Exp<? extends Number> right);

    public abstract Condition gt(Exp<? extends Number> left, Exp<? extends Number> right);

    public abstract Condition ge(Exp<? extends Number> left, Exp<? extends Number> right);
}
