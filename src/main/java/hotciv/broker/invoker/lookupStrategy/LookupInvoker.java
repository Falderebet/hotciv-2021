package hotciv.broker.invoker.lookupStrategy;

import hotciv.framework.City;
import hotciv.framework.NameService;
import hotciv.framework.Tile;
import hotciv.framework.Unit;

public interface LookupInvoker {
    Unit lookupUnit(String objectID, NameService nameService);

    City lookupCity(String objectID, NameService nameService);

    Tile lookupTile(String objectID, NameService nameService);
}
