/*    
 *  Třída Bus reprezentuje autobus zobrazený na vykreslené mapě a obsahuje operace potřebné k chodu autobusu.
 *  a své jméno.
 *	Autor: Roman Štafl xstafl01 <xstafl01@stud.fit.vutbr.cz>
 *  Autor: Michal Vašut xvasut02 <xvasut02@stud.fit.vutbr.cz>
 */    

package ija.ija2019.projekt.maps;

import java.util.Collections;
import java.util.List;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;

import javafx.scene.control.Label;

/**
 * Reprezentuje autobus, který je zobrazen na mapě, jezdí po zvolené lince a začíná na zvolených souřadnicích. Má uloženou celou cestu.
 * Autobus má informace o svých souřadnicích a souřadnice svého aktuálního cíle, které mohou reprezentovat stopku, zlom v ulici nebo začátek následující ulice.
 * Má aktuální a následující ulici, index aktuální ulice v Route, index aktuální souřadnice v ulici, int direction určující směr pohybu, a informaci, zda již vjel do poslední ulice v Route.
 */
public class Bus {
	public List<SimpleImmutableEntry<Street, Stop>> Route;
	public List<Stop> Stops = new ArrayList<Stop>();
	public Stop nextStop;
	public Coordinate coords = Coordinate.create(0,0);
	public Coordinate destination = Coordinate.create(1,1);;
	public Street currentStreet;
	Street nextStreet;
	int streetNumber = 0;
	int currentCoordinate = 0;
	public int direction = -1;
	Coordinate enteredLastStreetCoordinates = null;
	public List<Double> wholeRoute = new ArrayList<Double>();
	public List<Double> times = new ArrayList<Double>();
	public double traveled;
	public boolean tracked = false;
	public Line line;
	int wait = 0;
	public Double deportTime = new Double(0);
	public List<Label> labels = new ArrayList<Label>();
	Schedule schedule;
	
	public Bus(Line line, Coordinate coord, Schedule schedule){
		this.schedule = schedule;
		
		this.coords = coord;

		this.Route = line.getRoute();
		
		for(int i = 0; i < this.Route.size(); i++) {
			if(this.Route.get(i).getValue() != null) {
				this.Stops.add(this.Route.get(i).getValue());
				this.nextStop = this.Stops.get(0);
			}
		}
		
		this.line = line;

		this.currentStreet = this.Route.get(0).getKey();

		this.nextStreet = this.Route.get(1).getKey();
		
	}
	
	public void reset() {
		this.coords = Coordinate.create(this.line.getStops().get(0).getCoordinate().getX(), this.line.getStops().get(0).getCoordinate().getY());
		this.Route = line.getRoute();
		for(int i = 0; i < this.Route.size(); i++) {
			if(this.Route.get(i).getValue() != null) {
				this.Stops.add(this.Route.get(i).getValue());
				this.nextStop = this.Stops.get(0);
			}
		}
		this.currentStreet = this.Route.get(0).getKey();
		this.nextStreet = this.Route.get(1).getKey();
		this.streetNumber = 0;
		this.currentCoordinate = 0;
		this.direction = -1;
		this.wait = 0;
		this.tracked = false;
		this.traveled = 0.0;
		this.deportTime = 0.0;
		wholeRoad();
		updateLabels();
		getDestination();
	}
	
