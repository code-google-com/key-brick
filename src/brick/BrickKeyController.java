package brick;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.threed.jpct.Object3D;

public class BrickKeyController extends KeyAdapter {
	private float angle;
	private BrickPanel renderingArea;
	
	public BrickKeyController(float angle, BrickPanel renderingArea) {
		this.angle = angle;
		this.renderingArea = renderingArea;
	}
	
	@Override
	public void keyPressed(KeyEvent ke) {
		ArrayList<BrickObject> targets = renderingArea.getSelectedBricks();
		float mult = 1.0f;
		if(ke.isControlDown()) mult = 10.0f;
		else if(ke.isShiftDown()) mult = 0.1f;
		
		if (ke.getKeyCode() == KeyEvent.VK_U) {
			for(BrickObject object : targets)
				object.rotateX(-angle);
		} else if (ke.getKeyCode() == KeyEvent.VK_I) {
			for(BrickObject object : targets)
				object.rotateX(angle);
		} else if (ke.getKeyCode() == KeyEvent.VK_J) {
			for(BrickObject object : targets)
				object.rotateY(-angle);
		} else if (ke.getKeyCode() == KeyEvent.VK_K) {
			for(BrickObject object : targets)
				object.rotateY(angle);
		} else if (ke.getKeyCode() == KeyEvent.VK_M) {
			for(BrickObject object : targets)
				object.rotateZ(-angle);
		} else if (ke.getKeyCode() == KeyEvent.VK_COMMA) {
			for(BrickObject object : targets)
				object.rotateZ(angle);
		} else if (ke.getKeyCode() == 109) {
			for(BrickObject object : targets)
				object.scale(1.1f);
		} else if (ke.getKeyCode() == 107) {
			for(BrickObject object : targets)
				object.scale(1 / 1.1f);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD8) {
			for(BrickObject object : targets)
				object.translate(0f, 0f, 1f*mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD2) {
			for(BrickObject object : targets)
				object.translate(0f, 0f, -1f*mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD4) {
			for(BrickObject object : targets)
				object.translate(0f, 1f*mult, 0f);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD6) {
			for(BrickObject object : targets)
				object.translate(0f, -1f*mult, 0f);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD7) {
			for(BrickObject object : targets)
				object.translate(0f, -1f*mult, 1f*mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD9) {
			for(BrickObject object : targets)
				object.translate(0f, 1f*mult, 1f*mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD1) {
			for(BrickObject object : targets)
				object.translate(0f, 1f*mult, -1f*mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD3) {
			for(BrickObject object : targets)
				object.translate(0f, -1f*mult, -1f*mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD0) {
			for(BrickObject object : targets)
				object.translate(1f*mult, 0f, 0f);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD5) {
			for(BrickObject object : targets)
				object.translate(-1f*mult, 0f, 0f);
		} else {
			System.out.println(ke.getKeyChar() + ", " + ke.getKeyCode());
		}
		renderingArea.repaint();
		//System.out.println();
	}
}
