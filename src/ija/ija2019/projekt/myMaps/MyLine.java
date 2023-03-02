/*	
 *  Třída MyLine reprezentující linku. Uchovává informace o ulicích a stopkách, kterými vede a své jméno.
 * 	Autor: Roman Štafl xstafl01 <xstafl01@stud.fit.vutbr.cz>
 *  Autor: Michal Vašut xvasut02 <xvasut02@stud.fit.vutbr.cz>
 */	
package ija.ija2019.projekt.myMaps;

import java.util.AbstractMap.SimpleImmutableEntry;

import ija.ija2019.projekt.maps.Line;
import ija.ija2019.projekt.maps.Stop;
import ija.ija2019.projekt.maps.Street;

import java.util.ArrayList;
import java.util.List;

public class MyLine implements Line{
	String lineId;
	List<Street> lineStreets = new ArrayList<>();
	List<Stop> lineStops = new ArrayList<>();
	public MyLine(String id) {
		this.lineId = id;
		this.lineStreets.clear();
		this.lineStops.clear();
	}
	
	@Override
	public String getId(){
		return this.lineId;
	}
	
	@Override
	public List<Stop> getStops(){
		return this.lineStops;
	}
	
	@Override
	public boolean addStop(Stop stop) {
		if(!this.lineStreets.isEmpty()) {
			//lineStreets neni prazdny
			/*
			if(!this.lineStreets.get(this.lineStreets.size()-1).follows(stop.getStreet())) {
				return false;
			}
			*/
		}
		this.lineStreets.add(stop.getStreet());
		this.lineStops.add(stop);
		return true;
	}

	@Override
	public boolean addStreet(Street street) {
		if(!this.lineStreets.isEmpty()) {
			if(!this.lineStreets.get(this.lineStreets.size()-1).follows(street) || this.lineStreets.get(this.lineStreets.size()-1).equals(street)) {
				return false;
			}
			this.lineStops.add(null);
			this.lineStreets.add(street);

			return true;
		}
		this.lineStreets.add(street);
		this.lineStops.add(null);
		return false;
	}

	@Override
	public List<SimpleImmutableEntry<Street, Stop>> getRoute() {
		List<SimpleImmutableEntry<Street, Stop>> list = new ArrayList<SimpleImmutableEntry<Street, Stop>>();
		for(int i = 0; i < this.lineStreets.size(); i++) {
			SimpleImmutableEntry<Street, Stop> entry = new SimpleImmutableEntry<Street, Stop>(this.lineStreets.get(i), this.lineStops.get(i));
			list.add(entry);
		}
		return list;
	}

	@Override
	public List<Street> getStreets() {
		return this.lineStreets;
	}

}
