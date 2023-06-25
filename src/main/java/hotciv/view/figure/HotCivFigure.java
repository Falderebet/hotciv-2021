package hotciv.view.figure;

import minidraw.standard.ImageFigure;

import java.awt.*;

/** Default Figure class for figures in the HotCiv
 * graphical user interface. Represents a visual image
 * (like a unit, a shield, a hammer, etc.) and defines
 * a 'type' that uniquely identifies the image's "role"
 * in the game - like being a 'end-of-turn shield',
 * a 'unit figure', etc.
 *
 * A primary use is in the Composition Tool as it can
 * query the drawing for the image just below the (x,y)
 * coordinates of the mouse when clicked, and then
 * switch on the type string to determine which tool
 * to use.
 *
 * @author Henrik Bærbak Christensen, Aarhus University
 */
public class HotCivFigure extends ImageFigure {
  private final String typeString;

  public HotCivFigure(String typeString) {
    this.typeString = typeString;
  }

  public HotCivFigure(Image image, Point position, String typeString) {
    super(image, position);
    this.typeString = typeString;
  }

  public HotCivFigure(String imagename, Point position, String typeString) {
    super(imagename, position);
    this.typeString = typeString;
  }

  public String getTypeString() {
    return typeString;
  }

}
