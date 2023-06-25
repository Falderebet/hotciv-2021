package hotciv.variants;

import hotciv.domain.AlphaCivFactory;
import hotciv.framework.Game;
import hotciv.framework.GameObserver;
import hotciv.framework.Player;
import hotciv.framework.Position;
import hotciv.standard.GameImpl;
import hotciv.stub.StubObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static hotciv.framework.GameConstants.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;

public class TestObserver {

    private Game game;
    private GameImpl gameImpl;
    public StubObserver testObserver;


    @BeforeEach
    public void setUp() {
        game = new GameImpl(new AlphaCivFactory());
        gameImpl = new GameImpl(new AlphaCivFactory());
        testObserver = new StubObserver();
    }

    @Test
    public void testNotifyOfGame(){
        gameImpl.addObserver(testObserver);
        gameImpl.moveUnit(new Position(2,0), new Position(2,1));
        assertThat(testObserver.getWorldChangedAtIncrement(), is(2));
    }

    @Test
    public void notifyAtTheEndOfATurn() {
       gameImpl.addObserver(testObserver);
       gameImpl.endOfTurn();
       assertThat(testObserver.getEndOfTurnIncrement(), is(1));
    }

    @Test
    public void notifyTileFocusChange() {
        gameImpl.addObserver(testObserver);
        gameImpl.setTileFocus(new Position(2,1));
        assertThat(testObserver.getTileFocusChangedIncrement(), is(1));
    }

}
