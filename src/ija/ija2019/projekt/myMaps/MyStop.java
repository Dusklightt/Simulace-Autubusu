/*	
 *  Třída MyStop reprezentující stopku. Uchovává informaci o ulici, na které leží, své souřadnice a své jméno.
 * 	Autor: Roman Štafl xstafl01 <xstafl01@stud.fit.vutbr.cz>
 *  Autor: Michal Vašut xvasut02 <xvasut02@stud.fit.vutbr.cz>
 */	
package ija.ija2019.projekt.myMaps;

import ija.ija2019.projekt.maps.Coordinate;
import ija.ija2019.projekt.maps.Stop;
import ija.ija2019.projekt.maps.Street;

public class MyStop implements Stop{
	String stopId;
	Coordinate stopCoords;
	Street street;
	public MyStop(String id, Coordinate coord) {
		super();
		this.stopId = id;
		this.stopCoords = coord;
	}
	public MyStop(String id) {
		super();
		this.stopId = id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Stop stop = (Stop) obj;
		if(this.stopId == stop.getId()) {
			return true;
		}
		return false;
	}

	@Override
	public String getId() {
		return this.stopId;
	}

	@Override
	public Coordinate getCoordinate() {
		return this.stopCoords;
	}

	@Override
	public void setStreet(Street s) {
		this.street = s;
	}

	@Override
	public Street getStreet() {
		return this.street;
	}
	
	@Override
	public String toString() {
		return "stop("+this.getId()+")";
	}
	//NEW
	public boolean inBetween(Coordinate coord1, Coordinate coord2) {
		if(this.stopCoords.getX() == coord1.getX()) {
			if( (coord1.getY() > this.stopCoords.getY()) && (coord2.getY() < this.stopCoords.getY())) {
				return true;
			}
			if( (coord1.getY() < this.stopCoords.getY()) && (coord2.getY() > this.stopCoords.getY())) {
				return true;
			}
		}
		else if(this.stopCoords.getY() == coord1.getY()) {
			if( (coord1.getX() > this.stopCoords.getX()) && (coord2.getX() < this.stopCoords.getX())) {
				return true;
			}
			if( (coord1.getX() < this.stopCoords.getX()) && (coord2.getX() > this.stopCoords.getX())) {
				return true;
			}
		}
		return false;
	}


}
