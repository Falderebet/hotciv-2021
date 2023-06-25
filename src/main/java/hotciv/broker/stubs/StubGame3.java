package hotciv.broker.stubs;

import frds.broker.Servant;
import hotciv.framework.*;
import hotciv.standard.TileImpl;

import static hotciv.framework.GameConstants.DESERT;
import static hotciv.framework.GameConstants.UNITMAXMOVE;

public class StubGame3 implements Game, Servant {
    private Position position_of_green_city = new Position(1,1);


    @Override
    public Tile getTileAt(Position p) {
        return new TileImpl(DESERT);
    }

    @Override
    public Unit getUnitAt(Position p) {
        if(p.getColumn() == 1) {
            return new StubUnit2();
        }
        return null;
    }

    @Override
    public City getCityAt(Position p) {
        if (p.equals(position_of_green_city)) {
            System.out.println("JEG ER I StubGame");
            return new StubCity2(Player.GREEN, 4);
        }
        return null;
    }



    @Override
    public Player getPlayerInTurn() {
        return Player.GREEN;
    }

    @Override
    public Player getWinner() {
        return Player.YELLOW;
    }

    @Override
    public int getAge() {
        return 42;
    }

    @Override
    public boolean moveUnit(Position from, Position to) {
        if (legalMove(from, to)) {
            return true;
        }
        return false;
    }

    private boolean legalMove(Position from, Position to) {
        if (!checkLegalDistanceMove(from, to)) return false;
        return true;
    }

    private boolean checkLegalDistanceMove(Position from, Position to) {
        int deltaC = from.getColumn() - to.getColumn();
        int deltaR = from.getRow() - to.getRow();

        if (deltaC <= UNITMAXMOVE && deltaR <= UNITMAXMOVE) {
            if (deltaC >= -UNITMAXMOVE && deltaR >= -UNITMAXMOVE) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void endOfTurn() {
    }

    @Override
    public void changeWorkForceFocusInCityAt(Position p, String balance) {

    }

    @Override
    public void changeProductionInCityAt(Position p, String unitType) {

    }

    @Override
    public void performUnitActionAt(Position p) {

    }

    @Override
    public void addObserver(GameObserver observer) {

    }

    @Override
    public void setTileFocus(Position position) {

    }


}
