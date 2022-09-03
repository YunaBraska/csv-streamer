package berlin.yuna.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.UncheckedIOException;
import java.nio.file.Paths;

import static berlin.yuna.logic.CsvReaderTest.getResourceFile;
import static berlin.yuna.logic.ListCSV.listCSV;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("UnitTest")
class FileExtractionTest {

    @BeforeEach
    void setUp() {
        //Cleanup for better test coverage to test tmp dir creation
        deleteDir();
    }

    @Test
    void withUnzipTest() {
        assertThat(listCSV(getResourceFile("test.csv"), -1, null, true), hasSize(10));
        assertThat(listCSV(getResourceFile("test.tar"), -1, null, true), hasSize(10));
        assertThat(listCSV(getResourceFile("test.csv.zip"), -1, null, true), hasSize(10));
    }

    @Test
    void withOutUnzipTest() {
        assertThat(listCSV(getResourceFile("test.csv")), hasSize(10));
        assertThrows(UncheckedIOException.class, () -> listCSV(getResourceFile("test.tar")));
        assertThrows(UncheckedIOException.class, () -> listCSV(getResourceFile("test.csv.zip")));
    }

    private static void deleteDir() {
        Paths.get(System.getProperty("java.io.tmpdir"), CsvReader.class.getSimpleName(), "test");
    }
}
