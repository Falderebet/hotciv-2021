package hotciv.variants.UnitActionsStrategy;

import hotciv.framework.Position;
import hotciv.standard.GameImpl;

import java.util.Map;

public interface UnitActionsStrategy {

    void performUnitAction(GameImpl game, Position p);
}


