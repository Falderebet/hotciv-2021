package hotciv.broker.main;

import frds.broker.ClientRequestHandler;
import frds.broker.Requestor;
import frds.broker.ipc.socket.SocketClientRequestHandler;
import frds.broker.marshall.json.StandardJSONRequestor;
import hotciv.broker.proxies.GameProxy;
import hotciv.framework.Game;

public class HotCivManualClientTest {

    public static void main(String[] args) throws Exception{
        new HotCivManualClientTest("127.0.0.1");
    }

    public HotCivManualClientTest(String hostname){
        System.out.println("===HotCiv MANUAL TEST Client (socket) (host: "+ hostname + ") ===");
        ClientRequestHandler crh = new SocketClientRequestHandler();
        crh.setServer(hostname, 37321);
        Requestor requestor = new StandardJSONRequestor(crh);
        Game game = new GameProxy(requestor);

        System.out.println("=== Testing simple methods===");
        testSimpleMethods(game);
    }

    private void testSimpleMethods(Game game){
        System.out.println("== Testing simple methods ===");
        System.out.println("-> Game age " + game.getAge());
        System.out.println("-> Game winner " + game.getWinner());
        System.out.println("-> Game player in turn " + game.getPlayerInTurn());


    }
}
