package hotciv.broker;

import hotciv.framework.City;
import hotciv.framework.NameService;
import hotciv.framework.Tile;
import hotciv.framework.Unit;

import java.util.HashMap;
import java.util.Map;

public class NameServiceImpl implements NameService {
    private Map<String, City> cityMap;
    private Map<String, Unit> unitMap;
    private Map<String, Tile> tileMap;

    public NameServiceImpl() {
        this.cityMap = new HashMap<>();
        this.unitMap = new HashMap<>();
        this.tileMap = new HashMap<>();
    }

    @Override
    public void putCity(String objectID, City city) {
        cityMap.put(objectID, city);
    }

    @Override
    public void putUnit(String objectID, Unit unit) {
        unitMap.put(objectID, unit);
    }

    @Override
    public void putTile(String objectID, Tile tile) {
        tileMap.put(objectID, tile);
    }

    @Override
    public City getCity(String objectID) {
        return cityMap.get(objectID);
    }

    @Override
    public Unit getUnit(String objectID) {
        return unitMap.get(objectID);
    }

    @Override
    public Tile getTile(String objectID) {
        return tileMap.get(objectID);
    }
}
