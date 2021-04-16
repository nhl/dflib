package com.nhl.dflib.exp;

import com.nhl.dflib.DataFrame;
import com.nhl.dflib.Series;
import com.nhl.dflib.exp.condition.AndCondition;
import com.nhl.dflib.exp.condition.BooleanColumn;
import com.nhl.dflib.exp.condition.OrCondition;
import com.nhl.dflib.exp.num.DoubleColumn;
import com.nhl.dflib.exp.num.IntColumn;
import com.nhl.dflib.exp.num.LongColumn;

import java.util.Objects;

/**
 * @since 0.11
 */
public interface Exp<V> {

    static IntColumn $int(String name) {
        return new IntColumn(name);
    }

    static LongColumn $long(String name) {
        return new LongColumn(name);
    }

    static DoubleColumn $double(String name) {
        return new DoubleColumn(name);
    }

    // TODO: inconsistency - unlike numeric columns that support nulls, BooleanColumn is a "Condition",
    //  that can have no nulls, and will internally convert all nulls to "false"..
    //  Perhaps we need a distinction between a "condition" and a "boolean value expression"?
    static BooleanColumn $bool(String name) {
        return new BooleanColumn(name);
    }

    static StringColumn $str(String name) {
        return new StringColumn(name);
    }

    static Condition $or(Condition... conditions) {
        return new OrCondition(conditions);
    }

    static Condition $and(Condition... conditions) {
        return new AndCondition(conditions);
    }

    String getName();

    Class<V> getType();

    Series<V> eval(DataFrame df);

    /**
     * Creates a new expression by renaming the current expression.
     */
    default Exp<V> named(String name) {
        Objects.requireNonNull(name, "Null 'name'");
        return name.equals(getName()) ? this : new RenamedExp<>(name, this);
    }
}
