package hotciv.view.tool;

import hotciv.framework.Game;
import hotciv.framework.Position;
import hotciv.view.GfxConstants;
import hotciv.view.figure.HotCivFigure;
import minidraw.framework.Drawing;
import minidraw.framework.DrawingEditor;
import minidraw.framework.Tool;
import minidraw.standard.NullTool;
import minidraw.standard.handlers.DragTracker;

import java.awt.event.MouseEvent;

public class UnitMoveTool extends NullTool {
    private final DrawingEditor editor;
    private final Game game;
    private HotCivFigure draggedFigure;
    private Position fromPosition;
    private Tool fChild;
    protected Tool cachedNullTool;


    public UnitMoveTool(DrawingEditor editor, Game game) {
        this.editor = editor;
        this.game = game;
        fromPosition = null;
        fChild = cachedNullTool = new NullTool();
    }

    @Override
    public void mouseDown(MouseEvent e, int x, int y) {
        game.setTileFocus(GfxConstants.getPositionFromXY(x,y));
        Drawing model = editor.drawing();

        model.lock();

        draggedFigure = (HotCivFigure) editor.drawing().findFigure(x, y);

        if (draggedFigure != null) {
            fChild = createDragTracker(draggedFigure);
        }
        fChild.mouseDown(e, x, y);

        setFromPosition(GfxConstants.getPositionFromXY(x, y));


    }

    private Tool createDragTracker(HotCivFigure draggedFigure) {
        return new DragTracker(editor, draggedFigure);
    }

    private void setFromPosition(Position positionFromXY) {
        this.fromPosition = positionFromXY;
    }

    @Override
    public void mouseDrag(MouseEvent e, int x, int y) {
        fChild.mouseDrag(e, x, y);

    }

    @Override
    public void mouseUp(MouseEvent e, int x, int y) {
        editor.drawing().unlock();
        fChild.mouseUp(e, x, y);
        fChild = cachedNullTool;
        draggedFigure = null;
        Position droppedPosition = GfxConstants.getPositionFromXY(x, y);
        if(game.moveUnit(getFromPosition(), droppedPosition)) {
            game.setTileFocus(droppedPosition);
        }

    }

    private Position getFromPosition() {
        return this.fromPosition;
    }


}
