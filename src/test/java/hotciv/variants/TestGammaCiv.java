package hotciv.variants;

import hotciv.decorator.GameDecorator;
import hotciv.domain.GammaCivFactory;
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
import hotciv.variants.winningStrategy.AlphaWinningStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static hotciv.framework.GameConstants.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.*;

public class TestGammaCiv {
    private GameImpl gameImpl;
    private Game gameDecorater;

    private Position settlerPosition = new Position(4,3);
    private Position archerPosition = new Position(2,0);


    /**
     * Fixture for alphaciv testing.
     */
    @BeforeEach
    public void setUp() {
        gameImpl = new GameImpl(new GammaCivFactory());
        gameDecorater = new GameDecorator(gameImpl);
    }

    @Test
    public void testSettlersActionBuildCityAtSettlerPosition() {
        gameDecorater.performUnitActionAt(settlerPosition);
        assertThat(gameImpl.getCityAt(settlerPosition), is(not(nullValue())));

    }


    @Test
    public void testSettlersIsRemovedFromWorld() {
        gameImpl.performUnitActionAt(settlerPosition);
        assertThat(gameImpl.getUnitAt(settlerPosition), is(nullValue()));
    }

    @Test
    public void cityHasPopulationSizeOne(){
        gameImpl.performUnitActionAt(settlerPosition);
        assertThat(gameImpl.getCityAt(settlerPosition).getSize(), is(1));
    }

    @Test
    public void cityHasSameOwnerAsUnit(){
        Player player = gameImpl.getUnitAt(settlerPosition).getOwner();
        gameImpl.performUnitActionAt(settlerPosition);
        assertThat(player, is(gameImpl.getCityAt(settlerPosition).getOwner()));
    }

    @Test
    public void archerDefensiveStrengthIsDoubledWhenFortified(){
        gameImpl.performUnitActionAt(archerPosition);
        assertThat(gameImpl.getUnitAt(archerPosition).getDefensiveStrength(), is(6));
    }

    @Test
    public void archerCantMoveWhenFortified(){
        gameImpl.performUnitActionAt(archerPosition);
        assertThat(gameImpl.getUnitAt(archerPosition).getMoveCount(), is(0));
    }
    @Test
    public void fortificationRemovedProbably(){
        gameImpl.performUnitActionAt(archerPosition);
        gameImpl.performUnitActionAt(archerPosition);
        assertThat(gameImpl.getUnitAt(archerPosition).getMoveCount(), is(1));
        assertThat(gameImpl.getUnitAt(archerPosition).getDefensiveStrength(), is(ARCHERDEFENSE));
    }


}
