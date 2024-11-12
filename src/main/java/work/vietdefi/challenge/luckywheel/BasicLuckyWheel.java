package work.vietdefi.challenge.luckywheel;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class BasicLuckyWheel {
    Random random;
    public int[] shuffledRewards;
    private int[] prizeCounts; // Count the number of times each prize is won
    private int spinCount; // Count the total number of spins
    int[] percentages;
    double[] allowable_deviation;
    int errorCount;

    public BasicLuckyWheel(int[] percentages, double[] allowable_deviation){
        // Create a shuffled list containing all rewards according to their probabilities
        List<Integer> shuffledList = new LinkedList<>();
        for(int i = 0; i < percentages.length; i++){
            for(int j = 0; j < percentages[i]; j++){
                shuffledList.add(i);
            }
        }
        Collections.shuffle(shuffledList);
        this.shuffledRewards  = shuffledList.stream().mapToInt(Integer::intValue).toArray();
        this.prizeCounts = new int[percentages.length];
        this.spinCount = 0;
        this.random = new Random();
        this.percentages = percentages;
        this.allowable_deviation = allowable_deviation;
    }



    //return the prize index
    public int spin(){
        do {
            int random_index = random.nextInt(shuffledRewards.length);
            int prize_index = shuffledRewards[random_index];
            if (prize_index < allowable_deviation.length) {
                int rewardCount = prizeCounts[prize_index] + 1;
                int newSpinCount = spinCount + 1;
                double deviation = (1.0 * rewardCount / newSpinCount) - (1.0 * percentages[prize_index] / 100);
                if (deviation >= allowable_deviation[prize_index]) {
                    errorCount++;
                    continue;
                }
            }
            prizeCounts[prize_index]++;
            spinCount++;
            return prize_index;
        }while (true);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Basic Lucky Wheel:");
        sb.append("\nSpin Count: ").append(spinCount);
        sb.append("\nPrize Count: ").append(intArrayToString(prizeCounts));
        sb.append("\nError Count: ").append(errorCount);
        sb.append("\n");
        return sb.toString();
    }

    private String intArrayToString(int[] rewardCounts) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int i = 0; i < rewardCounts.length; i++){
            if(i < rewardCounts.length-1) {
                sb.append(rewardCounts[i]).append(", ");
            }else{
                sb.append(rewardCounts[i]);
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
