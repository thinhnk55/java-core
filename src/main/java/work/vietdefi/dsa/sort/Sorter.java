package work.vietdefi.dsa.sort;

/**
 * A contract for sorting algorithms.
 * Classes implementing this interface should provide a specific sorting algorithm
 * that rearranges the elements of an integer array in ascending order.
 */
public interface Sorter {

    /**
     * Sorts the given array of integers in ascending order.
     *
     * @param values The array of integers to be sorted.
     * @return A new array containing the sorted integers in ascending order.
     */
    int[] sort(int[] values);
}