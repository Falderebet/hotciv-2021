package hotciv.view.tool;

import hotciv.framework.Game;
import hotciv.view.GfxConstants;
import minidraw.framework.DrawingEditor;
import minidraw.standard.NullTool;

import java.awt.event.MouseEvent;

public class ActionTool extends NullTool {
    private final DrawingEditor editor;
    private final Game game;

    public ActionTool(DrawingEditor editor, Game game) {
        this.editor = editor;
        this.game = game;
    }

    @Override
    public void mouseDown(MouseEvent e, int x, int y){
        if (e.isShiftDown()) {
            System.out.println("jeg er trykket ned");
            game.performUnitActionAt(GfxConstants.getPositionFromXY(x, y));
        }
    }
}
