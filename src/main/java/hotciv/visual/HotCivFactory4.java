package hotciv.visual;

import hotciv.view.CivDrawing;
import hotciv.view.MapView;
import minidraw.framework.*;

import javax.swing.*;

import hotciv.framework.*;

/** Factory for visual testing of various SWEA template code */
public class HotCivFactory4 implements Factory {
  private Game game;
  public HotCivFactory4(Game g) { game = g; }

  public DrawingView createDrawingView(DrawingEditor editor) {
    DrawingView view = 
      new MapView(editor, game);
    return view;
  }

  public Drawing createDrawing(DrawingEditor editor) {
    return new CivDrawing(editor, game);
  }

  public JTextField createStatusField(DrawingEditor editor) {
    JTextField f = new JTextField("SWEA template code");
    f.setEditable(false);
    return f;
  }
}
