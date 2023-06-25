package hotciv.view;

import hotciv.framework.*;
import hotciv.standard.GameImpl;
import hotciv.view.figure.CityFigure;
import hotciv.view.figure.HotCivFigure;
import hotciv.view.figure.TextFigure;
import hotciv.view.figure.UnitFigure;
import minidraw.framework.*;
import minidraw.standard.ImageFigure;
import minidraw.standard.StandardFigureCollection;
import minidraw.standard.handlers.ForwardingFigureChangeHandler;
import minidraw.standard.handlers.StandardDrawingChangeListenerHandler;
import minidraw.standard.handlers.StandardSelectionHandler;

import java.awt.*;
import java.util.*;
import java.util.List;

/** CivDrawing is a specialized Drawing (MVC model component) from
 * MiniDraw that dynamically builds the list of Figures for MiniDraw
 * to render the Unit and other information objects that are visible
 * in the Game instance.
 *
 * TODO: This is a TEMPLATE for the SWEA Exercise! This means
 * that it is INCOMPLETE and that there are several options
 * for CLEANING UP THE CODE when you add features to it!

   This source code is from the book 
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

public class CivDrawing implements Drawing, GameObserver {

  // CivDrawing uses standard implementations from the MiniDraw
  // library for many of its sub responsibilities.
  private final SelectionHandler selectionHandler;
  private final DrawingChangeListenerHandler listenerHandler;
  private final ForwardingFigureChangeHandler figureChangeListener;
  private final FigureCollection figureCollection;

  // A mapping between position to the Unit figure at that position
  // allowing us to track where units move
  private Map<Position, UnitFigure> positionToUnitFigureMap;
  private Map<Position, CityFigure> positionToCityFigureMap;

  /** the Game instance that this CivDrawing is going to render units,
   * cities, ages, player-in-turn, from */
  protected Game game;

  public CivDrawing(DrawingEditor editor, Game game) {
    // Much of our behaviour can be delegated to standard MiniDraw
    // implementations, so we just reuse those...
    selectionHandler = new StandardSelectionHandler();
    listenerHandler = new StandardDrawingChangeListenerHandler();
    figureChangeListener = new ForwardingFigureChangeHandler(this,
            (StandardDrawingChangeListenerHandler) listenerHandler);
    figureCollection = new StandardFigureCollection(figureChangeListener);

    positionToUnitFigureMap = new HashMap<>();
    positionToCityFigureMap = new HashMap<>();

    // associate with game
    this.game = game;
    // register this unit drawing as listener to any game state
    // changes...
    game.addObserver(this);

    // ensure our drawing's figure collection of UnitFigures
    // reflects those present in the game
    synchronizeUnitFigureCollectionWithGameUnits();
    // and the set of 'icons' in status panel represents game state
    synchronizeIconsWithGameState();
  }
  
  /** The CivDrawing should not allow client side
   * units to add and manipulate figures; only figures
   * that renders game objects are relevant, and these
   * should be handled by observer events from the game
   * instance. Thus these methods are 'killed'.
   */
  @Override
  public Figure add(Figure arg0) {
    throw new RuntimeException("Should not be used, handled by Observing Game");
  }

  @Override
  public Figure remove(Figure arg0) {
    throw new RuntimeException("Should not be used, handled by Observing Game");
  }


  /** Ensure our collection of unit figures match those of the
   * game's units.
   */
  protected void synchronizeUnitFigureCollectionWithGameUnits() {
    // iterate all tile positions and ensure that our figure
    // collection truthfully match that of game by adding/removing
    // figures.

    Position p;
    for (int r = 0; r < GameConstants.WORLDSIZE; r++) {
      for (int c = 0; c < GameConstants.WORLDSIZE; c++) {
        p = new Position(r, c);
        Unit unit = game.getUnitAt(p);
        City city = game.getCityAt(p);
        UnitFigure unitFigure = positionToUnitFigureMap.get(p);
        CityFigure cityFigure = positionToCityFigureMap.get(p);

        // Synchronize each tile position with figure collection
        if (unit != null) {
          // if a unit is present in game, then
          if (unitFigure == null) {
            // if the associated unit figure has not been created, make it
            unitFigure = createUnitFigureFor(p, unit);
            // We add the figure both to our internal data structure
            positionToUnitFigureMap.put(p, unitFigure);
            // and of course to MiniDraw's figure collection for
            // visual rendering
            figureCollection.add(unitFigure);
            System.out.println("så langt så godt");
          }
        } else {
          // no unit at tile, maybe there is a unitFigure wrongly here
          if (unitFigure != null) {
            positionToUnitFigureMap.remove(p);
            figureCollection.remove(unitFigure);
          }
        }

        if(city != null){
          if(cityFigure == null){
            cityFigure = createCityFigureFor(p, city);
            System.out.println("så langt så godt");
            positionToCityFigureMap.put(p, cityFigure);
            figureCollection.add(cityFigure);
          }
        } else {
          if (cityFigure != null) {
            positionToCityFigureMap.remove(p);
            figureCollection.remove(cityFigure);
          }
        }
      }
    }
  }
  private CityFigure createCityFigureFor(Position p, City city){

    // convert the city's Position to (x,y) coordinates
    Point point = new Point( GfxConstants.getXFromColumn(p.getColumn()),
            GfxConstants.getYFromRow(p.getRow()) );

    CityFigure cityFigure = new CityFigure(city, point);
    return cityFigure;
  }

  /** Create a figure representing a unit at given position */
  private UnitFigure createUnitFigureFor(Position p, Unit unit) {
    String type = unit.getTypeString();
    // convert the unit's Position to (x,y) coordinates
    Point point = new Point( GfxConstants.getXFromColumn(p.getColumn()),
                             GfxConstants.getYFromRow(p.getRow()) );
    UnitFigure unitFigure =
      new UnitFigure(type, point, unit);
    return unitFigure;
  }

  // Figures representing icons (showing status in status panel)
  protected ImageFigure refreshButtonIcon;
  protected ImageFigure turnShieldIcon;
  protected TextFigure ageText;
  protected ImageFigure unitShieldIcon;
  protected TextFigure moveCountText;
  protected ImageFigure cityShieldIcon;
  protected ImageFigure productionIcon;
  protected ImageFigure balanceIcon;
  protected void synchronizeIconsWithGameState() {

    if (refreshButtonIcon == null) {
      refreshButtonIcon =
              new HotCivFigure("refresh",
                      new Point(GfxConstants.REFRESH_BUTTON_X,
                              GfxConstants.REFRESH_BUTTON_Y),
                      GfxConstants.REFRESH_BUTTON);
      figureCollection.add(refreshButtonIcon);
    }
    updateRefreshIcon();
    // Note - we have to guard creating figures and adding
    // them to the collection, so we do not create multiple
    // instances; this method is called on every 'requestRedraw'
    if (turnShieldIcon == null) {
      turnShieldIcon =
              new HotCivFigure("redshield",
                      new Point(GfxConstants.TURN_SHIELD_X,
                              GfxConstants.TURN_SHIELD_Y),
                      GfxConstants.TURN_SHIELD_TYPE_STRING);
      // insert in delegate figure list to ensure graphical
      // rendering.
      figureCollection.add(turnShieldIcon);
    }
    updateTurnShield(game.getPlayerInTurn());

    if (ageText == null) {
      ageText = new TextFigure(String.valueOf(game.getAge()), new Point(GfxConstants.AGE_TEXT_X, GfxConstants.AGE_TEXT_Y));
      figureCollection.add(ageText);
    }
    updateAgeText(String.valueOf(game.getAge()));

    if (unitShieldIcon == null) {
      unitShieldIcon =
              new HotCivFigure("black",
                      new Point(GfxConstants.UNIT_SHIELD_X,
                              GfxConstants.UNIT_SHIELD_Y),
                      GfxConstants.UNIT_SHIELD_TYPE_STRING);
      // insert in delegate figure list to ensure graphical
      // rendering.
      figureCollection.add(unitShieldIcon);
    }

    if (moveCountText == null) {
      moveCountText =
              new TextFigure("",
                      new Point(GfxConstants.UNIT_COUNT_X,
                              GfxConstants.UNIT_COUNT_Y));
      figureCollection.add(moveCountText);
    }

    if (cityShieldIcon == null) {
      cityShieldIcon =
              new HotCivFigure("black",
                      new Point(GfxConstants.CITY_SHIELD_X,
                              GfxConstants.CITY_SHIELD_Y),
                      GfxConstants.UNIT_SHIELD_TYPE_STRING);
      figureCollection.add(cityShieldIcon);
    }

    if (productionIcon == null) {
      productionIcon =
              new HotCivFigure("black",
                      new Point(GfxConstants.CITY_PRODUCTION_X,
                      GfxConstants.CITY_PRODUCTION_Y),
                      GfxConstants.UNIT_SHIELD_TYPE_STRING);
      figureCollection.add(productionIcon);
    }

    if (balanceIcon == null) {
      balanceIcon =
              new HotCivFigure("black",
                      new Point(GfxConstants.WORKFORCEFOCUS_X,
                              GfxConstants.WORKFORCEFOCUS_Y),
                      GfxConstants.UNIT_SHIELD_TYPE_STRING);
      figureCollection.add(balanceIcon);
    }
    // for other status panel info, like age, etc.
  }

  private void updateRefreshIcon() {
  }

  // === Observer Methods ===
  public void worldChangedAt(Position pos) {
    System.out.println( "CivDrawing: world changes at "+pos);
    Unit u = game.getUnitAt(pos);
    if (u == null) {
      // Unit has been removed
      UnitFigure uf = positionToUnitFigureMap.remove(pos);
      figureCollection.remove(uf);
    } else {
      // Unit has appeared
      UnitFigure uf = createUnitFigureFor(pos, u);
      positionToUnitFigureMap.put(pos, uf);
      figureCollection.add(uf);
    }

    City c = game.getCityAt(pos);
    Player owner = game.getPlayerInTurn();
    if(c != null){
    //City has appeared
      CityFigure cf = createCityFigureFor(pos, c);
      positionToCityFigureMap.put(pos, cf);
      figureCollection.add(cf);
    }
  }

  public void turnEnds(Player nextPlayer, int age) {
    updateTurnShield(nextPlayer);
    updateAgeText(String.valueOf(game.getAge()));
  }

  private void updateTurnShield(Player nextPlayer) {
    String playername = "red";
    if ( nextPlayer == Player.BLUE ) {
      playername = "blue";
    }
    turnShieldIcon.set( playername+"shield",
                        new Point( GfxConstants.TURN_SHIELD_X,
                                   GfxConstants.TURN_SHIELD_Y ) );
  }

  private void updateAgeText(String age) {
    ageText.setText(age);
  }

  public void tileFocusChangedAt(Position position) {
    Unit unit = game.getUnitAt(position);
    City city = game.getCityAt(position);
    if( unit != null) {
      updateUnitInformation(unit);
    }

    if( city != null) {
      updateCityInformation(city);
    }
    requestUpdate();
  }

  private void updateCityInformation(City city) {
      updateCityOwner(city.getOwner());
      updateCityBalance(city.getWorkforceFocus());
      updateCityProduction(city.getProduction());
  }

  private void updateCityProduction(String workforceFocus) {
    balanceIcon.set(workforceFocus, new Point(GfxConstants.CITY_PRODUCTION_X,
            GfxConstants.CITY_PRODUCTION_Y));
  }

  private void updateCityBalance(String productionFocus) {
    productionIcon.set(productionFocus, new Point(GfxConstants.WORKFORCEFOCUS_X,
            GfxConstants.WORKFORCEFOCUS_Y));
  }

  private void updateCityOwner(Player owner) {
    cityShieldIcon.set(owner.name().toLowerCase() + "shield", new Point(GfxConstants.CITY_SHIELD_X, GfxConstants.CITY_SHIELD_Y));
  }

  private void updateUnitInformation(Unit unit) {
    Player player = unit.getOwner();
    System.out.println(player.name());
    if (player == Player.RED) {
      drawUnitOwner(player);
      drawUnitCount(unit);
    }
    else {
      drawUnitOwner(player);
      drawUnitCount(unit);
    }
  }

  private void drawUnitCount(Unit unit) {
    moveCountText.setText(String.valueOf(unit.getMoveCount()));
  }

  private void drawUnitOwner(Player player) {
    String playername = "red";
    if ( player == Player.BLUE ) {
      playername = "blue";
    }
    unitShieldIcon.set(playername+"shield", new Point(GfxConstants.UNIT_SHIELD_X, GfxConstants.UNIT_SHIELD_Y));
  }

  @Override
  public void requestUpdate() {
    // A request to redraw from scratch, so we
    // synchronize with all game state
    synchronizeUnitFigureCollectionWithGameUnits();
    synchronizeIconsWithGameState();

  }

  @Override
  public void addToSelection(Figure arg0) {
    selectionHandler.addToSelection(arg0);
  }

  @Override
  public void clearSelection() {
    selectionHandler.clearSelection();
  }

  @Override
  public void removeFromSelection(Figure arg0) {
    selectionHandler.removeFromSelection(arg0);
  }

  @Override
  public List<Figure> selection() {
    return selectionHandler.selection();
  }

  @Override
  public void toggleSelection(Figure arg0) {
    selectionHandler.toggleSelection(arg0);
  }

  @Override
  public void figureChanged(FigureChangeEvent arg0) {
    figureChangeListener.figureChanged(arg0);
  }

  @Override
  public void figureInvalidated(FigureChangeEvent arg0) {
    figureChangeListener.figureInvalidated(arg0);
  }

  @Override
  public void addDrawingChangeListener(DrawingChangeListener arg0) {
    listenerHandler.addDrawingChangeListener(arg0);
  }

  @Override
  public void removeDrawingChangeListener(DrawingChangeListener arg0) {
    listenerHandler.removeDrawingChangeListener(arg0);
  }

  @Override
  public Figure findFigure(int arg0, int arg1) {
    return figureCollection.findFigure(arg0, arg1);
  }

  @Override
  public Figure zOrder(Figure figure, ZOrder order) {
    return figureCollection.zOrder(figure, order);
  }

  @Override
  public Iterator<Figure> iterator() {
    return figureCollection.iterator();
  }

  @Override
  @Deprecated
  public void lock() {
    // MiniDraw 2 has deprecated these methods...
  }

  @Override
  @Deprecated
  public void unlock() {
    // MiniDraw 2 has deprecated these methods...
  }
}
