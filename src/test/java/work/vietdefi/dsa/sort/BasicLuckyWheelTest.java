package work.vietdefi.dsa.sort;

import org.junit.jupiter.api.Test;
import work.vietdefi.challenge.luckywheel.BasicLuckyWheel;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BasicLuckyWheelTest {
    @Test
    public void testSpinRate() {
        int[] percentages = {1, 4, 10, 15, 15, 15, 15, 25};
        double[] allowable_deviation = new double[]{0.02, 0.02};
        BasicLuckyWheel basicLuckyWheel = new BasicLuckyWheel(percentages, allowable_deviation);
        int totalSpin = 1000000;
        int[] prizeCounts = new int[basicLuckyWheel.shuffledRewards.length];
        int spinCount = 0;
        for(int i = 0; i < totalSpin; i++) {
            int prize = basicLuckyWheel.spin();
            spinCount++;
            prizeCounts[prize]++;
            if(prize < allowable_deviation.length){
                double deviation = (1.0 * prizeCounts[prize] / spinCount) - (1.0 * percentages[prize] / 100);
                assertTrue(deviation < allowable_deviation[prize]);
            }
        }
        System.out.println(basicLuckyWheel);
    }
}