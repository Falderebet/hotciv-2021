package hotciv.variants.moveUnitStrategy;

import hotciv.framework.Game;
import hotciv.framework.Position;

import static hotciv.framework.GameConstants.MOUNTAINS;
import static hotciv.framework.GameConstants.OCEANS;

public class AlphaMoveStrategy implements MoveStrategy {
    @Override
    public boolean checkLegalTileType(Game game, Position from, Position to) {
        String tileType = game.getTileAt(to).getTypeString();

        if (tileType.equals(OCEANS) || tileType.equals(MOUNTAINS)) {
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public boolean checkLegalTileTypeWhenProducing(Game game, String unitType, Position to) {
        String tileType = game.getTileAt(to).getTypeString();

        if (tileType.equals(OCEANS) || tileType.equals(MOUNTAINS)) {
            return false;
        }
        else {
            return true;
        }
    }
}
