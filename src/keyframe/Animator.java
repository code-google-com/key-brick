package keyframe;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.JOptionPane;

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
	private BrickPanel panel;
	private World world;
	
	public Animator(BrickPanel panel){
		this.panel = panel;
		world = panel.getWorld();
	}
	
	
	public void moveBetweenFrames(Keyframe frame1, Keyframe frame2){
		moveBetweenFrames(frame1, frame2, 31);
	}
	
	//Assuming no pivot changes. That is, the pivot is only used for restoring.
	//Camera changes need to be added as well.
	//This method also cannot handle new bricks or removed bricks.
	//To remove a brick, translate it by (-1000,-1000,-1000).
	//This distance from the camera makes it not rendered.
	//Also, here are some defaults for your information:
	//		framerate: 31 frames per second
	//		seconds between two keyframes: 2
	//		frames between two keyframes: 31 fps * 2 sec = 62 frames
	//		*Technically, this counts the first keyframe as the first, and the
	//		 second keyframe as the 63rd frame (first of the next animation).
	public void moveBetweenFrames(Keyframe frame1, Keyframe frame2, int framerate){
		
		ArrayList<BrickObject> bricks = panel.getBricks();
		NonBrick[] startBricks = frame1.getBricksInScene();
		NonBrick[] endBricks = frame2.getBricksInScene();

		int steps = 62;
		float seconds = 2;
		
		if(frame2.getFramesBefore() != 0){
			steps = frame2.getFramesBefore();
			seconds = (float)steps/(float)framerate;
		}
		System.out.println("Will take " + seconds + " second.");
		
		int framelength = 1000/framerate;
		
		//Step is 1 here and not 0.
		//This is because frame 0 is already being shown.
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
			//System.out.println("step");
			//this attempts to keep a uniform frame length
			while(System.currentTimeMillis() - startTime < framelength);
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
	
	public void restoreFromFrame(Keyframe frame){
		
		panel.removeAllObjects();
		NonBrick[] scene = frame.getBricksInScene();
		try {
			BrickObject temp;
			for(NonBrick brick : scene){
				temp = panel.addNewBrick(brick.name);
				temp.build();
				temp.translate(brick.translation);
				System.out.println("Test: " + brick.rotationPivot);
				temp.setRotationPivot(new SimpleVector(brick.rotationPivot));
				temp.getRotationMatrix().matMul(brick.rotation);
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
	
	public static boolean saveMovieToFile(ArrayList<Keyframe> frames){
		String name = JOptionPane.showInputDialog("Name of movie: ");
		name = name.split("\\.", 2)[0];
		
		
		
		File f = new File(name.endsWith(".kf") ? name : name + ".kf");
		try {
			BufferedWriter bf = new BufferedWriter(new FileWriter(f));
			bf.write("MOVIE");
			//for(Keyframe kf : )
			bf.close();
			return true;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Save failed.");
			return false;
		}
		
	}
	
	public void zipEm(){
		
		
		File directory = new File("");
		 
		File files[] = directory.listFiles();
		 
		try {
			BufferedInputStream origin = null;
			FileOutputStream dest = new FileOutputStream("");
			ZipOutputStream zip = new ZipOutputStream(new BufferedOutputStream(dest));
			int BUFFER = 20480;
			byte data[] = new byte[BUFFER];
			
			File f = null;
			ZipEntry entry = null;
			FileInputStream fi = null;
			int count;
			
			// Iterate over the file list and add them to the zip file.
			for (int i=0; i <files.length; i++) {
				fi = new FileInputStream(files[i]);
				origin = new BufferedInputStream(fi, BUFFER);
				entry = new ZipEntry(files[i].getName());
				zip.putNextEntry(entry);
				
				while((count = origin.read(data,0,BUFFER)) != -1){
					zip.write(data,0,count);
				}
				count = 0;
				origin.close();
			}
			zip.close();
		}
		catch (Exception e){
		}
	}
}
