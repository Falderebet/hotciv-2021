package hotciv.variants.WorldLayoutStrategy;

import hotciv.framework.*;
import hotciv.standard.CityImpl;
import hotciv.standard.TileImpl;
import thirdparty.ThirdPartyFractalGenerator;

import java.util.HashMap;
import java.util.Map;



import static hotciv.framework.GameConstants.*;

public class WorldLayoutAdapterImpl implements WorldLayoutStrategy {
    private WorldLayoutStrategy alphaWorldLayout;
    private ThirdPartyFractalGenerator generator;

    public WorldLayoutAdapterImpl() {
        this.generator = new ThirdPartyFractalGenerator();
        this.alphaWorldLayout = new AlphaWorldLayout();
    }

    @Override
    public Map<Position, Tile> tileMap(Game game) {
        return convertFractalToHotCiv();
    }

    private Map<Position, Tile> convertFractalToHotCiv() {
        Map<Position,Tile> theWorld = new HashMap<>();
        String line;
        String[] layout = convertFractalToStringArray();
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

    private String[] convertFractalToStringArray() {
        String[] landscape = new String[WORLDSIZE];
        String line = "";

        for(int r = 0; r < WORLDSIZE; r++ ) {
            line = "";
            for(int c = 0; c < WORLDSIZE; c++) {
                line = line + generator.getLandscapeAt(r,c);
            }
            landscape[r] = line;
        }
        return landscape;
    }

    private String getLine() {
        String line = null;
        for (int r = 0; r < 16; r++) {
            line = "";
            for (int c = 0; c < 16; c++) {
                line = line + generator.getLandscapeAt(r, c);
            }
        }
        return line;
    }

    @Override
    public Map<Position, CityImpl> cityMap(Game game) {
        return alphaWorldLayout.cityMap(game);
    }

    @Override
    public Map<Position, Unit> unitMap(Game game) {
        return alphaWorldLayout.unitMap(game);
    }
}
