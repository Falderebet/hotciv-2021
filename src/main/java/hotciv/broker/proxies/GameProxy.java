package hotciv.broker.proxies;

import frds.broker.ClientProxy;
import frds.broker.IPCException;
import frds.broker.Requestor;
import hotciv.broker.OperationNames;
import hotciv.framework.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class GameProxy implements Game, ClientProxy{

    public static final String GAME_OBJECTID = "singleton";

    private final Requestor requestor;
    private ArrayList<GameObserver> gameObserverArrayList= new ArrayList<>();
    private GameObserver observer;

    public GameProxy(Requestor requestor) {
        this.requestor = requestor;
    }

    @Override
    public Tile getTileAt(Position p) {
        String tileID;
        Tile tile;
        try {
            tileID = requestor.sendRequestAndAwaitReply(GAME_OBJECTID,
                    OperationNames.GET_TILE_AT, String.class, p);
            if(tileID == null){
                return null;
            } else {
                tile = new TileProxy(tileID, requestor);
            }
        } catch (IPCException e) {
            if (e.getStatusCode() != HttpServletResponse.SC_NOT_FOUND) {
                throw e;
            }
            tile = null;
        }
        return tile;
    }

    @Override
    public Unit getUnitAt(Position p) {
        String unitID;
        Unit unit;
        try {
            unitID = requestor.sendRequestAndAwaitReply(GAME_OBJECTID,
                    OperationNames.GET_UNIT_AT, String.class, p);
            if(unitID == null){
                return null;
            } else {
                unit = new UnitProxy(unitID, requestor);
            }
        } catch (IPCException e) {
            if (e.getStatusCode() != HttpServletResponse.SC_NOT_FOUND) {
                throw e;
            }
            unit = null;
        }
        return unit;
    }

    @Override
    public City getCityAt(Position p) {
        String cityID;
        City city;

        try {
            cityID = requestor.sendRequestAndAwaitReply(GAME_OBJECTID,
                    OperationNames.GET_CITY_AT, String.class, p);
            if(cityID == null){
                return null;
            } else {
                city = new CityProxy(cityID, requestor);
            }
        } catch (IPCException e) {
            if (e.getStatusCode() != HttpServletResponse.SC_NOT_FOUND) {
                throw e;
            }
            city = null;
        }
        return city;
    }

    @Override
    public Player getPlayerInTurn() {
        Player player;
        try {
            player = requestor.sendRequestAndAwaitReply(GAME_OBJECTID,
                    OperationNames.GET_PLAYER_IN_TURN, Player.class);
        } catch (IPCException e) {
            if (e.getStatusCode() != HttpServletResponse.SC_NOT_FOUND) {
                throw e;
            }
            player = null;
        }
        return player;
    }

    @Override
    public Player getWinner() {
        Player player;
        try {
            player = requestor.sendRequestAndAwaitReply(GAME_OBJECTID,
                    OperationNames.GET_WINNER, Player.class);
        } catch (IPCException e) {
            if (e.getStatusCode() != HttpServletResponse.SC_NOT_FOUND) {
                throw e;
            }
            player = null;
        }
        return player;
    }

    @Override
    public int getAge() {
        int age;
        try {
            age = requestor.sendRequestAndAwaitReply(GAME_OBJECTID,
                    OperationNames.GET_AGE, int.class);
        } catch (IPCException e) {
            if (e.getStatusCode() != HttpServletResponse.SC_NOT_FOUND) {
                throw e;
            }
            age = 0;
        }
        return age;
    }

    @Override
    public boolean moveUnit(Position from, Position to) {
        boolean move;
        try{
            move = requestor.sendRequestAndAwaitReply(GAME_OBJECTID, OperationNames.MOVE_UNIT, boolean.class,
                    from, to);
        } catch (IPCException e) {
            if (e.getStatusCode() != HttpServletResponse.SC_NOT_FOUND) {
                throw e;
            }
            move = false;
        }
        if(move) {
            observer.worldChangedAt(from);
            observer.worldChangedAt(to);
        }
       return move;
    }

    @Override
    public void endOfTurn() {
        requestor.sendRequestAndAwaitReply(GAME_OBJECTID, OperationNames.END_OF_TURN, void.class);
        if(getPlayerInTurn() == Player.RED) {
            observer.turnEnds(Player.BLUE, getAge());
        } else {
            observer.turnEnds(Player.RED, getAge());
        }
    }

    // Frederik
    @Override
    public void changeWorkForceFocusInCityAt(Position p, String balance) {
        requestor.sendRequestAndAwaitReply(GAME_OBJECTID, OperationNames.CHANGE_WORKFORCE_FOCUS, void.class,
                p, balance);
    }

    // Anni
    @Override
    public void changeProductionInCityAt(Position p, String unitType) {
        requestor.sendRequestAndAwaitReply(GAME_OBJECTID, OperationNames.CHANGE_PRODUCTION_IN_A_CITY, void.class, p, unitType);
        observer.worldChangedAt(p);
    }

    // Fred
    @Override
    public void performUnitActionAt(Position p) {
        requestor.sendRequestAndAwaitReply(GAME_OBJECTID, OperationNames.PERFORM_UNIT_ACTION, void.class,
                p);
        observer.worldChangedAt(p);
    }

    //ANNI
    @Override
    public void addObserver(GameObserver observer) {
        this.observer = observer;
    }

    //FRED
    @Override
    public void setTileFocus(Position position) {
        //requestor.sendRequestAndAwaitReply(GAME_OBJECTID, OperationNames.SET_TILE_FOCUS, void.class,
          //      position);
        observer.tileFocusChangedAt(position);
    }

}
