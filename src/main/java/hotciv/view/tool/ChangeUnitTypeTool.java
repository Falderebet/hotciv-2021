package hotciv.view.tool;

import hotciv.framework.City;
import hotciv.framework.Game;
import hotciv.framework.GameConstants;
import hotciv.framework.Position;
import minidraw.framework.DrawingEditor;
import minidraw.framework.Tool;
import minidraw.standard.NullTool;

import java.awt.event.MouseEvent;

public class ChangeUnitTypeTool extends NullTool {
    private final DrawingEditor editor;
    private final Game game;
    private Position city;

    public ChangeUnitTypeTool(DrawingEditor editor, Game game, Position city) {
        this.editor = editor;
        this.game = game;
        this.city = city;
    }

    @Override
    public void mouseDown(MouseEvent e, int x, int y) {
        City productionCity = game.getCityAt(city);
        String production = productionCity.getProduction();
        System.out.println("heg er inde i tool: ");
        System.out.println(production);
        if(GameConstants.ARCHER.equals(production)) {
            System.out.println("jeg er inde i først if");
            production = GameConstants.LEGION;
            System.out.println(production);
        }
        else if(production.equals(GameConstants.LEGION)) {
            production = GameConstants.SETTLER;
        }
        else if(production.equals(GameConstants.SETTLER)) {
            production = GameConstants.SANDWORM;
        }
        else if(production.equals((GameConstants.SANDWORM))) {
            production = GameConstants.ARCHER;
        }

        game.changeProductionInCityAt(city, production);
    }
}
