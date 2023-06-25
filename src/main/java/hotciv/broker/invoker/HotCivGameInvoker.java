package hotciv.broker.invoker;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import frds.broker.Invoker;
import frds.broker.ReplyObject;
import frds.broker.RequestObject;
import hotciv.broker.NameServiceImpl;
import hotciv.broker.OperationNames;
import hotciv.framework.*;
import hotciv.storage.XDSException;

import jdk.dynalink.Operation;

import javax.servlet.http.HttpServletResponse;
import javax.swing.event.HyperlinkEvent;

public class HotCivGameInvoker implements Invoker {

    private final Game game;
    private final Gson gson;
    private final NameService nameService;

    public HotCivGameInvoker(Game servant, NameService nameService) {
        game = servant;
        gson = new Gson();
        this.nameService = nameService;
    }

    @Override
    public String handleRequest(String request) {
        // Demarsheling af indkommende beskeder.
        RequestObject requestObject =
                gson.fromJson(request, RequestObject.class);
        JsonArray array =
                JsonParser.parseString(requestObject.getPayload()).getAsJsonArray();

        ReplyObject reply;

        System.out.println("Jeg har fået en besked");

        try {
            // Så den skal få parametere fra JSON array
            // Så skal vi kalde servant metode
            // så skal vi få lavet en send tilbage besked som vi kan sende tilbage til client.

            if (requestObject.getOperationName().equals(OperationNames.GET_WINNER)) {
                // Den her skal muligvis ikke bruges.
                //Player player = gson.fromJson(array.get(0), Game.class);

                Player winner = game.getWinner();
                reply = new ReplyObject(HttpServletResponse.SC_CREATED,
                        gson.toJson(winner));
            } else if (requestObject.getOperationName().equals(OperationNames.GET_PLAYER_IN_TURN)){
                Player player = game.getPlayerInTurn();
                reply = new ReplyObject(HttpServletResponse.SC_OK,
                        gson.toJson(player));
            } else if (requestObject.getOperationName().equals(OperationNames.GET_AGE)){
                int age = game.getAge();
                reply = new ReplyObject(HttpServletResponse.SC_CREATED, gson.toJson(age));

            } else if (requestObject.getOperationName().equals(OperationNames.MOVE_UNIT)) {
                boolean move;
                Position from = gson.fromJson(array.get(0), Position.class);
                Position to = gson.fromJson(array.get(1), Position.class);
                if (from != null && to != null) {
                    move = game.moveUnit(from, to);
                } else {
                    move = false;
                }
                reply = new ReplyObject(HttpServletResponse.SC_CREATED, gson.toJson(move));

            } else if(requestObject.getOperationName().equals(OperationNames.GET_CITY_AT)){
                Position p = gson.fromJson(array.get(0), Position.class);
                City city = game.getCityAt(p);
                if(city == null) {
                    String id = null;
                    reply = new ReplyObject(HttpServletResponse.SC_CREATED, gson.toJson(id));
                } else {
                    String id = city.getCityID();
                    nameService.putCity(id, city);
                    reply = new ReplyObject(HttpServletResponse.SC_CREATED, gson.toJson(id));
                }

            } else if(requestObject.getOperationName().equals(OperationNames.GET_UNIT_AT)) {
                Position p = gson.fromJson(array.get(0), Position.class);
                Unit unit = game.getUnitAt(p);
                if(unit == null){
                    String id = null;
                    reply = new ReplyObject(HttpServletResponse.SC_CREATED, gson.toJson(id));
                } else {
                    String id = unit.getUnitId();
                    nameService.putUnit(id, unit);
                    reply = new ReplyObject(HttpServletResponse.SC_CREATED, gson.toJson(id));
                }

            } else if(requestObject.getOperationName().equals(OperationNames.GET_TILE_AT)) {
                Position p = gson.fromJson(array.get(0), Position.class);
                Tile tile = game.getTileAt(p);
                if(tile == null) {
                    String id = null;
                    reply = new ReplyObject(HttpServletResponse.SC_CREATED, gson.toJson(id));
                } else {
                String id = tile.getTileID();
                nameService.putTile(id, tile);
                reply = new ReplyObject(HttpServletResponse.SC_CREATED, gson.toJson(id)); }
            } else if(requestObject.getOperationName().equals(OperationNames.END_OF_TURN)) {
                game.endOfTurn();
                System.out.println("I have ended the turn");
                reply = new ReplyObject(HttpServletResponse.SC_NO_CONTENT, null);
            } else if(requestObject.getOperationName().equals(OperationNames.CHANGE_WORKFORCE_FOCUS)) {
                Position p = gson.fromJson(array.get(0), Position.class);
                String balance = gson.fromJson(array.get(1), String.class);
                game.changeWorkForceFocusInCityAt(p, balance);
                System.out.println("Changed workforce focus on city at position" + p.toString() +
                        " and to balance: " + balance);
                reply = new ReplyObject(HttpServletResponse.SC_CREATED, gson.toJson("I have done it"));
            } else if(requestObject.getOperationName().equals(OperationNames.PERFORM_UNIT_ACTION)) {
                Position p = gson.fromJson(array.get(0), Position.class);
                game.performUnitActionAt(p);
                System.out.println("Performed unit action at: " + p.toString());
                reply = new ReplyObject(HttpServletResponse.SC_NO_CONTENT, gson.toJson(null));
            }else if(requestObject.getOperationName().equals(OperationNames.CHANGE_PRODUCTION_IN_A_CITY)) {
                Position p = gson.fromJson(array.get(0), Position.class);
                String unittype = gson.fromJson(array.get(0), String.class);
                game.changeProductionInCityAt(p, unittype);
                reply = new ReplyObject(HttpServletResponse.SC_NO_CONTENT, gson.toJson(null));

            } else if(requestObject.getOperationName().equals(OperationNames.SET_TILE_FOCUS)) {
                Position p = gson.fromJson(array.get(0), Position.class);
                game.setTileFocus(p);
                System.out.println("Set tile focus on position: " + p.toString());
                reply = new ReplyObject(HttpServletResponse.SC_CREATED, gson.toJson(null));
            }
            else {
                reply = new ReplyObject(HttpServletResponse.SC_NOT_IMPLEMENTED,
                        "Server received unknown operation name: "
                                + requestObject.getOperationName() + ".");
            }
        } catch ( XDSException e  ) {
            reply =
                    new ReplyObject(
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            e.getMessage());
        }

        System.out.println(reply.toString());
        return gson.toJson(reply);
    }

    private Unit lookUpUnit(String objectId) {
        Unit unit = nameService.getUnit(objectId);
        System.out.println(unit.getTypeString());
        return unit;
    }

    private City lookUpCity(String objectID) {
        City city = nameService.getCity(objectID);
        return city;
    }
}
