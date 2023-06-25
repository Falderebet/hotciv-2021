package hotciv.variants;

import hotciv.domain.ZetaCivFactory;
import hotciv.framework.Game;
import hotciv.framework.Player;
import hotciv.framework.Position;
import hotciv.framework.Unit;
import hotciv.standard.CityImpl;
import hotciv.standard.GameImpl;
import hotciv.standard.UnitImpl;
import hotciv.variants.UnitActionsStrategy.AlphaUnitActionStrategy;
import hotciv.variants.UnitActionsStrategy.GammaUnitActionStrategy;
import hotciv.variants.UnitActionsStrategy.UnitActionsStrategy;
import hotciv.variants.WorldLayoutStrategy.AlphaWorldLayout;
import hotciv.variants.WorldLayoutStrategy.WorldLayoutStrategy;
import hotciv.variants.agingStrategy.AlphaAgingStrategy;
import hotciv.variants.attackingStrategy.AlphaAttackingStrategy;
import hotciv.variants.attackingStrategy.EpsilonAttackingStrategy;
import hotciv.variants.attackingStrategy.RandomDiceStrategy.FixedRandomValueStrategy;
import hotciv.variants.attackingStrategy.RandomDiceStrategy.RandomDiceStrategy;
import hotciv.variants.winningStrategy.AlphaWinningStrategy;
import hotciv.variants.winningStrategy.EpsilonWinningStrategy;
import hotciv.variants.winningStrategy.WinningStrategy;
import hotciv.variants.winningStrategy.ZetaWinningStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static hotciv.framework.GameConstants.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.*;

public class TestZetaCiv {
    private GameImpl gameImpl;



    @BeforeEach
    public void setUp() {
        gameImpl = new GameImpl(new ZetaCivFactory());
    }

    @Test
    public void testBetaWinningStrategyIsUsed() {
        UnitImpl unit = new UnitImpl(Player.RED, ARCHER);
        gameImpl.placeUnit(unit, new Position(4,0));
        gameImpl.moveUnit(new Position(4,0), new Position(4,1));
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        assertThat(gameImpl.getWinner(), is(Player.RED));
    }
    /**
    @Test
    public void setTestEpsilonCiv() {
        iterateRounds(19);
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
    } */

    public void iterateRounds(int rounds) {
        for (int i = 0; i < rounds; i++) {
            gameImpl.endOfTurn();
            gameImpl.endOfTurn();
        }
    }
    @Test
    public void testOneSuccessfulAttack() {
        iterateRounds(19);
        gameImpl.moveUnit(new Position(4,3), new Position(4,2));
        gameImpl.endOfTurn();
        gameImpl.moveUnit(new Position(3,2), new Position(4,2));
        assertThat(gameImpl.getSucceededAttacks(Player.BLUE), is(1));
    }

}
