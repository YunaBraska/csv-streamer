package berlin.yuna.logic;

import berlin.yuna.model.CsvRow;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;

import static berlin.yuna.logic.CsvReader.csvReader;

public class ListCSV {

    /**
     * @param file the path to the file - or path to a resource
     * @return the {@link CsvRow} from the file as a Stream
     */
    public static List<CsvRow> listCSV(final Path file) {
        return listCSV(file, -1);
    }

    /**
     * @param file the path to the file - or path to a resource
     * @param skip lines to skip while reading csv
     * @return the {@link CsvRow} from the file as a Stream
     */
    public static List<CsvRow> listCSV(final Path file, final long skip) {
        return listCSV(file, skip, null, false);
    }


    /**
     * @param file       the path to the file - or path to a resource
     * @param skip       lines to skip while reading csv
     * @param separators column split parameter (no regex)
     * @return the {@link CsvRow} from the file as a Stream
     */
    public static List<CsvRow> listCSV(final Path file, final long skip, final char... separators) {
        return listCSV(file, skip, null, separators);
    }

    /**
     * @param file       The path to the file - or path to a resource
     * @param skip       lines to skip while reading csv
     * @param charset    The charset to use for decoding the CSV file
     * @param separators Splits the CSV rows at the given separator <br/>Included fallback: [',']
     * @return the {@link CsvRow} from the file as a Stream
     */
    public static List<CsvRow> listCSV(final Path file, final long skip, final Charset charset, final char... separators) {
        return listCSV(file, skip, charset, false, separators);
    }

    /**
     * @param file       The path to the file - or path to a resource
     * @param skip       lines to skip while reading csv
     * @param charset    The charset to use for decoding the CSV file
     * @param unzip      On <b>true</b> detects and unzips the CSV file automatically
     * @param separators Splits the CSV rows at the given separator <br/>Included fallback: [',']
     * @return the {@link CsvRow} from the file as a Stream
     */
    public static List<CsvRow> listCSV(final Path file, final long skip, final Charset charset, final boolean unzip, final char... separators) {
        return listCSV(file, skip, charset, unzip, false, separators);
    }

    /**
     * @param file       The path to the file - or path to a resource
     * @param skip       lines to skip while reading csv
     * @param charset    The charset to use for decoding the CSV file
     * @param unzip      On <b>true</b> detects and unzips the CSV file automatically
     * @param autoSep    On <b>true</b> detects the separator automatically
     * @param separators Splits the CSV rows at the given separator <br/>Included fallback: [',']
     * @return the {@link CsvRow} from the file as a Stream
     */
    public static List<CsvRow> listCSV(final Path file, final long skip, final Charset charset, final boolean unzip, final boolean autoSep, final char... separators) {
        return csvReader().skipLines(skip).charset(charset).separator(separators).unzip(unzip).autoSep(autoSep).readAllRows(file);
    }


    private ListCSV() {
    }
}
