package hotciv.standard;

import hotciv.domain.GameFactory;
import hotciv.framework.*;
import hotciv.utility.Utility;
import hotciv.variants.UnitActionsStrategy.UnitActionsStrategy;
import hotciv.variants.WorldLayoutStrategy.WorldLayoutStrategy;
import hotciv.variants.agingStrategy.AgingStrategy;
import hotciv.variants.moveUnitStrategy.MoveStrategy;
import hotciv.variants.winningStrategy.WinningStrategy;
import hotciv.variants.attackingStrategy.AttackingStrategy;

import java.util.*;

import static hotciv.framework.GameConstants.*;
import static hotciv.framework.GameConstants.UNITMAXMOVE;

/** Skeleton implementation of HotCiv.

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

public class GameImpl implements Game {

    private Map<Position, Tile> tileMap = new HashMap<>();
    private Map<Position, CityImpl> cityMap = new HashMap<>();
    private Map<Position, Unit> unitMap = new HashMap<>();
    private Player playerInTurn;
    private int age;
    private Player winner;
    private int redSuccessfullAttacks;
    private int blueSuccessfullAttacks;
    private WinningStrategy winningStrategy;
    private AgingStrategy agingStrategy;
    private UnitActionsStrategy unitActionsStrategy;
    private WorldLayoutStrategy worldLayoutStrategy;
    private AttackingStrategy attackingStrategy;
    private MoveStrategy moveStrategy;
    private GameFactory gameFactory;
    private int amountOfRounds;
    private ArrayList<GameObserver> gameObserverArrayList= new ArrayList<>();

    // Field for unit charectaristics
    private final static int COST_INDEX = 0;
    private final static int DEFENSE_INDEX = 1;
    private final static int ATTACK_INDEX = 2;
    private Map<String, int[]> unitCharacteristics;


    public GameImpl(GameFactory gameFactory) {
        this.gameFactory = gameFactory;

        this.worldLayoutStrategy = gameFactory.createWorldStrategy();
        this.agingStrategy = gameFactory.createAgingStrategy();
        this.winningStrategy = gameFactory.createWinningStrategy();
        this.unitActionsStrategy = gameFactory.createUnitActionStrategy();
        this.attackingStrategy = gameFactory.createAttackingStrategy();
        this.moveStrategy = gameFactory.createMoveStrategy();
        this.tileMap = worldLayoutStrategy.tileMap(this);
        this.cityMap = worldLayoutStrategy.cityMap(this);
        this.unitMap = worldLayoutStrategy.unitMap(this);
        playerInTurn = Player.RED;
        this.age = STARTINGAGE;
        this.amountOfRounds = 1;

        // Setup of unit characteristics
        unitCharacteristics = new HashMap<>();
        unitCharacteristics.put(ARCHER, new int[]{ARCHERCOST, ARCHERDEFENSE, ARCHERATTACK});
        unitCharacteristics.put(LEGION, new int[]{LEGIONCOST, LEGIONDEFENSE, LEGIONATTACK});
        unitCharacteristics.put(SETTLER, new int[]{SETTLERCOST, SETTLERDEFENSE, SETTLERATTACK});
        unitCharacteristics.put(SANDWORM, new int[]{SANDWORMCOST, SANDWORMDEFENSE, SANDWORMATTACK});
    }

    public void setCityAt(Position pos, CityImpl city) {
        if (getCityAt(pos) == null) {
            cityMap.put(pos, city);
        }
    }

    public void removeUnit(Position pos) {
        if (getUnitAt(pos) != null) {
            unitMap.remove(pos);
        }
    }

    private int getUnitCost(String unitType) {
        if (unitType.equals(SETTLER)) {
            return SETTLERCOST;
        }
        if (unitType.equals(LEGION)) {
            return LEGIONCOST;
        }
        if (unitType.equals(ARCHER)) {
            return ARCHERCOST;
        }
        return 0;
    }


    public void placeUnit(Unit unit, Position pos) {
        if (!(checkUnitIsInsideWorld(pos))) {
            return;
        }
        if (!(checkLegalTileTypeForUnitProduction(unit.getTypeString(), pos))) {
            return;
        }

        if (unitMap.get(pos) == null) {
            unitMap.put(pos, unit);
            notifyWorldChange(pos);
        } else {
            for (Position p : Utility.get8neighborhoodOf(pos)) {
                if (unitMap.get(p) == null) {
                    if(checkLegalTileTypeForUnitProduction(unit.getTypeString(), pos)) {
                        unitMap.put(p, unit);
                        notifyWorldChange(p);
                        break;
                    }
                }
            }
        }
    }

    public boolean checkLegalTileTypeForUnitProduction(String unitType, Position to){
        return moveStrategy.checkLegalTileTypeWhenProducing(this, unitType, to);

    }

    /**
     * Method that returns a tile at a given position
     */
    public Tile getTileAt(Position p) {
        return this.tileMap.get(p);
    }
    /**
     * Method that returns a unit at a given position
     */
    public Unit getUnitAt(Position p) {
        return this.unitMap.get(p);
    }

    /**
     * Method that returns a City at a given position
     */
    public CityImpl getCityAt(Position p) {
        return this.cityMap.get(p);
    }

    /**
     * Returns the player in turn.
     */
    public Player getPlayerInTurn() {
        return playerInTurn;
    }

    /**
     * Methods that can check if there is a winner.
     * @return
     */
    public Player getWinner() {
        return this.winner;
    }

    /**
     * Returns the current age.
     */
    public int getAge() {
        return this.age;
    }

    /**
     * Method that returns true, if a player can move their unit from a given tile to a given tile.
     */
    public boolean moveUnit(Position from, Position to) {
        if (legalMove(from, to)) {
            changeOwnerOfCity(to, from);
            makeActualMove(from, to);
            notifyWorldChange(from);
            notifyWorldChange(to);
            return true;
        }
        notifyWrongMove(from);
        return false;
    }

    private void notifyWrongMove(Position from) {
        Unit unit = getUnitAt(from);
        unitMap.put(from, null);
        notifyWorldChange(from);
        unitMap.put(from, unit);
        notifyWorldChange(from);
    }

    public void makeActualMove(Position from, Position to) {
        reduceSelectedUnitMoveCount(from);
        unitMap.put(to, getUnitAt(from));
        unitMap.remove(from);
    }

    public boolean legalMove(Position from, Position to) {
        // checks if the distance the player is trying to move the unit is legal
        if(!checkLegalDistanceMove(from, to)) return false;
        if(!checkUnitIsAbleToMove(from, to)) return false;
        if(!checkWorldAllowsMovement(from, to)) return false;
        return true;
    }

    public boolean checkUnitIsAbleToMove(Position from, Position to){
        if (getUnitAt(from) == null) {
            return false;
        }
        //Check if the units owner on the to tile, is an opponent player.
        if(!checkUnitsOwnerOnTile(from, to)){
            return false;
        }

        // check if the player is the right player to move the unit
        if (!checkIfPlayerCanMoveUnit(from)) {
            return false;
        }
        //Check that the units moveCount is more than 0
        if (getUnitAt(from).getMoveCount() <= 0) {
            return false;
        }
        return true;
        }

    public boolean checkWorldAllowsMovement(Position from, Position to) {
        //Checks if unit is inside the world
        if(!checkUnitIsInsideWorld(to)) {
            return false;
        }
        // Check if the tile the unit is trying to move to is legal.
        if(!checkLegalTileType(from, to)) {
            return false;
        }
        return true;
    }

    //Checks if unit is inside the world
    public boolean checkUnitIsInsideWorld(Position to){
        if(to.getRow() > WORLDSIZE || to.getRow() < 0) {
            return false;
        }
        if (to.getColumn() > WORLDSIZE || to.getColumn() < 0) {
            return false;
        }
        return true;
    }
    /** Method that reduces a given units moveCount by one. */
    public void reduceSelectedUnitMoveCount(Position pos) {
        UnitImpl unit = (UnitImpl) unitMap.get(pos);
        unit.reduceMoveCount();
    }

    /** Getter method for getting a units movecount */
    public int getUnitMoveCount(Position pos){
        return unitMap.get(pos).getMoveCount();
    }


    /** Checks if the player in turn has an unit on the desired tile. */
    public boolean checkUnitsOwnerOnTile(Position from, Position to) {
        //if there is no unit (it returns true), we can move without even attacking
        if (!checkIfUnitIsOnTile(to)) {
            return true;
        }
        Player unitsOwner = unitMap.get(to).getOwner();
        Player thisTurnsPlayer = getPlayerInTurn();
        // returns true if the unit on the tile checked is different the the player of the turn.
        if (unitsOwner != thisTurnsPlayer ) {
            if (attackingStrategy.getAttackOutcome(from, to, this)){
                return true;
            }
        }
        return false;

    }

    /** Legal method that returns true if a player is trying to move their own units, and otherwise returns false. */
    public boolean checkIfPlayerCanMoveUnit(Position pos) {
        Player player = unitMap.get(pos).getOwner();
        Player thisTurnsPlayer = getPlayerInTurn();

        if (player == thisTurnsPlayer) {
            return true;
        }

        return false;
    }

    /** Legal method the returns true if a given unit isn't moved more than one tile specified by game rules. */
    public boolean checkLegalDistanceMove(Position from, Position to) {
        int deltaC = from.getColumn() - to.getColumn();
        int deltaR = from.getRow() - to.getRow();

        if (deltaC <= UNITMAXMOVE && deltaR <= UNITMAXMOVE) {
            if (deltaC >= -UNITMAXMOVE && deltaR >= -UNITMAXMOVE) {
                return true;
            }
        }
        return false;
    }

    /** Legal method that return false if a position is a illegal tile. */
    public boolean checkLegalTileType(Position from, Position to ) {
        return moveStrategy.checkLegalTileType(this, from, to);
    }

    /** Method that returns true if a unit is on a given tile. */
    public boolean checkIfUnitIsOnTile(Position pos) {
        Unit unit = unitMap.get(pos);
        if (unit == null) {
            return false;
        }
        else {
            return true;
        }
    }

    public void produceUnit(Position p, String unitType) {
        if(cityCanProduceUnit(p, unitType)) {
            UnitImpl unit = makeUnit(p, unitType);
            placeUnit(unit, p);
            notifyWorldChange(p);
            System.out.println("Hejsa her er den position");
            System.out.println(p.toString());
            removeTreasury(p, unitType);
        }
    }


    private void removeTreasury(Position p, String unitType) {
        CityImpl city = getCityAt(p);
        int unitCost = getUnitCost(unitType);
        city.addTreasury(-unitCost);
    }

    private UnitImpl makeUnit(Position p, String unitType) {
        Player player = getCityAt(p).getOwner();
        return new UnitImpl(player, unitType);
    }

    private boolean cityCanProduceUnit(Position p, String unitType ) {
        int unitCost = getUnitCost(unitType);
        if ( getCityAt(p).getTreasury() >= unitCost) {
            return true;
        }
        return false;
    }



    /** Method that handles end of a round */
    private void endOfRound() {
        this.amountOfRounds++;
        this.age = agingStrategy.newAge(this.age);

        resetAllUnitMoveCount();

        // this if statements gets triggered when there is a winner.
        if (winningStrategy.getWinner(this) != null) {
            this.winner = winningStrategy.getWinner(this);
        }

        for (Position p: cityMap.keySet()) {
            CityImpl element = cityMap.get(p);
            element.addTreasury(ROUNDPRODUCTIONBONUS);
            String unitName = element.getSelectedUnitForProduction();
            if(unitName != null){
                System.out.println(element.getTreasury());
                if(unitName.equals(ARCHER)){
                    produceUnit(p, ARCHER);
                }
                if(unitName.equals(LEGION)){
                    produceUnit(p, LEGION);
                }
                if(unitName.equals(SETTLER)){
                    produceUnit(p, SETTLER);
                }
                if(unitName.equals(SANDWORM)){
                    produceUnit(p, SANDWORM);
                }
            }
        }
    }

    /** Method that resets all units in the games move count */
    public void resetAllUnitMoveCount() {
        for (Unit u: unitMap.values()) {
            if(u != null) {
                u = (UnitImpl) u;
                ((UnitImpl) u).resetMoveCount();
            }
        }
    }


    /** Methods that proceeds the game one round. */
    public void endOfTurn() {
        if(playerInTurn == Player.RED) {
            playerInTurn = Player.BLUE;
            notifyTurnEnds(Player.BLUE, getAge());
        }
        else {
            playerInTurn = Player.RED;
            endOfRound();
            notifyTurnEnds(Player.RED, getAge());
        }
    }

    public int getSucceededAttacks(Player p){
        if(p == Player.RED){
            return redSuccessfullAttacks;
        }
        if (p == Player.BLUE) {
            return blueSuccessfullAttacks;
        }
        return 0;
    }

    public void newSuccesfullAttack(Player p){
        if(p == Player.RED){
            redSuccessfullAttacks++;
        }
        if (p == Player.BLUE) {
            blueSuccessfullAttacks++;
        }

    }
