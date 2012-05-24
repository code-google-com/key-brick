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
		BrickObject target = 
			renderingArea.getSelectedBricks().isEmpty()
			? null
			: renderingArea.getSelectedBricks().get(0).getBrick();
		float mult = 1.0f;
		if(ke.isControlDown()) mult = 10.0f;
		else if(ke.isShiftDown()) mult = 0.1f;
		
		if (ke.getKeyCode() == KeyEvent.VK_U) {
			target.rotateX(-angle);
		} else if (ke.getKeyCode() == KeyEvent.VK_I) {
			target.rotateX(angle);
		} else if (ke.getKeyCode() == KeyEvent.VK_J) {
			target.rotateY(-angle);
		} else if (ke.getKeyCode() == KeyEvent.VK_K) {
			target.rotateY(angle);
		} else if (ke.getKeyCode() == KeyEvent.VK_M) {
			target.rotateZ(-angle);
		} else if (ke.getKeyCode() == KeyEvent.VK_COMMA) {
			target.rotateZ(angle);
		} else if (ke.getKeyCode() == 107) {
			target.scale(1.1f);
		} else if (ke.getKeyCode() == 109) {
			target.scale(1 / 1.1f);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD8) {
			target.translate(0f, 0f, mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD2) {
			target.translate(0f, 0f, -mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD4) {
			target.translate(0f, mult, 0f);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD6) {
			target.translate(0f, -mult, 0f);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD7) {
			target.translate(0f, -mult, mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD9) {
			target.translate(0f, mult, mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD1) {
			target.translate(0f, mult, -mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD3) {
			target.translate(0f, -mult, -mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD0) {
			target.translate(mult, 0f, 0f);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD5) {
			target.translate(-mult, 0f, 0f);
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
