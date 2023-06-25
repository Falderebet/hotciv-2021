package hotciv.variants.winningStrategy;

import hotciv.framework.Player;
import hotciv.standard.GameImpl;

public class ZetaWinningStrategy implements WinningStrategy{

    WinningStrategy winningStrategyState;
    WinningStrategy epsilonWinningState;
    WinningStrategy betaWinningState;
    int succesfullAttacksUntilNow;

    public ZetaWinningStrategy(){
        epsilonWinningState = new EpsilonWinningStrategy();
        betaWinningState = new BetaWinningStrategy();
        succesfullAttacksUntilNow = 0;
    }

    @Override
    public Player getWinner(GameImpl game) {
        if(game.getTotalAmountOfRounds() == 21) {
            game.resetSuccessfulAttacks();
        }


        if (game.getTotalAmountOfRounds() > 20) {
            return epsilonWinningState.getWinner(game);
        }
        else {
            return betaWinningState.getWinner(game);
        }
    }
}
