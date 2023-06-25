package hotciv.variants.WorldLayoutStrategy;

import hotciv.framework.*;
import hotciv.standard.CityImpl;
import hotciv.standard.TileImpl;
import hotciv.standard.UnitImpl;


import java.util.HashMap;
import java.util.Map;

import static hotciv.framework.GameConstants.*;

public class DeltaWorldLayout implements WorldLayoutStrategy {
    private String[] layoutStrategy;
    private Map<Position, Unit> unitMap;

    public DeltaWorldLayout(String[] layoutStrategy, Map<Position, Unit> unitMap){
        this.layoutStrategy = layoutStrategy;
        this.unitMap = unitMap;
    }


    @Override
    public Map<Position, Tile> tileMap(Game game) {
        return createTileLayout(this.layoutStrategy);
    }

    @Override
    public Map<Position, CityImpl> cityMap(Game game) {
        Map<Position, CityImpl> cityMap = new HashMap<>();
        CityImpl redCity = new CityImpl(Player.RED);
        CityImpl blueCity = new CityImpl(Player.BLUE);

        cityMap.put(new Position(8,12), redCity);
        cityMap.put(new Position(4,5), blueCity);

        return cityMap;
    }

    @Override
    public Map<Position, Unit> unitMap(Game game) {
        return unitMap;
    }


    private Map<Position, Tile> createTileLayout(String[] layout) {
        Map<Position,Tile> theWorld = new HashMap<Position,Tile>();
        String line;
        for (int r = 0; r < GameConstants.WORLDSIZE; r++ ) {
            line = layout[r];
            for ( int c = 0; c < GameConstants.WORLDSIZE; c++ ) {
                char tileChar = line.charAt(c);
                String type = null;
                if ( tileChar == '.' ) { type = OCEANS; }
                if ( tileChar == 'o' ) { type = PLAINS; }
                if ( tileChar == 'M' ) { type = MOUNTAINS; }
                if ( tileChar == 'f' ) { type = FOREST; }
                if ( tileChar == 'h' ) { type = HILLS; }
                if ( tileChar == 'd' ) { type = DESERT; }
                Position p = new Position(r,c);
                theWorld.put( p, new TileImpl(type));
            }
        }
        return theWorld;
    }
}
