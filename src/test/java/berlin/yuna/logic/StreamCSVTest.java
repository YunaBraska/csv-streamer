package berlin.yuna.logic;

import berlin.yuna.model.CsvRow;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static berlin.yuna.logic.CsvReaderTest.EXAMPLE_CSV;
import static berlin.yuna.logic.CsvReaderTest.validateExampleCsv;
import static berlin.yuna.logic.StreamCSV.streamCSV;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@Tag("UnitTest")
class StreamCSVTest {

    @Test
    void listCsvTest() {
        final List<CsvRow> csv = new ArrayList<>();
        try (final Stream<CsvRow> streamCSV = streamCSV(EXAMPLE_CSV)) {
            streamCSV.forEach(csv::add);
        }
        validateExampleCsv(csv);
    }

    @Test
    void listCsvTestWithLineLimit() {
        final List<CsvRow> csv = new ArrayList<>();
        final List<CsvRow> csvCustom = new ArrayList<>();
        try (final Stream<CsvRow> streamCSV = streamCSV(EXAMPLE_CSV)) {
            streamCSV.forEach(csv::add);
        }
        try (final Stream<CsvRow> streamCSV = streamCSV(EXAMPLE_CSV, 1)) {
            streamCSV.forEach(csvCustom::add);
        }
        assertThat(csv, hasSize(10));
        assertThat(csvCustom, hasSize(9));
    }

    @Test
    void listCsvTestWithLineLimitAndSeparator() {
        final List<CsvRow> csv = new ArrayList<>();
        final List<CsvRow> csvCustom = new ArrayList<>();
        try (final Stream<CsvRow> streamCSV = streamCSV(EXAMPLE_CSV)) {
            streamCSV.forEach(csv::add);
        }
        try (final Stream<CsvRow> streamCSV = streamCSV(EXAMPLE_CSV, 1, ';')) {
            streamCSV.forEach(csvCustom::add);
        }
        assertThat(csv, hasSize(10));
        assertThat(csvCustom, hasSize(10)); //10 cause of wrong separator will count the empty line
    }

    @Test
    void listCsvTestWithLineLimitAndAscii() {
        final List<CsvRow> csv = new ArrayList<>();
        final List<CsvRow> csvCustom = new ArrayList<>();
        try (final Stream<CsvRow> streamCSV = streamCSV(EXAMPLE_CSV)) {
            streamCSV.forEach(csv::add);
        }
        try (final Stream<CsvRow> streamCSV = streamCSV(EXAMPLE_CSV, 1, US_ASCII)) {
            streamCSV.forEach(csvCustom::add);
        }
        assertThat(csv, hasSize(10));
        assertThat(csvCustom, hasSize(9));
    }

    @Test
    void listCsvTestWithLineLimitAndAsciiAndUnzip() {
        final List<CsvRow> csv = new ArrayList<>();
        final List<CsvRow> csvCustom = new ArrayList<>();
        try (final Stream<CsvRow> streamCSV = streamCSV(EXAMPLE_CSV)) {
            streamCSV.forEach(csv::add);
        }
        try (final Stream<CsvRow> streamCSV = streamCSV(EXAMPLE_CSV, 1, US_ASCII, true)) {
            streamCSV.forEach(csvCustom::add);
        }
        assertThat(csv, hasSize(10));
        assertThat(csvCustom, hasSize(9));
    }

    @Test
    void listCsvTestWithLineLimitAndAsciiAndUnzipAndSepDetection() {
        final List<CsvRow> csv = new ArrayList<>();
        final List<CsvRow> csvCustom = new ArrayList<>();
        try (final Stream<CsvRow> streamCSV = streamCSV(EXAMPLE_CSV)) {
            streamCSV.forEach(csv::add);
        }
        try (final Stream<CsvRow> streamCSV = streamCSV(EXAMPLE_CSV, 1, US_ASCII, true, true)) {
            streamCSV.forEach(csvCustom::add);
        }
        assertThat(csv, hasSize(10));
        assertThat(csvCustom, hasSize(9));
    }
}
