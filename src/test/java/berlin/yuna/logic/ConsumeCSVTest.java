package berlin.yuna.logic;

import berlin.yuna.model.CsvIndexRow;
import berlin.yuna.model.CsvRow;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static berlin.yuna.logic.ConsumeCSV.consumeCsv;
import static berlin.yuna.logic.CsvReaderTest.EXAMPLE_CSV;
import static berlin.yuna.logic.CsvReaderTest.validateExampleCsv;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@Tag("UnitTest")
class ConsumeCSVTest {

    @Test
    void listCsvTest() {
        final List<CsvRow> csv = new ArrayList<>();
        consumeCsv(EXAMPLE_CSV, csv::add);
        validateExampleCsv(csv);
    }

    @Test
    void listCsvTestWithLineLimit() {
        final List<CsvRow> csv = new ArrayList<>();
        final List<CsvIndexRow> csvCustom = new ArrayList<>();
        consumeCsv(EXAMPLE_CSV, csv::add);
        consumeCsv(EXAMPLE_CSV, csvCustom::add, 1);
        assertThat(csv, hasSize(10));
        assertThat(csvCustom, hasSize(9));
    }

    @Test
    void listCsvTestWithLineLimitAndSeparator() {
        final List<CsvRow> csv = new ArrayList<>();
        final List<CsvIndexRow> csvCustom = new ArrayList<>();
        consumeCsv(EXAMPLE_CSV, csv::add);
        consumeCsv(EXAMPLE_CSV, csvCustom::add, 1, ';');
        assertThat(csv, hasSize(10));
        assertThat(csvCustom, hasSize(10)); //10 cause of wrong separator will count the empty line
    }

    @Test
    void listCsvTestWithLineLimitAndAscii() {
        final List<CsvRow> csv = new ArrayList<>();
        final List<CsvIndexRow> csvCustom = new ArrayList<>();
        consumeCsv(EXAMPLE_CSV, csv::add);
        consumeCsv(EXAMPLE_CSV, csvCustom::add, 1, US_ASCII);
        assertThat(csv, hasSize(10));
        assertThat(csvCustom, hasSize(9));
    }

    @Test
    void listCsvTestWithLineLimitAndAsciiAndUnzip() {
        final List<CsvRow> csv = new ArrayList<>();
        final List<CsvIndexRow> csvCustom = new ArrayList<>();
        consumeCsv(EXAMPLE_CSV, csv::add);
        consumeCsv(EXAMPLE_CSV, csvCustom::add, 1, US_ASCII, true);
        assertThat(csv, hasSize(10));
        assertThat(csvCustom, hasSize(9));
    }

    @Test
    void listCsvTestWithLineLimitAndAsciiAndUnzipAndSepDetection() {
        final List<CsvRow> csv = new ArrayList<>();
        final List<CsvIndexRow> csvCustom = new ArrayList<>();
        consumeCsv(EXAMPLE_CSV, csv::add);
        consumeCsv(EXAMPLE_CSV, csvCustom::add, 1, US_ASCII, true, true);
        assertThat(csv, hasSize(10));
        assertThat(csvCustom, hasSize(9));
    }
}
