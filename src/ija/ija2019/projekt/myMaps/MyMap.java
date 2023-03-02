/*	
 *  Třída MyMap reprezentující mapu. Importuje data ze souboru a uchovává informace o stopkách, linkách a ulicích.
 * 	Autor: Roman Štafl xstafl01 <xstafl01@stud.fit.vutbr.cz>
 *  Autor: Michal Vašut xvasut02 <xvasut02@stud.fit.vutbr.cz>
 */	

package ija.ija2019.projekt.myMaps;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ija.ija2019.projekt.maps.Stop;
import ija.ija2019.projekt.maps.Street;
import ija.ija2019.projekt.maps.Coordinate;
import ija.ija2019.projekt.maps.Line;

/**
 * Reprezentuje mapu. Mapa má svůj unikátní identifikátor a dále seznam ulic a stopek, které na mapě leží a seznam linek, které po mapě jezdí.
 */
public class MyMap {
	String mapId;
	public List<Street> mapStreets = new ArrayList<>();
	public List<Stop> mapStops = new ArrayList<>();
	public List<Line> mapLines = new ArrayList<>();
	public MyMap(String id) {
		super();
		this.mapId = id;
	}
	
	/**
     * Přidá ulici do seznamu ulic mapy.
     * @param street Přidaná ulice.
     */
	public void addStreet(Street street){
		this.mapStreets.add(street);
	}
	
	/**
     * Přidá stopku do seznamu ulic mapy.
     * @param stop Přidaná stopka.
     */
	public void addStop(Stop stop){
		this.mapStops.add(stop);
	}
	
	/**
     * Přidá linku do seznamu linek mapy.
     * @param line Přidaná linka.
     */
	public void addLine(Line line){
		this.mapLines.add(line);
	}
	
	/**
     * Vrátí ulici ze seznamu ulic mapy podle předaného identifikátoru ulice.
     * @param id Identifikátor ulice.
     * @return Ulice ze seznamu ulic.
     */
	public Street getStreet(String id) {
		for(int i = 0; i < this.mapStreets.size(); i++) {
			Street street = this.mapStreets.get(i);
			if(id.equals(street.getId())) {
				return street;
			}
		}
		return null;
	}
	
	/**
     * Vrátí stopku ze seznamu stopek mapy podle předaného identifikátoru stopky.
     * @param id Identifikátor stopky.
     * @return Stopka ze seznamu stopek.
     */
	public Stop getStop(String id) {
		if(id.equals("null")) {
			return null;
		}
		int size = this.mapStops.size();
		for(int i = 0; i < size; i++) {
			Stop stop = this.mapStops.get(i);
			if(id.equals(stop.getId())) {
				return stop;
			}
		}
		return null;
	}
	
	/**
     * Načte data o mapě z textového dokumentu. Volá se jen na začátku programu.
     */
	public void ImportData(){
		try {

			File myObj = new File("./data/mapdata.txt");

			Scanner myReader = new Scanner(myObj);

			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				String [] line = data.split(" ");

				if(line[0].equals("ulice")) {
					String name = line[1];
					data = myReader.nextLine();
					line = data.split(" ");
					Coordinate[] coords;
					coords = new Coordinate[line.length];

					for(int i = 0; i < line.length; i++) {
						int coordx = Integer.parseInt(line[i].split(",")[0]);
						int coordy = Integer.parseInt(line[i].split(",")[1]);
						coords[i] = new Coordinate(coordx, coordy);
					}

					Street street = Street.defaultStreet(name, coords);

					addStreet(street);
				}
				if(line[0].equals("stop")){
					Coordinate coords = new Coordinate(Integer.parseInt(line[2].split(",")[0]),Integer.parseInt(line[2].split(",")[1]));
					Stop stop = Stop.defaultStop(line[1], coords);
					
					stop.setStreet(getStreet(line[3]));
					this.addStop(stop);
				}
				if(line[0].equals("linka")) {
					String name = line[1];
					Line linka = Line.defaultLine(name);

					data = myReader.nextLine();
					String [] streets = data.split(" ");	

					data = myReader.nextLine();
					String [] stops = data.split(" ");

					for(int i = 0; i < streets.length; i++) {
						Street street = getStreet(streets[i]);
						String [] streetStops = stops[i].split(",");
						for(int j = 0; j < streetStops.length; j++) {
							Stop stop = getStop(streetStops[j]);
							if(stop == null) {
								continue;
							}
							street.addStop(stop);
							linka.addStop(stop);
						}
						linka.addStreet(street);
					}

					this.mapLines.add(linka);
				}
			}
			myReader.close();
		} 
		catch (FileNotFoundException e) {
			//blank map
			System.out.println("file not found");
		}
	}
}
