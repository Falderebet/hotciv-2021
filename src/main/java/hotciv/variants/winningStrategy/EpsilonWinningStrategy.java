package hotciv.variants.winningStrategy;

import hotciv.framework.Player;
import hotciv.standard.GameImpl;

public class EpsilonWinningStrategy implements WinningStrategy{

    @Override
    public Player getWinner(GameImpl game) {

        if(game.getSucceededAttacks(Player.RED) >= 3) {
            return Player.RED;
        }
        if(game.getSucceededAttacks(Player.BLUE) >= 3) {
            return Player.BLUE;
        }
        return null;
    }
}
