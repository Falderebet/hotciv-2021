package hotciv.variants.UnitActionsStrategy;

import hotciv.domain.ThetaTestFactory;
import hotciv.framework.Position;
import hotciv.framework.Unit;
import hotciv.standard.GameImpl;

import static hotciv.framework.GameConstants.*;

public class ThetaUnitActionStrategy implements UnitActionsStrategy{
    GammaUnitActionStrategy gammaUnitActionStrategy;

    public ThetaUnitActionStrategy() {
        gammaUnitActionStrategy = new GammaUnitActionStrategy();
    }

    @Override
    public void performUnitAction(GameImpl game, Position p) {
        if(game.getUnitAt(p).getTypeString() == SETTLER){
            gammaUnitActionStrategy.settlerAction(game, p);
        }

        else if(game.getUnitAt(p).getTypeString() == ARCHER){
            gammaUnitActionStrategy.archerAction(game, p);
        }

        else if(game.getUnitAt(p).getTypeString() == SANDWORM) {
            sandWormAction(game, p);
        }
    }

    private void sandWormAction(GameImpl game, Position pos) {
        for (Position p : hotciv.utility.Utility.get8neighborhoodOf(pos)) {
            if (game.getUnitAt(p) != null) {
                if (game.getUnitAt(pos).getOwner() != game.getUnitAt(p).getOwner()) {
                    game.removeUnit(p);
                }
            }
        }
    }
}
