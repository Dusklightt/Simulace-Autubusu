/*	
 *  Rozhraní Line reprezentuje linku.
 * 	Autor: Roman Štafl xstafl01 <xstafl01@stud.fit.vutbr.cz>
 *  Autor: Michal Vašut xvasut02 <xvasut02@stud.fit.vutbr.cz>
 */	
package ija.ija2019.projekt.maps;

import java.util.AbstractMap;
import java.util.List;

import ija.ija2019.projekt.myMaps.MyLine;
/**
 * Reprezentuje linku. Linka má svůj unikátní identifikátor a dále seznam ulic a stopek, které na lince leží.
 * Linka je jedinečná svým identifikátorem. 
 */
public interface Line {
	/**
     * Vytvoří novou instanci linky MyLine.
     * @param id Název linky.
     * @return Nově vytvořená instance linky.
     */
	static Line defaultLine(String id) {
		MyLine line = new MyLine(id);
		return line;
	}

	/**
     * Vrátí seznam všech stopek na lince.
     * @return Seznam stopek na lince.
     */
	public List<Stop> getStops();
	
	/**
     * Přidá stopku do linky.
     * @param stop1 Stopka, která se má přidat do linky.
     * @return True nebo False.
     */
	boolean addStop(Stop stop1);

	/**
     * Přidá ulici do linky.
     * @param s2 Ulice, která se má přidat do linky.
     * @return True nebo False.
     */
	boolean addStreet(Street s2);
	
	/**
     * Vrací seznam postupně jdoucích dvojic Street,Stop definujících linku.
     * @return Seznam dvojich Street,Stop.
     */
	public List<AbstractMap.SimpleImmutableEntry<Street,Stop>> getRoute();

	/**
     * Vrátí Identifikátor linky.
     * @return Identifikátor linky.
     */
	public String getId();

	/**
     * Vrátí seznam všech ulic na lince.
     * @return Seznam ulic na lince.
     */
	public List<Street> getStreets();
}
