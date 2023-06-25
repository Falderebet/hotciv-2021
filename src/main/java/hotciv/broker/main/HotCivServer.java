package hotciv.broker.main;

import frds.broker.Invoker;
import frds.broker.ipc.socket.SocketServerRequestHandler;
import hotciv.broker.invoker.HotCivGameInvoker;
import hotciv.broker.invoker.HotCivRootInvoker;
import hotciv.broker.invoker.lookupStrategy.LookupInvoker;
import hotciv.broker.invoker.lookupStrategy.LookupStrategy;
import hotciv.broker.stubs.StubGame3;
import hotciv.domain.GameFactory;
import hotciv.domain.SemiCivFactory;
import hotciv.framework.*;
import hotciv.standard.GameImpl;

public class HotCivServer {

    private static Thread daemon;
    private GameFactory semiCivFactory;
    private LookupInvoker lookupStrategy;

    public static void main(String[] args) throws Exception {
        new HotCivServer();
    }


    public HotCivServer() throws Exception {
        int port = 37321;
        lookupStrategy = new LookupStrategy();

        semiCivFactory = new SemiCivFactory();
        Game gameServant = new GameImpl(semiCivFactory);
        Invoker invoker = new HotCivRootInvoker(gameServant, lookupStrategy);

        SocketServerRequestHandler ssrh = new SocketServerRequestHandler();
        ssrh.setPortAndInvoker(port, invoker);

        System.out.println("=== HOTCIV socked based server port: "
        + port + " ===");
        ssrh.start();
    }
}
