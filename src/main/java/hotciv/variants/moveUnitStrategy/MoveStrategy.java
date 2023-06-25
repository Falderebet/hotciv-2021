package hotciv.variants.moveUnitStrategy;

import hotciv.framework.Game;
import hotciv.framework.Position;

public interface MoveStrategy {
    boolean checkLegalTileType(Game game, Position from, Position to);

    boolean checkLegalTileTypeWhenProducing(Game game, String unitType, Position to);
}


