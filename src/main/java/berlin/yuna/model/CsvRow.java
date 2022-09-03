package berlin.yuna.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class CsvRow extends ArrayList<String> {

    private static final UnsupportedOperationException IMMUTABLE_EXCEPTION = new UnsupportedOperationException("[" + CsvRow.class.getSimpleName() + "] is immutable");

    /**
     * @param lines list of string to be transformed
     * @return {@link CsvRow} with separate columns
     */
    public static CsvRow csvRowOf(final Collection<String> lines) {
        return new CsvRow(lines);
    }

    /**
     * @param lines list of list to be transformed
     * @return {@link List<CsvRow>} with separate columns
     */
    public static List<CsvRow> csvRowListOf(final Collection<List<String>> lines) {
        return lines.stream().map(CsvRow::csvRowOf).collect(Collectors.toList());
    }

    /**
     * @param lines list of array to be transformed
     * @return {@link List<CsvRow>} with separate columns
     */
    public static List<CsvRow> csvRowArrayOf(final Collection<String[]> lines) {
        return lines.stream().map(CsvRow::csvRowOf).collect(Collectors.toList());
    }

    /**
     * @param row input array to be transformed
     * @return {@link CsvRow} with separate columns
     */
    public static CsvRow csvRowOf(final String... row) {
        return new CsvRow(Arrays.asList(row));
    }

    /**
     * @param row input string to be split
     * @return {@link CsvRow} with separate columns
     */
    public static CsvRow csvRowOf(final String row) {
        return csvRowOf(row, validateSeparator(new char[]{}));
    }

    /**
     * @param row        input string to be split
     * @param separators delimiter to split the input
     * @return {@link CsvRow} with separate columns
     */
    public static CsvRow csvRowOf(final String row, final char... separators) {
        final List<String> columns = new ArrayList<>();
        splitRow(row, columns::add, separators);
        return csvRowOf(columns);
    }


    /**
     * Validates the separator
     *
     * @param separators separator to validate
     * @return separator for parameter or fallback ','
     */
    public static char[] validateSeparator(final char[] separators) {
        return separators.length > 0 ? separators : new char[]{','};
    }

    /**
     * @return {@link List<String>} from the {@link CsvRow}
     */
    public List<String> toList() {
        return new ArrayList<>((this));
    }

    /**
     * @param collection collection to be transformed to an array
     * @return array from the collection. Null fallback = empty array
     */
    public static String[] toArray(final Collection<String> collection) {
        return collection == null ? new String[0] : collection.toArray(new String[0]);
    }

    /**
     * Returns the column value at the specified position in this {@link CsvRow}.
     *
     * @param index index of the column value to return
     * @return the column value at the specified position in this list - fallback = null
     */
    @Override
    public String get(final int index) {
        return index < 0 || index >= this.size() ? null : super.get(index);
    }

    /**
     * Returns the column value at the specified position in this {@link CsvRow}.
     *
     * @param index index of the column value to return
     * @return the column value as {@link Optional<String>} at the specified position in this list - fallback = {@link Optional<String>#empty()}
     */
    public Optional<String> getOpt(final int index) {
        return Optional.ofNullable(this.get(index));
    }

    /**
     * Returns the column value at the specified position in this {@link CsvRow}.
     *
     * @param index      index of the column value to return
     * @param separators Splits the CSV rows at the given separator
     * @return the column value as {@link CsvRow} at the specified position - fallback = null
     */
    public CsvRow get(final int index, final char... separators) {
        return index < 0 || index >= this.size() ? null : csvRowOf(super.get(index), separators);
    }

    /**
     * Returns the column value at the specified position in this {@link CsvRow}.
     *
     * @param index      index of the column value to return
     * @param separators Splits the CSV rows at the given separator
     * @return the column value as {@link Optional<CsvRow>} at the specified position - {@link Optional<CsvRow>#empty()}
     */
    public Optional<CsvRow> getOpt(final int index, final char... separators) {
        return Optional.ofNullable(this.get(index, separators));
    }

    /**
     * Not implemented cause of immutable object
     */
    @Override
    public String set(final int index, final String element) {
        throw IMMUTABLE_EXCEPTION;
    }

    /**
     * Not implemented cause of immutable object
     */
    @Override
    public boolean add(final String element) {
        throw IMMUTABLE_EXCEPTION;
    }

    /**
     * Not implemented cause of immutable object
     */
    @Override
    public void add(final int index, final String element) {
        throw IMMUTABLE_EXCEPTION;
    }

    /**
     * Not implemented cause of immutable object
     */
    @Override
    public boolean addAll(final int index, final Collection<? extends String> collection) {
        throw IMMUTABLE_EXCEPTION;
    }

    /**
     * Not implemented cause of immutable object
     */
    @Override
    public boolean addAll(final Collection<? extends String> collection) {
        throw IMMUTABLE_EXCEPTION;
    }

    /**
     * Not implemented cause of immutable object
     */
    @Override
    public boolean remove(final Object remove) {
        throw IMMUTABLE_EXCEPTION;
    }

    /**
     * Not implemented cause of immutable object
     */
    @Override
    public String remove(final int index) {
        throw IMMUTABLE_EXCEPTION;
    }

    /**
     * Not implemented cause of immutable object
     */
    @Override
    public boolean removeAll(final Collection<?> collection) {
        throw IMMUTABLE_EXCEPTION;
    }

    /**
     * Not implemented cause of immutable object
     */
    @Override
    public boolean removeIf(final Predicate<? super String> filter) {
        throw IMMUTABLE_EXCEPTION;
    }

    /**
     * Not implemented cause of immutable object
     */
    @Override
    public boolean retainAll(final Collection<?> collection) {
        throw IMMUTABLE_EXCEPTION;
    }

    /**
     * Not implemented cause of immutable object
     */
    @Override
    public void replaceAll(final UnaryOperator<String> operator) {
        throw IMMUTABLE_EXCEPTION;
    }

    /**
     * Not implemented cause of immutable object
     */
    @Override
    public void removeRange(final int fromIndex, final int toIndex) {
        throw IMMUTABLE_EXCEPTION;
    }

    /**
     * Not implemented cause of immutable object
     */
    @Override
    public void clear() {
        throw IMMUTABLE_EXCEPTION;
    }

    /**
     * @return array from the collection. Null fallback = empty array
     */
    @Override
    public String[] toArray() {
        return toArray(this);
    }

    protected CsvRow(final Collection<String> collection) {
        super(collection);
    }

    private static void splitRow(final String row, final Consumer<String> result, final char... separators) {
        final char[] splits = validateSeparator(separators);
        int firstQuote = -1;
        int lastQuote = -1;
        int lastSeparator = -1;

        final StringBuilder sb = new StringBuilder();
        final char[] chars = row.toCharArray();
        for (int index = 0; index < chars.length; index++) {
            final char c = chars[index];

            if (isQuote(c)) {
                if (firstQuote == -1) {
                    firstQuote = index;
                } else {
                    lastQuote = index;
                }
            } else if (isSeparator(c, splits)) {
                lastSeparator = index;
            }

            //END COLUMN
            if (lastSeparator != -1 && (firstQuote == -1 || (lastQuote != -1 && lastQuote < lastSeparator))) {
                firstQuote = -1;
                lastQuote = -1;
                lastSeparator = -1;
                result.accept(normalizeColumn(sb.toString()));
                sb.setLength(0);
                continue;
            }

            sb.append(c);
        }
        result.accept(normalizeColumn(sb.toString()));
    }

    private static boolean isQuote(final char c) {
        return isSeparator(c, '\'', '\"');
    }

    private static boolean isSeparator(final char c, final char... separators) {
        for (char s : separators) {
            if (c == s) {
                return true;
            }
        }
        return false;
    }

    private static String replaceStr(final String input, final CharSequence target, final CharSequence replacement) {
        return input.contains(target) ? input.replace(target, replacement) : input;
    }

    //TODO: move logic to parser to avoid the overhead
    private static String normalizeColumn(final String column) {
        String result = removeQuotes(column);
        if (result != null) {
            result = replaceStr(result, "\"\"", "\"");
            result = replaceStr(result, "\\\"", "\"");
            result = replaceStr(result, "''", "'");
            result = replaceStr(result, "\\'", "'");
        }
        return result;
    }

    private static String removeQuotes(final String string) {
        if (string != null) {
            final String input = string.trim();
            if ((input.startsWith("\"") && input.endsWith("\"")) || (input.startsWith("'") && input.endsWith("'"))) {
                return input.substring(1, input.length() - 1).trim();
            }
            return input;
        }
        return null;
    }
}
