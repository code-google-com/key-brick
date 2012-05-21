package brick;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import com.threed.jpct.*;

@SuppressWarnings("serial")
public class BrickPanel extends JPanel {
	private World world;
	private FrameBuffer buffer;
	private ArrayList<BrickObject> objs;
	private ArrayList<BrickObject> selected;
	protected AdjustmentPane ap;
	
	public BrickPanel() {
		super();
		world = new World();
		world.setAmbientLight(0, 0, 0);
		buffer = new FrameBuffer(800, 600, FrameBuffer.SAMPLINGMODE_NORMAL);
		objs = new ArrayList<BrickObject>();
		selected = new ArrayList<BrickObject>();
		ap = new AdjustmentPane(world, this);
		addMouseListener(new BrickMouseController(this, world, buffer, 10000));
		
	}
	
	public void addObject(BrickObject obj) {
		world.addObject(obj);
		obj.setCulling(Object3D.CULLING_DISABLED);
	}
	
	public int indexOf(BrickObject brick){
		return objs.indexOf(brick);
	}
	
	public void addBrick(BrickObject brick){
		objs.add(brick);
	}
	
	public int numObjs(){
		return objs.size();
	}
	
	public void setSelectedBrick(BrickObject b){
		selected.clear();
		selected.add(b);
		ap.updateSelectedObject(b);
	}
	
	public void addSelectedBrick(BrickObject b){
		if(!selected.contains(b)){
			selected.add(b);
		}
	}
	
	public ArrayList<BrickObject> getSelectedBricks(){
		return selected;
	}
	
	public BrickObject getMostRecent() {
		return objs.get(objs.size() - 1);
	}
	
	public BrickObject getObject(int index){
		return objs.get(index >= objs.size() ? objs.size() - 1 : index);
	}
	
	public void buildAll() {
		world.buildAllObjects();
		//Camera cam = world.getCamera();
//		world.getCamera().setPosition(0, -50, 0);
//		world.getCamera().lookAt(new SimpleVector(0, 1, 0));//getMostRecent().getTransformedCenter());
		world.getCamera().setPosition(-100.0f, 0, 0);
		world.getCamera().lookAt(new SimpleVector(1f, 0, 0));//getMostRecent().getTransformedCenter());
		
	}
	
	public SimpleVector project3D2D(SimpleVector point3D) {
		return Interact2D.project3D2D(world.getCamera(), buffer, point3D);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void paintComponent(Graphics g) {
		//This reeeeally makes Java unhappy.
		ap.update();
		for (Enumeration<BrickObject> bricks = world.getObjects(); bricks.hasMoreElements();) {
			bricks.nextElement().adjustColor();
		}
		buffer.clear(Color.BLUE);
		world.renderScene(buffer);
		world.draw(buffer);
		buffer.update();
		buffer.display(g);
	}
	
	public void dispose() {
		buffer.disableRenderer(IRenderer.RENDERER_OPENGL);
		buffer.dispose();
	}
}
