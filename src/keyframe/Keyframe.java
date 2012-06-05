package keyframe;

import java.util.ArrayList;

import brick.BrickObject;
import brick.BrickPanel;

import com.threed.jpct.Camera;
import com.threed.jpct.Matrix;
import com.threed.jpct.SimpleVector;


//Class to handle keyframes of the animation.
public class Keyframe {
	private NonCamera nCam;
	private NonBrick[] nBricks;
	private int framesBefore;
	
	public Keyframe(BrickPanel panel, int framesBefore){
		this.framesBefore = framesBefore;
		ArrayList<BrickObject> bricks = panel.getBricks();
		nCam = new NonCamera(panel.getWorld().getCamera());
		nBricks = new NonBrick[bricks.size()];
		for(int i = 0; i < nBricks.length; i++){
			nBricks[i] = new NonBrick(bricks.get(i), panel);
		}
	}
	
	public NonBrick[] getBricksInScene(){
		return nBricks;
	}
	
	public NonCamera getCameraInfo(){
		return nCam;
	}
	
	public int getFramesBefore(){
		return framesBefore;
	}
	
	//Everything you wanted to know about a brick without actually having a brick. 
	class NonBrick{
		public SimpleVector translation;
		public SimpleVector rotationPivot;
		public Matrix rotation;
		public String name;
		//Color code might be off for selected bricks. Remove this when it's fixed.
		public int colorCode;
		
		public NonBrick(BrickObject obj, BrickPanel panel){
			translation = new SimpleVector(obj.getTranslation());
			rotationPivot = new SimpleVector(obj.getRotationPivot());
			rotation = obj.getRotationMatrix().cloneMatrix();
			colorCode = panel.getTrueColor(obj);
			name = obj.getName();
		}
	}
	
	//Everything needed to restore a camera.
	class NonCamera{
		public SimpleVector position;
		public SimpleVector direction;
		public SimpleVector up;
		
		public NonCamera(Camera cam){
			position = new SimpleVector(cam.getPosition());
			direction = new SimpleVector(cam.getDirection());
			up = new SimpleVector(cam.getUpVector());
		}
	}
	
}
