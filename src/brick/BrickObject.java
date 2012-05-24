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
	private boolean grouped = false;
	
	private void init(BrickPanel world, ColorBase colors, String name) {
		world.addObject(this);
		this.colors = colors;
		this.name = name;
		setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
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
	
	private void setGrouped(boolean b){
		grouped = b;
	}
	
	public void addChild(BrickObject child){
		super.addChild((Object3D)child);
		if(!this.equals(child)) child.setGrouped(true);
	}
	public void removeChild(BrickObject child){
		super.removeChild((Object3D)child);
		if(child.getParents().length == 0) child.setGrouped(false);
	}
	
	public void addParent(BrickObject parent){
		super.addParent((Object3D)parent);
		if(!this.equals(parent)) setGrouped(true);
	}
	public void removeParent(BrickObject parent){
		super.removeParent((Object3D)parent);
		if(getParents().length == 0) setGrouped(false);
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
	
	
	//Basic translation stuff for now, ignoring any sort of relative positions.
	public void translateX(float f){
		translate(new SimpleVector(f, 0, 0));
	}
	
	public void translateY(float f){
		translate(new SimpleVector(0, f, 0));
	}
	
	public void translateZ(float f){
		translate(new SimpleVector(0, 0, f));
	}
}
