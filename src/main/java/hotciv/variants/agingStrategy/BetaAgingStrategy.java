package hotciv.variants.agingStrategy;

import hotciv.framework.Game;

public class BetaAgingStrategy implements AgingStrategy {
    @Override
    public int newAge(int age) {
        int newAge = age;

        if(newAge < -100 && newAge >= -4000){
            return newAge + 100;
        }
        else if(newAge == -100){
            return -1;
        }
        else if(newAge == -1){
            return 1;
        }
        else if(newAge == 1){
            return 50;
        }
        else if(newAge > 50 && newAge <= 1750){
            return newAge + 50;
        }
        else if(newAge < 1900 && newAge >= 1750){
            return newAge + 25;
        }
        else if(newAge > 1900 && newAge <= 1970){
            return newAge + 5;
        }

        return newAge+1;
    }
}
