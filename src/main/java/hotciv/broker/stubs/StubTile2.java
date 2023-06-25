package hotciv.broker.stubs;

import hotciv.framework.GameConstants;
import hotciv.framework.Tile;
import hotciv.framework.TileProxy;

import java.util.UUID;

public class StubTile2 implements Tile{
    String id;

    public StubTile2() {
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public String getTypeString() {
        return GameConstants.DESERT;
    }

    @Override
    public String getTileID() {
        return this.id;
    }
}
