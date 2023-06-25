package hotciv.variants.UnitActionsStrategy;

import hotciv.framework.Position;
import hotciv.standard.CityImpl;
import hotciv.standard.GameImpl;
import hotciv.standard.UnitImpl;

import java.util.HashMap;
import java.util.Map;

import static hotciv.framework.GameConstants.*;

public class GammaUnitActionStrategy implements UnitActionsStrategy {
    @Override
    public void performUnitAction(GameImpl game, Position p) {

        if(game.getUnitAt(p).getTypeString() == SETTLER){
            settlerAction(game, p);
        }

        else if(game.getUnitAt(p).getTypeString() == ARCHER){
            archerAction(game, p);
        }
    }

    public void archerAction(GameImpl game, Position p) {
        UnitImpl unit = (UnitImpl) game.getUnitAt(p);
        if (isArcherFortified(game, unit)) {
            unit.setDefensiveStrength(ARCHERDEFENSE);
            unit.setMoveCount(1);
        }

        else {
            unit.setDefensiveStrength(ARCHERDEFENSE * 2);
            unit.setMoveCount(FORTIFIEDMOVECOUNT);
        }
    }

    private boolean isArcherFortified(GameImpl game, UnitImpl unit) {
        if (unit.getDefensiveStrength() == (ARCHERDEFENSE * 2)) {
            return true;
        }
        else {
            return false;
        }
    }

    public void settlerAction(GameImpl game, Position p){
        CityImpl city = new CityImpl(game.getUnitAt(p).getOwner());
        game.setCityAt(p, city);
        game.removeUnit(p);
    }
}
