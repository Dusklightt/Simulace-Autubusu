package ija.ija2019.projekt.maps;

import ija.ija2019.projekt.maps.Coordinate;
import ija.ija2019.projekt.myMaps.MyStop;

/**
 * Reprezentuje zastávku. Zastávka má svůj unikátní identifikátor a dále souřadnice umístění a zná ulici, na které je umístěna.
 * Zastávka je jedinečná svým identifikátorem. Reprezentace zastávky může existovat, ale nemusí mít
 * přiřazeno umístění (tj. je bez souřadnic a bez znalosti ulice). Pro shodu objektů platí, že dvě zastávky jsou shodné, pokud
 * mají stejný identifikátor.
 */
public interface Stop {
    /**
     * Vrátí identifikátor zastávky.
     * @return Identifikátor zastávky.
     */
    public String getId();
    
    /**
     * Vrátí pozici zastávky.
     * @return Pozice zastávky. Pokud zastávka existuje, ale dosud nemá umístění, vrací null.
     */
    public Coordinate getCoordinate();

    /**
     * Nastaví ulici, na které je zastávka umístěna.
     * @param s Ulice, na které je zastávka umístěna.
     */
    public void setStreet(Street s);

    /**
     * Vrátí ulici, na které je zastávka umístěna.
     * @return Ulice, na které je zastávka umístěna. Pokud zastávka existuje, ale dosud nemá umístění, vrací null.
     */
    public Street getStreet();

	/**
     * Vytvoří novou instanci MyStop.
     * @param id Název stopky.
     * @param coords Souřadnice stopky.
     * @return Nově vytvořená instance stopky.
     */
	public static Stop defaultStop(String id, Coordinate coords) {
		MyStop stop = new MyStop(id, coords);
		return stop;
	}
	
	public boolean inBetween(Coordinate coord1, Coordinate coord2);
	
}
