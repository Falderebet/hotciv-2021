package hotciv.broker.proxies;

import frds.broker.ClientProxy;
import frds.broker.IPCException;
import frds.broker.Requestor;
import hotciv.broker.OperationNames;
import hotciv.framework.City;
import hotciv.framework.Player;

import javax.servlet.http.HttpServletResponse;

public class CityProxy implements City, ClientProxy {

    private final Requestor requestor;
    private final String objectID;

    public CityProxy(String objectID, Requestor requestor) {
        this.requestor = requestor;
        this.objectID = objectID;
    }

    @Override
    public Player getOwner() {
        Player player;
        try {
            player = requestor.sendRequestAndAwaitReply(objectID,
                    OperationNames.GET_OWNER_OF_CITY, Player.class);
        } catch (IPCException e) {
            if (e.getStatusCode() != HttpServletResponse.SC_NOT_FOUND) {
                throw e;
            }
            player = null;
        }
        return player;
    }

    @Override
    public int getSize() {
        int size;
        try {
            size = requestor.sendRequestAndAwaitReply(objectID,
                    OperationNames.GET_SIZE, Integer.class);
        } catch (IPCException e) {
            if (e.getStatusCode() != HttpServletResponse.SC_NOT_FOUND) {
                throw e;
            }
            size = 0;
        }
        return size;
    }

    @Override
    public int getTreasury() {
        int treasury;
        try {
            treasury = requestor.sendRequestAndAwaitReply(objectID,
                    OperationNames.GET_TREASURY, Integer.class);
        } catch (IPCException e) {
            if (e.getStatusCode() != HttpServletResponse.SC_NOT_FOUND) {
                throw e;
            }
            treasury = 0;
        }
        return treasury;
    }

    @Override
    public String getProduction() {
        String produciton;
        try {
            produciton = requestor.sendRequestAndAwaitReply(objectID,
                    OperationNames.GET_PRODUCTION, String.class);
        } catch (IPCException e) {
            if (e.getStatusCode() != HttpServletResponse.SC_NOT_FOUND) {
                throw e;
            }
            produciton = null;
        }
        return produciton;
    }

    @Override
    public String getWorkforceFocus() {
        String workforce;
        try {
            workforce = requestor.sendRequestAndAwaitReply(objectID,
                    OperationNames.GET_WORKFORCEFOCUS, String.class);
        } catch (IPCException e) {
            if (e.getStatusCode() != HttpServletResponse.SC_NOT_FOUND) {
                throw e;
            }
            workforce = null;
        }
        return workforce;
    }

    @Override
    public String getCityID() {
        return this.objectID;
    }
}
