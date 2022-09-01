package berlin.yuna.logic;

import berlin.yuna.model.CsvRow;

import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.stream.Stream;

import static berlin.yuna.logic.CsvReader.csvReader;

public class StreamCSV {

    /**
     * '{@link Stream}<{@link CsvRow}>' <b>must be closed with 'try'-with-resources statement</b>
     * <p> The returned stream encapsulates a {@link Reader}.  If timely
     * disposal of file system resources is required, the try-with-resources
     * construct should be used to ensure that the stream's
     * {@link Stream#close close} method is invoked after the stream operations
     * are completed.<br/>
     * <b>See example: {@link CsvReader#readAllRows(Path)}</b>
     *
     * @param file the path to the file - or path to a resource
     * @return the {@link CsvRow} from the file as a Stream
     */
    public static Stream<CsvRow> streamCSV(final Path file) {
        return streamCSV(file, -1);
    }

    /**
     * '{@link Stream}<{@link CsvRow}>' <b>must be closed with 'try'-with-resources statement</b>
     * <p> The returned stream encapsulates a {@link Reader}.  If timely
     * disposal of file system resources is required, the try-with-resources
     * construct should be used to ensure that the stream's
     * {@link Stream#close close} method is invoked after the stream operations
     * are completed.<br/>
     * <b>See example: {@link CsvReader#readAllRows(Path)}</b>
     *
     * @param file the path to the file - or path to a resource
     * @param skip lines to skip while reading csv
     * @return the {@link CsvRow} from the file as a Stream
     */
    public static Stream<CsvRow> streamCSV(final Path file, final long skip) {
        return streamCSV(file, skip, null, false);
    }

    /**
     * '{@link Stream}<{@link CsvRow}>' <b>must be closed with 'try'-with-resources statement</b>
     * <p> The returned stream encapsulates a {@link Reader}.  If timely
     * disposal of file system resources is required, the try-with-resources
     * construct should be used to ensure that the stream's
     * {@link Stream#close close} method is invoked after the stream operations
     * are completed. <br/>
     * <b>See example: {@link CsvReader#readAllRows(Path)}</b>
     *
     * @param file       the path to the file - or path to a resource
     * @param skip       lines to skip while reading csv
     * @param separators column split parameter (no regex)
     * @return the {@link CsvRow} from the file as a Stream
     */
    public static Stream<CsvRow> streamCSV(final Path file, final long skip, final char... separators) {
        return streamCSV(file, skip, null, separators);
    }

    /**
     * '{@link Stream}<{@link CsvRow}>' <b>must be closed with 'try'-with-resources statement</b>
     * <p> The returned stream encapsulates a {@link Reader}.  If timely
     * disposal of file system resources is required, the try-with-resources
     * construct should be used to ensure that the stream's
     * {@link Stream#close close} method is invoked after the stream operations
     * are completed. <br/>
     * <b>See example: {@link CsvReader#readAllRows(Path)}</b>
     *
     * @param file       The path to the file - or path to a resource
     * @param skip       lines to skip while reading csv
     * @param charset    The charset to use for decoding the CSV file
     * @param separators Splits the CSV rows at the given separator <br/>Included fallback: [',']
     * @return the {@link CsvRow} from the file as a Stream
     */
    public static Stream<CsvRow> streamCSV(final Path file, final long skip, final Charset charset, final char... separators) {
        return streamCSV(file, skip, charset, false, separators);
    }

    /**
     * '{@link Stream}<{@link CsvRow}>' <b>must be closed with 'try'-with-resources statement</b>
     * <p> The returned stream encapsulates a {@link Reader}.  If timely
     * disposal of file system resources is required, the try-with-resources
     * construct should be used to ensure that the stream's
     * {@link Stream#close close} method is invoked after the stream operations
     * are completed <br/>
     * <b>See example: {@link CsvReader#readAllRows(Path)}</b>
     *
     * @param file       The path to the file - or path to a resource
     * @param skip       lines to skip while reading csv
     * @param charset    The charset to use for decoding the CSV file
     * @param unzip      On <b>true</b> detects and unzips the CSV file automatically
     * @param separators Splits the CSV rows at the given separator <br/>Included fallback: [',']
     * @return the {@link CsvRow} from the file as a Stream
     */
    public static Stream<CsvRow> streamCSV(final Path file, final long skip, final Charset charset, final boolean unzip, final char... separators) {
        return streamCSV(file, skip, charset, unzip, false, separators);
    }

    /**
     * '{@link Stream}<{@link CsvRow}>' <b>must be closed with 'try'-with-resources statement</b>
     * <p> The returned stream encapsulates a {@link Reader}.  If timely
     * disposal of file system resources is required, the try-with-resources
     * construct should be used to ensure that the stream's
     * {@link Stream#close close} method is invoked after the stream operations
     * are completed <br/>
     * <b>See example: {@link CsvReader#readAllRows(Path)}</b>
     *
     * @param file       The path to the file - or path to a resource
     * @param skip       lines to skip while reading csv
     * @param charset    The charset to use for decoding the CSV file
     * @param unzip      On <b>true</b> detects and unzips the CSV file automatically
     * @param autoSep    On <b>true</b> detects the separator automatically
     * @param separators Splits the CSV rows at the given separator <br/>Included fallback: [',']
     * @return the {@link CsvRow} from the file as a Stream
     */
    public static Stream<CsvRow> streamCSV(final Path file, final long skip, final Charset charset, final boolean unzip, final boolean autoSep, final char... separators) {
        return csvReader().skipLines(skip).charset(charset).separator(separators).unzip(unzip).autoSep(autoSep).stream(file);
    }


    private StreamCSV() {
    }
}
