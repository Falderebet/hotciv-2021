package hotciv.variants;

import hotciv.domain.*;
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
import hotciv.variants.attackingStrategy.RandomDiceStrategy.DiceStrategy;
import hotciv.variants.attackingStrategy.RandomDiceStrategy.FixedRandomValueStrategy;
import hotciv.variants.attackingStrategy.RandomDiceStrategy.RandomDiceStrategy;
import hotciv.variants.moveUnitStrategy.ThetaMoveStrategy;
import hotciv.variants.winningStrategy.AlphaWinningStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static hotciv.framework.GameConstants.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.*;


public class TestThetaCiv {
    private GameImpl gameImpl;
    private GameImpl alphaTestGameImpl;
    private ThetaMoveStrategy moveStrategy;

    @BeforeEach
    public void setUp() {
        gameImpl = new GameImpl(new ThetaCivFactory());
        alphaTestGameImpl = new GameImpl(new ThetaTestFactory());
        moveStrategy = new ThetaMoveStrategy();
    }

    @Test
    public void desertTileOn2_2() {
        assertThat(gameImpl.getTileAt(new Position(2,2)).getTypeString(), is(DESERT));
        assertThat(gameImpl.getTileAt(new Position(8,6)).getTypeString(), is(DESERT));
    }

    @Test
    void oceanTileOn0_0() {
        assertThat(gameImpl.getTileAt(new Position(0,0)).getTypeString(), is(OCEANS));
    }

    @Test
    void sandWormIsOnTile9_6() {
        assertThat(gameImpl.getUnitAt(new Position(9,6)).getTypeString(), is(SANDWORM));
    }


    @Test
    void canSandWormMoveToNoSandBOIII() {
        gameImpl.endOfTurn();
        assertThat(gameImpl.moveUnit(new Position(9,6), new Position(9,7)), is(false));
    }

    @Test
    void SandWormCanMoveToDesert(){
        gameImpl.endOfTurn();
        gameImpl.moveUnit(new Position(9,6), new Position(8,6));
        assertThat(gameImpl.getUnitAt(new Position(8,6)), is(notNullValue()));
    }

    @Test
    void sandwormCanMoveTwice(){
        gameImpl.endOfTurn();
        gameImpl.moveUnit(new Position(9,6), new Position(8,6));
        gameImpl.moveUnit(new Position(8,6), new Position(7,6));
        assertThat(gameImpl.getUnitAt(new Position(7,6)), is(notNullValue()));
    }

    @Test
    void resetMoveCount(){
        gameImpl.endOfTurn();
        gameImpl.moveUnit(new Position(9,6), new Position(8,6));
        gameImpl.endOfTurn();
        assertThat(gameImpl.getUnitAt(new Position(8,6)).getMoveCount(), is(2));
    }

    @Test
    void cityProduceSandworm(){
        gameImpl.getCityAt(new Position(8,12)).setSelectedUnitForProduction(SANDWORM);
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        assertThat(gameImpl.getUnitAt(new Position(8, 12)), is(not(nullValue())));
    }

    @Test
    void cityProduceSandwormOnAdjecentTile(){
        gameImpl.getCityAt(new Position(8,12)).setSelectedUnitForProduction(ARCHER);
        gameImpl.getCityAt(new Position(8,12)).setSelectedUnitForProduction(SANDWORM);
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        assertThat(gameImpl.getUnitAt(new Position(8, 13)), is(not(nullValue())));
    }

    @Test
    public void cityCantProduceSandwormWithoutSand(){
        alphaTestGameImpl.getCityAt(new Position(1,1)).setSelectedUnitForProduction(SANDWORM);
        alphaTestGameImpl.endOfTurn();
        alphaTestGameImpl.endOfTurn();
        alphaTestGameImpl.endOfTurn();
        alphaTestGameImpl.endOfTurn();
        alphaTestGameImpl.endOfTurn();
        alphaTestGameImpl.endOfTurn();
        alphaTestGameImpl.endOfTurn();
        alphaTestGameImpl.endOfTurn();
        assertThat(alphaTestGameImpl.getUnitAt(new Position(1, 1)), is(nullValue()));
        assertThat(alphaTestGameImpl.getUnitAt(new Position(0, 1)), is(nullValue()));
        assertThat(alphaTestGameImpl.getUnitAt(new Position(2, 1)), is(nullValue()));
    }

    @Test
    void sandWormHas10Defense() {
        gameImpl.endOfTurn();
        Unit unit = gameImpl.getUnitAt(new Position(9,6));
        int defense = gameImpl.getUnitCharacteristics(unit.getTypeString())[DEFENSE_INDEX];
        assertThat(defense, is(10));
    }

    @Test
    void sandWormHas0Attack() {
        gameImpl.endOfTurn();
        Unit unit = gameImpl.getUnitAt(new Position(9,6));
        int defense = gameImpl.getUnitCharacteristics(unit.getTypeString())[ATTACK_INDEX];
        assertThat(defense, is(0));
    }

    @Test
    void sandwormActionKillsUnitsInNeighbourhood(){
       Unit unit = gameImpl.getUnitAt(new Position(9,6));
        gameImpl.endOfTurn();
        gameImpl.moveUnit(new Position(9,6), new Position(8,5));
        gameImpl.moveUnit(new Position(8,5), new Position(7,5));
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.moveUnit(new Position(7,5), new Position(6,5));
        gameImpl.moveUnit(new Position(6,5), new Position(5,5));
        gameImpl.performUnitActionAt(new Position(5,5 ));
        assertThat(gameImpl.getUnitAt(new Position(4,5)), is(nullValue()));

    }
}
