package hotciv.domain;

import hotciv.variants.UnitActionsStrategy.GammaUnitActionStrategy;
import hotciv.variants.UnitActionsStrategy.UnitActionsStrategy;
import hotciv.variants.WorldLayoutStrategy.AlphaWorldLayout;
import hotciv.variants.WorldLayoutStrategy.WorldLayoutStrategy;
import hotciv.variants.agingStrategy.AgingStrategy;
import hotciv.variants.agingStrategy.BetaAgingStrategy;
import hotciv.variants.attackingStrategy.AttackingStrategy;
import hotciv.variants.attackingStrategy.EpsilonAttackingStrategy;
import hotciv.variants.attackingStrategy.RandomDiceStrategy.DiceStrategy;
import hotciv.variants.attackingStrategy.RandomDiceStrategy.RandomDiceStrategy;
import hotciv.variants.moveUnitStrategy.AlphaMoveStrategy;
import hotciv.variants.moveUnitStrategy.MoveStrategy;
import hotciv.variants.winningStrategy.EpsilonWinningStrategy;
import hotciv.variants.winningStrategy.WinningStrategy;




public class SemiCivFactory implements GameFactory{


    @Override
    public MoveStrategy createMoveStrategy() {
        return new AlphaMoveStrategy();
    }

    @Override
    public AgingStrategy createAgingStrategy() {
        return new BetaAgingStrategy();
    }

    @Override
    public AttackingStrategy createAttackingStrategy() {
        return new EpsilonAttackingStrategy(new RandomDiceStrategy());
    }

    @Override
    public UnitActionsStrategy createUnitActionStrategy() {
        return new GammaUnitActionStrategy();
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
