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
	protected AdjustmentPane ap;
	
	public BrickPanel() {
		super();
		world = new World();
		world.setAmbientLight(0, 0, 0);
		buffer = new FrameBuffer(800, 600, FrameBuffer.SAMPLINGMODE_NORMAL);
		objs = new ArrayList<BrickObject>();
		ap = new AdjustmentPane(world, this);
	}
	
	public void addObject(BrickObject obj) {
		System.out.println("Called");
		world.addObject(obj);
		objs.add(obj);
		obj.setCulling(Object3D.CULLING_DISABLED);
	}
	
	public int numObjs(){
		return objs.size();
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
		world.getCamera().setPosition(0, -50, 0);
		//cam.
		world.getCamera().lookAt(new SimpleVector(0, 1, 0));//getMostRecent().getTransformedCenter());
		
	}
	
	public SimpleVector project3D2D(SimpleVector point3D) {
		return Interact2D.project3D2D(world.getCamera(), buffer, point3D);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		System.out.println(objs.size());
		for (BrickObject obj: objs) {
			obj.adjustColor();
		}
		buffer.clear(Color.BLUE);
		world.renderScene(buffer);
		world.draw(buffer);
		buffer.update();
		buffer.display(g);
		ap.update();
	}
	
	public void dispose() {
		buffer.disableRenderer(IRenderer.RENDERER_OPENGL);
		buffer.dispose();
	}
}
