package berlin.yuna.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static berlin.yuna.model.CsvIndexRow.csvIndexRowOf;
import static berlin.yuna.model.CsvRow.csvRowOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@Tag("UnitTest")
class CsvIndexRowTest {

    @Test
    void CsvIndexRowCreationTest() {
        final CsvIndexRow csvIndexRow = csvIndexRowOf(2, csvRowOf("column 1, column 2, column 3"));
        assertThat(csvIndexRow.index(), is(2L));
        assertThat(csvIndexRow.get(0), is(equalTo("column 1")));
        assertThat(csvIndexRow.get(1), is(equalTo("column 2")));
        assertThat(csvIndexRow.get(2), is(equalTo("column 3")));
        assertThat(csvIndexRow.get(3), is(nullValue()));
        assertThat(csvIndexRow.toCsvRow(), is(equalTo(csvIndexRow)));
        assertThat(csvIndexRow.hashCode(), is(not(0)));
    }
}
