package hotciv.decorator;

import hotciv.framework.*;

public class GameDecorator implements Game {
    private Game game;

    public GameDecorator(Game game) {
        this.game = game;
    }

    @Override
    public Tile getTileAt(Position p) {
        return game.getTileAt(p);
    }

    @Override
    public Unit getUnitAt(Position p) {
        return game.getUnitAt(p);
    }

    @Override
    public City getCityAt(Position p) {
        return game.getCityAt(p);
    }

    @Override
    public Player getPlayerInTurn() {
        return game.getPlayerInTurn();
    }

    @Override
    public int getAge() {
        return game.getAge();
    }

    @Override
    public boolean moveUnit(Position from, Position to) {
        Unit fromUnit = game.getUnitAt(from);
        if (game.moveUnit(from, to)) {
            System.out.println("Player " + game.getPlayerInTurn().toString() + " moved " + fromUnit.getTypeString() +
                    " from position " + from.toString() + " to " + to.toString());
            return true;
        }
        return false;
    }

    @Override
    public Player getWinner() {
        if(game.getWinner() != null) {
            System.out.println("The winner is player: " + game.getWinner().toString());
        }
        return game.getWinner();
    }

    @Override
    public void endOfTurn() {
        game.endOfTurn();
        System.out.println(game.getPlayerInTurn().toString() + " ends turn");
    }

    @Override
    public void changeWorkForceFocusInCityAt( Position p, String balance ) {
        game.changeWorkForceFocusInCityAt(p, balance);
        System.out.print(game.getPlayerInTurn() + " changes work force focus in city at " + p.toString() + "to"
                + balance);
    }

    @Override
    public void changeProductionInCityAt( Position p, String unitType ){
        System.out.print(game.getPlayerInTurn() + " changes production in a city at " + p.toString() + "to"
                + unitType);
    }

    @Override
    public void performUnitActionAt( Position p ){
    game.performUnitActionAt(p);
    System.out.println(game.getPlayerInTurn() + " performed unit action on " + p);
    }

    @Override
    public void addObserver(GameObserver observer) {

    }

    @Override
    public void setTileFocus(Position position) {

    }
}
