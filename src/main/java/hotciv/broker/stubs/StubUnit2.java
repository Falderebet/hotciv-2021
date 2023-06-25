package hotciv.broker.stubs;

import hotciv.framework.Game;
import hotciv.framework.GameConstants;
import hotciv.framework.Player;
import hotciv.framework.Unit;

import java.util.UUID;

public class StubUnit2 implements Unit {

    private final String id;

    public StubUnit2() {
        this.id = UUID.randomUUID().toString();
    }
    @Override
    public String getTypeString() {
        return GameConstants.SETTLER;
    }

    @Override
    public Player getOwner() {
        return Player.YELLOW;
    }

    @Override
    public int getMoveCount() {
        return 10;
    }

    @Override
    public int getDefensiveStrength() {
        return 123;
    }

    @Override
    public int getAttackingStrength() {
        return 99;
    }

    @Override
    public String getUnitId() {
        return this.id;
    }
}
