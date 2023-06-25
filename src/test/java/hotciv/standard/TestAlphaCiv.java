package hotciv.standard;

import hotciv.decorator.GameDecorator;
import hotciv.domain.AlphaCivFactory;
import hotciv.framework.*;

import hotciv.utility.Utility;
import org.junit.jupiter.api.*;

import static hotciv.framework.GameConstants.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.*;

/** Skeleton class for AlphaCiv test cases

 Updated Aug 2020 for JUnit 5 includes
 Updated Oct 2015 for using Hamcrest matchers

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
public class TestAlphaCiv {
    private Game game;
    private GameImpl gameImpl;
    private Game gameDecorater;

    private Position p1_1 = new Position(1,1);
    private Position p2_0 = new Position(2,0);
    private Position p3_2 = new Position(3,2);
    private Position p2_1 = new Position(2,1);
    private Position p5_5 = new Position(5,5);
    private Position p4_1 = new Position(4,1);
    private CityImpl redCity;
    private CityImpl blueCity;


    /**
     * Fixture for alphaciv testing.
     */
    @BeforeEach
    public void setUp() {
        game = new GameImpl(new AlphaCivFactory());
        gameImpl = new GameImpl(new AlphaCivFactory());
        gameDecorater = new GameDecorator(gameImpl);

        redCity = gameImpl.getCityAt(p1_1);
        blueCity = gameImpl.getCityAt(p4_1);
    }

    /**
     * Test that a given city can produce an archer, when it has enough production.
     */
    @Test
    public void archerProductionForACity() {
        gameImpl.produceUnit(p1_1, ARCHER);
        assertThat(gameImpl.getUnitAt(p1_1), is(nullValue()));
        redCity.addTreasury(ARCHERCOST);
        gameImpl.produceUnit(p1_1, ARCHER);

        assertThat(gameImpl.getUnitAt(p1_1), is(not(nullValue())));
        assertThat(gameImpl.getUnitAt(p1_1).getOwner(), is(Player.RED));
        assertThat(gameImpl.getUnitAt(p1_1).getTypeString(), is(ARCHER));
    }

    /**
     * Test that cities loses treasury when building unit
     */
    @Test
    public void cityLoosesProduction() {
        redCity.addTreasury(ARCHERCOST);
        gameImpl.produceUnit(p1_1, ARCHER);
        assertThat(redCity.getTreasury(), is(0));
    }

    /**
     * Tests that the world has 16x16 tiles
     */
    @Test
    public void createWorld16_16() {
        Position lastTile = new Position(WORLDSIZE - 1, WORLDSIZE - 1);
        Position notATile = new Position(16, 17);

        assertThat(game.getTileAt(lastTile), is(not(nullValue())));
        assertThat(game.getTileAt(notATile), is(nullValue()));
    }

    /**
     * Test that there is a ocean tile on tile 0, 1
     */
    @Test
    public void oceanTileOn1_0() {
        Tile oceans = new TileImpl(OCEANS);
        assertThat(game.getTileAt(new Position(1, 0)).getTypeString(), is(oceans.getTypeString()));
    }

    /**
     * Test that there is a mountain tile on tile 2, 2
     */
    @Test
    public void mountainTileOn2_2() {
        Tile mountain = new TileImpl(MOUNTAINS);
        assertThat(game.getTileAt(new Position(2, 2)).getTypeString(), is(mountain.getTypeString()));
    }

    /**
     * Test that there is a hill tile on tile 0,1
     */
    @Test
    public void hillsTileOn0_1() {
        Tile hills = new TileImpl(HILLS);
        assertThat(game.getTileAt(new Position(0, 1)).getTypeString(), is(hills.getTypeString()));
    }

    /**
     * Tests that there is a red city at position (1,1)
     */
    @Test
    public void redCityPosition1_1() {
        City testRedCity = game.getCityAt(p1_1);
        assertThat(testRedCity, is(not(nullValue())));
        assertThat(testRedCity.getOwner(), is(Player.RED));
    }

    /**
     * Tests that there is a red city at position (1,1)
     */
    @Test
    public void blueCityPosition4_1() {
        City blueCity = game.getCityAt(p4_1);
        assertThat(blueCity, is(not(nullValue())));
        assertThat(blueCity.getOwner(), is(Player.BLUE));
    }

    /**
     * Test that cities get 6 production after each round
     */
    @Test
    public void shouldProduce6ProductionAfterFirstRound() {
        assertThat(redCity.getTreasury(), is(0));

        gameImpl.endOfTurn();
        gameImpl.endOfTurn();

        assertThat(redCity.getTreasury(), is(ROUNDPRODUCTIONBONUS));
        assertThat(blueCity.getTreasury(), is(ROUNDPRODUCTIONBONUS));
    }

    // FRS p. 455 states that 'Red is the first player to take a turn'.
    @Test
    public void shouldBeRedAsStartingPlayer() {
        assertThat(game.getPlayerInTurn(), is(Player.RED));
    }

    /**
     * Test that checks that there are two players
     */
    @Test
    public void twoPlayersInTotal() {
        assertThat(game.getPlayerInTurn(), is(Player.RED));
        game.endOfTurn();
        assertThat(game.getPlayerInTurn(), is(Player.BLUE));
        game.endOfTurn();
        assertThat(game.getPlayerInTurn(), is(Player.RED));
    }

    /**
     * Test that game starts at age 4000 BC
     */
    @Test
    public void gameStartsAtAge4000BC() {
        assertThat(game.getAge(), is(STARTINGAGE));
    }

    /**
     * Tests that the game advances 100 years each round
     */
    @Test
    public void gameAdvances100Years() {
        assertThat(game.getAge(), is(STARTINGAGE));
        game.endOfTurn();
        game.endOfTurn();
        assertThat(game.getAge(), is(STARTINGAGE + 100));
    }

    /**
     * Tests that red player wins when the age of the game is 3000 BC
     */
    @Test
    public void redPlayerWinsAt3000BC() {
        assertThat(gameDecorater.getWinner(), is(nullValue()));
        for (int i = 0; i < 20; i++) {
            gameDecorater.endOfTurn();
        }
        assertThat(gameImpl.getAge(), is(WINNINGAGE));
        assertThat(gameDecorater.getWinner(), is(Player.RED));
    }

    /**
     * Tests that Cities do not grow but stay at population size 1
     */
    @Test
    public void CitiesDoNotGrow() {
        assertThat(redCity.getSize(), is(1));
        game.endOfTurn();
        game.endOfTurn();
        assertThat(redCity.getSize(), is(1));
    }

    /**
     * Tests that units are constructed probably
     */
    @Test
    public void unitsAreConstructedProbably() {
        Unit unit = new UnitImpl(Player.RED, ARCHER) {
        };
        assertThat(unit.getOwner(), is(Player.RED));
        assertThat(unit.getTypeString(), is(ARCHER));
    }

    /**
     * Tests that there is a red archer unit at position (2,0)
     */
    @Test
    public void redArcherAt2_0() {
        Unit redArcher = game.getUnitAt(p2_0);
        assertThat(redArcher, is(not(nullValue())));
        assertThat(redArcher.getOwner(), is(Player.RED));
        assertThat(redArcher.getTypeString(), is(ARCHER));
    }

    /**
     * Tests that there is a Blue legion unit at position (3,2)
     */
    @Test
    public void blueLegionAt3_2() {
        Unit blueLegion = game.getUnitAt(p3_2);
        assertThat(blueLegion, is(not(nullValue())));
        assertThat(blueLegion.getOwner(), is(Player.BLUE));
        assertThat(blueLegion.getTypeString(), is(LEGION));
    }

    /**
     * Tests that there is a red settler unit at position (4,3)
     */
    @Test
    public void redSettlerAt4_3() {
        Unit redSettler = game.getUnitAt(new Position(4, 3)); //TODO:TJEK OM DER SKAL VÆRE FELTVAIRABEL
        assertThat(redSettler, is(not(nullValue())));
        assertThat(redSettler.getOwner(), is(Player.RED));
        assertThat(redSettler.getTypeString(), is(SETTLER));
    }


    /** Tests if unit is placed on the city that produces it when produced. */
    @Test
    public void unitIsPlacedOnCityWhenProduced() {
        CityImpl testCity = gameImpl.getCityAt(p1_1);
        testCity.addTreasury(SETTLERCOST);
        gameImpl.produceUnit(p1_1, SETTLER);
        assertThat(gameImpl.getUnitAt(p1_1), is(not(nullValue())));
    }

    /** Tests that unit is placed north if it cant be placed on city tile. */
    @Test
    public void unitIsPlacedNorthETCIfAUnitIsInCity(){ //TODO: der er redundancy ift til testen unitIsPlacedOnCItyWhenProduced()
        CityImpl testCity = gameImpl.getCityAt(p1_1);
        testCity.addTreasury(SETTLERCOST*2);
        gameImpl.produceUnit(p1_1, SETTLER);
        gameImpl.produceUnit(p1_1, ARCHER);
        assertThat(gameImpl.getUnitAt(p1_1).getTypeString(), is(SETTLER));
        assertThat(gameImpl.getUnitAt(new Position(0, 1)), is(not(nullValue())));//TODO tjek for feltvariabel
    }

    /** Tests that the unit the city has selected is made after the city has enough production */
    @Test
    public void selectUnitForProduction(){
        CityImpl testCity = gameImpl.getCityAt(p1_1);
        testCity.setSelectedUnitForProduction(ARCHER);
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        assertThat(gameImpl.getUnitAt(p1_1), is(not(nullValue())));
    }

    /** Tests that unit can not move more than one tile at a time.*/
    @Test
    public void testUnitCanNotMoveMoreThan1Tile() {
        gameDecorater.moveUnit(p2_0, p5_5);
        assertThat(gameImpl.getUnitAt(p2_0), is(not(nullValue())));
        assertThat(gameImpl.getUnitAt(p5_5), is(nullValue()));
    }

    /** Tests that unit cannot move more than one tile. */
    @Test
    public void testLegalDistanceMove(){
        System.out.println("Vi skal til at bruge vores decorater lige under her: ");
        gameDecorater.moveUnit(p2_0, p2_1);
        assertThat(gameImpl.getUnitAt(p2_1), is(not(nullValue())));
        assertThat(gameImpl.getUnitAt(p2_0), is(nullValue()));
    }

    /** Tests that unit cant move to a tile where a unit cant be placed. */
    @Test
    public void testUnitCanNotMoveToIllegalTile(){
        boolean legalTileTypeMove = gameImpl.checkLegalTileType(null, new Position (1,0));//TODO
        assertThat(legalTileTypeMove, is(false));
    }

    /** Tests that only the right player can move their own units. */
    @Test
    public void testIfPlayerCanMoveUnit(){
        boolean legalUnitOwner = gameImpl.checkIfPlayerCanMoveUnit(p3_2);
        assertThat(legalUnitOwner, is(false));
        gameImpl.endOfTurn();
        legalUnitOwner = gameImpl.checkIfPlayerCanMoveUnit(p3_2);
        assertThat(legalUnitOwner, is(true));
    }

    /** Tests method that checks if a unit is on a tile. */
    @Test
    public void testIfUnitIsOnTile(){
        boolean legalUnitTile = gameImpl.checkIfUnitIsOnTile(p2_0);
        assertThat(legalUnitTile, is(true));
    }

    /** Tests that the unit on a given tile is the same or not as the player in turn. */
    @Test
    public void testUnitsOwnerOnTile(){
        boolean legalUnitOwnerOnTile = gameImpl.checkUnitsOwnerOnTile(null, p3_2);
        assertThat(legalUnitOwnerOnTile, is(true));
        gameImpl.endOfTurn();
        legalUnitOwnerOnTile = gameImpl.checkUnitsOwnerOnTile(null, p3_2);
        assertThat(legalUnitOwnerOnTile, is(false));
    }

    @Test
    public void unitCanNotMoveOutsideWorld(){
    gameImpl.moveUnit(p2_0, new Position(1,1));
    gameImpl.moveUnit(new Position(1,1), new Position(1,0));
    assertThat(gameImpl.moveUnit(new Position(1,0), new Position(1, -1)), is(false));

    }

    @Test
    public void onlyMoveOneTile() {
        gameImpl.moveUnit(p2_0, new Position(3,1));
        gameImpl.moveUnit(new Position(3,1), new Position(4,1));
        assertThat(gameImpl.getUnitAt(new Position(4,1)), is(nullValue()));
        assertThat(gameImpl.getUnitAt(new Position(3,1)), is(not(nullValue())));
    }

    @Test
    public void canMoveAfterARound() {
        gameImpl.moveUnit(p2_0, new Position(3,1));
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.moveUnit(new Position(3,1), new Position(4,1));
        assertThat(gameImpl.getUnitAt(new Position(4,1)), is(not(nullValue())));
        assertThat(gameImpl.getUnitAt(new Position(3,1)), is(nullValue()));
    }

    @Test
    public void movementDoesNotAddUp() {
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.moveUnit(p2_0, new Position(3,1));
        gameImpl.moveUnit(new Position(3,1), new Position(4,1));
        assertThat(gameImpl.getUnitAt(new Position(4,1)), is(nullValue()));
        assertThat(gameImpl.getUnitAt(new Position(3,1)), is(not(nullValue())));
    }
