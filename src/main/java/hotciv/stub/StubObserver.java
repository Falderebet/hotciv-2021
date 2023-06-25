package hotciv.stub;

import hotciv.framework.GameObserver;
import hotciv.framework.Player;
import hotciv.framework.Position;

public class StubObserver implements GameObserver {
    public int worldChangedAtIncrement;
    public int endOfTurnIncrement;
    public int tileFocusChangedIncrement;


    public StubObserver() {
        this.worldChangedAtIncrement = 0;
        this.endOfTurnIncrement = 0;
    }

    public int getWorldChangedAtIncrement() {
        return this.worldChangedAtIncrement;
    }

    @Override
    public void worldChangedAt(Position pos) {
        this.worldChangedAtIncrement++;
    }

    @Override
    public void turnEnds(Player nextPlayer, int age) {
        endOfTurnIncrement++;
    }
    public int getEndOfTurnIncrement(){
       return endOfTurnIncrement;
    }

    @Override
    public void tileFocusChangedAt(Position position) {
        tileFocusChangedIncrement++;
    }

    public int getTileFocusChangedIncrement(){
        return tileFocusChangedIncrement;
    }


}
