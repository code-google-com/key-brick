package brick;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;

public class BrickKeyController extends KeyAdapter {
	private float angle;
	private BrickPanel renderingArea;
	
	public BrickKeyController(float angle, BrickPanel renderingArea) {
		this.angle = angle;
		this.renderingArea = renderingArea;
	}
	
	@Override
	//THIS IS BROKEN.
	//I coded it broken to get the framework in.
	//This should move bricks individually, yet at the same time.
	//Need to find unified center and make movements based on that.
	public void keyPressed(KeyEvent ke) {
		ArrayList<BrickColorPair> targets = renderingArea.getSelectedBricks();
		float mult = 1.0f;
		if(ke.isControlDown()) mult = 10.0f;
		else if(ke.isShiftDown()) mult = 0.1f;
		
		if (ke.getKeyCode() == KeyEvent.VK_U) {
			//Object3D obj = new Object3D(0);
			SimpleVector[] centers = new SimpleVector[targets.size()];
			//Matrix[] rots = new Matrix[targets.size()];
			int place = 0;

			System.out.println("Centers:");
			for(BrickColorPair pair : targets){
				//rots[place] = pair.getBrick().getRotationMatrix();
				centers[place] = pair.getBrick().getTranslation();
				System.out.println("\t" + place + ": " + centers[place]);
				place++;
			}
			SimpleVector center = getCentralPoint(centers);
			System.out.println("\tC: " + center);
			Object3D dummy = Object3D.createDummyObj();
			dummy.translate(center);
			dummy.rotateX(-angle);
			//Matrix rot = dummy.getRotationMatrix();
			//System.out.println(rot);
			place = 0;
			
			//dummy.rotateX(-angle);
			BrickObject main = targets.get(0).getBrick();
			main.rotateX(-angle);
//			for(int i = 1; i < targets.size(); i++){
//				//Matrix previous = pair.getBrick().getRotationMatrix();
//				pair.getBrick().setRotationPivot(new SimpleVector(0, 24, -10));
//				pair.getBrick().rotateX(-angle);
//				//System.out.println(previous);
//				//pair.getBrick().setRotationMatrix(rot);
//				System.out.println("\tP: " + pair.getBrick().getRotationPivot());
//				//pair.getBrick().rotateX(-angle);
//				//pair.getBrick().setRotationMatrix(previous);
//				place++;
//			}
			
		} else if (ke.getKeyCode() == KeyEvent.VK_I) {
			for(BrickColorPair pair : targets)
				pair.getBrick().rotateX(angle);
		} else if (ke.getKeyCode() == KeyEvent.VK_J) {
			for(BrickColorPair pair : targets)
				pair.getBrick().rotateY(-angle);
		} else if (ke.getKeyCode() == KeyEvent.VK_K) {
			for(BrickColorPair pair : targets)
				pair.getBrick().rotateY(angle);
		} else if (ke.getKeyCode() == KeyEvent.VK_M) {
			for(BrickColorPair pair : targets)
				pair.getBrick().rotateZ(-angle);
		} else if (ke.getKeyCode() == KeyEvent.VK_COMMA) {
			for(BrickColorPair pair : targets)
				pair.getBrick().rotateZ(angle);
		} else if (ke.getKeyCode() == 107) {
			for(BrickColorPair pair : targets)
				pair.getBrick().scale(1.1f);
		} else if (ke.getKeyCode() == 109) {
			for(BrickColorPair pair : targets)
				pair.getBrick().scale(1 / 1.1f);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD8) {
			for(BrickColorPair pair : targets)
				pair.getBrick().translate(0f, 0f, mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD2) {
			for(BrickColorPair pair : targets)
				pair.getBrick().translate(0f, 0f, -mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD4) {
			for(BrickColorPair pair : targets)
				pair.getBrick().translate(0f, mult, 0f);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD6) {
			for(BrickColorPair pair : targets)
				pair.getBrick().translate(0f, -mult, 0f);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD7) {
			for(BrickColorPair pair : targets)
				pair.getBrick().translate(0f, -mult, mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD9) {
			for(BrickColorPair pair : targets)
				pair.getBrick().translate(0f, mult, mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD1) {
			for(BrickColorPair pair : targets)
				pair.getBrick().translate(0f, mult, -mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD3) {
			for(BrickColorPair pair : targets)
				pair.getBrick().translate(0f, -mult, -mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD0) {
			for(BrickColorPair pair : targets)
				pair.getBrick().translate(mult, 0f, 0f);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD5) {
			for(BrickColorPair pair : targets)
				pair.getBrick().translate(-mult, 0f, 0f);
		} else {
			//System.out.println(ke.getKeyChar() + ", " + ke.getKeyCode());
		}
		renderingArea.repaint();
		//System.out.println();
	}

	private SimpleVector getCentralPoint(SimpleVector[] centers) {
		if(centers.length == 0) return new SimpleVector();
		if(centers.length == 1) return centers[0];
		//SimpleVector res = pivots[0];
		float x = 0; float y = 0; float z = 0;
		for(SimpleVector p : centers){
			x += p.x;
			y += p.y;
			z += p.z;
		}
		
		SimpleVector res = new SimpleVector(x, y, z);
		res.scalarMul(1.0f/(float)centers.length);
		return res;
		
	}
	
	
}
