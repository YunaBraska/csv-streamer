package berlin.yuna.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static berlin.yuna.model.CsvRow.csvRowArrayOf;
import static berlin.yuna.model.CsvRow.csvRowListOf;
import static berlin.yuna.model.CsvRow.csvRowOf;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("UnitTest")
class CsvRowTest {

    @Test
    void CsvRowCreationTest() {
        final CsvRow csvRow = csvRowOf("column 1, column 2, column 3");
        final CsvRow csvRowStrings = csvRowOf("column 1", "column 2", "column 3");
        final CsvRow csvRowArray = csvRowOf(csvRow.toArray());
        final CsvRow csvRowList = csvRowOf(csvRow.toList());
        assertThat(csvRow, is(equalTo(csvRowArray)));
        assertThat(csvRow, is(equalTo(csvRowList)));
        assertThat(csvRow, is(equalTo(csvRowStrings)));
        assertThat(csvRow.hashCode(), is(not(0)));
    }

    @Test
    void CsvRowCollectionCreationTest() {
        final List<String> line1 = asList("column 1", "column 2", " column 3");
        final List<String> line2 = asList("column 4", "column 5", " column 6");

        final List<List<String>> list = new ArrayList<>();
        list.add(line1);
        list.add(line2);

        final List<String[]> array = new ArrayList<>();
        array.add(line1.toArray(new String[0]));
        array.add(line2.toArray(new String[0]));

        final List<CsvRow> csvRow = csvRowListOf(list);
        final List<CsvRow> csvRowArray = csvRowArrayOf(array);
        assertThat(csvRow, is(equalTo(csvRowArray)));
        assertThat(csvRow.get(0), is(equalTo(line1)));
        assertThat(csvRow.get(1), is(equalTo(line2)));
        assertThat(csvRow.hashCode(), is(not(0)));
    }

    @Test
    void CsvRowGetterTest() {
        final CsvRow csvRow = csvRowOf("column 1, column 2, column 3");
        assertThat(csvRow.get(0), is(equalTo("column 1")));
        assertThat(csvRow.get(1), is(equalTo("column 2")));
        assertThat(csvRow.get(2), is(equalTo("column 3")));
        assertThat(csvRow.get(3), is(nullValue()));

        assertThat(csvRow.getOpt(0), is(equalTo(Optional.of("column 1"))));
        assertThat(csvRow.getOpt(1), is(equalTo(Optional.of("column 2"))));
        assertThat(csvRow.getOpt(2), is(equalTo(Optional.of("column 3"))));
        assertThat(csvRow.getOpt(3), is(equalTo(Optional.empty())));
    }

    @Test
    void CsvRowSetterTest() {
        final CsvRow csvRow = csvRowOf("column 1, column 2, column 3");
        extractedUnsupportedException(() -> csvRow.set(1, "set"));
        extractedUnsupportedException(() -> csvRow.add(1, "add"));
        extractedUnsupportedException(() -> csvRow.add("add"));
        extractedUnsupportedException(() -> csvRow.addAll(1, asList("add", "all")));
        extractedUnsupportedException(() -> csvRow.addAll(asList("add", "all")));
        extractedUnsupportedException(() -> csvRow.remove("remove"));
        extractedUnsupportedException(() -> csvRow.remove(1));
        extractedUnsupportedException(() -> csvRow.removeAll(asList("remove", "all")));
        extractedUnsupportedException(() -> csvRow.retainAll(asList("remove", "all")));
        extractedUnsupportedException(() -> csvRow.removeIf(String::isEmpty));
        extractedUnsupportedException(() -> csvRow.replaceAll(String::toUpperCase));
        extractedUnsupportedException(() -> csvRow.removeRange(1, 2));
        extractedUnsupportedException(csvRow::clear);

    }

    private static void extractedUnsupportedException(final Executable executable) {
        final Exception directory = assertThrows(UnsupportedOperationException.class, executable);
        assertThat(directory.getMessage(), is(equalTo("[" + CsvRow.class.getSimpleName() + "] is immutable")));
    }
}
