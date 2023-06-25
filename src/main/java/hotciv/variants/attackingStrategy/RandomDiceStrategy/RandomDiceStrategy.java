package hotciv.variants.attackingStrategy.RandomDiceStrategy;

import java.util.Random;

public class RandomDiceStrategy implements DiceStrategy {
    @Override
    public int rollDice() {
        Random dice = new Random();
        return dice.nextInt(6) + 1;
    }
}
