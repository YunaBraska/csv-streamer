package berlin.yuna.logic;

import berlin.yuna.model.IoCsvException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.nio.file.Files.newBufferedReader;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Comparator.comparingLong;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;

public class FileUtils {

    public static final char[] POSSIBLE_CSV_SEPARATORS = {'\t', ',', ';', '|'};

    private FileUtils() {
    }

    public static char detectSeparator(final Path file, final Charset charset) {
        return detectSeparator(readAllLines(file, charset, 15));
    }

    public static char detectSeparator(final Collection<String> lines) {
        //<Separator, CountPerLine> == <Integer, List<Long>>
        return lines.stream().map(line -> line.chars()
                                .filter(c -> CharBuffer.wrap(POSSIBLE_CSV_SEPARATORS).chars().anyMatch(c2 -> c == c2)).boxed()
                                .collect(groupingBy(identity(), Collectors.counting()))
                        //remove wrapper list
                ).flatMap(integerLongMap -> integerLongMap.entrySet().stream())
                .collect(groupingBy(
                        Map.Entry::getKey,
                        mapping(Map.Entry::getValue, Collectors.toList())
                ))
                //Find separator by most occurrence
                .entrySet().stream()
                .map(e -> new SimpleEntry<>(e.getKey(), getPopularElement(e.getValue().toArray(new Long[0]))[1]))
                .max(comparingLong(SimpleEntry::getValue)).map(e -> ((char) e.getKey().intValue())).orElse(',');
    }

    public static long[] getPopularElement(final Long[] array) {
        int count = 1;
        int tempCount;
        long popular = array[0];
        long temp;
        for (int i = 0; i < (array.length - 1); i++) {
            temp = array[i];
            tempCount = 0;
            for (int j = 1; j < array.length; j++) {
                if (temp == array[j])
                    tempCount++;
            }
            if (tempCount > count) {
                popular = temp;
                count = tempCount;
            }
        }
        return new long[]{popular, count};
    }

    public static List<String> readAllLines(final Path file, final Charset charset, final int limit) {
        try (final BufferedReader reader = newBufferedReader(file, charset)) {
            final AtomicInteger count = new AtomicInteger(-1);
            final List<String> result = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null && count.getAndIncrement() < limit) {
                result.add(line);
            }
            return result;
        } catch (IOException e) {
            throw new IoCsvException("File read error [" + file + "]", e);
        }
    }

    public static Path getFile(final String file) {
        return getFile(Paths.get(file));
    }

    public static Path getFile(final Path file) {
        return getFile(file, () -> getResourceFile(file));
    }

    public static Path getFile(final Path file, final Supplier<Path> fallback) {
        if (file == null) {
            throw new IoCsvException("File cannot be [null]", new NullPointerException());
        } else if (Files.isDirectory(file)) {
            throw new IoCsvException("File cannot be a [Directory]", new NoSuchFileException(file.toString()));
        } else if (!Files.exists(file)) {
            return fallback.get();
        }
        return file;
    }

    public static Path getResourceFile(final Path file) {
        Path tempFile = null;
        try (final InputStream in = FileUtils.class.getClassLoader().getResourceAsStream(file.toString())) {
            tempFile = Files.createTempFile(CsvReader.class.getSimpleName() + "_", ".csv");
            Files.copy(validateInputStream(in, () -> file), tempFile, REPLACE_EXISTING);
            return tempFile;
        } catch (Exception e) {
            deleteTmpFile(tempFile);
            throw new IoCsvException("File read error [" + file + "]", e);
        }
    }

    public static <T> T validateInputStream(final T input, final Supplier<Path> file) {
        if (input == null) {
            final String fileName = file.get().toString();
            throw new IoCsvException("File not exists [" + fileName + "]", new NoSuchFileException(fileName));
        }
        return input;
    }

    public static void deleteTmpFile(final Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new IoCsvException("Could not delete temp file", e);
        }
    }
}
