package ija.ija2019.projekt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class MyClock{
	 private Text txtTime = new Text();
	 public Date time = new Date();
	 private int speed = 1;
	 public Timeline timer;
	 public long desiredTime = -3600001;
	 List<Timeline> timelines;
	 private double oldRate = 1;
	 
	 public MyClock(List<Timeline> timelines){
		 // this is timer thread which will update out time view every second
		 this.timelines = timelines;
	     SimpleDateFormat dt = new SimpleDateFormat("HH:mm:ss");
	     try {
			this.time = dt.parse("00:00:00");
	     } catch (ParseException e) {
			// wrong format
			e.printStackTrace();
	     }
	     Date time = this.time;
	     double rate = this.oldRate;
		 this.timer = new Timeline(new KeyFrame(Duration.seconds(this.speed), new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					time.setTime(time.getTime()+1000);
			        String formatted = dt.format(time);
			        txtTime.setText(formatted);
			        //System.out.println(time.getTime());
			        //System.out.println(desiredTime);
			        if(time.getTime() == getDesiredTime()) {
			        	setDesiredTime(-3600001);
			        	timelines.forEach((timeline) -> {
			        		timeline.setRate(rate);
			        	});
			        }
				}
		 }));
		 this.timer.setCycleCount(Timeline.INDEFINITE);
		 this.timer.play();
	 }
	 
	 public Text getClockElement() {
		 return this.txtTime;
	 }
	 
	 public void reset() {
	     time.setTime(-3600000);
	 }
	 
	 public void setRate(double rate) {
		 this.oldRate = rate;
	 }
	 
	 public double getRate() {
		 return this.timer.getRate();
	 }
	 
	 public long getTime() {
		 return this.time.getTime();
	 }
	 
	 public void setDesiredTime(long time) {
		 this.desiredTime = time;
	 }
	 
	 public long getDesiredTime() {
		 return this.desiredTime;
	 }
}