package cn.mindit.atom.test.core.util;

import cn.mindit.atom.core.util.NaturalComparator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NaturalComparatorTest {

    private final NaturalComparator comparator = new NaturalComparator();

    @Test
    void sortsNumberPartsNumerically() {
        List<String> list = new ArrayList<>(Arrays.asList(
            "aka-11.jpg", "aka-2.jpg", "aka-1.jpg"
        ));
        list.sort(comparator);
        assertThat(list).containsExactly("aka-1.jpg", "aka-2.jpg", "aka-11.jpg");
    }

    @Test
    void equalStringsReturnZero() {
        assertThat(comparator.compare("abc", "abc")).isZero();
    }

    @Test
    void shorterStringIsSmallerWhenPrefixMatches() {
        assertThat(comparator.compare("abc", "abcd")).isNegative();
        assertThat(comparator.compare("abcd", "abc")).isPositive();
    }

    @Test
    void comparesCaseInsensitivelyForLetters() {
        assertThat(comparator.compare("abc", "ABC")).isZero();
    }

    @Test
    void whenNumbersEqualInValueShorterIsGreater() {
        // 由实现 -Integer.compare(data1.length(), data2.length()) 决定
        assertThat(comparator.compare("01", "1")).isNegative();
        assertThat(comparator.compare("1", "01")).isPositive();
    }

    @Test
    void handlesSpaceAndDotSeparately() {
        List<String> list = new ArrayList<>(Arrays.asList(
            "file.2.txt", "file.1.txt", "file.10.txt"
        ));
        list.sort(comparator);
        assertThat(list).containsExactly("file.1.txt", "file.2.txt", "file.10.txt");
    }

}