/**
    @Test
    public void unitCanAttack() {
        gameImpl.endOfTurn();
       gameImpl.attackUnit(p3_2, new Position(4,3));
       assertThat(gameImpl.getUnitAt(new Position(4,3)).getOwner(), is(Player.BLUE));
       assertThat(gameImpl.getUnitAt(p3_2), is(nullValue()));
    }

    @Test
    public void unitCannotAttackOwnUnits() {
        gameImpl.endOfTurn();
        Unit unit = new UnitImpl(Player.BLUE, ARCHER);
        gameImpl.placeUnit(unit, new Position(3,3));
        gameImpl.attackUnit(p3_2, new Position(3,3));
        assertThat(gameImpl.getUnitAt(p3_2), is(notNullValue()));
    }*/

    @Test
    public void cityChangesOwnerWhenMoved() {
        gameImpl.moveUnit(new Position(4,3), new Position(4,2));
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.moveUnit(new Position(4,2), new Position(4,1));
        assertThat(gameImpl.getCityAt(p4_1).getOwner(), is(Player.RED));
    }

    /**
    @Test
    public void cityChangesOwnerWhenAttacked() {
        UnitImpl unit = new UnitImpl(Player.BLUE, ARCHER);
        gameImpl.placeUnit(unit, p4_1);
        gameImpl.moveUnit(new Position(4,3), new Position(4,2));
        gameImpl.endOfTurn();
        gameImpl.endOfTurn();
        gameImpl.attackUnit(new Position(4,2), p4_1);
        assertThat(gameImpl.getCityAt(p4_1).getOwner(), is(Player.RED));
    }
*/


    // In the next section the itterator is tested. Code below is written by Henrik
    @Test
    public void shouldHave78AsFirstElement() {
        Iterator<Position> i8 = Utility.get8neighborhoodIterator(new Position(8,8));
        Position p = i8.next();
        assertThat(p, is(new Position(7,8)));
    }

    @Test
    public void shouldHave79AsSecondElement() {
        Iterator<Position> i8 = Utility.get8neighborhoodIterator(new Position(8,8));
        Position p = i8.next();
        p = i8.next();
        assertThat(p, is(new Position(7,9)));
    }

    @Test
    public void shouldHaveTheRestOK() {
        Iterator<Position> i8 = Utility.get8neighborhoodIterator(new Position(8,8));
        Position p = i8.next();
        p = i8.next();

        p = i8.next();
        assertThat(p, is(new Position(8,9)));

        p = i8.next();
        assertThat(p, is(new Position(9,9)));

        p = i8.next();
        assertThat(p, is(new Position(9,8)));

        p = i8.next();
        assertThat(p, is(new Position(9,7)));

        p = i8.next();
        assertThat(p, is(new Position(8,7)));

        p = i8.next();
        assertThat(p, is(new Position(7,7)));

        assertThat(i8.hasNext(), is(false));
    }

    @Test
    public void shouldOnlyHave3ElementsAround00Position() {
        Iterator<Position> i8 = Utility.get8neighborhoodIterator(new Position(0,0));
        Position p = i8.next();
        assertThat(p, is(new Position(0,1)));

        p = i8.next();
        assertThat(p, is(new Position(1,1)));

        p = i8.next();
        assertThat(p, is(new Position(1,0)));

        assertThat(i8.hasNext(), is(false));
    }

    @Test
    public void shouldOnlyHave3ElementsAround15_15Position() {
        Iterator<Position> i8 = Utility.get8neighborhoodIterator(
                new Position(GameConstants.WORLDSIZE - 1, GameConstants.WORLDSIZE - 1));

        Position p = i8.next();
        assertThat(p, is(new Position(14, 15)));

        p = i8.next();
        assertThat(p, is(new Position(15, 14)));

        p = i8.next();
        assertThat(p, is(new Position(14, 14)));

        assertThat(i8.hasNext(), is(false));
    }

    @Test
    public void shouldSupportIterable() {
        List<Position> list = new ArrayList<>();
        for (Position p : Utility.get8neighborhoodOf(new Position(3,4))) {
            list.add(p);
        }
        assertThat(list, hasItems( new Position(2,4),
                new Position(3,3)));
        assertThat(list, not(hasItem(new Position(3,4))));
        assertThat(list.size(), is(8));
    }

    @Test
    public void whenUnitIsProducedItIsPlacedInWorld(){
        UnitImpl unit = new UnitImpl( Player.RED, ARCHER);
        gameImpl.placeUnit(unit, new Position(0,0));
        gameImpl.placeUnit(unit, new Position(0,0));

        assertThat(gameImpl.getUnitAt(new Position(-1, 0)), is(nullValue()));



    }
}



