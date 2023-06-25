package hotciv.broker.invoker;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import frds.broker.Invoker;
import frds.broker.ReplyObject;
import frds.broker.RequestObject;
import hotciv.broker.OperationNames;
import hotciv.broker.invoker.lookupStrategy.LookupInvoker;
import hotciv.broker.stubs.StubTile2;
import hotciv.framework.NameService;
import hotciv.framework.Tile;
import hotciv.standard.TileImpl;

import javax.servlet.http.HttpServletResponse;

public class HotCivTileInvoker implements Invoker {
    private Gson gson;
    private Tile tile;
    private NameService nameService;
    private LookupInvoker lookupStrategy;

    public HotCivTileInvoker(Tile tileServant, NameService nameService, LookupInvoker lookupStrategy) {
        this.lookupStrategy = lookupStrategy;
        gson = new Gson();
        this.tile = tileServant;
        this.nameService = nameService;
    }

    @Override
    public String handleRequest(String request) {
        RequestObject requestObject = gson.fromJson(request, RequestObject.class);
        String objectID = requestObject.getObjectId();
        String operationName = requestObject.getOperationName();
        String payload = requestObject.getPayload();

        ReplyObject reply = null;

        // Demarshall
        JsonParser parser = new JsonParser();
        JsonArray array =
                parser.parse(payload).getAsJsonArray();

        Tile tile = lookupTile(objectID);

        if (operationName.equals(OperationNames.GET_TILE_TYPESTRING)) {
            String tileString = tile.getTypeString();
            reply = new ReplyObject(HttpServletResponse.SC_CREATED, gson.toJson(tileString));
        }

        return gson.toJson(reply);
    }

    private Tile lookupTile(String objectID) {
        return lookupStrategy.lookupTile(objectID, nameService);
    }
}
