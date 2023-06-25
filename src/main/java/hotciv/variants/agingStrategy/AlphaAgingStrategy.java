package hotciv.variants.agingStrategy;

import hotciv.framework.Game;

public class AlphaAgingStrategy implements AgingStrategy {

    @Override
    public int newAge(int age) {
    int newAge = age + 100;
        return newAge;
    }


}
