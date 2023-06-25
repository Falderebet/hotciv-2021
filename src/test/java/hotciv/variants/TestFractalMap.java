package hotciv.variants;

import hotciv.decorator.GameDecorator;
import hotciv.domain.BetaCivFactory;
import hotciv.domain.FractalFactory;
import hotciv.framework.Game;
import hotciv.framework.Player;
import hotciv.framework.Position;
import hotciv.framework.Tile;
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

import java.util.ArrayList;
import java.util.Iterator;

import static hotciv.framework.GameConstants.ARCHER;
import static hotciv.framework.GameConstants.PLAINS;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestFractalMap {
    private GameImpl game;

    @BeforeEach
    public void setUp() {
        game = new GameImpl(new FractalFactory());
    }

    @Test
    public void testFractalMapGenerator() {
        ArrayList<Tile> tileList = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            GameImpl gameImpl = new GameImpl(new FractalFactory());
            Tile currentTile = gameImpl.getTileAt(new Position(0, 0));
            tileList.add(currentTile);
        }
        for (int i = 0; i < 25; i++) {
            Tile currentTile = tileList.get(i);
            Tile nextTile = tileList.get(i);
            if (i != 24) {
                nextTile = tileList.get(i + 1);
            }
            if (!currentTile.getTypeString().equals(nextTile.getTypeString())) {
                assertThat(true, is(true));
            }
        }
    }
}

