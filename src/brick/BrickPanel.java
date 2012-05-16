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
	
	public BrickPanel() {
		super();
		world = new World();
		world.setAmbientLight(0, 0, 0);
		buffer = new FrameBuffer(800, 600, FrameBuffer.SAMPLINGMODE_NORMAL);
		objs = new ArrayList<BrickObject>();
	}
	
	public void addObject(BrickObject obj) {
		world.addObject(obj);
		objs.add(obj);
		obj.setCulling(Object3D.CULLING_DISABLED);
	}
	
	public BrickObject getMostRecent() {
		return objs.get(objs.size() - 1);
	}
	
	public void buildAll() {
		world.buildAllObjects();
		
		world.getCamera().setPosition(0, -50, 0);
		world.getCamera().lookAt(getMostRecent().getTransformedCenter());
	}
	
	public SimpleVector project3D2D(SimpleVector point3D) {
		return Interact2D.project3D2D(world.getCamera(), buffer, point3D);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		for (BrickObject obj: objs) {
			obj.adjustColor();
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
