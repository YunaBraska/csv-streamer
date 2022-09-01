package berlin.yuna.logic;

import berlin.yuna.model.CsvRow;
import berlin.yuna.model.IoCsvException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static berlin.yuna.logic.CsvReader.csvReader;
import static berlin.yuna.logic.StreamCSV.streamCSV;
import static berlin.yuna.model.CsvRow.csvRowOf;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("UnitTest")
class CsvReaderTest {

    public static final Path EXAMPLE_CSV = Paths.get("test.csv");
    public static final String EXAMPLE_CSV_SEPARATORS = "SeparatorDetection.csv";

    @Test
    void listCsv() {
        validateExampleCsv(csvReader().readAllRows(EXAMPLE_CSV));
    }

    @Test
    void consumeCsvTest() {
        final List<CsvRow> csv = new ArrayList<>();
        csvReader().consume(EXAMPLE_CSV, csv::add);
        validateExampleCsv(csv);
    }

    @Test
    void streamCsvTest() {
        final List<CsvRow> csv = new ArrayList<>();
        try (final Stream<CsvRow> csvRowStream = csvReader().stream(EXAMPLE_CSV)) {
            csvRowStream.forEach(csv::add);
        }
        validateExampleCsv(csv);
    }

    @Test
    void csvFileValidations() throws URISyntaxException {
        final Path userDir = Paths.get(System.getProperty("user.dir"));
        final Path invalidFile = Paths.get("invalidFile");
        final Exception directory = assertThrows(IoCsvException.class, () -> streamCSV(userDir));
        assertThat(directory.getMessage(), is(equalTo("File cannot be a [Directory]")));

        final Exception nullFile = assertThrows(IoCsvException.class, () -> streamCSV(null));
        assertThat(nullFile.getMessage(), is(equalTo("File cannot be [null]")));

        final Exception invalid = assertThrows(IoCsvException.class, () -> streamCSV(invalidFile));
        assertThat(invalid.getMessage(), is(equalTo("File read error [invalidFile]")));

        try (final Stream<CsvRow> stream = streamCSV(EXAMPLE_CSV)) {
            assertThat(stream.collect(Collectors.toList()), is(not(empty())));
        }

        try (final Stream<CsvRow> stream = streamCSV(Paths.get(requireNonNull(this.getClass().getClassLoader().getResource("test.csv")).toURI()))) {
            assertThat(stream.collect(Collectors.toList()), is(not(empty())));
        }
    }

    @Test
    void getterSetterTest() {
        final CsvReader csvReader = csvReader();
        assertThat(csvReader.separator(), is(new char[]{','}));
        assertThat(csvReader.separator(';').separator(), is(new char[]{';'}));

        assertThat(csvReader.autoSep(), is(false));
        assertThat(csvReader.autoSep(true).autoSep(), is(true));

        assertThat(csvReader.unzip(), is(false));
        assertThat(csvReader.unzip(true).unzip(), is(true));

        assertThat(csvReader.charset(), is(UTF_8));
        assertThat(csvReader.charset(US_ASCII).charset(), is(US_ASCII));

        assertThat(csvReader.skipLines(), is(-1L));
        assertThat(csvReader.skipLines(1).skipLines(), is(1L));
    }

    public static void validateExampleCsv(final List<CsvRow> csv) {
        assertThat(csv.size(), is(10));
        assertThat(csv.get(0), is(equalTo(csvRowOf("normal", "unquoted", "line"))));
        assertThat(csv.get(1), is(equalTo(csvRowOf("normal", "quoted", "line"))));
        assertThat(csv.get(2), is(equalTo(csvRowOf("mixed", "quoted", "line"))));
        assertThat(csv.get(3), is(equalTo(csvRowOf("mixed", "quoted", "line, with separator"))));
        assertThat(csv.get(4), is(equalTo(csvRowOf("mixed", "quoted", "line, with escaped quote \" variant", "1"))));
        assertThat(csv.get(5), is(equalTo(csvRowOf("mixed", "quoted", "line, with escaped quote \" variant", "2"))));
        assertThat(csv.get(6), is(equalTo(csvRowOf("mixed", "quoted", "line, with separator, and escaped quote \" variant", "1"))));
        assertThat(csv.get(7), is(equalTo(csvRowOf("mixed", "quoted", "line, with separator, and escaped quote \" variant", "2"))));
        assertThat(csv.get(8), is(equalTo(csvRowOf("empty", "line", "above"))));
        assertThat(csv.get(9), is(equalTo(csvRowOf("empty", "columns", "above"))));
    }

}
