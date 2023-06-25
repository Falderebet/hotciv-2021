package hotciv.broker.proxies;

import frds.broker.ClientProxy;
import frds.broker.IPCException;
import frds.broker.Requestor;
import hotciv.broker.OperationNames;
import hotciv.framework.Tile;

import javax.servlet.http.HttpServletResponse;

public class TileProxy implements Tile, ClientProxy {
    private final Requestor requestor;
    private final String objectId;

    public TileProxy(String objectId, Requestor requestor) {
        this.requestor = requestor;
        this.objectId = objectId;
    }

    @Override
    public String getTypeString() {
        String typeString;
        try {
            typeString = requestor.sendRequestAndAwaitReply(objectId,
                    OperationNames.GET_TILE_TYPESTRING, String.class);
        } catch (IPCException e) {
            if (e.getStatusCode() != HttpServletResponse.SC_NOT_FOUND) {
                throw e;
            }
            typeString = null;
        }
        return typeString;
    }

    @Override
    public String getTileID() {
        return this.objectId;
    }
}