/** TODO: DEN HER METODE KAN SLETTES BLIVER IKKE BRUGT I RESTEN AF PROGRAMMET.

    public void attackUnit(Position attackingUnitPosition, Position defendingUnitPosition) {
        Unit unit = getUnitAt(attackingUnitPosition);
        if (getPlayerInTurn() != getUnitAt(attackingUnitPosition).getOwner()) {
            return;
        }
        if (unitMap.get(defendingUnitPosition) != null && unitMap.get(defendingUnitPosition).getOwner() != unit.getOwner()) {
            if(checkLegalDistanceMove(attackingUnitPosition, defendingUnitPosition)) {
                changeOwnerOfCity(defendingUnitPosition, attackingUnitPosition);
                reduceSelectedUnitMoveCount(attackingUnitPosition);
                unitMap.remove(attackingUnitPosition);
                unitMap.put(defendingUnitPosition, unit);


            }
        }
    } */

    public void changeOwnerOfCity(Position newPosition, Position movingUnit) {

        if (getCityAt(newPosition) == null) {
            return;
        }
        if (getCityAt(newPosition) != null) {
            if (getCityAt(newPosition).getOwner() != getUnitAt(movingUnit).getOwner()) {
                getCityAt(newPosition).changeOwner();
            }
        }
    }

    public void changeWorkForceFocusInCityAt( Position p, String balance ) {
        CityImpl city = getCityAt(p);

    }
    public void changeProductionInCityAt( Position p, String unitType ) {
        CityImpl city = getCityAt(p);
        city.setSelectedUnitForProduction(unitType);
        notifyTileFocusChange(p);
    }

    public void performUnitActionAt( Position p ) {
        unitActionsStrategy.performUnitAction(this, p);
        notifyWorldChange(p);
        }

    @Override
    public void addObserver(GameObserver observer) {
    gameObserverArrayList.add(observer);
    }

    //Notify observer about world changes
    public void notifyWorldChange(Position position){
       for(GameObserver gameObserver: gameObserverArrayList){
           gameObserver.worldChangedAt(position);
       }
    }
    //Notify observer about turn ending
    public void notifyTurnEnds(Player nextPlayer, int age){
        for(GameObserver gameObserver: gameObserverArrayList){
            gameObserver.turnEnds(nextPlayer, age);
        }
    }
    //Notify observer about tile focus changes
    public void notifyTileFocusChange(Position position){
        for(GameObserver gameObserver: gameObserverArrayList){
            gameObserver.tileFocusChangedAt(position);
        }
    }
    @Override
    public void setTileFocus(Position position) {
        notifyTileFocusChange(position);
    }

    public ArrayList<CityImpl> getCityMap() {
        ArrayList<CityImpl> localCityMap = new ArrayList<>();
        for(CityImpl c : cityMap.values()) {
            localCityMap.add(c);
        }
        return localCityMap;
    }

    public Map<Position, Unit> getUnitMap() {
        return this.unitMap;
    }

    // Method that returns int array with desired unit characteristics.
    public int[] getUnitCharacteristics(String unit) {
        return this.unitCharacteristics.get(unit);
    }

    // Method that returns the total amount of rounds.
    public int getTotalAmountOfRounds() {
        return this.amountOfRounds;
    }

    public void resetSuccessfulAttacks() {
        this.blueSuccessfullAttacks = 0;
        this.redSuccessfullAttacks = 0;
    }

}
