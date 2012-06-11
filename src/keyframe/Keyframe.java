package keyframe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
	
	public Keyframe(File f) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = br.readLine();
		if(line == null) throw new Exception("Empty file.");
		if(!line.equals("SETTINGS")){
			throw new Exception("Not a keyframe file.");
		}
		framesBefore = Integer.parseInt(br.readLine());
		
		line = br.readLine();
		if(!line.equals("CAMERA")){
			throw new Exception("No camera information in file.");
		}else{
			String reg = "\\,";
			String[] vals = br.readLine().substring(1).split(reg);
			SimpleVector position = new SimpleVector(Float.parseFloat(vals[0]), Float.parseFloat(vals[1]), Float.parseFloat(vals[2].substring(0, vals[2].length() - 1)));
			vals = br.readLine().substring(1).split(reg);
			SimpleVector direction = new SimpleVector(Float.parseFloat(vals[0]), Float.parseFloat(vals[1]), Float.parseFloat(vals[2].substring(0, vals[2].length() - 1)));
			vals = br.readLine().substring(1).split(reg);
			SimpleVector up = new SimpleVector(Float.parseFloat(vals[0]), Float.parseFloat(vals[1]), Float.parseFloat(vals[2].substring(0, vals[2].length() - 1)));
			nCam = new NonCamera(position, direction, up);
			//nCam = new NonCamera(a, b, c);//new SimpleVector(scan.nextFloat(), scan.nextFloat(), scan.nextFloat()),
					         	 //new SimpleVector(scan.nextFloat(), scan.nextFloat(), scan.nextFloat()),
					         	 //new SimpleVector(scan.nextFloat(), scan.nextFloat(), scan.nextFloat()));
		}
		
		ArrayList<NonBrick> bricks = new ArrayList<Keyframe.NonBrick>();
		
		line = br.readLine();
		while(line.equals("BRICK")){
			String name = br.readLine();
			int colorCode = Integer.parseInt(br.readLine());
			String reg = "\\,";
			String[] vals = br.readLine().substring(1).split(reg);
			SimpleVector trans =  new SimpleVector(Float.parseFloat(vals[0]), Float.parseFloat(vals[1]), Float.parseFloat(vals[2].substring(0, vals[2].length() - 1)));
			vals = br.readLine().substring(1).split(reg);
			SimpleVector rPivot =  new SimpleVector(Float.parseFloat(vals[0]), Float.parseFloat(vals[1]), Float.parseFloat(vals[2].substring(0, vals[2].length() - 1)));
			br.readLine();//Open parenthesis.
			
			//I don't like this. At all.
			//Also, dump is totally the official word for this.
			Matrix rot = new Matrix();
			vals = (br.readLine() + br.readLine() + br.readLine() + br.readLine()).trim().split("\\s");
			
			float[] dump = new float[vals.length];
			for(int i = 0; i < 16; i++)
				dump[i] = Float.parseFloat(vals[i]);
			
			rot.fillDump(dump);
			bricks.add(new NonBrick(name, colorCode, trans, rPivot, rot));
			br.readLine();//Close parenthesis
			line = br.readLine();
		}
		
		//Because toArray() is dumb...
		nBricks = new NonBrick[bricks.size()];
		for(int b = 0; b < nBricks.length; b++){
			nBricks[b] = bricks.get(b);
		}
		
		
//		
//		this.framesBefore = framesBefore;
//		ArrayList<BrickObject> bricks = panel.getBricks();
//		nCam = new NonCamera(panel.getWorld().getCamera());
//		nBricks = new NonBrick[bricks.size()];
//		for(int i = 0; i < nBricks.length; i++){
//			nBricks[i] = new NonBrick(bricks.get(i), panel);
//		}
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
		
		public NonBrick(String name, int colorCode, SimpleVector translation,
						SimpleVector rotationPivot, Matrix rotation){
			this.translation = translation;
			this.rotationPivot = rotationPivot;
			this.rotation = rotation;
			this.colorCode = colorCode;
			this.name = name;
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
		
		public NonCamera(SimpleVector position, SimpleVector direction, SimpleVector up){
			this.position = position;
			this.direction = direction;
			this.up = up;
		}
	}
	
	public String toString(){
		StringBuilder output = new StringBuilder();
		output.append("SETTINGS\n");
		output.append(framesBefore + "\n");
		output.append("CAMERA\n");
		output.append(getCameraInfo().position + "\n");
		output.append(getCameraInfo().direction + "\n");
		output.append(getCameraInfo().up + "\n");
		for(NonBrick brick : getBricksInScene()){
			output.append("BRICK\n");
			output.append(brick.name + "\n");
			output.append(brick.colorCode + "\n");
			output.append(brick.translation + "\n");
			output.append(brick.rotationPivot + "\n");
			output.append(brick.rotation);
		}
		output.append("END");
		return output.toString();
	}
	
	public boolean saveToFile(String filename){
		File f = new File(filename.endsWith(".kf") ? filename : filename + ".kf");
		try {
			BufferedWriter bf = new BufferedWriter(new FileWriter(f));
			bf.write(toString());
			bf.close();
			return true;
		} catch (IOException e) {
			System.err.println("File failed to save.");
			return false;
		}
		
	}
	
}
