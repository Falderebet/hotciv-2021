package hotciv.broker;

import frds.broker.ClientRequestHandler;
import frds.broker.Invoker;
import frds.broker.Requestor;
import frds.broker.marshall.json.StandardJSONRequestor;
import hotciv.broker.invoker.HotCivCityInvoker;
import hotciv.broker.invoker.HotCivGameInvoker;
import hotciv.broker.invoker.HotCivRootInvoker;
import hotciv.broker.invoker.HotCivUnitInvoker;
import hotciv.broker.invoker.lookupStrategy.LookupInvoker;
import hotciv.broker.invoker.lookupStrategy.TestStubLookupStrategy;
import hotciv.broker.proxies.CityProxy;
import hotciv.broker.proxies.GameProxy;
import hotciv.broker.proxies.TileProxy;
import hotciv.broker.proxies.UnitProxy;
import hotciv.broker.stubs.StubCity2;
import hotciv.broker.stubs.StubGame3;
import hotciv.broker.stubs.StubTile2;
import hotciv.broker.stubs.StubUnit2;
import hotciv.framework.*;
import hotciv.stub.StubTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static hotciv.framework.GameConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;


public class TestBroker {
    private Game game;
    private City city;
    private Unit unit;
    private Tile tile;
    private LookupInvoker lookupStrategy;

    @BeforeEach
    public void setup(){
        lookupStrategy = new TestStubLookupStrategy();
        Game servant = new StubGame3();
        GameObserver nullObserver = new NullObserver();
        servant.addObserver(nullObserver);

        Invoker invoker = new HotCivRootInvoker(servant, lookupStrategy);
        ClientRequestHandler crh = new LocalMethodClientRequestHandler(invoker);

        Requestor requestor = new StandardJSONRequestor(crh);

        game = new GameProxy(requestor);
        game.addObserver(nullObserver);

        // City test setup.
        City cityServant = new StubCity2(Player.GREEN, 404);
        Invoker cityInvoker = new HotCivRootInvoker(servant, lookupStrategy);
        ClientRequestHandler crhCity = new LocalMethodClientRequestHandler(cityInvoker);
        Requestor cityRequestor = new StandardJSONRequestor(crhCity);
        city = new CityProxy(null, cityRequestor);
        
        // Unit test setup.
        Unit unitServant = new StubUnit2();
        Invoker unitInvoker = new HotCivRootInvoker(servant, lookupStrategy);
        ClientRequestHandler crhUnit = new LocalMethodClientRequestHandler(unitInvoker);
        Requestor unitRequestor = new StandardJSONRequestor(crhUnit);
        unit = new UnitProxy(null, unitRequestor);

        // Tile
        Tile tileServant = new StubTile2();
        Invoker tileInvoker = new HotCivRootInvoker(servant, lookupStrategy);
        ClientRequestHandler chrTile = new LocalMethodClientRequestHandler(tileInvoker);
        Requestor tileRequestor = new StandardJSONRequestor(chrTile);
        tile = new TileProxy(null, tileRequestor);
    }

    @Test
    public void shouldHaveWinner() {
        Player winner = game.getWinner();
        assertThat(winner, is(Player.YELLOW));
    }

    /*
    LILLE GUIDE TIL NÅR VI SKAL GÅ I GANG IGEN ._.
    0. Skriv en test der fejler.
    1. Gå ind i OperationNames tilføje metode navn.
    2. Gå ind i GameProxy og implementere metode (kig på getWinner metoden for inspiration).
    3. Gå ind i HotCivGameInvoker under try lav et nyt else if (VIGTIGT).
    4. Kør test og se den lykkes.
     */

    @Test
    public void getCityAt_ownerTest() {
        City city = game.getCityAt(new Position(1,1));
        assertThat(city.getOwner(), is(Player.GREEN));
    }

    @Test
    public void getCityAt_sizeTest() {
        City city = game.getCityAt(new Position(1,1));
        assertThat(city.getSize(), is(404));
    }

    @Test
    public void getCityAt_ProductionTest() {
        City city = game.getCityAt(new Position(1,1));
        assertThat(city.getProduction(), is(SANDWORM));
    }

    @Test
    public void getUnitAt_TypeStringTest() {
        Unit unit = game.getUnitAt(new Position(1,1));
        assertThat(unit.getTypeString(), is(SETTLER));
    }

    @Test
    public void getUnitAt_MoveCountTest() {
        Unit unit = game.getUnitAt(new Position(1,1));
        assertThat(unit.getMoveCount(), is(10));
    }

    @Test
    public void getUnitAt_OwnerTest() {
        Unit unit = game.getUnitAt(new Position(1,1));
        assertThat(unit.getOwner(), is(Player.YELLOW));
    }

    @Test
    public void getTileAt_TypeStringTest(){
        Tile tile = game.getTileAt(new Position(1,1));
        assertThat(tile.getTypeString(), is(DESERT));
    }
    @Test
    public void getPlayerInTurn() {
        Player player = game.getPlayerInTurn();
        assertThat(player, is(Player.GREEN));
    }

    @Test
    public void tryEndingTurn() {
        game.endOfTurn();
        assertThat(true, is(true));
    }


    @Test
    public void getCorrectAge() {
    int age = game.getAge();
    assertThat(age, is(42));
    }


   @Test
    public void possibleToMoveUnit() {
        boolean moveUnit = game.moveUnit(new Position(2,2), new Position(2,3));
        assertThat(moveUnit, is(true));
    }


    // City tests
    @Test
    public void testGetOwnerCity() {
        Player owner = city.getOwner();
        assertThat(owner, is(Player.GREEN));
    }

    @Test
    public void testGetSize() {
        int size = city.getSize();
        assertThat(size, is(404));
    }

    @Test
    public void testGetTreasury() {
        int treasury = city.getTreasury();
        assertThat(treasury, is(69));
    }

    @Test
    public void testGetProduction() {
        String unitString = city.getProduction();
        assertThat(unitString, is(SANDWORM));
    }

    @Test
    public void testGetWorkForceFocus() {
        String getWorkforce = city.getWorkforceFocus();
        assertThat(getWorkforce, is(productionFocus));
    }

    // Now we test the unit methods.
    @Test
    public void testGetTypeString() {
        String unitTypeString = unit.getTypeString();
        assertThat(unitTypeString, is(SETTLER));
    }

    @Test
    public void testGetOwnerUnit() {
        Player owner = unit.getOwner();
        assertThat(owner, is(Player.YELLOW));
    }

    @Test
    public void testGetMoveCount() {
        int moveCount = unit.getMoveCount();
        assertThat(moveCount, is(10));
    }

    @Test
    public void testGetDefensiveStrength() {
        int defense = unit.getDefensiveStrength();
        assertThat(defense, is(123));
    }

    @Test
    public void testGetAttackingStrength() {
        int attack = unit.getAttackingStrength();
        assertThat(attack, is(99));
    }

    // Test for tile interface
    //@Test
    //public void testGetTypeStringTile() {
    //    String tileString = tile.getTypeString();
    //    assertThat(tileString, is(DESERT));
    //}

    // TODO: SET TILE FOCUS OG ADD OBSERVER.

}
