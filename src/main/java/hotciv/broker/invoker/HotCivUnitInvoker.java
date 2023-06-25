package hotciv.broker.invoker;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import frds.broker.Invoker;
import frds.broker.ReplyObject;
import frds.broker.RequestObject;
import hotciv.broker.OperationNames;
import hotciv.broker.invoker.lookupStrategy.LookupInvoker;
import hotciv.broker.invoker.lookupStrategy.LookupStrategy;
import hotciv.broker.stubs.StubUnit2;
import hotciv.framework.NameService;
import hotciv.framework.Player;
import hotciv.framework.Unit;

import javax.naming.Name;
import javax.servlet.http.HttpServletResponse;

public class HotCivUnitInvoker implements Invoker {
    private Gson gson;
    private Unit unit;
    private NameService nameService;
    private LookupInvoker lookupStrategy;

    public HotCivUnitInvoker(Unit unitServant, NameService nameService, LookupInvoker lookupStrategy) {
        gson = new Gson();
        this.unit = unitServant;
        this.nameService = nameService;
        this.lookupStrategy = lookupStrategy;
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

        Unit unit = lookupUnit(objectID);

        if (operationName.equals(OperationNames.GET_TYPESTRING)) {
            String unitString = unit.getTypeString();
            reply = new ReplyObject(HttpServletResponse.SC_CREATED, gson.toJson(unitString));
        } else if (operationName.equals(OperationNames.GET_UNIT_OWNER)) {
            Player owner = unit.getOwner();
            reply = new ReplyObject(HttpServletResponse.SC_CREATED, gson.toJson(owner));
        } else if (operationName.equals(OperationNames.GET_MOVECOUNT)) {
            int moveCount = unit.getMoveCount();
            reply = new ReplyObject(HttpServletResponse.SC_CREATED, gson.toJson(moveCount));
        } else if (operationName.equals(OperationNames.GET_DEFENSE)) {
            int defense = unit.getDefensiveStrength();
            reply = new ReplyObject(HttpServletResponse.SC_CREATED, gson.toJson(defense));
        } else if (operationName.equals(OperationNames.GET_ATTACK)) {
            int attack = unit.getAttackingStrength();
            reply = new ReplyObject(HttpServletResponse.SC_CREATED, gson.toJson(attack));
        }
        return gson.toJson(reply);
    }

    private Unit lookupUnit(String objectID) {
        return lookupStrategy.lookupUnit(objectID, nameService);
    }
}
