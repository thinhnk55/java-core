package work.vietdefi.basic;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Random;

public class Ex1Test {

    @Test
    @DisplayName("Test if 0 is even")
    public void testZeroIsEven() {
        Ex1 ex1 = new Ex1();
        assertTrue(ex1.isEvenNumber(0), "0 should be even");
    }

    @Test
    @DisplayName("Test if 1 is odd")
    public void testOneIsOdd() {
        Ex1 ex1 = new Ex1();
        assertFalse(ex1.isEvenNumber(1), "1 should be odd");
    }

    @Test
    @DisplayName("Test if 2 is even")
    public void testTwoIsEven() {
        Ex1 ex1 = new Ex1();
        assertTrue(ex1.isEvenNumber(2), "2 should be even");
    }
}