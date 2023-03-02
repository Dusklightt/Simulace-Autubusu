package ija.ija2019.projekt.maps;

import javafx.scene.Group;
import javafx.scene.control.Label;


public class Schedule {
	Group group;
	Group dataGroup;
	
	public Schedule() {
		this.dataGroup = new Group();
		this.group = new Group();
		this.group.getChildren().add(dataGroup);
		
		javafx.scene.shape.Line line1 = new javafx.scene.shape.Line();
		javafx.scene.shape.Line line2 = new javafx.scene.shape.Line();
		
		line1.setStartX(800);
		line1.setStartY(100);
		line1.setEndX(800);
		line1.setEndY(500);
		
		line2.setStartX(700);
		line2.setStartY(150);
		line2.setEndX(900);
		line2.setEndY(150);
		
		Label label1 = new Label("Zastávky");
		Label label2 = new Label("Čas");
		label1.setLayoutX(725);
		label1.setLayoutY(125);
		label2.setLayoutX(825);
		label2.setLayoutY(125);
		
		this.group.getChildren().add(label1);
		this.group.getChildren().add(label2);
		
		this.group.getChildren().add(line1);
		this.group.getChildren().add(line2);
		
	}
	
	public Group getGroup() {
		return this.group;
	}
	
	public void update(Bus bus) {
		this.dataGroup.getChildren().clear();
		for(int i = 0; i < bus.labels.size(); i++) {
			Label label1 = new Label(bus.Stops.get(i).getId());
			Label label2 = new Label(bus.labels.get(i).getText());
			label1.setLayoutX(725);
			label1.setLayoutY(125+32*(i+1));
			label2.setLayoutX(825);
			label2.setLayoutY(125+32*(i+1));
			
			this.dataGroup.getChildren().add(label1);
			this.dataGroup.getChildren().add(label2);
			
		}
		
	}
	
	public void clear() {
		this.dataGroup.getChildren().clear();
	}
	
}
