package hotciv.variants.WorldLayoutStrategy.customUnitMap;

import hotciv.framework.Game;
import hotciv.framework.Player;
import hotciv.framework.Position;
import hotciv.framework.Unit;
import hotciv.standard.UnitImpl;

import java.util.HashMap;
import java.util.Map;

import static hotciv.framework.GameConstants.*;

public class DeltaUnitMap implements CustomUnitMaps {
    @Override
    public Map<Position, Unit> unitMap() {
        Map<Position, Unit> unitMap = new HashMap<>();
        Unit redArcher = new UnitImpl(Player.RED, ARCHER);
        Unit blueLegion = new UnitImpl(Player.BLUE, LEGION);
        Unit redSettler = new UnitImpl(Player.RED, SETTLER);

        unitMap.put(new Position(3,8), redArcher);
        unitMap.put(new Position(4,4), blueLegion);
        unitMap.put(new Position(5,5), redSettler);

        return unitMap;
    }
}
