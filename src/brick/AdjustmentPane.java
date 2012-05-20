package brick;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

@SuppressWarnings("serial")
public class AdjustmentPane extends JPanel {
	private World world;
	private JPanel ra;
	private JTextField xpos, ypos, zpos;
	private JTextField xrot, yrot, zrot;
	
	public AdjustmentPane(World w, JPanel renderingArea){
		super();
		world = w;
		ra = renderingArea;
		
		xpos = new JTextField(5);
		xpos.addKeyListener(new FieldListener(xpos, this));
		ypos = new JTextField(5);
		ypos.addKeyListener(new FieldListener(ypos, this));
		zpos = new JTextField(5);
		zpos.addKeyListener(new FieldListener(zpos, this));
		
		xrot = new JTextField(5);
		xrot.addKeyListener(new FieldListener(xrot, this));
		yrot = new JTextField(5);
		yrot.addKeyListener(new FieldListener(yrot, this));
		zrot = new JTextField(5);
		zrot.addKeyListener(new FieldListener(zrot, this));
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(new JLabel("Camera Position: "));
		add(xpos); add(ypos); add(zpos);
		
		add(new JLabel("Camera Rotation: "));
		add(xrot); add(yrot); add(zrot);
		update();
	}
	
	public void update(){
		SimpleVector pos = world.getCamera().getPosition();
		
		xpos.setText("" + pos.x);
		ypos.setText("" + pos.y);
		zpos.setText("" + pos.z);
		
		SimpleVector dir = world.getCamera().getDirection();
		
		xrot.setText("" + dir.x);
		yrot.setText("" + dir.y);
		zrot.setText("" + dir.z);
	}
	public float getXPos(){
		return Float.parseFloat(xpos.getText());
	}
	public float getYPos(){
		return Float.parseFloat(ypos.getText());
	}
	public float getZPos(){
		return Float.parseFloat(zpos.getText());
	}
	public float getXRot(){
		return Float.parseFloat(xrot.getText());
	}
	public float getYRot(){
		return Float.parseFloat(yrot.getText());
	}
	public float getZRot(){
		return Float.parseFloat(zrot.getText());
	}
	public void updateCamera(){
		//SimpleVector newRot = new SimpleVector(getXRot(), getYRot(), getZRot());
		SimpleVector newPos = new SimpleVector(getXPos(), getYPos(), getZPos());
		world.getCamera().setPosition(newPos);
		//world.getCamera().rotateCameraX(getXRot());
		//world.getCamera().rotateCameraY(getYRot());
		//world.getCamera().rotateCameraZ(getZRot());
		ra.repaint();
		System.out.println("Updated.");
	}
	
	private class FieldListener extends KeyAdapter{
		private JTextField val;
		private AdjustmentPane ap;
		
		FieldListener(JTextField tf, AdjustmentPane ap){
			val = tf;
			this.ap = ap;
		}
		public void keyPressed(KeyEvent ke) {
			if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
				System.out.println("Here.");
				ap.updateCamera();
			} else if (ke.getKeyCode() == KeyEvent.VK_UP) {
				if(ke.isControlDown()){
					setVal(10.0f);
				} else if (ke.isShiftDown()){
					setVal(0.1f);
				} else {
					setVal(1.0f);
				}
				ap.updateCamera();
			} else if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
				if(ke.isControlDown()){
					setVal(-10.0f);
				} else if (ke.isShiftDown()){
					setVal(-0.1f);
				} else {
					setVal(-1.0f);
				}
				ap.updateCamera();
			}
		}
		
		private void setVal(float f){
			String res = Float.toString(Float.parseFloat(val.getText()) + f);
			res = res.substring(0, res.length() > 5 ? 5 : res.length());
			val.setText(res);
		}
		
	}
	
}
