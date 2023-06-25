package hotciv.variants.WorldLayoutStrategy;

import hotciv.framework.*;
import hotciv.standard.CityImpl;

import java.util.Map;

public interface WorldLayoutStrategy {


    Map<Position, Tile> tileMap(Game game);

    Map<Position, CityImpl> cityMap(Game game);

    Map<Position, Unit> unitMap(Game game);

}
