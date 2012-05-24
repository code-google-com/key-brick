package brick;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import com.threed.jpct.*;

@SuppressWarnings("serial")
public class BrickPanel extends JPanel {
	private World world;
	private FrameBuffer buffer;
	private ArrayList<BrickObject> bricks;
	private ArrayList<BrickColorPair> selected;
	protected AdjustmentPane ap;
	public final int selectColor = 7;
	
	public BrickPanel() {
		super();
		world = new World();
		world.setAmbientLight(0, 0, 0);
		buffer = new FrameBuffer(800, 600, FrameBuffer.SAMPLINGMODE_NORMAL);
		bricks = new ArrayList<BrickObject>();
		selected = new ArrayList<BrickColorPair>();
		ap = new AdjustmentPane(world, this);
		addMouseListener(new BrickMouseController(this, world, buffer, 10000));
		
	}
	
	//Add an Object3D to the world (component/child of a full BrickObject).
	//Also needs to be called on the BrickObjects themselves.
	public void addObject(BrickObject obj) {
		world.addObject(obj);
		obj.setCulling(Object3D.CULLING_DISABLED);
	}
	
	public boolean isBrick(Object3D o){
		return o instanceof BrickObject ? bricks.contains(o) : false;
	}
	
	public int indexOf(BrickObject brick){
		return bricks.indexOf(brick);
	}
	
	//Add a BrickObject to the ArrayList.
	//Provides for easier tracking of full objects.
	public void addBrick(BrickObject brick){
		bricks.add(brick);
	}
	
	public int numBricks(){
		return bricks.size();
	}
	
	//Change it from some number of selected bricks to only one.
	//Also edits colors here now.
	public void setSelectedBrick(BrickObject b){
		//Need first brick: Selection hierarchy makes this one the "root" of what's chosen.
		if(!selected.isEmpty()){
			BrickObject first = selected.get(0).getBrick();
			for(BrickColorPair pair : selected){
				if(!pair.getBrick().equals(first)){
					first.removeChild(pair.getBrick());
				}
				pair.getBrick().setColorCode(pair.getColorCode());
			}
			selected.clear();
		}
		selected.add(new BrickColorPair(b));
		b.setColorCode(selectColor);
		repaint();
		//ap.updateSelectedObject(b);
	}
	
	//Set from the information pane. 
	public void setSelectedBrick(int i){
		setSelectedBrick(bricks.get(i));
	}
	
	//Add a new brick to the selection, or remove it from the selection if already selected.
	public void addOrRemoveSelectedBrick(BrickObject b){
		BrickColorPair pair = new BrickColorPair(b);
		if(!selected.contains(pair)){
			//Then add.
			System.out.println("Does not contain");
			selected.add(pair);
			pair.getBrick().setColorCode(selectColor);
			//"If we're not about to try and set a brick to be its own parent"
			if(selected.size() > 1){
				BrickObject base = selected.get(0).getBrick();
				SimpleVector trans = base.getTranslation();
				base.translate(-trans.x, -trans.y, -trans.z);
				base.addChild(pair.getBrick());
				base.translate(trans);
			}
		} else {
			//Then remove.
			System.out.println("Contains");
			//If this is the only selected object, it's not its own parent.
			if(selected.size() > 1){
				BrickObject base = selected.get(0).getBrick();
				if(base.equals(pair.getBrick())){
					//If we're trying to deselect the base brick.
					BrickObject newBase = selected.get(1).getBrick();
					base.removeChild(newBase);
					for(int i = 2; i < selected.size(); i++){
						base.removeChild(selected.get(i).getBrick());
						newBase.addChild(selected.get(i).getBrick());
					}
					
				} else {
					base.removeChild(pair.getBrick());
				}
			}
			//This is a weird line. We're basically just wanting the original color and this gets it
			pair.getBrick().setColorCode(selected.get(selected.indexOf(pair)).getColorCode());
			selected.remove(pair);
		}
	}
	
	public ArrayList<BrickColorPair> getSelectedBricks(){
		return selected;
	}
	
	public BrickObject getMostRecent() {
		return bricks.get(bricks.size() - 1);
	}
	
	public BrickObject getObject(int index){
		return bricks.get(index >= bricks.size() ? bricks.size() - 1 : index);
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
