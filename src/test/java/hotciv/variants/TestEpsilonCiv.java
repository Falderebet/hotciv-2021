package hotciv.variants;

import hotciv.domain.EpsilonCivFactory;
import hotciv.domain.EpsilonTestFactory;
import hotciv.framework.Player;
import hotciv.framework.Position;
import hotciv.framework.Tile;
import hotciv.standard.GameImpl;
import hotciv.standard.UnitImpl;
import hotciv.variants.UnitActionsStrategy.AlphaUnitActionStrategy;
import hotciv.variants.WorldLayoutStrategy.AlphaWorldLayout;
import hotciv.variants.WorldLayoutStrategy.DeltaWorldLayout;
import hotciv.variants.agingStrategy.AlphaAgingStrategy;
import hotciv.variants.agingStrategy.BetaAgingStrategy;
import hotciv.variants.attackingStrategy.AlphaAttackingStrategy;
import hotciv.variants.attackingStrategy.EpsilonAttackingStrategy;
import hotciv.variants.attackingStrategy.RandomDiceStrategy.DiceStrategy;
import hotciv.variants.attackingStrategy.RandomDiceStrategy.FixedRandomValueStrategy;
import hotciv.variants.attackingStrategy.RandomDiceStrategy.RandomDiceStrategy;
import hotciv.variants.winningStrategy.AlphaWinningStrategy;
import hotciv.variants.winningStrategy.BetaWinningStrategy;
import hotciv.variants.winningStrategy.EpsilonWinningStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static hotciv.framework.GameConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestEpsilonCiv {
    private GameImpl gameImpl;
    private EpsilonAttackingStrategy epsilonAttack;
    private DiceStrategy randomDice;

    @BeforeEach
    public void setUp() {
        epsilonAttack = new EpsilonAttackingStrategy(new FixedRandomValueStrategy());
        randomDice = new RandomDiceStrategy();
        gameImpl = new GameImpl(new EpsilonTestFactory());
    }

    @Test
    public void testSucceededAttacks() {
        gameImpl.moveUnit(new Position(4,3), new Position(4,2));
        gameImpl.endOfTurn();
        gameImpl.moveUnit(new Position(3,2), new Position(4,2));
        assertThat(gameImpl.getSucceededAttacks(Player.BLUE), is(1));
    }

    @Test
    public void testEpsilonWin() {
        gameImpl.moveUnit(new Position(4,3), new Position(4,2));
        gameImpl.endOfTurn();
        gameImpl.moveUnit(new Position(3,2), new Position(4,2));

        int succeededAttacks= gameImpl.getSucceededAttacks(Player.BLUE);

        for(int i = 1; i < (succeededAttacks * 3); i++) {
            gameImpl.newSuccesfullAttack(Player.BLUE);
        }
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();

       assertThat(gameImpl.getWinner(), is(Player.BLUE));
    }

    // Tests that the adjacent tile strength measurement is correct, and only counted if your own unit.
    @Test
    public void testAdjacentStrengthAreAdded() {
        assertThat(epsilonAttack.getAdjecentStrengths(gameImpl, new Position(2,0)), is(0));
    }

    // Tests that tile hills add 2 to strength, and city adds 3.
    @Test
    public void testTileAddsCorrectValue() {
        gameImpl.moveUnit(new Position(2,0), new Position(1,1));
        assertThat(epsilonAttack.getTileMultiplier(gameImpl, new Position(1,1)), is(3));
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.moveUnit(new Position(1,1), new Position(0,1));
        assertThat(epsilonAttack.getTileMultiplier(gameImpl, new Position(0,1)), is(2));
    }

    // Tests that when a unit is on a city its attack strength is multiplied by 3, or 2 if on a hills tile.
    @Test
    public void testUnitStrengthIsProbablyAdded() {
        // Moves unit to a city tile.
        gameImpl.moveUnit(new Position(2,0), new Position(1,1));
        int attackingStrength = epsilonAttack.getUnitCombinedStrength(gameImpl, new Position(1,1), true);
        assertThat(attackingStrength, is(6));
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        // Moves unit to a hills tile.
        gameImpl.moveUnit(new Position(1,1), new Position(0,1));
        int attackingStrengthOnHills = epsilonAttack.getUnitCombinedStrength(gameImpl, new Position(0,1), true);
        assertThat(attackingStrengthOnHills, is(4));
    }

    @Test
    public void testRandomDiceRoll(){
        int diceCount = 0;
        for (int i = 0 ; i<1000 ; i++){
            diceCount  += randomDice.rollDice();
        }
        float average = diceCount/1000;
        assertTrue(average >= 3 && average<=4);
    }
}













