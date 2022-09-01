package berlin.yuna.logic;

import berlin.yuna.model.CsvIndexRow;
import berlin.yuna.model.CsvRow;
import berlin.yuna.model.IoCsvException;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static berlin.yuna.logic.FileUtils.getFile;
import static berlin.yuna.logic.FileUtils.getResourceFile;
import static berlin.yuna.model.CsvRow.csvRowOf;
import static berlin.yuna.model.CsvRow.validateSeparator;
import static java.nio.charset.StandardCharsets.UTF_8;

public class CsvReader {

    private char[] separator = new char[]{','};
    private boolean autoSep = false;
    private boolean unzip = false;
    private Charset charset = UTF_8;
    private long skipLines = -1;

    /**
     * @param file     the path to the file - or path to a resource
     * @param consumer consumes row
     */
    public void consume(final Path file, final Consumer<CsvIndexRow> consumer) {
        try (final Stream<CsvRow> stream = stream(file)) {
            final AtomicLong index = new AtomicLong(0);
            stream.forEach(csvRow -> consumer.accept(CsvIndexRow.csvIndexRowOf(index.getAndIncrement(), csvRow)));
        }
    }

    /**
     * @param file the path to the file - or path to a resource
     * @return the {@link CsvRow} from the file as a Stream
     */
    public List<CsvRow> readAllRows(final Path file) {
        try (final Stream<CsvRow> stream = stream(file)) {
            return stream.collect(Collectors.toList());
        }
    }

    /**
     * '{@link Stream}<{@link CsvRow}>' must be closed with 'try'-with-resources statement
     * <p> The returned stream encapsulates a {@link Reader}. If timely
     * disposal of file system resources is required, the try-with-resources
     * construct should be used to ensure that the stream's
     * {@link Stream#close close} method is invoked after the stream operations
     * are completed.<br/>
     * <b>See example: {@link CsvReader#readAllRows(Path)}</b>
     *
     * @param file the path to the file - or path to a resource
     * @return the {@link CsvRow} from the file as a Stream
     */
    public Stream<CsvRow> stream(final Path file) {
        final Path tmpFile = getFile(file, () -> getResourceFile(file));
        final char[] sep = autoSep ? new char[]{FileUtils.detectSeparator(tmpFile, charset)} : separator;
        try {
            final Stream<String> stream = Files.lines(tmpFile, charset);
            return stream.skip(skipLines > 0 ? skipLines : 0)
                    .map(row -> csvRowOf(row, sep))
                    .filter(csvRow -> !csvRow.stream().allMatch(CsvReader::isNullOrEmpty))
                    .onClose(() -> {
                        stream.close();
                        deleteTmpFile(file, tmpFile);
                    });
        } catch (IOException e) {
            deleteTmpFile(file, tmpFile);
            throw new IoCsvException("File read error [" + file + "]", e);
        }
    }

    /**
     * @return new configurable {@link CsvReader}
     */
    public static CsvReader csvReader() {
        return new CsvReader();
    }

    /**
     * @return separator to use for splitting the csv row
     */
    public char[] separator() {
        return separator;
    }

    /**
     * @param separator Splits the CSV rows at the given separator <br/>
     *                  Included fallback: [',']
     * @return self [{@link CsvReader}]
     */
    public CsvReader separator(final char... separator) {
        this.separator = validateSeparator(separator);
        return this;
    }

    /**
     * @return On <b>true</b> detects the separator automatically
     */
    public boolean autoSep() {
        return autoSep;
    }

    /**
     * @param autoSep - On <b>true</b> detects the separator automatically
     * @return self [{@link CsvReader}]
     */
    public CsvReader autoSep(final boolean autoSep) {
        this.autoSep = autoSep;
        return this;
    }

    /**
     * @return On <b>true</b> detects and unzips the CSV file automatically
     */
    public boolean unzip() {
        return unzip;
    }

    /**
     * @param unzip - On <b>true</b> detects and unzips the CSV file automatically
     * @return self [{@link CsvReader}]
     */
    public CsvReader unzip(final boolean unzip) {
        this.unzip = unzip;
        return this;
    }

    /**
     * @return The charset to use for decoding the CSV file
     */
    public Charset charset() {
        return charset;
    }

    /**
     * @param charset The charset to use for decoding the CSV file
     * @return self [{@link CsvReader}]
     */
    public CsvReader charset(final Charset charset) {
        this.charset = charset == null ? UTF_8 : charset;
        return this;
    }

    /**
     * @return lines to skip while reading csv
     */
    public long skipLines() {
        return skipLines;
    }

    /**
     * @param skipLines lines to skip while reading csv
     * @return self [{@link CsvReader}]
     */
    public CsvReader skipLines(final long skipLines) {
        this.skipLines = skipLines;
        return this;
    }

    private static boolean isNullOrEmpty(final String input) {
        return input == null || input.trim().isEmpty();
    }

    protected CsvReader() {
    }

    private void deleteTmpFile(final Path file, final Path path) {
        if (file.compareTo(path) != 0) {
            FileUtils.deleteTmpFile(path);
        }
    }

}
