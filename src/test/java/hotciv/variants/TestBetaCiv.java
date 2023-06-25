package hotciv.variants;


import hotciv.decorator.GameDecorator;
import hotciv.domain.BetaCivFactory;
import hotciv.framework.Game;
import hotciv.framework.Player;
import hotciv.framework.Position;
import hotciv.standard.GameImpl;
import hotciv.standard.UnitImpl;
import hotciv.variants.UnitActionsStrategy.AlphaUnitActionStrategy;
import hotciv.variants.WorldLayoutStrategy.AlphaWorldLayout;
import hotciv.variants.agingStrategy.AlphaAgingStrategy;
import hotciv.variants.agingStrategy.BetaAgingStrategy;
import hotciv.variants.attackingStrategy.AlphaAttackingStrategy;
import hotciv.variants.winningStrategy.BetaWinningStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static hotciv.framework.GameConstants.ARCHER;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestBetaCiv {
    private GameImpl gameImpl;
    private BetaAgingStrategy betaAging;

    @BeforeEach
    public void setUp() {
        betaAging = new BetaAgingStrategy();
        gameImpl = new GameImpl(new BetaCivFactory());

    }

    @Test
    public void betaWinningStrategy() {
        UnitImpl unit = new UnitImpl(Player.RED, ARCHER);
        gameImpl.placeUnit(unit, new Position(4,0));
        gameImpl.moveUnit(new Position(4,0), new Position(4,1));
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        assertThat(gameImpl.getWinner(), is(Player.RED));
    }

    public void playRounds(int rounds){
        for(int i = 0 ; i<rounds ; i ++){
            gameImpl.endOfTurn();
            gameImpl.endOfTurn();
        }

    }

    @Test
    public void testBetaAgingStrategy(){
        assertThat(betaAging.newAge(-4000), is(-3900));
    }

    @Test
    public void testAgeBetween100BC_4000BC(){
        playRounds(1);
        assertThat(gameImpl.getAge(), is(-3900));
    }

    @Test
    public void testAgeIsMinus1WhenAgeIsMinus100(){
        playRounds(40);
        assertThat(gameImpl.getAge(), is(-1));

    }

    @Test
    public void testAgeIs1WhenAgeIsMinus1(){
        playRounds(41);
        assertThat(gameImpl.getAge(), is(1));

    }

    @Test
    public void testAgeIs50WhenAgeIs1(){
        playRounds(42);
        assertThat(gameImpl.getAge(), is(50));

    }

    @Test
    public void testAgeBetween50_1750(){
        playRounds(45);
        assertThat(gameImpl.getAge(), is(151));
    }

    @Test
    public void testAgeBetween1750_1900(){
        playRounds(77);
        assertThat(gameImpl.getAge(), is(1751));
    }

    @Test
    public void testAgeBetween1900_1970(){
        playRounds(88);
        assertThat(gameImpl.getAge(), is(1926));
    }

    @Test
    public void testAgeMoreThan1970(){
        playRounds(100);
        assertThat(gameImpl.getAge(), is(1974));
    }


}
