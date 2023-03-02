/*	
 *  
 * 	Autor: Roman Štafl xstafl01 <xstafl01@stud.fit.vutbr.cz>
 *  Autor: Michal Vašut xvasut02 <xvasut02@stud.fit.vutbr.cz>
 */	

package ija.ija2019.projekt;

import ija.ija2019.projekt.maps.Coordinate;
import ija.ija2019.projekt.maps.Stop;
import ija.ija2019.projekt.maps.Street;
import ija.ija2019.projekt.maps.Line;
import ija.ija2019.projekt.maps.Schedule;
import ija.ija2019.projekt.maps.Bus;
import ija.ija2019.projekt.myMaps.MyMap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.AbstractMap.SimpleImmutableEntry;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Třída main obstarávající vykreslení mapy a interakci s ní a řízení pohybu autobusů.
 * Má mapu MyMap obsahující seznamy všech ulic, stopek a linek. Má seznam všech vytvořených autobusů.
 * Má skupinu mainGroup do které se ukládají všechny prvky potřebné pro vykreslení mapy.
 * Má seznam levels uchovávající barvy pro znázornění stupnů provozu.
 * Má seznam časovačů autobusů, potřebný pro manipulaci s časem.
 */
public class Main extends Application{
	Schedule schedule = new Schedule();
	MyMap map = new MyMap("defaultmap");
    List<Bus> buses = new ArrayList<>();
    List<Color> levels = new ArrayList<>();
    List<Timeline> timelines = new ArrayList<>();
    Group mainGroup = new Group();
    Group secondaryGroup = new Group();
	MyClock mainClock = new MyClock(this.timelines);
	int busWait = 5;
	int selection = 0; // 0 - normal, 1 - poslouchani na uzavreni ulice, 2 - cekani na definici objizdky
	Button modeButton;
	Street closingStreet;
    List<Street> driveAround = new ArrayList<>();
    /**
     * Funkce pro vykreslení všech ulic. Pro každou ulici v seznamu zavolá funkci drawStreet.
     * @param streets Seznam všech ulic.
     */
    private void drawStreets(List<Street> streets) {
    	for(int i = 0; i < streets.size(); i++) {
    		this.drawStreet(streets.get(i));
    	}
    }
    
