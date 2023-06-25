package hotciv.variants.winningStrategy;

import hotciv.framework.Game;
import hotciv.framework.Player;
import hotciv.standard.GameImpl;

import static hotciv.framework.GameConstants.WINNINGAGE;

public interface WinningStrategy {

    /** Methods that can check if there is a winner. */
    Player getWinner(GameImpl game);
}
