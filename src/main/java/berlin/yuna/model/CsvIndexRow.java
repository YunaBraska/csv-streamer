package berlin.yuna.model;

import java.util.Collection;

public class CsvIndexRow extends CsvRow {

    private final long index;

    public static CsvIndexRow csvIndexRowOf(final long index, final CsvRow row) {
        return new CsvIndexRow(index, row);
    }

    public CsvIndexRow(final long index, final Collection<String> collection) {
        super(collection);
        this.index = index;
    }

    /**
     * @return index of current row
     */
    public long index() {
        return index;
    }

    public CsvRow toCsvRow() {
        return this;
    }

    @Override
    public boolean equals(final Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
