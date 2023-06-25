
package hotciv.broker.invoker;
import com.google.gson.Gson;
import frds.broker.Invoker;
import frds.broker.RequestObject;
import hotciv.broker.NameServiceImpl;
import hotciv.broker.OperationNames;
import hotciv.broker.invoker.lookupStrategy.LookupInvoker;
import hotciv.broker.invoker.lookupStrategy.LookupStrategy;
import hotciv.broker.stubs.StubGame3;
import hotciv.framework.*;

import java.util.HashMap;
import java.util.Map;

public class HotCivRootInvoker implements Invoker{
        private final Map<String, Invoker> invokerMap;
        private Gson gson;
        Game gameServant;
        City cityServant;
        Unit unitServant;
        Tile tileServant;
        NameService nameService;
        LookupInvoker lookupStrategy;

        public HotCivRootInvoker(Game servant, LookupInvoker lookupStrategy) {
            gson = new Gson();
            nameService = new NameServiceImpl();
            invokerMap = new HashMap<>();
            this.gameServant = servant;
            this.lookupStrategy = lookupStrategy;

            // Create an invoker for each handled type/class
            // and put them in a map, binding them to the
            // operationName prefixes
            Invoker HotCivGameInvoker = new HotCivGameInvoker(gameServant, nameService);
            invokerMap.put(OperationNames.GAME_PREFIX, HotCivGameInvoker);
            Invoker HotCivCityInvoker = new HotCivCityInvoker(cityServant, nameService, lookupStrategy);
            invokerMap.put(OperationNames.CITY_PREFIX, HotCivCityInvoker);
            Invoker HotCivUnitInvoker = new HotCivUnitInvoker(unitServant, nameService, lookupStrategy);
            invokerMap.put(OperationNames.UNIT_PREFIX, HotCivUnitInvoker);
            Invoker HotCivTileInvoker = new HotCivTileInvoker(tileServant, nameService, lookupStrategy);
            invokerMap.put(OperationNames.TILE_PREFIX, HotCivTileInvoker);
        }

        @Override
        public String handleRequest(String request) {
            RequestObject requestObject = gson.fromJson(request, RequestObject.class);
            String operationName = requestObject.getOperationName();

            String reply;

            // Identify the invoker to use
            String type = operationName.substring(0, operationName.indexOf(OperationNames.SEPERATOR));
            Invoker subInvoker = invokerMap.get(type);

            // And do the upcall on the subInvoker
            reply= subInvoker.handleRequest(request);

            return reply;

        }
}