	/**
     * Spočítá délku cesty linky od jedné zastávky k další a list těchto délek uloží do "wholeRoute".
     * Zároveň spočítá čas potřebný k přijetí všech zastávek a uloží to do seznamu "times".
     */
	public void wholeRoad() {
		double route = 0;
		Coordinate lastCoord = null;
		this.times.clear();
		this.wholeRoute.clear();
		for(int i = 0; i < this.Route.size(); i++) {
			Street street = this.Route.get(i).getKey();
			
			List<Stop> stops = new ArrayList<>();
			int j = i;
			for(; j < this.Route.size(); j++) {
				if(this.Route.get(j).getKey().equals(street)) {
					stops.add(this.Route.get(j).getValue());
					i++;
				}
				else {
					break;
				}
			}
			i = j - 1;
			
			List<Coordinate> coords = street.getCoordinates();
			boolean reversed = false;
			if(coords.get(coords.size()-1).equals(lastCoord)) {
				Collections.reverse(coords);
				reversed = true;
			}
			
			for(j = 0; j < coords.size()-1; j++) {
				if(stops.get(0) != null) {
					for(int k = 0; k < stops.size(); k++) {
						if(stops.get(k).inBetween(coords.get(j), coords.get(j+1))) {
							route = route + Math.abs(coords.get(j).getX() - stops.get(k).getCoordinate().getX());
							route = route + Math.abs(coords.get(j).getY() - stops.get(k).getCoordinate().getY());
							route = Math.abs(route);
							
							this.times.add(route*0.2);
							this.wholeRoute.add(route);
							
							route = 0;
							
							if( k < (stops.size()-1) ) {
								if(stops.get(k+1).inBetween(coords.get(j), coords.get(j+1))) {
									for(int l = k; stops.get(l).inBetween(coords.get(j), coords.get(j+1)); l++) {
										if( l >= (stops.size()-1) ) {
											break;
										}
										route = route + stops.get(l).getCoordinate().getX() - stops.get(l+1).getCoordinate().getX();
										route = route + stops.get(l).getCoordinate().getY() - stops.get(l+1).getCoordinate().getY();
										route = Math.abs(route);
										
										this.times.add(route*0.2);
										this.wholeRoute.add(route);
										
										k++;
										route = 0;
									}
								}
							}
							
							route = route + Math.abs(coords.get(j+1).getX() - stops.get(k).getCoordinate().getX());
							route = route + Math.abs(coords.get(j+1).getY() - stops.get(k).getCoordinate().getY());
							route = Math.abs(route);
							break;
						}
						if( k >= (stops.size()-1)) {
							route = route + Math.abs(coords.get(j).getX() - coords.get(j+1).getX());
							route = route + Math.abs(coords.get(j).getY() - coords.get(j+1).getY());
							route = Math.abs(route);
						}
					}
				}
				else {
					route = route + Math.abs(coords.get(j).getX() - coords.get(j+1).getX());
					route = route + Math.abs(coords.get(j).getY() - coords.get(j+1).getY());
					route = Math.abs(route);
				}
			}
			lastCoord = coords.get(coords.size()-1);
			if(reversed == true) {
				Collections.reverse(coords);
			}
		}
		this.times.add(route*0.2);
		this.wholeRoute.add(route);
		
		this.wholeRoute.remove(0);
		this.wholeRoute.remove(this.wholeRoute.size()-1);
		
		this.times.set(0, 0.0);
		this.times.remove(this.times.size()-1);
	}

	/**
     * Vrátí rychlost autobusu vypočítanou z defaultní rychlosti a stupně provozu na aktuální ulici.
     * @return Rychlost autobusu.
     */
	public double getSpeed() {
		return 100/this.currentStreet.getLevel();
	}
	
	/**
	 * Funkce, která kontroluje jestli autobus má ještě čekat a když musí čekat tak sníží jeho dobu čekání o 1.
	 * @return true pokud autobus má ještě čekat jinak false.
	 */
	public boolean waiting() {
		if(this.wait > 0) {
			this.wait--;
			return true;
		}
		return false;
	}
	/**
	 * Nastaví pomocnou hodnotu "wait" pro čekání autobusu.
	 * @param seconds Kolik tiků má autobus čekat 
	 */
	public void setWait(int ticks) {
		this.wait = ticks;
	}
	
	private String doubleToTime(double time) {
		int sec = (int) (time%60);
		time = time/60;
		Integer hours = (int) (time/60);
		Integer minutes = (int) (time%60);
		Integer seconds = sec;
		String finalString;
		finalString = hours.toString();
		if(minutes < 10) {
			finalString = finalString + ":" + "0" + minutes.toString();
		}
		else {
			finalString = finalString + ":" + minutes.toString();
		}
		if(seconds < 10) {
			finalString = finalString + ":" + "0" + seconds.toString();
		}
		else {
			finalString = finalString + ":" + seconds.toString();
		}
		return finalString;
	}
	
	public void updateLabels() {
		Double time = this.deportTime + 1;
		Double calculatedTime = this.deportTime + 1;
		for(int i = 0; i <= this.times.size(); i++) {
			if(i > this.times.size()) {
				calculatedTime = calculatedTime + this.times.get(i) + 5;
			}
    	}
		for(int i = 0; i < this.wholeRoute.size(); i++) {
			time = time + this.times.get(i);
			if(this.traveled >= 0) {
				if(this.labels.size() != 0) {
					this.labels.get(i).setText(doubleToTime(time));
				}
    		}
    		else {
    			if(this.labels.size() != 0) {
					this.labels.get(i).setText(doubleToTime(time));
				}
    			if((i+1) > this.times.size()) {
	    			calculatedTime = calculatedTime - this.times.get(i+1);
	    			calculatedTime = calculatedTime - 5;
    			}
    		}
			time = time + 5;
		}
	}
	
