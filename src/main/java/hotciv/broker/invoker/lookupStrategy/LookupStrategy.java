package hotciv.broker.invoker.lookupStrategy;

import hotciv.framework.City;
import hotciv.framework.NameService;
import hotciv.framework.Tile;
import hotciv.framework.Unit;

public class LookupStrategy implements LookupInvoker{

    @Override
    public Unit lookupUnit(String objectID, NameService nameService) {
        Unit unit = nameService.getUnit(objectID);
        return unit;
    }

    @Override
    public City lookupCity(String objectID, NameService nameService) {
        City city = nameService.getCity(objectID);
        return city;
    }

    @Override
    public Tile lookupTile(String objectID, NameService nameService) {
        Tile tile = nameService.getTile(objectID);
        return tile;
    }
}
