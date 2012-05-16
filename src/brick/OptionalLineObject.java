package brick;

// TODO: The colors don't look right here.

import com.threed.jpct.*;
import java.awt.Color;

@SuppressWarnings("serial")
public class OptionalLineObject extends BrickObject {
	private SimpleVector p1, p2, control1, control2;
	private ColorBase colors;
	private BrickPanel world;
	
	public OptionalLineObject(Object3D src, BrickPanel world, ColorBase colors, SimpleVector p1, SimpleVector p2, SimpleVector control1, SimpleVector control2) {
		super(src, world, colors, "(optional edge)");
		this.p1 = p1;
		this.p2 = p2;
		this.control1 = control1;
		this.control2 = control2;
		this.world = world;
		this.colors = colors;
	}
	
	public void adjustColor() {
		int code = findColor();
		Color c = showLine() ? colors.getEdgeColor(code) : colors.getColor(code);
		setAdditionalColor(c);
	}
	
	private SimpleVector projectPoint(BrickPanel world, SimpleVector p) {
		Matrix transform = getWorldTransformation();
		return world.project3D2D(Util.matMul(transform, p));
	}
	
	public boolean showLine() {
		SimpleVector project1 = projectPoint(world, p1);
		SimpleVector project2 = projectPoint(world, p2);
		SimpleVector projectC1 = projectPoint(world, control1);
		SimpleVector projectC2 = projectPoint(world, control2);
		float ctrlSide1 = Util.pointSide(project1, project2, projectC1);
		float ctrlSide2 = Util.pointSide(project1, project2, projectC2);
		return (ctrlSide1 < 0 && ctrlSide2 < 0) || (ctrlSide1 > 0 && ctrlSide2 > 0);
	}
}
