package hotciv.broker.main;

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

    public NameServiceImpl(){
        this.cityMap = new HashMap<>();
        this.unitMap = new HashMap<>();
        this.tileMap = new HashMap<>();
    }
    @Override
    public void putCity(String objectId, City city) {
        cityMap.put(objectId, city);
    }

    @Override
    public void putUnit(String objectId, Unit unit) {
        unitMap.put(objectId, unit);

    }

    @Override
    public void putTile(String objectId, Tile tile) {
        tileMap.put(objectId,tile);
    }

    @Override
    public City getCity(String objectId) {
       return cityMap.get(objectId);
    }

    @Override
    public Unit getUnit(String objectId) {
        return unitMap.get(objectId);
    }

    @Override
    public Tile getTile(String objectId) {
        return tileMap.get(objectId);
    }
}
