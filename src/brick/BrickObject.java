package brick;

import com.threed.jpct.*;
import java.awt.Color;

@SuppressWarnings("serial")
public class BrickObject extends Object3D {
	private static final boolean debug = false;
	
	public static final int MAIN_COLOR = 16;
	public static final int EDGE_COLOR = 24;
	
	private int colorCode;
	private ColorBase colors;
	private String name;
	
	private void init(BrickPanel world, ColorBase colors, String name) {
		world.addObject(this);
		this.colors = colors;
		this.name = name;
	}
	
	public BrickObject(int triangles, BrickPanel world, ColorBase colors, String name) {
		super(triangles);
		init(world, colors, name);
	}
	
	public BrickObject(Object3D src, BrickPanel world, ColorBase colors, String name) {
		super(src);
		init(world, colors, name);
	}
	
	public String getName() {return name;}
	
	public void setColorCode(int color) {
		colorCode = color;
	}
	
	public int findColor() {
		//if (debug) {System.out.println("findColor() for " + getName());}
		if (colorCode != MAIN_COLOR && colorCode != EDGE_COLOR) {
			if (debug) {System.out.println(" Returning " + colorCode);}
			return colorCode;
		} else {
			return ((BrickObject)getParents()[0]).findColor();
		}
	}
	
	public void adjustColor() {
		Color c = colorCode == EDGE_COLOR ? colors.getEdgeColor(findColor()) : colors.getColor(findColor());
		setAdditionalColor(c);
	}
}
