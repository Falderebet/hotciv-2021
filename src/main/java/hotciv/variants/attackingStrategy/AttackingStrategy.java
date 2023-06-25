package hotciv.variants.attackingStrategy;

import hotciv.framework.Position;
import hotciv.standard.GameImpl;

public interface AttackingStrategy {
   boolean getAttackOutcome(Position from, Position to, GameImpl game);

}
