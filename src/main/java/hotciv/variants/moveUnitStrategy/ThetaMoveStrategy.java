package hotciv.variants.moveUnitStrategy;

import hotciv.framework.Game;
import hotciv.framework.Position;

import static hotciv.framework.GameConstants.*;

public class ThetaMoveStrategy implements MoveStrategy {
    @Override
    public boolean checkLegalTileType(Game game, Position from, Position to) {
        String tileType = game.getTileAt(to).getTypeString();

        if (tileType.equals(OCEANS) || tileType.equals(MOUNTAINS)) {
            return false;
        }
        if (checkSandWorm(game, from) && !tileType.equals(DESERT)) {
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

        if (unitType.equals(SANDWORM) && !tileType.equals(DESERT)) {
            return false;
        }
        else {
            return true;
        }
    }

    private boolean checkSandWorm(Game game, Position from) {
        if (game.getUnitAt(from) == null) {
            return false;
        }
        if (game.getUnitAt(from).getTypeString().equals(SANDWORM)) {
            return true;
        }
        else {
            return false;
        }
    }


}
