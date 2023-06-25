package hotciv.variants.WorldLayoutStrategy;

import hotciv.framework.*;
import hotciv.standard.CityImpl;
import hotciv.standard.TileImpl;
import hotciv.standard.UnitImpl;

import java.util.HashMap;
import java.util.Map;

import static hotciv.framework.GameConstants.*;
import static hotciv.framework.GameConstants.SETTLER;

public class AlphaWorldLayout implements WorldLayoutStrategy{

    public Map<Position, Tile> createTileMap() {
        Map<Position, Tile> tileMap = new HashMap<>();
        Tile plains = new TileImpl(PLAINS);

        for (int i = 0;  i < WORLDSIZE; i++) {
            for (int j = 0; j < WORLDSIZE; j++) {
                tileMap.put(new Position(i,j), plains);
            }
        }

        // Creation of Alphaciv tilemap
        Tile mountains = new TileImpl(MOUNTAINS);
        Tile hills = new TileImpl(HILLS);
        Tile oceans = new TileImpl(OCEANS);
        tileMap.put(new Position(1,0), oceans);
        tileMap.put(new Position(2,2), mountains);
        tileMap.put(new Position(0,1), hills);

        return tileMap;
    }

    @Override
    public Map<Position, Tile> tileMap(Game game) {
        return createTileMap();
    }

    @Override
    public Map<Position, CityImpl> cityMap(Game game) {
        Map<Position, CityImpl> cityMap = new HashMap<>();
        cityMap.put(new Position(1,1), new CityImpl(Player.RED));
        cityMap.put(new Position(4,1), new CityImpl(Player.BLUE));

        return cityMap;
    }

    @Override
    public Map<Position, Unit> unitMap(Game game) {
        Map<Position, Unit> unitMap = new HashMap<>();
        UnitImpl redArcher = new UnitImpl(Player.RED, ARCHER);
        UnitImpl blueLegion = new UnitImpl(Player.BLUE, LEGION);
        UnitImpl redSettler = new UnitImpl(Player.RED, SETTLER);

        unitMap.put(new Position(2,0), redArcher);
        unitMap.put(new Position(3,2), blueLegion);
        unitMap.put(new Position(4,3), redSettler);

        return unitMap;
    }
}
