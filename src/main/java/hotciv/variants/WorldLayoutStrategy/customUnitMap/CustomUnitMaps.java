package hotciv.variants.WorldLayoutStrategy.customUnitMap;

import hotciv.framework.Game;
import hotciv.framework.Position;
import hotciv.framework.Unit;

import java.util.Map;

public interface CustomUnitMaps {
    Map<Position, Unit> unitMap();
}
