package hotciv.broker.proxies;

import frds.broker.ClientProxy;
import frds.broker.IPCException;
import frds.broker.Requestor;
import hotciv.broker.OperationNames;
import hotciv.framework.Player;
import hotciv.framework.Unit;

import javax.servlet.http.HttpServletResponse;

import static hotciv.framework.GameConstants.SETTLER;

public class UnitProxy implements Unit, ClientProxy {

    private final Requestor requestor;
    private final String objectId;

    public UnitProxy(String objectId, Requestor unitRequestor) {
        this.requestor = unitRequestor;
        this.objectId = objectId;
    }


    @Override
    public String getTypeString() {
        String typeString;
        try {
            typeString = requestor.sendRequestAndAwaitReply(objectId,
                    OperationNames.GET_TYPESTRING, String.class);
        } catch (IPCException e) {
            if (e.getStatusCode() != HttpServletResponse.SC_NOT_FOUND) {
                throw e;
            }
            typeString = null;
        }
        return typeString;
    }

    @Override
    public Player getOwner() {
        Player owner;
        try {
            owner = requestor.sendRequestAndAwaitReply(objectId,
                    OperationNames.GET_UNIT_OWNER, Player.class);
        } catch (IPCException e) {
            if (e.getStatusCode() != HttpServletResponse.SC_NOT_FOUND) {
                throw e;
            }
            owner = null;
        }
        return owner;
    }

    @Override
    public int getMoveCount() {
        int moveCount;
        try {
            moveCount = requestor.sendRequestAndAwaitReply(objectId,
                    OperationNames.GET_MOVECOUNT, Integer.class);
        } catch (IPCException e) {
            if (e.getStatusCode() != HttpServletResponse.SC_NOT_FOUND) {
                throw e;
            }
            moveCount = 0;
        }
        return moveCount;
    }

    @Override
    public int getDefensiveStrength() {
        int defense;
        try {
            defense = requestor.sendRequestAndAwaitReply(objectId,
                    OperationNames.GET_DEFENSE, Integer.class);
        } catch (IPCException e) {
            if (e.getStatusCode() != HttpServletResponse.SC_NOT_FOUND) {
                throw e;
            }
            defense = 0;
        }
        return defense;
    }

    @Override
    public int getAttackingStrength() {
        int attack;
        try {
            attack = requestor.sendRequestAndAwaitReply(objectId,
                    OperationNames.GET_ATTACK, Integer.class);
        } catch (IPCException e) {
            if (e.getStatusCode() != HttpServletResponse.SC_NOT_FOUND) {
                throw e;
            }
            attack = 0;
        }
        return attack;
    }

    @Override
    public String getUnitId() {
        return objectId;
    }
}
