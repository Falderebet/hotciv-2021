package hotciv.broker.invoker;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import frds.broker.Invoker;
import frds.broker.ReplyObject;
import frds.broker.RequestObject;
import hotciv.broker.OperationNames;
import hotciv.broker.invoker.lookupStrategy.LookupInvoker;
import hotciv.broker.stubs.StubCity2;
import hotciv.framework.City;
import hotciv.framework.NameService;
import hotciv.framework.Player;

import javax.servlet.http.HttpServletResponse;

public class HotCivCityInvoker implements Invoker {
    private Gson gson;
    private City city;
    private NameService nameService;
    private LookupInvoker lookupStrategy;

    public HotCivCityInvoker(City cityServant, NameService nameService, LookupInvoker lookupStrategy) {
        gson = new Gson();
        this.city = cityServant;
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

        // Demarshal
        JsonParser parser = new JsonParser();
        JsonArray array =
                parser.parse(payload).getAsJsonArray();

        City city = lookupCity(objectID);

        System.out.println(operationName);
        if (operationName.equals(OperationNames.GET_OWNER_OF_CITY)) {
            Player owner = city.getOwner();
            reply = new ReplyObject(HttpServletResponse.SC_CREATED, gson.toJson(owner));
        } else if (operationName.equals(OperationNames.GET_SIZE)) {
            int size = city.getSize();
            reply = new ReplyObject(HttpServletResponse.SC_CREATED, gson.toJson(size));
        } else if (operationName.equals(OperationNames.GET_TREASURY)) {
            int treasury = city.getTreasury();
            reply = new ReplyObject(HttpServletResponse.SC_CREATED, gson.toJson(treasury));
        } else if (operationName.equals(OperationNames.GET_PRODUCTION)) {
            String unitString = city.getProduction();
            reply = new ReplyObject(HttpServletResponse.SC_CREATED, gson.toJson(unitString));
        } else if (operationName.equals(OperationNames.GET_WORKFORCEFOCUS)) {
            String workforce = city.getWorkforceFocus();
            reply = new ReplyObject(HttpServletResponse.SC_CREATED, gson.toJson(workforce));
        }

        return gson.toJson(reply);


    }

    private City lookupCity(String objectID) {
        return lookupStrategy.lookupCity(objectID, nameService);
    }
}
