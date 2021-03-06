package com.nhl.dflib.accumulator;

/**
 * @since 0.8
 */
public class IntHolder implements ValueHolder<Integer> {

    private int v;

    @Override
    public Integer get() {
        return v;
    }

    @Override
    public void set(Integer v) {
        this.v = v != null ? v : 0;
    }

    @Override
    public void store(Accumulator<Integer> accumulator) {
        accumulator.addInt(v);
    }

    @Override
    public void store(int pos, Accumulator<Integer> accumulator) {
        accumulator.setInt(pos, v);
    }

    @Override
    public int getInt() {
        return v;
    }

    @Override
    public void setInt(int v) {
        this.v = v;
    }
}
