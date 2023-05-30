/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package henry.savaryjackson.javatetrisproject.utils;

/**
 *
 * @author hsavaryjackson
 */
public class Tile {
    
    private int x;
    private int y;
    
    public Tile(int x, int y){
	this.x = x;
	this.y = y;
	
    }
    
    @Override
    public int hashCode(){
	int result = x;
        result = 31 * result + y;
        return result;
    }
    
    @Override 
    public boolean equals(Object t){
	
	if (!(t instanceof Tile)){
	    return false;
	}
	Tile asTile = (Tile)t;
	
	return asTile.getX() == this.x && asTile.getY()== this.y;
    }

    /**
     * @return the x
     */
    public int getX() {
	return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
	this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
	return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
	this.y = y;
    }
    
}
