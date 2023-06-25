package hotciv.standard;

import hotciv.framework.City;
import hotciv.framework.GameConstants;
import hotciv.framework.Player;
import hotciv.framework.Unit;
import hotciv.standard.GameImpl;

import java.util.UUID;

import static hotciv.framework.GameConstants.*;

public class CityImpl implements City {

    private Player owner;
    private int treasury;
    private String selectedUnitForProduction;
    private String workforceFocus;
    private String id;

    public CityImpl(Player owner) {
        this.owner = owner;
        this.selectedUnitForProduction = ARCHER;
        this.workforceFocus = foodFocus;
        this.treasury = 0;
        id = UUID.randomUUID().toString();
    }

    @Override
    /** Method that returns the owner of the city */
    public Player getOwner() {
        return owner;
    }

    /**  */
    @Override
    public int getSize() {
        return CITYSIZE;
    }

    @Override
    public int getTreasury() {
        return this.treasury;
    }

    @Override
    public String getProduction() {
        return this.selectedUnitForProduction;
    }

    @Override
    public String getWorkforceFocus() {
        return this.workforceFocus;
    }

    @Override
    public String getCityID() {
        return this.id;
    }

    public void addTreasury(int amount) {
        this.treasury += amount;
    }

    public String getSelectedUnitForProduction() {
        return this.selectedUnitForProduction;
    }

    public void setSelectedUnitForProduction(String unitType) {
        this.selectedUnitForProduction = unitType;
    }

    public void setWorkforceFocus(String workforceFocus) {
        this.workforceFocus = workforceFocus;
    }

    public void changeOwner() {
        if (this.owner == Player.BLUE) {
            this.owner = Player.RED;
        }
        else {
            this.owner = Player.BLUE;
        }
    }
}
