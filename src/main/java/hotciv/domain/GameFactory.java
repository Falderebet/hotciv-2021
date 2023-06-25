package hotciv.domain;

import hotciv.variants.UnitActionsStrategy.UnitActionsStrategy;
import hotciv.variants.WorldLayoutStrategy.WorldLayoutStrategy;
import hotciv.variants.agingStrategy.AgingStrategy;
import hotciv.variants.attackingStrategy.AttackingStrategy;
import hotciv.variants.attackingStrategy.RandomDiceStrategy.DiceStrategy;
import hotciv.variants.moveUnitStrategy.MoveStrategy;
import hotciv.variants.winningStrategy.WinningStrategy;

public interface GameFactory {

    MoveStrategy createMoveStrategy();

    AgingStrategy createAgingStrategy();

    AttackingStrategy createAttackingStrategy();

    UnitActionsStrategy createUnitActionStrategy();

    WinningStrategy createWinningStrategy();

    WorldLayoutStrategy createWorldStrategy();

}