	/**
	 * Funkce, která zjístí jestli autobus je na zastávce.
	 * Postupně ukládá následující zastávky do "nextStop" a vždy když je na této zastávce přepíše se na následující zastávku.
	 * @return True pokud autobus je na zastávce a False pokud není
	 */
	public boolean isBusOnStop() {
		if(this.coords.equals(this.nextStop.getCoordinate())) {
			this.nextStop = this.Stops.get(this.Stops.indexOf(nextStop)+1);
			return true;
		}
		return false;
	}
	/**
     * Zjistí kam má autobus jet a kterým směrem. Vypočítá kde na sebe aktuální a následující ulice navazují a v kterých souřadnicích se liší a z toho zjistí směr.
     * Zároven kontroluje, zda nejsme na poslední ulici v lince, v tom případě provede na poslední stopce reverse Route a jede po stejné trase zpět.
     */
	public void getDestination() {
		//pokud na zacatku nebo na konci dalsi ulice
		if(this.coords.equals(this.nextStreet.getCoordinates().get(0)) || this.coords.equals(this.nextStreet.getCoordinates().get(this.nextStreet.getCoordinates().size()-1))) {
			if(this.currentStreet.getDelay() != 0.0) {
				this.times.set(this.Stops.indexOf(this.nextStop), this.currentStreet.getDelay() + this.times.get(this.Stops.indexOf(this.nextStop)));
				updateLabels();
				this.schedule.update(this);
			}
			if(this.enteredLastStreetCoordinates == null) {
				//jsme na konci ulice currentStreet
				this.currentStreet = this.nextStreet;
				while(currentStreet.equals(nextStreet)) {
					if(this.streetNumber == Route.size()-1) {
						this.enteredLastStreetCoordinates = this.coords;
						break;
					}
					this.nextStreet = Route.get(this.streetNumber+1).getKey();
					this.streetNumber++;
				}
				this.currentCoordinate = 0;
				this.direction = -1;
				
				return;
			}
		}
		
		Coordinate currentStart, currentEnd, nextStart, nextEnd, busPosition;
		busPosition = this.coords;
		currentStart = this.currentStreet.getCoordinates().get(0);
		currentEnd = this.currentStreet.getCoordinates().get(this.currentStreet.getCoordinates().size()-1);
		nextStart = this.nextStreet.getCoordinates().get(0);
		nextEnd = this.nextStreet.getCoordinates().get(this.nextStreet.getCoordinates().size()-1);
		Coordinate to = null;
		
		
		
		if(this.enteredLastStreetCoordinates != null) {
			this.destination = this.Route.get(this.Route.size()-1).getValue().getCoordinate();
			if(this.coords.equals(this.destination)) {

				//jsme na posledni stopce
				Collections.reverse(this.Route);
				this.currentStreet = Route.get(0).getKey();
				this.nextStreet = Route.get(1).getKey();
				this.streetNumber = 0;
				this.currentCoordinate = 0;
				this.direction = -1;
				this.enteredLastStreetCoordinates = null;
				this.traveled = -this.traveled;
				Collections.reverse(this.Stops);
				for(int i = 0; i < this.times.size(); i++) {
					this.deportTime = this.deportTime + this.times.get(i) + 5;
				}
				return;
				
			}
			
			if(this.enteredLastStreetCoordinates == this.currentStreet.getCoordinates().get(0)) {
				this.currentCoordinate++;
				to = this.currentStreet.getCoordinates().get(this.currentStreet.getCoordinates().size()-1-this.currentCoordinate);
			}
			else {
				this.currentCoordinate++;
				to = this.currentStreet.getCoordinates().get(this.currentCoordinate);
			}
		}
		else if(currentStart.equals(nextStart) || currentStart.equals(nextEnd)) {
			//zjisteni kde na sebe ulice navazuji a tim zjisteni pozadovany smer autobusu - z aktualni pozice smerem k dalsi ulici
			// smerem k zacatku currentStreet
			to = currentStart;
			
			//zjisteni kde se nachazime vuci currentStreet

			//od currentEnd k currentStart
			this.currentCoordinate++;
			to = this.currentStreet.getCoordinates().get(this.currentStreet.getCoordinates().size()-1-this.currentCoordinate);
			this.destination = to;
			
		}
		else if(currentEnd.equals(nextEnd) || currentEnd.equals(nextStart)) {
			// smerem ke konci currentStreet
			to = currentEnd;
			
			//zjisteni kde se nachazime vuci currentStreet
			
			//od currentStart k currentEnd
			this.currentCoordinate++;
			to = this.currentStreet.getCoordinates().get(this.currentCoordinate);
			this.destination = to;
		}
		
		if(busPosition.getX() == this.destination.getX()) {
			//pohyb po Y souradnicich
			if(busPosition.getY()>this.destination.getY()) {
				//pohyb nahoru
				this.direction = 0;
			}
			else {
				//pohyb dolu
				this.direction = 1;
			}
		}
		else if(busPosition.getY() == this.destination.getY()) {
			//pohyb po X souradnicich
			if(busPosition.getX()<this.destination.getX()) {
				//pohyb doprava

				this.direction = 2;
			}
			else {
				//pohyb doleva

				this.direction = 3;
			}
		}
	}
}
