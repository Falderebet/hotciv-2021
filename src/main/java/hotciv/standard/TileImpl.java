package hotciv.standard;

import hotciv.framework.Tile;

import java.util.UUID;

public class TileImpl implements Tile {
    private final String type;
    private final String id;

    public TileImpl(String type) {
        this.type = type;
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public String getTypeString() {
        return type;
    }

    @Override
    public String getTileID() {
        return id;
    }
}
