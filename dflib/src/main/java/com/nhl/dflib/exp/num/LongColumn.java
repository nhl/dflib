package com.nhl.dflib.exp.num;

import com.nhl.dflib.exp.ColumnExp;
import com.nhl.dflib.exp.NumericExp;

/**
 * @since 0.11
 */
public class LongColumn extends ColumnExp<Long> implements NumericExp<Long> {

    public LongColumn(String name) {
        super(name, Long.class);
    }
}
