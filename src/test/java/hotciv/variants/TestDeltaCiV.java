package hotciv.variants;

import hotciv.domain.DeltaCivFactory;
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
import hotciv.variants.winningStrategy.AlphaWinningStrategy;
import hotciv.variants.winningStrategy.BetaWinningStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static hotciv.framework.GameConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestDeltaCiV {
    private GameImpl gameImpl;

    @BeforeEach
    public void setUp() {
        gameImpl = new GameImpl(new DeltaCivFactory());
    }


    @Test
    public void worldSizeIsCorrect() {
        Position lastTile = new Position(WORLDSIZE - 1, WORLDSIZE - 1);
        Position notATile = new Position(WORLDSIZE, WORLDSIZE + 1);

        assertThat(gameImpl.getTileAt(lastTile), is(not(nullValue())));
        assertThat(gameImpl.getTileAt(notATile), is(nullValue()));
    }

    @Test
    public void oceanTileOn8_6() {
        Tile tile = gameImpl.getTileAt(new Position(8,6));
        assertThat(tile.getTypeString(), is(OCEANS));
    }

    @Test
    public void mountainTileOn0_5() {
        Tile tile = gameImpl.getTileAt(new Position(0,5));
        assertThat(tile.getTypeString(), is(MOUNTAINS));
    }

    @Test
    public void hillTileOn1_4() {
        Tile tile = gameImpl.getTileAt(new Position(1,4));
        assertThat(tile.getTypeString(), is(HILLS));
    }

    @Test
    public void forestTileOn1_9() {
        Tile tile = gameImpl.getTileAt(new Position(1,9));
        assertThat(tile.getTypeString(), is(FOREST));
    }

    @Test
    public void plainTileOn0_3() {
        Tile tile = gameImpl.getTileAt(new Position(0,3));
        assertThat(tile.getTypeString(), is(PLAINS));
    }

    @Test
    public void redCityOn8_12() {
        assertThat(gameImpl.getCityAt(new Position(8,12)).getOwner(), is(Player.RED));
    }

    @Test
    public void blueCityAt4_5() {
        assertThat(gameImpl.getCityAt(new Position(4,5)).getOwner(), is(Player.BLUE));
    }

    @Test
    public void redArcherIsOn3_8() {
        assertThat(gameImpl.getUnitAt(new Position(3,8)).getOwner(), is(Player.RED));
        assertThat(gameImpl.getUnitAt(new Position(3,8)).getTypeString(), is(ARCHER));
    }

    @Test
    public void blueLegion4_4() {
        assertThat(gameImpl.getUnitAt(new Position(4,4)).getOwner(), is(Player.BLUE));
        assertThat(gameImpl.getUnitAt(new Position(4,4)).getTypeString(), is(LEGION));
    }

    @Test
    public void redSettler5_5() {
        assertThat(gameImpl.getUnitAt(new Position(5,5)).getOwner(), is(Player.RED));
        assertThat(gameImpl.getUnitAt(new Position(5,5)).getTypeString(), is(SETTLER));
    }


}
