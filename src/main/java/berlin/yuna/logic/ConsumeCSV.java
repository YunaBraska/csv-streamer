package berlin.yuna.logic;

import berlin.yuna.model.CsvIndexRow;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.function.Consumer;

import static berlin.yuna.logic.CsvReader.csvReader;

public class ConsumeCSV {

    /**
     * @param file the path to the file - or path to a resource
     */
    public static void consumeCsv(final Path file, final Consumer<CsvIndexRow> consumer) {
        consumeCsv(file, consumer, -1);
    }

    /**
     * @param file the path to the file - or path to a resource
     * @param skip lines to skip while reading csv
     */
    public static void consumeCsv(final Path file, final Consumer<CsvIndexRow> consumer, final long skip) {
        consumeCsv(file, consumer, skip, null, false);
    }

    /**
     * @param file       the path to the file - or path to a resource
     * @param skip       lines to skip while reading csv
     * @param separators column split parameter (no regex)
     */
    public static void consumeCsv(final Path file, final Consumer<CsvIndexRow> consumer, final long skip, final char... separators) {
        consumeCsv(file, consumer, skip, null, separators);
    }

    /**
     * <b>See example: {@link CsvReader#readAllRows(Path)}</b>
     *
     * @param file       The path to the file - or path to a resource
     * @param skip       lines to skip while reading csv
     * @param charset    The charset to use for decoding the CSV file
     * @param separators Splits the CSV rows at the given separator <br/>Included fallback: [',']
     */
    public static void consumeCsv(final Path file, final Consumer<CsvIndexRow> consumer, final long skip, final Charset charset, final char... separators) {
        consumeCsv(file, consumer, skip, charset, false, separators);
    }

    /**
     * @param file       The path to the file - or path to a resource
     * @param skip       lines to skip while reading csv
     * @param charset    The charset to use for decoding the CSV file
     * @param unzip      On <b>true</b> detects and unzips the CSV file automatically
     * @param separators Splits the CSV rows at the given separator <br/>Included fallback: [',']
     */
    public static void consumeCsv(final Path file, final Consumer<CsvIndexRow> consumer, final long skip, final Charset charset, final boolean unzip, final char... separators) {
        consumeCsv(file, consumer, skip, charset, unzip, false, separators);
    }

    /**
     * @param file       The path to the file - or path to a resource
     * @param skip       lines to skip while reading csv
     * @param charset    The charset to use for decoding the CSV file
     * @param unzip      On <b>true</b> detects and unzips the CSV file automatically
     * @param autoSep    On <b>true</b> detects the separator automatically
     * @param separators Splits the CSV rows at the given separator <br/>Included fallback: [',']
     */
    public static void consumeCsv(final Path file, final Consumer<CsvIndexRow> consumer, final long skip, final Charset charset, final boolean unzip, final boolean autoSep, final char... separators) {
        csvReader().skipLines(skip).charset(charset).separator(separators).unzip(unzip).autoSep(autoSep).consume(file, consumer);
    }

    private ConsumeCSV() {
    }
}
