package work.vietdefi.basic;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class Ex2Test {

    @Test
    @DisplayName("Test sum")
    public void testSum() {
        Ex2 ex2 = new Ex2();

        // Test when input is 1
        assertEquals(1, ex2.sum(1));

        // Test when input is 2
        assertEquals(3, ex2.sum(2)); // 1 + 2 = 3

        // Test when input is 5
        assertEquals(15, ex2.sum(5)); // 1 + 2 + 3 + 4 + 5 = 15

        // Test when input is 10
        assertEquals(55, ex2.sum(10)); // 1 + 2 + ... + 10 = 55
    }
}