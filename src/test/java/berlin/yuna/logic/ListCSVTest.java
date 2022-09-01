package berlin.yuna.logic;

import berlin.yuna.model.CsvRow;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static berlin.yuna.logic.CsvReaderTest.EXAMPLE_CSV;
import static berlin.yuna.logic.CsvReaderTest.validateExampleCsv;
import static berlin.yuna.logic.ListCSV.listCSV;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@Tag("UnitTest")
class ListCSVTest {

    @Test
    void listCsvTest() {
        validateExampleCsv(ListCSV.listCSV(EXAMPLE_CSV));
    }

    @Test
    void listCsvTestWithLineLimit() {
        final List<CsvRow> csv = ListCSV.listCSV(EXAMPLE_CSV);
        final List<CsvRow> csvCustom = ListCSV.listCSV(EXAMPLE_CSV, 1);
        assertThat(csv, hasSize(10));
        assertThat(csvCustom, hasSize(9));
    }

    @Test
    void listCsvTestWithLineLimitAndSeparator() {
        final List<CsvRow> csv = ListCSV.listCSV(EXAMPLE_CSV);
        final List<CsvRow> csvCustom = ListCSV.listCSV(EXAMPLE_CSV, 1, ';');
        assertThat(csv, hasSize(10));
        assertThat(csvCustom, hasSize(10)); //10 cause of wrong separator will count the empty line
    }

    @Test
    void listCsvTestWithLineLimitAndAscii() {
        final List<CsvRow> csv = ListCSV.listCSV(EXAMPLE_CSV);
        final List<CsvRow> csvCustom = ListCSV.listCSV(EXAMPLE_CSV, 1, US_ASCII);
        assertThat(csv, hasSize(10));
        assertThat(csvCustom, hasSize(9));
    }

    @Test
    void listCsvTestWithLineLimitAndAsciiAndUnzip() {
        final List<CsvRow> csv = ListCSV.listCSV(EXAMPLE_CSV);
        final List<CsvRow> csvCustom = listCSV(EXAMPLE_CSV, 1, US_ASCII, true);
        assertThat(csv, hasSize(10));
        assertThat(csvCustom, hasSize(9));
    }

    @Test
    void listCsvTestWithLineLimitAndAsciiAndUnzipAndSepDetection() {
        final List<CsvRow> csv = ListCSV.listCSV(EXAMPLE_CSV);
        final List<CsvRow> csvCustom = ListCSV.listCSV(EXAMPLE_CSV, 1, US_ASCII, true, true);
        assertThat(csv, hasSize(10));
        assertThat(csvCustom, hasSize(9));
    }
}
