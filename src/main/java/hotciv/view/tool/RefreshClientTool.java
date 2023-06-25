package hotciv.view.tool;

import hotciv.framework.Game;
import minidraw.framework.DrawingEditor;
import minidraw.framework.Tool;
import minidraw.standard.NullTool;

import java.awt.event.MouseEvent;

public class RefreshClientTool extends NullTool {
    private final DrawingEditor editor;
    private final Game game;

    public RefreshClientTool(DrawingEditor editor, Game game) {
        this.editor = editor;
        this.game = game;
    }

    @Override
    public void mouseUp(MouseEvent e, int x, int y) {
        editor.drawing().requestUpdate();
    }
}
