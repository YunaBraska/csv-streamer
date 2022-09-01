# CSV Streamer

[![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.com/donate/?hosted_button_id=HFHFUT3G6TZF6)

[![Build][build_shield]][build_link]
[![Maintainable][maintainable_shield]][maintainable_link]
[![Coverage][coverage_shield]][coverage_link]
[![Issues][issues_shield]][issues_link]
[![Commit][commit_shield]][commit_link]
[![Dependencies][dependency_shield]][dependency_link]
[![License][license_shield]][license_link]
[![Central][central_shield]][central_link]
[![Tag][tag_shield]][tag_link]
[![Javadoc][javadoc_shield]][javadoc_link]
[![Size][size_shield]][size_shield]
![Label][label_shield]

[build_shield]: https://github.com/YunaBraska/csv-streamer/workflows/JAVA_CI/badge.svg
[build_link]: https://github.com/YunaBraska/csv-streamer/actions?query=workflow%3AJAVA_CI
[maintainable_shield]: https://img.shields.io/codeclimate/maintainability/YunaBraska/csv-streamer?style=flat-square
[maintainable_link]: https://codeclimate.com/github/YunaBraska/csv-streamer/maintainability
[coverage_shield]: https://img.shields.io/codeclimate/coverage/YunaBraska/csv-streamer?style=flat-square
[coverage_link]: https://codeclimate.com/github/YunaBraska/csv-streamer/test_coverage
[issues_shield]: https://img.shields.io/github/issues/YunaBraska/csv-streamer?style=flat-square
[issues_link]: https://github.com/YunaBraska/csv-streamer/commits/main
[commit_shield]: https://img.shields.io/github/last-commit/YunaBraska/csv-streamer?style=flat-square
[commit_link]: https://github.com/YunaBraska/csv-streamer/issues
[license_shield]: https://img.shields.io/github/license/YunaBraska/csv-streamer?style=flat-square
[license_link]: https://github.com/YunaBraska/csv-streamer/blob/main/LICENSE
[dependency_shield]: https://img.shields.io/librariesio/github/YunaBraska/csv-streamer?style=flat-square
[dependency_link]: https://libraries.io/github/YunaBraska/csv-streamer
[central_shield]: https://img.shields.io/maven-central/v/berlin.yuna/csv-streamer?style=flat-square
[central_link]:https://search.maven.org/artifact/berlin.yuna/csv-streamer
[tag_shield]: https://img.shields.io/github/v/tag/YunaBraska/csv-streamer?style=flat-square
[tag_link]: https://github.com/YunaBraska/csv-streamer/releases
[javadoc_shield]: https://javadoc.io/badge2/berlin.yuna/csv-streamer/javadoc.svg?style=flat-square
[javadoc_link]: https://javadoc.io/doc/berlin.yuna/csv-streamer
[size_shield]: https://img.shields.io/github/repo-size/YunaBraska/csv-streamer?style=flat-square
[label_shield]: https://img.shields.io/badge/Yuna-QueenInside-blueviolet?style=flat-square
[gitter_shield]: https://img.shields.io/gitter/room/YunaBraska/csv-streamer?style=flat-square
[gitter_link]: https://gitter.im/csv-streamer/Lobby

Simple lazy CSV reader

### Motivation

I wanted to make my pure java native CSV parser public
Features:

* No dependencies
* Auto detect delimiters
* lazy stream & consume CSV lines
* Immutable
* Inner CSV (for columns with a internal CSV)
* Unzip
* Reads files also from resources
* tolerant (e.g. ignores empty lines, ignores missing columns)
    * No NullPointerException or IndexOutOfBoundsException  

### Classes

| ClassName  | UsageType | Description              |
|------------|-----------|--------------------------|
| StreamCSV  | static    | Stream methods (lazy)    | 
| ConsumeCSV | static    | Consumer methods (lazy)  | 
| ListCsv    | static    | List methods (in memory) | 
| CsvReader  | Object    | Configurable reader      | 

### Options

| Options    | Type    | Default | Description                                               |
|------------|---------|---------|-----------------------------------------------------------|
| Skip       | long    | -1      | Lines to skip while reading csv                           | 
| charset    | Charset | UTF_8   | Charset to use for decoding the CSV file                  | 
| unzip      | boolean | false   | On **true** detects and unzips the CSV file automatically | 
| autoSep    | boolean | false   | On **true** detects the separator automatically           | 
| separators | char... | ','     | Splits the CSV rows at the given separator                | 

### Example listCsv

```java
        final List<CsvRow> csvList = listCsv.listCsv(EXAMPLE_CSV);
```

### Example ConsumeCSV

```java
        ConsumeCSV.consumeCsv(EXAMPLE_CSV,System.out::println);
```

### Example StreamCSV

```java
        try(final Stream<CsvRow> StreamCSV.streamCSV=streamCSV(EXAMPLE_CSV)){
            streamCSV.forEach(System.out::println);
        }
```

### Example CsvReader

```java
import static berlin.yuna.logic.CsvReader.csvReader;

public class CsvReaderTest {

    //configuration
    CsvReader reader = csvReader().skipLines(1).charset(UTF_8).separator(';').unzip(true).autoSep(true);

    //read
    List<CsvRow> allLines = reader.readAllRows(EXAMPLE_CSV);

    //consume
    reader.consume(EXAMPLE_CSV,System.out::println);

    //stream
    try(final Stream<CsvRow> lazyStream = reader.streamCSV(EXAMPLE_CSV)){
        lazyStream.forEach(System.out::println);
    }
}
```

### TODO

* [ ] Inner CSV
* [ ] Unzip (e.g. Gz, Tar, Zip) 
