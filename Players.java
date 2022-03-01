/*
*/

import java.io.*;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Players extends BasketballA {
	String name;
	String description;
	double cost;
	int points;
	
	//constructors
	public Players() {
		
	}
	
	public Players(String n, String d, double c, int p){
		name = n;
		description = d;
		cost = c;
		points = p;
	}
	//getters and setters
	public String getName(){
		return name;
	}
	public void setName(String n){
		name = n;
	}
	public String getDescription(){
		return description;
	}
	public void setDescription(String d){
		description = d;
	}
	public double getCost(){
		return cost;
	}
	public void setCost(double c){
		cost = c;
	}
	public int getPoints(){
		return points;
	}
	public void setPoints(int p){
		points = p;
	}
}