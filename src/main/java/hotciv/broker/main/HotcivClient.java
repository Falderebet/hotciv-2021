package hotciv.broker.main;

import frds.broker.ClientRequestHandler;
import frds.broker.Requestor;
import frds.broker.ipc.socket.SocketClientRequestHandler;
import frds.broker.marshall.json.StandardJSONRequestor;
import hotciv.broker.proxies.GameProxy;
import hotciv.domain.GameFactory;
import hotciv.domain.SemiCivFactory;
import hotciv.framework.Game;
import hotciv.framework.Position;
import hotciv.standard.GameImpl;
import hotciv.view.tool.CompositionTool;
import hotciv.visual.HotCivFactory4;
import minidraw.framework.DrawingEditor;
import minidraw.standard.MiniDrawApplication;

public class HotcivClient {

    private GameFactory semiCivFactory;

    public static void main(String[] args) throws Exception{
        new HotcivClient("127.0.0.1");
    }

    public HotcivClient(String hostname){
        System.out.println("===HotCiv MANUAL TEST Client (socket) (host: "+ hostname + ") ===");
        ClientRequestHandler crh = new SocketClientRequestHandler();
        crh.setServer(hostname, 37321);
        Requestor requestor = new StandardJSONRequestor(crh);
        Game game = new GameProxy(requestor);
        //semiCivFactory = new SemiCivFactory();
        //Game game = new GameImpl(semiCivFactory);

        DrawingEditor editor = new MiniDrawApplication(
                "our SemiCiv program with GUI",
                new HotCivFactory4(game));
        editor.open();
        editor.showStatus("Red player starts the game");

        editor.setTool(new CompositionTool(editor, game));

        System.out.println(game.getCityAt(new Position(1,1)));
    }
}
