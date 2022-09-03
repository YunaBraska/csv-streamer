package berlin.yuna.logic;

import berlin.yuna.model.IoCsvException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static berlin.yuna.logic.CsvReaderTest.EXAMPLE_CSV_SEPARATORS;
import static berlin.yuna.logic.FileUtils.detectSeparator;
import static berlin.yuna.logic.FileUtils.getFile;
import static berlin.yuna.logic.FileUtils.getPopularElement;
import static berlin.yuna.logic.FileUtils.getResourceFile;
import static java.util.Objects.requireNonNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("UnitTest")
class FileUtilsTest {

    public static final Path TMP_CSV_SEP = getFile(EXAMPLE_CSV_SEPARATORS);

    @AfterAll
    static void afterAll() throws IOException {
        Files.deleteIfExists(TMP_CSV_SEP);
    }

    @Test
    void detectSeparatorTest() {
        final char separator = detectSeparator(TMP_CSV_SEP, StandardCharsets.UTF_8);
        assertThat(separator, is(';'));
    }

    @Test
    void getPopularElementTest() {
        final long[] popularElement = getPopularElement(new Long[]{0L, 0L, 1L, 1L, 2L, 2L, 3L, 3L, 4L, 4L, 5L, 5L, 6L, 6L, 5L, 5L, 4L, 4L, 4L, 3L, 3L, 0L, 0L});
        assertThat(popularElement[0], is(4L));
        assertThat(popularElement[1], is(5L));
    }

    @Test
    void getFileTest() throws IOException, URISyntaxException {
        final Path absolutePath = getFile(Paths.get(requireNonNull(this.getClass().getClassLoader().getResource(EXAMPLE_CSV_SEPARATORS)).toURI()));
        final Path resourcePath = TMP_CSV_SEP;
        final Path fallbackPath = getFile(Paths.get("invalid"), () -> resourcePath);
        assertThat(absolutePath, is(not(equalTo(resourcePath))));
        assertThat(Files.readAllLines(resourcePath), is(equalTo(Files.readAllLines(absolutePath))));
        assertThat(Files.readAllLines(resourcePath), is(equalTo(Files.readAllLines(fallbackPath))));
    }

    @Test
    void getFileWithInvalidInputTest() {
        final Exception directory = assertThrows(IoCsvException.class, () -> getFile(System.getProperty("user.dir")));
        assertThat(directory.getMessage(), is(equalTo("File cannot be a [Directory]")));

        final Exception nullInput = assertThrows(IoCsvException.class, () -> getFile(null, () -> null));
        assertThat(nullInput.getMessage(), is(equalTo("File cannot be [null]")));
    }

    @Test
    void getResourceFileWithInvalidInputTest() {
        final Exception directory = assertThrows(IoCsvException.class, () -> getResourceFile(Paths.get("invalid")));
        assertThat(directory.getMessage(), is(equalTo("File read error [invalid]")));

    }
}
