package hotciv.domain;

import hotciv.variants.UnitActionsStrategy.AlphaUnitActionStrategy;
import hotciv.variants.UnitActionsStrategy.UnitActionsStrategy;
import hotciv.variants.WorldLayoutStrategy.AlphaWorldLayout;
import hotciv.variants.WorldLayoutStrategy.WorldLayoutStrategy;
import hotciv.variants.agingStrategy.AgingStrategy;
import hotciv.variants.agingStrategy.AlphaAgingStrategy;
import hotciv.variants.attackingStrategy.AttackingStrategy;
import hotciv.variants.attackingStrategy.EpsilonAttackingStrategy;
import hotciv.variants.attackingStrategy.RandomDiceStrategy.FixedRandomValueStrategy;
import hotciv.variants.attackingStrategy.RandomDiceStrategy.RandomDiceStrategy;
import hotciv.variants.moveUnitStrategy.AlphaMoveStrategy;
import hotciv.variants.moveUnitStrategy.MoveStrategy;
import hotciv.variants.winningStrategy.EpsilonWinningStrategy;
import hotciv.variants.winningStrategy.WinningStrategy;

public class EpsilonTestFactory implements GameFactory {
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
        return new EpsilonAttackingStrategy(new FixedRandomValueStrategy());
    }

    @Override
    public UnitActionsStrategy createUnitActionStrategy() {
        return new AlphaUnitActionStrategy();
    }

    @Override
    public WinningStrategy createWinningStrategy() {
        return new EpsilonWinningStrategy();
    }

    @Override
    public WorldLayoutStrategy createWorldStrategy() {
        return new AlphaWorldLayout();
    }
}