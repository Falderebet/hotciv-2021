package hotciv.visual;

import hotciv.domain.SemiCivFactory;
import hotciv.framework.Game;
import hotciv.standard.GameImpl;
import hotciv.view.tool.CompositionTool;
import minidraw.framework.DrawingEditor;
import minidraw.standard.MiniDrawApplication;

public class ShowSemiCiv {

    public static void main(String[] args) {
        Game game = new GameImpl(new SemiCivFactory());

        DrawingEditor editor = new MiniDrawApplication(
                "our SemiCiv program with GUI",
                new HotCivFactory4(game));
        editor.open();
        editor.showStatus("Red player starts the game");

        editor.setTool(new CompositionTool(editor, game));
    }
}
