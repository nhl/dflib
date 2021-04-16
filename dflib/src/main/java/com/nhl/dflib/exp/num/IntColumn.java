package com.nhl.dflib.exp.num;

import com.nhl.dflib.exp.ColumnExp;
import com.nhl.dflib.exp.NumericExp;

/**
 * @since 0.11
 */
public class IntColumn extends ColumnExp<Integer> implements NumericExp<Integer> {

    public IntColumn(String name) {
        super(name, Integer.class);
    }
}
