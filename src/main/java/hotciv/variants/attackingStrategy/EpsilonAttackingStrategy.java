package hotciv.variants.attackingStrategy;

import hotciv.framework.Position;
import hotciv.framework.Unit;
import hotciv.standard.CityImpl;
import hotciv.standard.GameImpl;
import hotciv.utility.Utility;
import hotciv.variants.attackingStrategy.RandomDiceStrategy.DiceStrategy;

import java.util.Map;

import static hotciv.framework.GameConstants.*;


public class EpsilonAttackingStrategy implements AttackingStrategy {

    private DiceStrategy dice;

    public EpsilonAttackingStrategy(DiceStrategy diceStrategy) {
        dice = diceStrategy;
    }

    @Override
    public boolean getAttackOutcome(Position from, Position to, GameImpl game) {


        int attackingStrength = getUnitCombinedStrength(game, from, true);
        int defendingStrength = getUnitCombinedStrength(game, to, false);
        int totalAttackingStrength = getTotalStrength(attackingStrength);
        int totalDefendingStrength = getTotalStrength(defendingStrength);

        if (totalAttackingStrength > totalDefendingStrength) {
            game.newSuccesfullAttack(game.getUnitAt(from).getOwner());
            return true;
        }
        else {
            return false;
        }
    }

    public int getTotalStrength(int combinedStrength) {
        return combinedStrength * dice.rollDice();
    }

    public int getUnitCombinedStrength(GameImpl game, Position pos, Boolean attacking) {
        Unit unit = game.getUnitAt(pos);
        if (attacking) {
            return (game.getUnitCharacteristics(unit.getTypeString())[ATTACK_INDEX] + getAdjecentStrengths(game, pos)) * getTileMultiplier(game, pos);
        }
        else {
            return (game.getUnitCharacteristics(unit.getTypeString())[DEFENSE_INDEX] + getAdjecentStrengths(game, pos)) * getTileMultiplier(game, pos);
        }
    }

    public int getAdjecentStrengths(GameImpl game, Position pos) {
        int additionalStrength = 0;
        Map<Position, Unit> unitMap = game.getUnitMap();
        for (Position p : Utility.get8neighborhoodOf(pos)) {
            if (unitMap.get(p) != null) {
                Unit unit = unitMap.get(p);
                if (unit.getOwner() == game.getUnitAt(pos).getOwner()) {
                    additionalStrength++;
                }
            }
        }
        return additionalStrength;
    }

    public int getTileMultiplier(GameImpl game, Position pos) {
        String tile = game.getTileAt(pos).getTypeString();
        CityImpl city = game.getCityAt(pos);

        if (city != null) {
            return 3;
        }
        if (tile.equals(FOREST) || tile.equals(HILLS)) {
            return 2;
        }

        return 1;
    }
}
