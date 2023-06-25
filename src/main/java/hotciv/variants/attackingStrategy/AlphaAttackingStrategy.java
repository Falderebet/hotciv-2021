package hotciv.variants.attackingStrategy;

import hotciv.framework.Position;
import hotciv.framework.Unit;
import hotciv.standard.GameImpl;

public class AlphaAttackingStrategy implements AttackingStrategy {

    @Override
    public boolean getAttackOutcome(Position attackingUnitPosition, Position defendingUnitPosition, GameImpl game) {
        if(game.getUnitAt(attackingUnitPosition) != null) {
            game.newSuccesfullAttack(game.getUnitAt(attackingUnitPosition).getOwner());
        }
        return true;
    }

}
