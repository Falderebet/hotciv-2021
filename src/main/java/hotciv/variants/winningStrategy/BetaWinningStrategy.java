package hotciv.variants.winningStrategy;

import hotciv.framework.Game;
import hotciv.framework.Player;
import hotciv.framework.Position;
import hotciv.standard.CityImpl;
import hotciv.standard.GameImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static hotciv.framework.GameConstants.WORLDSIZE;

public class BetaWinningStrategy implements WinningStrategy {

    @Override
    public Player getWinner(GameImpl game) {
        int redCities = 0;
        int blueCities = 0;
        ArrayList<CityImpl> cityList;
        cityList = game.getCityMap();
        for(CityImpl c : cityList) {
            if (c.getOwner() == Player.RED) {
                redCities++;
            }
            if (c.getOwner() == Player.BLUE) {
                blueCities++;
            }
        }

        if (redCities == 0) {
            return Player.BLUE;
        }
        if (blueCities == 0) {
            return Player.RED;
        }
        else {
            return null;
        }
    }
}
