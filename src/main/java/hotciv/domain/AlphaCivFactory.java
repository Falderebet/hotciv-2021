package hotciv.domain;

import hotciv.variants.UnitActionsStrategy.AlphaUnitActionStrategy;
import hotciv.variants.UnitActionsStrategy.UnitActionsStrategy;
import hotciv.variants.WorldLayoutStrategy.AlphaWorldLayout;
import hotciv.variants.WorldLayoutStrategy.WorldLayoutStrategy;
import hotciv.variants.agingStrategy.AgingStrategy;
import hotciv.variants.agingStrategy.AlphaAgingStrategy;
import hotciv.variants.attackingStrategy.AlphaAttackingStrategy;
import hotciv.variants.attackingStrategy.AttackingStrategy;
import hotciv.variants.moveUnitStrategy.AlphaMoveStrategy;
import hotciv.variants.moveUnitStrategy.MoveStrategy;
import hotciv.variants.winningStrategy.AlphaWinningStrategy;
import hotciv.variants.winningStrategy.WinningStrategy;

public class AlphaCivFactory implements GameFactory {
    @Override
    public MoveStrategy createMoveStrategy() {
        return new AlphaMoveStrategy();
    }

    @Override
    public AgingStrategy createAgingStrategy() {
        return new AlphaAgingStrategy();
    }

    @Override
    public AttackingStrategy createAttackingStrategy() {
        return new AlphaAttackingStrategy();
    }

    @Override
    public UnitActionsStrategy createUnitActionStrategy() {
        return new AlphaUnitActionStrategy();
    }

    @Override
    public WinningStrategy createWinningStrategy() {
        return new AlphaWinningStrategy();
    }

    @Override
    public WorldLayoutStrategy createWorldStrategy() {
        return new AlphaWorldLayout();
    }
}