    /**
     * Vykreslí ulici jako čáru. Zároven ulici přiřadí event listener pro interaktivnost.
     * @param street Vykreslovaná ulice.
     */
	private void drawStreet(Street street) {
		List<Coordinate> coords = street.getCoordinates();
		Group group = new Group();
		List<Color> levels = this.levels;
		
		for(int i = 0; i < coords.size()-1; i++) {
			Coordinate coord1 = coords.get(i);
			Coordinate coord2 = coords.get(i+1);
			
			javafx.scene.shape.Line line = new javafx.scene.shape.Line();
			line.setStartX(coord1.getX());
			line.setStartY(coord1.getY());
			line.setEndX(coord2.getX());
			line.setEndY(coord2.getY());
			group.getChildren().add(line);
			street.addLine(line);
		}
		
		List<Bus> buses = this.buses;
		// CLICK NA ULICI
		group.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	if(getMode() == 0 || getMode() == 1) {
	            	for(Bus bus : buses) {
	            		if(bus.currentStreet.equals(street)) {
	            			return;
	            		}
	            	}
            	}

            	//mode - zvysovani hustoty provozu
            	if(getMode() == 0) {
	        		int level = street.getLevel();
	        		if(level == -1) {
	        			//ulice je uzavrena, do nothing
	        		}
	        		else if(event.getButton() == MouseButton.PRIMARY) {
	            		if(level != 1) {
	            			level--;
	            			if(level == 3) {
	            				level = 2;
	            			}
	            			
	            			if(level == 7) {
	                            level = 4;
	                        }
	            			
	            			street.setLevel(level);
	            			group.getChildren().forEach((line) -> {
	            				((Shape) line).setStroke(levels.get(street.getLevel()-1));
	            			});
	            		}
	            		double delay = 0.0;
	            		for(int i = 0; i < street.getCoordinates().size()-1; i++) {
	            			delay = delay + street.getCoordinates().get(i).getX() - street.getCoordinates().get(i+1).getX();
	            			delay = delay + street.getCoordinates().get(i).getY() - street.getCoordinates().get(i+1).getY();
	            			delay = Math.abs(delay);
	            		}
	            		delay = delay * 0.2 * level - delay * 0.2;
	            		street.setDelay(delay);
	            		
	            	}
	            	else if(event.getButton() == MouseButton.SECONDARY) {
	            		if(level != 8) {
	            			level++;
	            			if(level == 3) {
	            				level = 4;
	            			}
	            			if(level == 5) {
	                            level = 8;
	                        }
	            			street.setLevel(level);
	            			group.getChildren().forEach((line) -> {
	            				((Shape) line).setStroke(levels.get(street.getLevel()-1));
	            			});
	            		}
	            		double delay = 0.0;
	            		for(int i = 0; i < street.getCoordinates().size()-1; i++) {
	            			delay = delay + street.getCoordinates().get(i).getX() - street.getCoordinates().get(i+1).getX();
	            			delay = delay + street.getCoordinates().get(i).getY() - street.getCoordinates().get(i+1).getY();
	            			delay = Math.abs(delay);
	            		}
	            		delay = delay * 0.2 * level - delay * 0.2;
	            		street.setDelay(delay);
	            		
	            	}
	            	else {
	            		//nothing
	            	}
            	}
            	//mode - uzavreni ulice
            	else if(getMode() == 1){
            		pauseTime();
            		street.setLevel(-1);
            		changeMode(null);
            		setClosingStreet(street);
            		group.getChildren().forEach((line) -> {
        				((Shape) line).getStrokeDashArray().add(20d);
        				((Shape) line).setStroke(Color.BLACK);
        			});
            	}
            	//mode - vyber objizdky
            	else {
            		driveAroundAddStreet(street);
            	}
            }
        });

		this.mainGroup.getChildren().add(group);
	}
	
    /**
     * Funkce pro vykreslení všech stopek. Zavolá funkci drawStop pro každou stopku v seznamu.
     * @param stops Seznam všech stopek.
     */
	private void drawStops(List<Stop> stops) {
		for(int i = 0; i < stops.size(); i++) {
    		this.drawStop(stops.get(i));
    	}
	}
	
    /**
     * Vykreslí stopku jako čtverec.
     * @param stop Vykreslovaná stopka.
     */
	private void drawStop(Stop stop) {
		Coordinate coords = stop.getCoordinate();
		Group group = new Group();
		
		Rectangle rec = new Rectangle();
		rec.setX(coords.getX()-2);
		rec.setY(coords.getY()-2);
		rec.setWidth(4);
		rec.setHeight(4);

		group.getChildren().add(rec);
		
		this.mainGroup.getChildren().add(group);
	}
	
    /**
     * Funkce pro vykreslení všech autobusů.
     * @param lines Seznam všech autobusových linek.
     */
	private void drawLines(List<Line> lines) {
		for(int i = 0; i < lines.size(); i++) {
    		this.drawLine(lines.get(i));
    	}
	}
	
	/**
	 * Funkce pro převedení času z formátu "double" do "String".
	 * @param double Čas v minutách
	 * @return String Čas ve formátu HH:mm
	 */
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
	
    /**
     * Vykreslí autobus jako kolečko a přiřadí mu časovač, díky kterému se na mapě posouvá.
     * @param line Linka, po které autobus jezdí.
     */
	private void drawLine(Line line) {
		//autobus zacina na prvni stopce v lince

		Coordinate coords = Coordinate.create(line.getStops().get(0).getCoordinate().getX(), line.getStops().get(0).getCoordinate().getY());

		Bus bus = new Bus(line, coords, this.schedule);
		Group group = new Group();

		this.buses.add(bus);
		
		Circle circle = new Circle();
		circle.setCenterX(coords.getX());
		circle.setCenterY(coords.getY());
		circle.setRadius(5);
		circle.setFill(Color.TRANSPARENT);
		circle.setStroke(Color.BLACK);
		
		Group group2 = this.secondaryGroup;
		
		
		bus.wholeRoad();
		List<Bus> buses = this.buses;
		List<Color> levels = this.levels;
		javafx.scene.shape.Line line2 = new javafx.scene.shape.Line();
		Schedule schedule = this.schedule;
		// CLICK NA AUTOBUS
		circle.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            	// kliknuti na jiny autobus
            	buses.forEach((bus) -> {
            		if(bus.tracked == true) {
            			bus.tracked = false;
            			bus.Route.forEach((streetstop) -> {
            				streetstop.getKey().getLines().forEach((cara) -> {
            					cara.setStroke(levels.get(streetstop.getKey().getLevel()-1));
            				});
            			});
            		}
            	});
            	
            	//zvýraznění celé linky
            	bus.Route.forEach((streetstop) -> {
            		streetstop.getKey().getLines().forEach((cara) -> {
            			cara.setStroke(Color.DARKGREEN);
            			bus.tracked = true;
            		});
            	});
            	
            	group2.getChildren().clear();
            	
            	//zobrazeni itinerare
            	int lineSize = 0;
            	int labelOffsetY = 898;
            	int labelprevOffsetX = 0;
            	Double time = bus.deportTime + 1;
            	bus.labels.clear();
            	Double calculatedTime = bus.deportTime + 1;
            	for(int i = 0; i < bus.times.size(); i++) {
            		calculatedTime = calculatedTime + bus.times.get(i) + busWait;
            	}
            	calculatedTime = calculatedTime - busWait;
            	for(int i = 0; i < bus.wholeRoute.size();i++) {
            		//vytvoreni zastavky
            		Rectangle rec = new Rectangle();
            		rec.setX(lineSize/4+50);
            		rec.setY(898);
            		rec.setWidth(4);
            		rec.setHeight(4);
            		//vytvoreni labelu s casem
            		if(labelOffsetY == 882) {
            			labelOffsetY = 898;
            		}
            		else if(labelprevOffsetX > (lineSize/4+50-10)) {
            			labelOffsetY = 882;
            		}
            		Label label = new Label();
            		time = time + bus.times.get(i);
            		if(bus.traveled >= 0) {
	            		label.setText(doubleToTime(time));
            		}
            		else {
            			label.setText(doubleToTime(calculatedTime));
            			calculatedTime = calculatedTime - bus.times.get(i+1);
            			calculatedTime = calculatedTime - busWait;
            		}
            		time = time + busWait;
            		label.setLayoutX(lineSize/4+50-10);
            		label.setLayoutY(labelOffsetY);
            		label.setMaxWidth(40);
            		labelprevOffsetX = lineSize/4+50-10+40;
            		lineSize = (int) (lineSize + bus.wholeRoute.get(i));
            		
            		group2.getChildren().add(rec);
            		group2.getChildren().add(label);
            		bus.labels.add(label);
            	}
            	
            	Rectangle rec = new Rectangle();
        		rec.setX(lineSize/4+50);
        		rec.setY(898);
        		rec.setWidth(4);
        		rec.setHeight(4);
        		group2.getChildren().add(rec);
            	
        		if(labelOffsetY == 882) {
        			labelOffsetY = 898;
        		}
        		else if(labelprevOffsetX > (lineSize/4+50-10)) {
        			labelOffsetY = 882;
        		}
        		time = time + bus.times.get(bus.times.size()-1);
        		Label label = new Label();
        		if(bus.traveled >= 0) {
            		label.setText(doubleToTime(time));
        		}
        		else {
        			label.setText(doubleToTime(bus.deportTime));
        		}
        		label.setLayoutX(lineSize/4+50-10);
        		label.setLayoutY(labelOffsetY);
        		label.setMaxWidth(60);
        		group2.getChildren().add(label);
        		bus.labels.add(label);
        		
        		//vytvoreni trasy autobusu
    			line2.setStartX(50);
    			line2.setStartY(900);
    			line2.setEndX(lineSize/4+50);
    			line2.setEndY(900);
    			group2.getChildren().add(line2);
    			
    			Circle circle = new Circle();
    			circle.setCenterX(Math.abs(bus.traveled)/4 + 50);
    			circle.setCenterY(900);
    			circle.setRadius(5);
    			circle.setFill(Color.TRANSPARENT);
    			circle.setStroke(Color.BLACK);
    			group2.getChildren().add(circle);
    			
    			schedule.update(bus);
    			
    			Timeline oneSecond = new Timeline(new KeyFrame(Duration.seconds(0.2), new EventHandler<ActionEvent>() {
    				@Override
    				public void handle(ActionEvent event) {
    					circle.setCenterX(Math.abs(bus.traveled)/4 + 50);
    				}
    			}));
    			oneSecond.setCycleCount(Timeline.INDEFINITE);
    			oneSecond.play();
            }
        });
		
		group.getChildren().add(circle);
		this.mainGroup.getChildren().add(group);
		this.secondaryGroup = group2;
		Timeline oneSecond = new Timeline(new KeyFrame(Duration.seconds(0.2), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(bus.coords.equals(bus.destination)) {
					bus.getDestination();
				}
				if(bus.waiting() != true) {
					if(bus.isBusOnStop()) {
						
						bus.setWait((int) (busWait/0.2) - 1);
					}
					double change = (1.0/bus.currentStreet.getLevel());
					switch(bus.direction){
						case 0:
							//nahoru
							bus.coords.setY(bus.coords.getY()-change);
							circle.setCenterY(bus.coords.getY());
							circle.setCenterX(bus.coords.getX());
							break;
						case 1:
							//dolu
							bus.coords.setY(bus.coords.getY()+change);
							circle.setCenterY(bus.coords.getY());
							circle.setCenterX(bus.coords.getX());
							break;
						case 2:
							//doprava
							bus.coords.setX(bus.coords.getX()+change);
							circle.setCenterY(bus.coords.getY());
							circle.setCenterX(bus.coords.getX());
							break;
						case 3:
							//doleva
							bus.coords.setX(bus.coords.getX()-change);
							circle.setCenterY(bus.coords.getY());
							circle.setCenterX(bus.coords.getX());
							break;
						default:
							break;
					}
					bus.traveled = bus.traveled + change;
				}
			}
		}));
		bus.getDestination();
		oneSecond.setCycleCount(Timeline.INDEFINITE);
		oneSecond.play();
		this.timelines.add(oneSecond);
	}
	
	public void changeMode(Button button) {
		if(button == null) {
			this.selection = 2;
			return;
		}
		if(this.selection == 0) {
			this.selection = 1;
			button.setText("Objizdky");
		}
		else if(this.selection == 1){
			this.selection = 0;
			button.setText("Interakce");
		}
		else {
			this.selection = 0;
			this.modeButton.setText("Interakce");
			this.closingStreet.getLines().forEach((line) -> {
				line.getStrokeDashArray().clear();
			});
			this.closingStreet.setLevel(0);
			this.closingStreet = null;
			unPauseTime();
		}
	}
	
	public int getMode() {
		return this.selection;
	}
	
	public void setClosingStreet(Street street) {
		this.closingStreet = street;
	}
	
	public void pauseTime() {
		this.timelines.forEach((timeline) -> {
			timeline.pause();
		});
	}
	
	public void unPauseTime() {
		this.timelines.forEach((timeline) -> {
			timeline.play();
		});
	}
	
	public void driveAroundAddStreet(Street street) {
		if(this.driveAround.isEmpty()) {
			if(street.follows(this.closingStreet)) {
				if(!street.equals(this.closingStreet)) {
					this.driveAround.add(street);
					//System.out.println("ADDED");
					street.getLines().forEach((line) -> {
						line.setStroke(Color.BLUE);
					});
					
					// v pripade ze objizdka je 1 ulici dlouha - hned zrusit vyber a aplikovat objizdku
					if(street.getCoordinates().get(0).equals(this.closingStreet.getCoordinates().get(0)) || street.getCoordinates().get(0).equals(this.closingStreet.getCoordinates().get(this.closingStreet.getCoordinates().size()-1))) {
						if(street.getCoordinates().get(street.getCoordinates().size()-1).equals(this.closingStreet.getCoordinates().get(0)) || street.getCoordinates().get(street.getCoordinates().size()-1).equals(this.closingStreet.getCoordinates().get(this.closingStreet.getCoordinates().size()-1))) {
							//System.out.println("ADDED LAST");

							this.buses.forEach((bus) -> {
								List<SimpleImmutableEntry<Street, Stop>> newRoute = new ArrayList<SimpleImmutableEntry<Street, Stop>>();
								for(int i = 0; i < bus.Route.size(); i++) {
									if(bus.Route.get(i).getKey().equals(this.closingStreet)) {
										if(!this.closingStreet.getStops().isEmpty()) {
											this.closingStreet.getStops().forEach((stop)->{
												while(bus.Stops.contains(stop)){
													bus.Stops.remove(bus.Stops.indexOf(stop));
												}
											});
										}
										this.driveAround.forEach((driveStreet) -> {
											SimpleImmutableEntry<Street, Stop> entry = new SimpleImmutableEntry<Street, Stop>(driveStreet, null);
											newRoute.add(entry);
										});
									}
									else {
										newRoute.add(bus.Route.get(i));
									}
								}
								bus.Route = newRoute;
								bus.wholeRoad();
								bus.updateLabels();
								this.schedule.update(bus);
							});
							
							this.driveAround.forEach((driveStreet) ->{
								driveStreet.getLines().forEach((line) -> {									
									line.setStroke(this.levels.get(driveStreet.getLevel()-1));
								});
							});
							
							this.selection = 1;
							changeMode(this.modeButton);
							this.closingStreet = null;
							this.driveAround.clear();
							unPauseTime();
						}
					}
				}
			}
		}
		else {
			if(street.follows(this.driveAround.get(this.driveAround.size()-1))) {
				this.driveAround.add(street);
				//System.out.println("ADDED");
				street.getLines().forEach((line) -> {
					line.setStroke(Color.BLUE);
				});
				
				if(street.follows(this.closingStreet)) {
					if(!street.follows(this.driveAround.get(0))) {
						//System.out.println("ADDED LAST");

						this.buses.forEach((bus) -> {
							List<SimpleImmutableEntry<Street, Stop>> newRoute = new ArrayList<SimpleImmutableEntry<Street, Stop>>();
							for(int i = 0; i < bus.Route.size(); i++) {
								if(bus.Route.get(i).getKey().equals(this.closingStreet)) {
									if(!this.closingStreet.getStops().isEmpty()) {
										this.closingStreet.getStops().forEach((stop)->{
											while(bus.Stops.contains(stop)){
												bus.Stops.remove(bus.Stops.indexOf(stop));
											}
										});
									}
									// do nove routy ulice ve spravnem poradi - zjistit v kterych coord vjede bus do ulice, zjistit jestli prvni nebo posledni ulice ma ty koordinaty									
									Street previousStreet = bus.Route.get(i-1).getKey();
									Street currentStreet = bus.Route.get(i).getKey();
									if(previousStreet.getCoordinates().get(0).equals(currentStreet.getCoordinates().get(0)) || previousStreet.getCoordinates().get(previousStreet.getCoordinates().size()-1).equals(currentStreet.getCoordinates().get(0))) {
										//autobus vjede do ulice v koordinatech currentStreet(0)
										Street prvni = this.driveAround.get(0);
										Coordinate vstup = currentStreet.getCoordinates().get(0);
										if(vstup.equals(prvni.getCoordinates().get(0)) || vstup.equals(prvni.getCoordinates().get(prvni.getCoordinates().size()-1))) {
											// autobus nejdriv vjede do prvni ulice v driveAround
											this.driveAround.forEach((driveStreet) -> {
												SimpleImmutableEntry<Street, Stop> entry = new SimpleImmutableEntry<Street, Stop>(driveStreet, null);
												newRoute.add(entry);
											});
										}
										else {
											// autobus nejdriv vjede do posledni ulice v driveAround - potreba reverse driveAround
											Collections.reverse(this.driveAround);
											this.driveAround.forEach((driveStreet) -> {
												SimpleImmutableEntry<Street, Stop> entry = new SimpleImmutableEntry<Street, Stop>(driveStreet, null);
												newRoute.add(entry);
											});
										}
									}
									else {
										//autobus vjede do ulice v koordinatech currentStreet(-1)
										Street prvni = this.driveAround.get(0);
										Coordinate vstup = currentStreet.getCoordinates().get(currentStreet.getCoordinates().size()-1);
										if(vstup.equals(prvni.getCoordinates().get(0)) || vstup.equals(prvni.getCoordinates().get(prvni.getCoordinates().size()-1))) {
											// autobus nejdriv vjede do prvni ulice v driveAround
											this.driveAround.forEach((driveStreet) -> {
												SimpleImmutableEntry<Street, Stop> entry = new SimpleImmutableEntry<Street, Stop>(driveStreet, null);
												newRoute.add(entry);
											});
										}
										else {
											// autobus nejdriv vjede do posledni ulice v driveAround - potreba reverse driveAround
											Collections.reverse(this.driveAround);
											this.driveAround.forEach((driveStreet) -> {
												SimpleImmutableEntry<Street, Stop> entry = new SimpleImmutableEntry<Street, Stop>(driveStreet, null);
												newRoute.add(entry);
											});
										}
									}
								}
								else {
									newRoute.add(bus.Route.get(i));
								}

							}
							bus.Route = newRoute;
							bus.wholeRoad();
							bus.updateLabels();
							this.schedule.update(bus);
						});
						
						this.driveAround.forEach((driveStreet) ->{
							driveStreet.getLines().forEach((line) -> {
								line.setStroke(this.levels.get(driveStreet.getLevel()-1));
							});
						});
						this.selection = 1;
						changeMode(this.modeButton);
						this.closingStreet = null;
						this.driveAround.clear();
						unPauseTime();
					}
				}
			}
		}
	}
	
	public List<Street> getDriveAround(){
		return this.driveAround;
	}
	
	public void reset() {
		this.buses.forEach((bus) -> {
			bus.reset();
		});
		this.map.mapStreets.forEach((street) -> {
			street.setLevel(1);
			street.getLines().forEach((line) -> {
				line.setStroke(Color.BLACK);
				line.getStrokeDashArray().clear();
			});
		});
		this.schedule.clear();
		this.mainClock.reset();
		this.secondaryGroup.getChildren().clear();
	}
	
    /**
     * Funkce která se spustí na začátku programu. 
     * @param primaryStage Hlavní okno pro vykreslení mapy.
     */
	public void start(Stage primaryStage) {
		this.timelines.add(this.mainClock.timer);
		MyMap map = this.map;

        this.levels.add(Color.BLACK);
        this.levels.add(Color.YELLOW);
        this.levels.add(Color.ORANGE);
        this.levels.add(Color.ORANGE);
        this.levels.add(Color.RED);
        this.levels.add(Color.RED);
        this.levels.add(Color.RED);
        this.levels.add(Color.RED);
        
		Circle border0 = new Circle();
		border0.setCenterX(0);
		border0.setCenterY(0);
		border0.setRadius(5);
		border0.setFill(Color.TRANSPARENT);
		border0.setStroke(Color.TRANSPARENT);
		Circle border1 = new Circle();
		border1.setCenterX(0);
		border1.setCenterY(1000);
		border1.setRadius(5);
		border1.setFill(Color.TRANSPARENT);
		border1.setStroke(Color.TRANSPARENT);
		Circle border2 = new Circle();
		border2.setCenterX(1000);
		border2.setCenterY(0);
		border2.setRadius(5);
		border2.setFill(Color.TRANSPARENT);
		border2.setStroke(Color.TRANSPARENT);
		Circle border3 = new Circle();
		border3.setCenterX(1000);
		border3.setCenterY(1000);
		border3.setRadius(5);
		border3.setFill(Color.TRANSPARENT);
		border3.setStroke(Color.TRANSPARENT);

		
		map.ImportData();

		this.drawStreets(map.mapStreets);
		this.drawStops(map.mapStops);
		this.drawLines(map.mapLines);

		Button button1 = new Button("Zrychlit");
		Button button2 = new Button("Zpomalit");
		this.modeButton = new Button("Interakce");
		Button button3 = new Button("Reset");
		Text rychlost = new Text(10, 20, "1x");
		rychlost.setFont(new Font(20));
		rychlost.setX(125);
		rychlost.setY(970);
		button1.setLayoutX(0);
		button1.setLayoutY(950);
		button2.setLayoutX(60);
		button2.setLayoutY(950);
		this.modeButton.setLayoutX(250);
		this.modeButton.setLayoutY(950);
		button3.setLayoutX(325);
		button3.setLayoutY(950);
		
		Label label = new Label("Skok na čas:");
		label.setLayoutX(375);
		label.setLayoutY(955);
		TextField textField = new TextField("HH:mm:ss");
		textField.setLayoutX(450);
		textField.setLayoutY(950);
		Button button4 = new Button("Potvrdit");
		button4.setLayoutX(600);
		button4.setLayoutY(950);
		
		Text clockText = this.mainClock.getClockElement();
		clockText.setX(200);
		clockText.setY(970);
		Group root = new Group(this.mainGroup, border0, border1, border2, border3);
		root.setLayoutX(20);
		root.setLayoutY(20);
		
		Group withButtons = new Group(root, button1, button2, button3, this.modeButton, label, textField, button4, rychlost, clockText, this.secondaryGroup, this.schedule.getGroup());
        Scene scene = new Scene(withButtons, 1000, 1000);
        
	    final DoubleProperty zoomProperty = new SimpleDoubleProperty(1.0);
	    final DoubleProperty sourceX = new SimpleDoubleProperty(0);
	    final DoubleProperty sourceY = new SimpleDoubleProperty(0);
	    
        List<Timeline> timelines = this.timelines;
	    button1.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	timelines.forEach((timeline) -> {
            		timeline.setRate(timeline.getRate()+1);
                	rychlost.setText(Math.round(timeline.getRate())+"x");
            	});
            }
        });
	    
	    button2.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	timelines.forEach((timeline) -> {
            		if(timeline.getRate() != 1) {
            			timeline.setRate(timeline.getRate()-1);
            			rychlost.setText(Math.round(timeline.getRate())+"x");
            		}
            	});
            }
        });
	    
	    Button modeButton = this.modeButton;
	    this.modeButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	changeMode(modeButton);
            }
        });
	    
	    button3.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	reset();
            }
        });
	    
	    MyClock clock = this.mainClock;
	    button4.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
       	     	SimpleDateFormat dt = new SimpleDateFormat("HH:mm:ss");
	       	     try {
	       	    	 Date time = dt.parse(textField.getText());
		       	     double rate = clock.getRate();
		       	     if(clock.getTime() < time.getTime()) {
		       	    	 //SPEED UP, CATCH UP
		       	    	 pauseTime();
		       	    	 timelines.forEach((timeline) -> {
		       	    		 timeline.setRate(1000);
		       	    	 });
		       	    	 clock.setDesiredTime(time.getTime());
		       	    	 clock.setRate(rate);
		       	    	 unPauseTime();
		       	     }
		       	     else {
		       	    	 //RESET, SPEED UP, CATCH UP
		       	    	 pauseTime();
		       	    	 reset();
		       	    	 timelines.forEach((timeline) -> {
		       	    		 timeline.setRate(1000);
		       	    	 });
		       	    	 clock.setDesiredTime(time.getTime());
		       	    	 clock.setRate(rate);
		       	    	 unPauseTime();
		       	     }
	     	     } catch (ParseException e) {
	     			// wrong format
	     			e.printStackTrace();
	     	     }
            }
        });
	    
        primaryStage.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaY() > 0) {
                    zoomProperty.set(zoomProperty.get() * 1.1);
                    root.setScaleX(zoomProperty.get());
                    root.setScaleY(zoomProperty.get());
                } else if (event.getDeltaY() < 0) {
                    zoomProperty.set(zoomProperty.get() / 1.1);
                    root.setScaleX(zoomProperty.get());
                    root.setScaleY(zoomProperty.get());
                }
            }
        });
        primaryStage.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	sourceX.set(event.getX());
            	sourceY.set(event.getY());
            }
        });
        
        primaryStage.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                root.setLayoutX(root.getLayoutX()+event.getX()-sourceX.get());
                root.setLayoutY(root.getLayoutY()+event.getY()-sourceY.get());
                sourceX.set(event.getX());
                sourceY.set(event.getY());
            }
        });
        
        primaryStage.setTitle("Map");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

	public static void main(String[] args) {
        launch(args);
    }
}
