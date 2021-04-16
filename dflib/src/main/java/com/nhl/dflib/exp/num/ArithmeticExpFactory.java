package com.nhl.dflib.exp.num;

import com.nhl.dflib.exp.Exp;
import com.nhl.dflib.exp.NumericExp;

import java.util.HashMap;
import java.util.Map;

/**
 * @since 0.11
 */
public abstract class ArithmeticExpFactory {

    protected static final Map<Class<? extends Number>, Integer> typeConversionRank;
    protected static final Map<Class<? extends Number>, ArithmeticExpFactory> factories;

    static {
        typeConversionRank = new HashMap<>();
        typeConversionRank.put(Double.class, 1);
        typeConversionRank.put(Float.class, 2);
        typeConversionRank.put(Long.class, 3);
        typeConversionRank.put(Integer.class, 4);
        typeConversionRank.put(Short.class, 5);
        typeConversionRank.put(Byte.class, 6);

        factories = new HashMap<>();
        factories.put(Double.class, new DoubleExpFactory());
        factories.put(Double.TYPE, factories.get(Double.class));

        factories.put(Integer.class, new IntExpFactory());
        factories.put(Integer.TYPE, factories.get(Integer.class));

        factories.put(Long.class, new LongExpFactory());
        factories.put(Long.TYPE, factories.get(Long.class));
    }

    public abstract NumericExp<?> plus(Exp<? extends Number> left, Exp<? extends Number> right);

    public abstract NumericExp<?> minus(Exp<? extends Number> left, Exp<? extends Number> right);

    public abstract NumericExp<?> multiply(Exp<? extends Number> left, Exp<? extends Number> right);

    public abstract NumericExp<?> divide(Exp<? extends Number> left, Exp<? extends Number> right);

    public static ArithmeticExpFactory factory(Exp<? extends Number> left, Exp<? extends Number> right) {
        return factory(left.getType(), right.getType());
    }

    public static ArithmeticExpFactory factory(Class<? extends Number> left, Class<? extends Number> right) {

        Class<? extends Number> type = resultType(left, right);

        ArithmeticExpFactory factory = factories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Unsupported arithmetic type: " + type);
        }

        return factory;
    }

    protected static Class<? extends Number> resultType(Class<? extends Number> left, Class<? extends Number> right) {

        Integer lr = typeConversionRank.get(left);
        if (lr == null) {
            throw new IllegalArgumentException("Unsupported arithmetic type: " + left);
        }

        Integer rr = typeConversionRank.get(right);
        if (rr == null) {
            throw new IllegalArgumentException("Unsupported arithmetic type: " + right);
        }

        // widening conversion that matches standard Java primitive arithmetics
        return lr.compareTo(rr) < 0 ? left : right;
    }
}
