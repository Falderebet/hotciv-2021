package hotciv.broker.stubs;

import hotciv.framework.City;
import hotciv.framework.GameConstants;
import hotciv.framework.Player;

import java.util.UUID;

public class StubCity2 implements City {
    private Player owner;
    private String selectedUnitForProduction;
    private String id;

    public StubCity2(Player green, int i) {
        this.owner = green;
        this.id = UUID.randomUUID().toString();
    }


    @Override
    public Player getOwner() {
        return this.owner;
    }

    @Override
    public int getSize() {
        return 404;
    }

    @Override
    public int getTreasury() {
        return 69;
    }

    @Override
    public String getProduction() {
        return GameConstants.SANDWORM;
    }

    @Override
    public String getWorkforceFocus() {
        return GameConstants.productionFocus;
    }

    @Override
    public String getCityID() {
        return this.id;
    }

    public void setSelectedUnitForProduction(String unitType) {
        this.selectedUnitForProduction = unitType;
    }
}
