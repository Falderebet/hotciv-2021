package hotciv.view;

import hotciv.stub.ThetaConstants;
import minidraw.framework.*;
import minidraw.standard.*;
import java.awt.*;

import hotciv.framework.*;

/** MapView: A MiniDraw DrawingView specializing
    in drawing the background for a HotCiv Game
    world including the matrix of tiles, and
    optimizing performance of tile redraws.

    Note: this class assumes that the type
    strings used for tiles are identical to
    the names of the GIF files loaded by MiniDraw.
    An exception is the ocean/desert tiles that must
    be named with a binary encoding stating the
    coast line properties.

    Responsibilities:
    1) Draw fixed graphics, like borders and status panel
    2) Draw the world, consisting of a matrix of terrain tiles.

   2021 Update:
   The current version is performance optimized using
   caching of the game's tile types to lower 'getTile()'
   calls which in a Broker based remote setting will
   slow the system down significantly. Therefore
   once a tile has been fetched from the Game, it
   will stay FIXED! Thus, the current version
   DOES NOT SUPPORT HOTCIV GAMES IN WHICH TILE
   TYPES DYNAMICALLY CHANGE!

   This source code is adapted from the book
     "Flexible, Reliable Software:
       Using Patterns and Agile Development"
     published 2010 by CRC Press.
   Author: 
     Henrik B Christensen 
     Department of Computer Science
     Aarhus University
   
   Please visit http://www.baerbak.com/ for further information.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
 
       http://www.apache.org/licenses/LICENSE-2.0
 
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

public class MapView extends StdViewWithBackground {
  
  private final Game game;

  public MapView( DrawingEditor editor, Game game ) {
    super(editor, "hotciv-background" );
    // Optimization - Introduce a caching proxy on the game instance
    // which only loads tiles once
    this.game = new TileCachingProxy(game);
  }

  // Note - the drawing af figures is controlled by the super class
  // which interacts with the CivDrawing

  /** Draw the background graphics. This includes of course the
   * 'frame' and a few other fixed graphics stored in a gif file, but
   * beyond this we have to draw the world of tiles. For most of the
   * tiles this is basically just drawing the terrain graphics but for
   * oceans and deserts we have to be a bit more clever to draw coast lines
   * that appear nice.
   */
  public void drawBackground(Graphics g) {
    // draw the background graphics from the superclass
    super.drawBackground(g);

    ImageManager im = ImageManager.getSingleton();
    Image img; Position tilePosition;

    // Retrieve the dirty rectangle - the part of the screen
    // that needs redrawing
    Rectangle clip = g.getClipBounds();

    // draw the map as a matrix of tiles with cities on top
    // but only if within the clipping region
    for ( int r = 0; r < GameConstants.WORLDSIZE; r++ ) {
      for ( int c = 0; c < GameConstants.WORLDSIZE; c++ ) {
        // Construct the actual rectangle that this tile covers
        tilePosition = new Position(r,c);
        int xpos = GfxConstants.getXFromColumn(c);
        int ypos = GfxConstants.getYFromRow(r);
        Rectangle tileRect = new Rectangle(xpos, ypos, GfxConstants.TILESIZE, GfxConstants.TILESIZE);

        // Optimize redrawing to only those tiles that intersect the clip rectangle
        if (clip.intersects(tileRect)) {
          // Draw proper terrain
          Tile t = game.getTileAt(tilePosition);
          String image_name = t.getTypeString();
          // special handling of terrains with a
          // 'coastline'...
          if (image_name.equals(GameConstants.OCEANS) ||
                  image_name.equals(ThetaConstants.DESERT)) {
            image_name = image_name +
                    MapAlgorithms.getCoastlineCoding(game, tilePosition, t.getTypeString());
          }
          img = im.getImage(image_name);
          g.drawImage(img, xpos, ypos, null);
        }
      }
    }
  }

  /** A Proxy that caches getTileAt() permanently, so
   * after the first invocation on a specific tile, the
   * same tile is always returned.
   */
  private class TileCachingProxy implements Game {
    private final Game proxee;
    private Tile tileCache[][];

    public TileCachingProxy(Game game) {
      this.proxee = game;
      tileCache = new Tile[GameConstants.WORLDSIZE][GameConstants.WORLDSIZE];
    }
    @Override
    public Tile getTileAt(Position p) {
      Tile tile = tileCache[p.getRow()][p.getColumn()];
      // if cache is not warm, fetch tile and update cache
      if (tile == null) {
        tileCache[p.getRow()][p.getColumn()] = proxee.getTileAt(p);
        tile = tileCache[p.getRow()][p.getColumn()];
      }
      return tile;
    }

    @Override
    public Unit getUnitAt(Position p) {
      return proxee.getUnitAt(p);
    }

    @Override
    public City getCityAt(Position p) {
      return proxee.getCityAt(p);
    }

    @Override
    public Player getPlayerInTurn() {
      return proxee.getPlayerInTurn();
    }

    @Override
    public Player getWinner() {
      return proxee.getWinner();
    }

    @Override
    public int getAge() {
      return proxee.getAge();
    }

    @Override
    public boolean moveUnit(Position from, Position to) {
      return proxee.moveUnit(from, to);
    }

    @Override
    public void endOfTurn() {
      proxee.endOfTurn();
    }

    @Override
    public void changeWorkForceFocusInCityAt(Position p, String balance) {
      proxee.changeWorkForceFocusInCityAt(p, balance);
    }

    @Override
    public void changeProductionInCityAt(Position p, String unitType) {
      proxee.changeProductionInCityAt(p, unitType);
    }

    @Override
    public void performUnitActionAt(Position p) {
      proxee.performUnitActionAt(p);
    }

    @Override
    public void addObserver(GameObserver observer) {
      proxee.addObserver(observer);
    }

    @Override
    public void setTileFocus(Position position) {
      proxee.setTileFocus(position);
    }
  }
}
