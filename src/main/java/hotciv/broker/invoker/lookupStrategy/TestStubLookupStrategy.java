package hotciv.broker.invoker.lookupStrategy;

import hotciv.broker.stubs.StubCity2;
import hotciv.broker.stubs.StubTile2;
import hotciv.broker.stubs.StubUnit2;
import hotciv.framework.*;

public class TestStubLookupStrategy implements LookupInvoker {
    @Override
    public Unit lookupUnit(String objectID, NameService nameService) {
        return new StubUnit2();
    }

    @Override
    public City lookupCity(String objectID, NameService nameService) {
        return new StubCity2(Player.GREEN, 7);
    }

    @Override
    public Tile lookupTile(String objectID, NameService nameService) {
        return new StubTile2();
    }
}
