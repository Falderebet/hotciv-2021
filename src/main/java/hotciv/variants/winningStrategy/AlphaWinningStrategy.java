package hotciv.variants.winningStrategy;

import hotciv.framework.Game;
import hotciv.framework.Player;
import hotciv.standard.GameImpl;

import static hotciv.framework.GameConstants.WINNINGAGE;

public class AlphaWinningStrategy implements WinningStrategy {

    @Override
    public Player getWinner(GameImpl game) {
        if(game.getAge() >= WINNINGAGE) {
            return Player.RED;
        }
        else {
            return null;
        }
    }
}
