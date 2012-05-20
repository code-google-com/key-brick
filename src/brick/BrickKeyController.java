package brick;

import java.awt.event.*;

import javax.swing.JPanel;

import com.threed.jpct.*;

public class BrickKeyController extends KeyAdapter {
	private Object3D object;
	private float angle;
	private JPanel renderingArea;
	
	public BrickKeyController(Object3D target, float angle, JPanel renderingArea) {
		object = target;
		this.angle = angle;
		this.renderingArea = renderingArea;
	}
	
	@Override
	public void keyPressed(KeyEvent ke) {
		float mult = 1.0f;
		if(ke.isControlDown()) mult = 10.0f;
		else if(ke.isShiftDown()) mult = 0.1f;
		
		if (ke.getKeyCode() == KeyEvent.VK_U) {
			object.rotateX(-angle);
		} else if (ke.getKeyCode() == KeyEvent.VK_I) {
			object.rotateX(angle);
		} else if (ke.getKeyCode() == KeyEvent.VK_J) {
			object.rotateY(-angle);
		} else if (ke.getKeyCode() == KeyEvent.VK_K) {
			object.rotateY(angle);
		} else if (ke.getKeyCode() == KeyEvent.VK_M) {
			object.rotateZ(-angle);
		} else if (ke.getKeyCode() == KeyEvent.VK_COMMA) {
			object.rotateZ(angle);
		} else if (ke.getKeyCode() == 109) {
			object.scale(1.1f);
		} else if (ke.getKeyCode() == 107) {
			object.scale(1 / 1.1f);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD8) {
			object.translate(0f, 0f, 1f*mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD2) {
			object.translate(0f, 0f, -1f*mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD4) {
			object.translate(0f, 1f*mult, 0f);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD6) {
			object.translate(0f, -1f*mult, 0f);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD7) {
			object.translate(0f, -1f*mult, 1f*mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD9) {
			object.translate(0f, 1f*mult, 1f*mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD1) {
			object.translate(0f, 1f*mult, -1f*mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD3) {
			object.translate(0f, -1f*mult, -1f*mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD0) {
			object.translate(1f*mult, 0f, 0f);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD5) {
			object.translate(-1f*mult, 0f, 0f);
		} else {
			System.out.println(ke.getKeyChar() + ", " + ke.getKeyCode());
		}
		renderingArea.repaint();
		//System.out.println();
	}
}
