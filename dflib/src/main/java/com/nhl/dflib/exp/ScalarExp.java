package com.nhl.dflib.exp;

import com.nhl.dflib.DataFrame;
import com.nhl.dflib.Exp;
import com.nhl.dflib.Series;

import java.util.Objects;

/**
 * A unary expression with a scalar argument.
 *
 * @since 0.11
 */
public abstract class ScalarExp<T> implements Exp<T> {

    private final Class<T> type;
    private final T value;

    public ScalarExp(T value, Class<T> type) {
        this.value = value;
        this.type = Objects.requireNonNull(type);
    }

    @Override
    public String toString() {
        return toQL();
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public String toQL() {
        boolean quotes = value != null && !(value instanceof Number);
        String unquoted = String.valueOf(value);
        return quotes ? "'" + unquoted + "'" : unquoted;
    }

    @Override
    public String toQL(DataFrame df) {
        return toQL();
    }

    @Override
    public Series<T> eval(DataFrame df) {
        return doEval(df.height(), value);
    }

    @Override
    public Series<T> eval(Series<?> s) {
        return doEval(s.size(), value);
    }

    protected abstract Series<T> doEval(int height, T value);
}
