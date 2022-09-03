package berlin.yuna.logic;

import berlin.yuna.model.IoCsvException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.exists;
import static java.util.Optional.ofNullable;

public class FileExtraction {

    private static final String TMPDIR = System.getProperty("java.io.tmpdir");

    private FileExtraction() {
    }

    /**
     * @param compressedFile zip or gzip file
     * @return extracted file or input parameter if it's not extractable
     */
    public static Path extractFile(final Path compressedFile) {
        final Path unzippedFile = unzip(compressedFile);
        return compressedFile.equals(unzippedFile) ? unGzip(compressedFile) : unzippedFile;
    }

    /**
     * @param zipFile to be extracted
     * @return extracted file or input parameter if it's not extractable
     */
    public static Path unzip(final Path zipFile) {
        try (final ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFile))) {
            ZipEntry zipEntry = zis.getNextEntry();
            final Path parent = createParentDir(zipFile, ofNullable(zipEntry).map(ZipEntry::getName).orElse(null));
            while (zipEntry != null) {
                final Path newFile = Paths.get(parent.toString(), zipFile.getFileName().toString());
                copy(zis, newFile);
                zis.closeEntry();
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            return getFirstFile(parent, zipFile);
        } catch (IOException e) {
            throw new IoCsvException("ZipFile is not extractable [" + zipFile + "]", e);
        }
    }

    /**
     * @param gZipFile to be extracted
     * @return extracted file or input parameter if it's not extractable
     */
    public static Path unGzip(final Path gZipFile) {
        try (final GZIPInputStream in = new GZIPInputStream(Files.newInputStream(gZipFile))) {
            final Path parent = createParentDir(gZipFile, gZipFile.getFileName().toString());
            return copy(in, Paths.get(parent.toString(), gZipFile.getFileName().toString()));
        } catch (ZipException ze) {
            return gZipFile;
        } catch (IOException e) {
            throw new IoCsvException("GZip File is not extractable [" + gZipFile + "]", e);
        }
    }

    private static Path copy(final InputStream in, final Path out) throws IOException {
        try (final FileOutputStream fos = new FileOutputStream(out.toFile(), false)) {

            final byte[] buffer = new byte[1024];
            int len;

            while ((len = in.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            return out;
        }
    }

    private static Path getFirstFile(final Path path, final Path fallback) throws IOException {
        try (final Stream<Path> stream = exists(path) ? Files.list(path) : Stream.of(fallback)) {
            return stream.limit(1).findFirst().orElse(fallback);
        }
    }

    private static Path createParentDir(final Path zipFile, final String fileName) throws IOException {
        final String name = zipFile.getFileName().toString();
        final Path tmpDir = Paths.get(
                TMPDIR,
                CsvReader.class.getSimpleName(),
                name.contains(".") ? name.substring(0, name.indexOf(".")) : name
        );
        if (fileName != null && !exists(tmpDir)) {
            createDirectories(tmpDir);
        }
        return tmpDir;
    }
}
