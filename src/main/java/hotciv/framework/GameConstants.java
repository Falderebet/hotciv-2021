package hotciv.framework;

/** Collection of constants used in HotCiv Game. Note that strings are
 * used instead of enumeration types to keep the set of valid
 * constants open to extensions by future HotCiv variants.  Enums can
 * only be changed by compile time modification.

   This source code is from the book 
     "Flexible, Reliable Software:
       Using Patterns and Agile Development"
     published 2010 by CRC Press.
   Author: 
     Henrik B Christensen 
     Department of Computer Science
     Aarhus University
   
   Please visit http://www.baerbak.com/ for further information.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
 
       http://www.apache.org/licenses/LICENSE-2.0
 
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/
public class GameConstants {
  // The size of the world is set permanently to a 16x16 grid 
  public static final int WORLDSIZE = 16;
  //The size of the Cities permanently set at size one in AlphaCiv
  public static final int CITYSIZE = 1;

  // Unit characteristics constants
  public final static int COST_INDEX = 0;
  public final static int DEFENSE_INDEX = 1;
  public final static int ATTACK_INDEX = 2;

  // How fare units can move in alphaciv
  public static final int UNITMAXMOVE = 1;

  //Valid aging constants
  public static final int STARTINGAGE = -4000;
  public static final int WINNINGAGE = -3000;


  //Valid production constants
  public static final int ROUNDPRODUCTIONBONUS = 6;


  //Costs of units
  public static final int ARCHERCOST = 10;
  public static final int LEGIONCOST = 15;
  public static final int SETTLERCOST = 30;
  public static final int SANDWORMCOST = 30;

  // Defense of units
  public static final int ARCHERDEFENSE = 3;
  public static final int LEGIONDEFENSE = 2;
  public static final int SETTLERDEFENSE = 3;
  public static final int SANDWORMDEFENSE = 10;

  // Attack of units
  public static final int ARCHERATTACK = 2;
  public static final int LEGIONATTACK = 4;
  public static final int SETTLERATTACK = 0;
  public static final int SANDWORMATTACK = 0;

  // FORTIFIED MOVE COUNT
  public static final int FORTIFIEDMOVECOUNT = 0;

  //SARNDWORM MOVE COUNT
  public static final int SANDWORMMOVECOUNT = 2;


  // Valid unit types
  public static final String ARCHER    = "archer";
  public static final String LEGION    = "legion";
  public static final String SETTLER   = "settler";
  public static final String SANDWORM = "sandworm";

  // Valid terrain types
  public static final String PLAINS    = "plains";
  public static final String OCEANS    = "ocean";
  public static final String FOREST    = "forest";
  public static final String HILLS     = "hills";
  public static final String MOUNTAINS = "mountain";
  public static final String DESERT = "desert";


  // Valid production balance types
  public static final String productionFocus = "hammer";
  public static final String foodFocus = "apple";
}
