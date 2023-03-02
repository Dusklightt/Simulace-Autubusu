/*	
 *  Třída Coordinate reprezentuje souřadnice x,y.
 * 	Autor: Roman Štafl xstafl01 <xstafl01@stud.fit.vutbr.cz>
 *  Autor: Michal Vašut xvasut02 <xvasut02@stud.fit.vutbr.cz>
 */	
package ija.ija2019.projekt.maps;

public class Coordinate{
	double x;
	double y;
	public Coordinate(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public static Coordinate create(double x, double y) {
		if (x < 0 || y < 0) {
			return null;
		}
		else
		{
			Coordinate novy = new Coordinate(x, y);
			return novy;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Coordinate coord = (Coordinate) obj;
		if(this.x == coord.getX() && this.y == coord.getY()) {
			return true;
		}
		return false;
	}
	
	public double getX() {
		return this.x;
	}
	public double getY() {
		return this.y;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double d) {
		this.y = d;
	}
}
