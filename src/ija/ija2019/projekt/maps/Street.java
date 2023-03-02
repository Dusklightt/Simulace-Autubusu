package ija.ija2019.projekt.maps;

import java.util.List;

import ija.ija2019.projekt.maps.Coordinate;
import ija.ija2019.projekt.myMaps.MyStreet;
import javafx.scene.shape.Line;

/**
 * Reprezentuje jednu ulici v mapě. Ulice má svůj identifikátor (název) a je definována souřadnicemi. Pro 1. úkol
 * předpokládejte pouze souřadnice začátku a konce ulice.
 * Na ulici se mohou nacházet zastávky.
 */
public interface Street {

	/**
     * Vrátí identifikátor ulice.
     * @return Identifikátor ulice.
     */
    public String getId();
    
    /**
     * Vrátí seznam souřadnic definujících ulici. První v seznamu je vždy počátek a poslední v seznamu konec ulice.
     * @return Seznam souřadnic ulice.
     */
    
    public List<Coordinate> getCoordinates();
    
    /**
     * Vrátí seznam zastávek na ulici.
     * @return Seznam zastávek na ulici. Pokud ulize nemá žádnou zastávku, je seznam prázdný.
     */
    public List<Stop> getStops();
    
    /**
     * Přidá do seznamu zastávek novou zastávku.
     * @param stop Nově přidávaná zastávka.
     * @return True nebo False.
     */
    public boolean addStop(Stop stop);

	/**
     * Vytvoří novou instanci MyStreet.
     * @param id Název ulice.
     * @param coords Libovolný počet souřadnic definujících ulici.
     * @return Nově vytvořená instance ulice.
     */
	public static Street defaultStreet(String id, Coordinate... coords) {
		MyStreet street = new MyStreet(id, null, null);
		street.streetCoords.clear();
		for(int i = 0; i < (coords.length-1); i++) {
			if((coords[i].getX() == coords[i+1].getX()) || (coords[i].getY() == coords[i+1].getY())) {
				street.streetCoords.add(coords[i]);
			}
			else {
				return null;
			}
		}
		street.streetCoords.add(coords[coords.length-1]);
		return street;
	}
	
    /**
     * Vrátí souřadnice začátku ulice.
     * @return Souřadnice začátku ulice.
     */
	public Coordinate begin();
	
	/**
     * Vrátí souřadnice konce ulice.
     * @return Souřadnice konce ulice.
     */
	public Coordinate end();

	/**
     * Zkontroluje, zda tato ulice navazuje na předanou ulici.
     * @param street Předaná ulice pro kontrolu.
     * @return True nebo False.
     */
	public boolean follows(Street street);

	/**
     * Vrátí stupen provozu ulice.
     * @return Stupen provozu ulice.
     */
	public int getLevel();

	/**
     * Nastaví stupen provozu ulice.
     * @param stupen Stupen provozu ulice.
     */
	public void setLevel(int stupen);

	public void addLine(Line line);

	public List<Line> getLines();

	public void setDelay(double delay);
	
	public double getDelay();
}
