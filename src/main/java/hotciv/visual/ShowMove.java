package hotciv.visual;

import hotciv.domain.AlphaCivFactory;
import hotciv.standard.GameImpl;
import hotciv.stub.FakeObjectGame;
import hotciv.view.tool.UnitMoveTool;
import minidraw.standard.*;
import minidraw.framework.*;

import hotciv.framework.*;

/** Template code for exercise.

   This source code is from the book 
     "Flexible, Reliable Software:
       Using Patterns and Agile Development"
     published 2010 by CRC Press.
   Author: 
     Henrik B Christensen 
     Computer Science Department
     Aarhus University
   
   This source code is provided WITHOUT ANY WARRANTY either 
   expressed or implied. You may study, use, modify, and 
   distribute it for non-commercial purposes. For any 
   commercial use, see http://www.baerbak.com/
 */
public class ShowMove {
  
  public static void main(String[] args) {
    Game game = new FakeObjectGame();

    DrawingEditor editor = 
      new MiniDrawApplication( "Move any unit using the mouse",  
                               new HotCivFactory4(game) );
    editor.open();
    editor.showStatus("Move units to see Game's moveUnit method being called.");

    // TODO: Replace the setting of the tool with your UnitMoveTool implementation.
    editor.setTool(new UnitMoveTool(editor, game));
  }
}
