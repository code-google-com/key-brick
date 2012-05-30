package brick;

import java.awt.Color;
import java.awt.Graphics;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JPanel;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.IRenderer;
import com.threed.jpct.Interact2D;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

@SuppressWarnings("serial")
public class BrickPanel extends JPanel {
	private World world;
	private FrameBuffer buffer;
	private ArrayList<BrickObject> bricks;
	private ArrayList<BrickColorPair> selected;
	protected AdjustmentPane ap;
	public final int selectColor = 7;
	private SimpleVector selectionPivot;
	
	
	public BrickPanel() {
		this(800, 600);
	}
	
	public BrickPanel(int width, int height) {
		super();
		world = new World();
		world.setAmbientLight(0, 0, 0);
		buffer = new FrameBuffer(width, height, FrameBuffer.SAMPLINGMODE_NORMAL);
		bricks = new ArrayList<BrickObject>();
		selected = new ArrayList<BrickColorPair>();
		ap = new AdjustmentPane(world, this);
	}
	

	public void setupDefaultListeners(){
		MouseRotateController mrc = new MouseRotateController(this, world);
		
		addMouseListener(new MouseSelectController(this, world, buffer, 10000));
		addMouseListener(mrc);
		addMouseMotionListener(mrc);
		
	}

	public void addNewBrick(String fileLoc) throws PartNotFoundException, FileNotFoundException {
		PartFactory fact = new PartFactory(BrickViewer.ldrawPath);
		PartSpec model = fact.getPart(fileLoc);
		BrickObject obj = model.toBrickObject(new Matrix(), this);
		addBrick(obj);
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
	
	public boolean isTopBrick(Object3D o){
		return o instanceof BrickObject ? (bricks.contains(o) && !((BrickObject)o).isGrouped()) : false;
	}
	
	public int indexOf(BrickObject brick){
		return bricks.indexOf(brick);
	}
	
	//Add a completed BrickObject to the ArrayList.
	//Provides for easier tracking of full objects.
	public void addBrick(BrickObject brick){
		bricks.add(brick);
	}
	
	public int numBricks(){
		return bricks.size();
	}
	
	public void removeAllObjects(){
		world.removeAllObjects();
		selected.clear();
		bricks.clear();
	}
	
	public SimpleVector getSelectionPivot(){
		return selectionPivot;
	}
	
	//Change it from some number of selected bricks to only one.
	//Also edits colors here now.
	public void setSelectedBrick(BrickObject b){
		//Need first brick: Selection hierarchy makes this one the "root" of what's chosen.
		if(!selected.isEmpty()){
			for(BrickColorPair pair : selected){
				pair.getBrick().setColorCode(selected.get(selected.indexOf(pair)).getColorCode());
			}
			selected.clear();
		}
		selected.add(new BrickColorPair(b));
		selectionPivot = Util.findCenter(selected);
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

				//System.out.println("Before Adding: " + b.getTranslation());
				//System.out.println("Before Adding: " + b.getCenter());
				//BrickObject base = selected.get(0).getBrick();
				//SimpleVector trans = base.getTranslation();
				//Matrix origRot = b.getRotationMatrix();
				//SimpleVector origCenter = b.getCenter();
				//base.addChild(b);
				//b.setRotationMatrix(base.getRotationMatrix().invert());
				//SimpleVector newCenter = b.getCenter();
				//System.out.println("Orig Center: " + origCenter);
				//System.out.println("New Center: " + newCenter);
				//b.getCenter();
				//b.set(base.getInverseWorldTransformation());
				//System.out.println("After adding: " + b.getTranslation());
				//System.out.println("After adding: " + b.getCenter());
				//base.translate(trans);
			}
		} else {
			//Then remove.
			System.out.println("Contains");
			//If this is the only selected object, it's not its own parent.
//			if(selected.size() > 1){
//				BrickObject base = selected.get(0).getBrick();
//				if(base.equals(pair.getBrick())){
//					//If we're trying to deselect the base brick.
//					BrickObject newBase = selected.get(1).getBrick();
//					base.removeChild(newBase);
//					for(int i = 2; i < selected.size(); i++){
//						//base.removeChild(selected.get(i).getBrick());
//						//newBase.addChild(selected.get(i).getBrick());
//					}
//					//newBase.setTranslationMatrix(base.getTranslationMatrix());
//					newBase.translate(base.getTranslation());
//					
//				} else {
//					//base.removeChild(pair.getBrick());
//					//b.setRotationMatrix(base.getRotationMatrix().cloneMatrix());
//					pair.getBrick().translate(base.getTranslation());
//				}
//			}
			//This is a weird line. We're basically just wanting the original color and this gets it
			pair.getBrick().setColorCode(selected.get(selected.indexOf(pair)).getColorCode());
			selected.remove(pair);
		}
		selectionPivot = Util.findCenter(selected);
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
		ap.update();
		//This reeeeally makes Java unhappy.
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
