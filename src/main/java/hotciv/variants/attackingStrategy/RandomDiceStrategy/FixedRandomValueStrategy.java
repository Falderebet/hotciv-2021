package hotciv.variants.attackingStrategy.RandomDiceStrategy;

public class FixedRandomValueStrategy implements DiceStrategy {
    @Override
    public int rollDice() {
        return 3;
    }
}
