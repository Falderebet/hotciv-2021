package hotciv.framework;

public interface NameService {

    void putCity(String objectID, City city);

    void putUnit(String objectID, Unit unit);

    void putTile(String objectID, Tile tile);

    City getCity(String objectID);

    Unit getUnit(String objectID);

    Tile getTile(String objectID);
}
