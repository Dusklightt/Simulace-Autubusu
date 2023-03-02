/*	
 *  Třída MyStreet reprezentující ulici. Uchovává seznam souřadnic, které ulici definují, seznam stopek, které na ulici leží, stupen provozu na ulici
 *  a své jméno.
 * 	Autor: Roman Štafl xstafl01 <xstafl01@stud.fit.vutbr.cz>
 *  Autor: Michal Vašut xvasut02 <xvasut02@stud.fit.vutbr.cz>
 */	
package ija.ija2019.projekt.myMaps;

import java.util.ArrayList;
import java.util.List;

import ija.ija2019.projekt.maps.Coordinate;
import ija.ija2019.projekt.maps.Stop;
import ija.ija2019.projekt.maps.Street;

public class MyStreet implements Street{
	String streetId;
	public List<Coordinate> streetCoords = new ArrayList<>();
	List<Stop> streetStops = new ArrayList<>();
	List<javafx.scene.shape.Line> streetLines = new ArrayList<>();
	public int stupen = 1;
//////////////////////////////////////////////////////
	public double delay = 0.0;
//////////////////////////////////////////////////////
	public MyStreet(String id, Coordinate start, Coordinate end) {
		super();
		this.streetId = id;
		List<Coordinate> list = this.streetCoords;
		list.add(start);
		list.add(end);
		this.streetCoords = list;
	}

	@Override
	public String getId() {
		return this.streetId;
	}

	@Override
	public List<Coordinate> getCoordinates() {
		return this.streetCoords;
	}

	@Override
	public List<Stop> getStops() {
		return this.streetStops;
	}
	
	@Override
	public void addLine(javafx.scene.shape.Line line) {
		this.streetLines.add(line);
	}
	
	@Override
	public List<javafx.scene.shape.Line> getLines() {
		return this.streetLines;
	}

	@Override
	public boolean addStop(Stop stop) {
		for(int i = 0; i < this.streetCoords.size()-1; i++) {
			//ulice.X0 == ulice.X1
			if(this.streetCoords.get(i).getX() == this.streetCoords.get(i+1).getX()) {
				//stop.X == ulice.X0
				if(stop.getCoordinate().getX() == this.streetCoords.get(i).getX()) {
					/* 
					[ stop.Y > ulice.Y0  AND  stop.Y < ulice.Y1 ] 
					OR
					[ stop.Y < ulice.Y0 && stop.Y > ulice.Y1 ]
					*/
					if(stop.getCoordinate().getY() > this.streetCoords.get(i).getY() && stop.getCoordinate().getY() < this.streetCoords.get(i+1).getY() || stop.getCoordinate().getY() < this.streetCoords.get(i).getY() && stop.getCoordinate().getY() > this.streetCoords.get(i+1).getY()) {
						this.streetStops.add(stop);
						stop.setStreet(this);
						return true;
					}
				}
			}
			else if(this.streetCoords.get(i).getY() == this.streetCoords.get(i+1).getY()) {
				if(stop.getCoordinate().getY() == this.streetCoords.get(i).getY()) {
					if(stop.getCoordinate().getX() > this.streetCoords.get(i).getX() && stop.getCoordinate().getX() < this.streetCoords.get(i+1).getX() || stop.getCoordinate().getX() < this.streetCoords.get(i).getX() && stop.getCoordinate().getX() > this.streetCoords.get(i+1).getX()) {
						this.streetStops.add(stop);
						stop.setStreet(this);
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public Coordinate begin() {
		return this.streetCoords.get(0);
	}

	@Override
	public Coordinate end() {
		return this.streetCoords.get(this.streetCoords.size()-1);
	}

	@Override
	public boolean follows(Street street) {
		if (this.begin().equals(street.begin()) || this.begin().equals(street.end()) || this.end().equals(street.begin()) || this.end().equals(street.end())) {
			return true;
		}
		return false;
	}

	@Override
	public int getLevel() {
		return this.stupen;
	}

	@Override
	public void setLevel(int stupen) {
		this.stupen = stupen;
	}
//////////////////////////////////////////////////////
	@Override
	public void setDelay(double delay) {
		this.delay = delay;
	}
	
	@Override
	public double getDelay() {
		return this.delay;
	}
//////////////////////////////////////////////////////

}
