package com.nhl.dflib.csv.loader;

import com.nhl.dflib.Series;
import com.nhl.dflib.series.builder.Accumulator;
import com.nhl.dflib.series.builder.ValueConverter;
import org.apache.commons.csv.CSVRecord;

/**
 * @since 0.8
 */
public class CsvAccumulatorColumn<T> {

    private ValueConverter<String, T> converter;
    private Accumulator<T> accumulator;
    private int csvColumnPosition;

    public CsvAccumulatorColumn(ValueConverter<String, T> converter, Accumulator<T> accumulator, int csvColumnPosition) {
        this.converter = converter;
        this.accumulator = accumulator;
        this.csvColumnPosition = csvColumnPosition;
    }

    public Series<T> toSeries() {
        return accumulator.toSeries();
    }

    public void add(CSVRecord record) {
        converter.convertAndStore(record.get(csvColumnPosition), accumulator);
    }

    public void add(CsvValueHolderColumn<?>[] values) {
        // values are already converted, so bypassing the converter
        CsvValueHolderColumn vhColumn = values[csvColumnPosition];
        vhColumn.store(accumulator);
    }
}
