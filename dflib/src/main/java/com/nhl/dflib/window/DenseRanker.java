package com.nhl.dflib.window;

import com.nhl.dflib.DataFrame;
import com.nhl.dflib.sort.IntComparator;

/**
 * @since 0.8
 */
public class DenseRanker extends Ranker {

    public DenseRanker(IntComparator sorter) {
        super(sorter);
    }

    @Override
    protected RankResolver createRankResolver(DataFrame dataFrame, int[] rank) {
        return (i, row, prow) -> sorter.compare(row, prow) == 0 ? rank[prow] : rank[prow] + 1;
    }
}
