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
		} else if (ke.getKeyCode() == KeyEvent.VK_9) {
			object.scale(1.1f);
		} else if (ke.getKeyCode() == KeyEvent.VK_0) {
			object.scale(1 / 1.1f);
		}
		renderingArea.repaint();
		System.out.println();
	}
}
