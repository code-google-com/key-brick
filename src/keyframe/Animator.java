package keyframe;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import keyframe.Keyframe.NonBrick;
import keyframe.Keyframe.NonCamera;
import brick.BrickObject;
import brick.BrickPanel;
import brick.PartNotFoundException;

import com.threed.jpct.Camera;
import com.threed.jpct.Matrix;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

public class Animator {
	
	public static int steps = 60;
	public static int seconds = 1;
	
	//Assuming no pivot changes. That is, the pivot is only used for restoring. 
	public static void moveBetweenFrames(BrickPanel panel, World world,
										 Keyframe frame1, Keyframe frame2){
		ArrayList<BrickObject> bricks = panel.getBricks();
		NonBrick[] startBricks = frame1.getBricksInScene();
		NonBrick[] endBricks = frame2.getBricksInScene();
		
		float step = 1;
		//float timeStep = (float)seconds / (float)steps;
		//float timePassed = 0;
		
		while(step < steps){
			long startTime = System.currentTimeMillis();
			NonBrick start;
			NonBrick end;
			BrickObject brick;
			float progress = (float)step/(float)steps;
			for(int i = 0; i < startBricks.length; i++){
				brick = bricks.get(i);
				start = startBricks[i];
				end = endBricks[i];
				if(!start.rotation.equals(end.rotation)){
					System.out.println("Updating rotation");
					Matrix inBetween = new Matrix(); 
					inBetween.interpolate(start.rotation, end.rotation, progress);
					brick.setRotationMatrix(inBetween.cloneMatrix());
				}
				if(!start.translation.equals(end.translation)){
					SimpleVector change = new SimpleVector(end.translation);
					change.sub(start.translation);
					change.scalarMul(1f/(float)steps);
					System.out.println("cha: " + change);
					System.out.print(brick.getTranslation() + " => ");
					brick.translate(change);
					System.out.println(brick.getTranslation());
				}
			}
			step++;
			System.out.println("step");
			while(System.currentTimeMillis() - startTime < 50){System.out.print(".");}
			panel.repaint();
		}
		for(int i = 0; i < startBricks.length; i++){
			NonBrick end;
			BrickObject brick;
			brick = bricks.get(i);
			end = endBricks[i];
			SimpleVector t = brick.getTranslation();
			t.scalarMul(-1);
			brick.translate(t);
			brick.translate(end.translation);
			brick.setRotationMatrix(end.rotation.cloneMatrix());
		}
		
		
		if(endBricks.length > startBricks.length){
			try {
				BrickObject temp;
				NonBrick add;
				for(int i = startBricks.length ; i < endBricks.length; i++){
					add = endBricks[i];
					temp = panel.addNewBrick(add.name);
					temp.translate(add.translation);
					temp.setRotationPivot(new SimpleVector(add.rotationPivot));
					temp.setRotationMatrix(add.rotation.cloneMatrix());
					temp.setColorCode(add.colorCode);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (PartNotFoundException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public static void restoreFromFrame(BrickPanel panel,
										Keyframe frame){
		
		World world = panel.getWorld();
		panel.removeAllObjects();
		NonBrick[] scene = frame.getBricksInScene();
		try {
			BrickObject temp;
			for(NonBrick brick : scene){
				temp = panel.addNewBrick(brick.name);
				temp.translate(brick.translation);
				temp.setRotationPivot(new SimpleVector(brick.rotationPivot));
				temp.setRotationMatrix(brick.rotation.cloneMatrix());
				temp.setColorCode(brick.colorCode);
			}
			
			NonCamera nc = frame.getCameraInfo();
			Camera cam = new Camera();
			cam.setPosition(nc.position);
			SimpleVector lookAt = new SimpleVector(nc.position);
			lookAt.add(nc.direction);
			lookAt.normalize();
			cam.setOrientation(nc.direction, nc.up);
			world.setCameraTo(cam);
			
			panel.repaint();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (PartNotFoundException e) {
			e.printStackTrace();
		}
	}
}
