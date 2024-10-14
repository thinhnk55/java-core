package work.vietdefi.dsa.sort;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class BubbleSorterTest {

    @Test
    public void testSort_EmptyArray() {
        Sorter sorter = new BubbleSorter();
        int[] input = {};
        int[] expected = {};
        assertArrayEquals(expected, sorter.sort(input), "Sorting an empty array should return an empty array.");
    }

    @Test
    public void testSort_SingleElementArray() {
        Sorter sorter = new BubbleSorter();
        int[] input = {1};
        int[] expected = {1};
        assertArrayEquals(expected, sorter.sort(input), "A single-element array should remain unchanged.");
    }

    @Test
    public void testSort_AlreadySortedArray() {
        Sorter sorter = new BubbleSorter();
        int[] input = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};
        assertArrayEquals(expected, sorter.sort(input), "Already sorted array should remain the same.");
    }

    @Test
    public void testSort_UnsortedArray() {
        Sorter sorter = new BubbleSorter();
        int[] input = {5, 3, 1, 4, 2};
        int[] expected = {1, 2, 3, 4, 5};
        assertArrayEquals(expected, sorter.sort(input), "The unsorted array should be sorted in ascending order.");
    }

    @Test
    public void testSort_ArrayWithDuplicates() {
        Sorter sorter = new BubbleSorter();
        int[] input = {3, 1, 2, 2, 1};
        int[] expected = {1, 1, 2, 2, 3};
        assertArrayEquals(expected, sorter.sort(input), "Array with duplicates should be sorted correctly.");
    }
}