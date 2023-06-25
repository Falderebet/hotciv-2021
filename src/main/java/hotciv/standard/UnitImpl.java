package hotciv.standard;

import hotciv.framework.Player;
import hotciv.framework.Unit;

import java.util.HashMap;
import java.util.UUID;

import static hotciv.framework.GameConstants.SANDWORM;


public class UnitImpl implements Unit {

    private Player owner;
    private String unitType;
    private int moveCount;
    private int defensiveStrength;
    private int attackingStrength;
    private String id;

    public UnitImpl(Player owner, String unitType){
        this.owner = owner;
        this.unitType = unitType;
        this.moveCount = getUnitMoveCount(this.unitType);
        id = UUID.randomUUID().toString();
    }

    @Override
    public String getTypeString() {
        return this.unitType;
    }

    @Override
    public Player getOwner() {
        return this.owner;
    }

    @Override
    public int getMoveCount() {
        return this.moveCount;
    }

    public int getUnitMoveCount(String type){
      if (type.equals(SANDWORM)) {
          return 2;
      }
      else{
          return 1;
      }
    }

    @Override
    public int getDefensiveStrength() {
        return this.defensiveStrength;
    }

    @Override
    public int getAttackingStrength() {
        return this.attackingStrength;
    }

    @Override
    public String getUnitId() {
        return id;
    }

    public void resetMoveCount() {
        this.moveCount = getUnitMoveCount(this.unitType);
    }

    public void setMoveCount(int newMoveCount) {
        this.moveCount = newMoveCount;
    }

    public void setDefensiveStrength(int defensiveStrength) {
        this.defensiveStrength  = defensiveStrength;
    }

    public void setAttackingStrength(int attackingStrength) {
        this.attackingStrength  = attackingStrength;
    }

    public void reduceMoveCount() {
        if (this.moveCount > 0) {
            this.moveCount--;
        }
    }
}
