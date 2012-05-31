package brick;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import keyframe.Animator;
import keyframe.Keyframe;

import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

@SuppressWarnings("serial")
public class AdjustmentPane extends JPanel implements ActionListener {
	private BrickObject chosen;
	private int numBricks = 0;
	//Misleading name: Actually number of objects in total, not just bricks.
	
	private World world;
	private BrickPanel ra;
	private JTextField xpos, ypos, zpos;
	private JTextField xrot, yrot, zrot;
	private JTextField obj, objx, objy, objz;
	private JButton addButton, objrx, objry, objrz;
	private JButton shiftx, shifty, shiftz;
	private JButton takeFrame, restoreFrame, clear;
	private JButton play;
	private Keyframe frame1;
	private Keyframe frame2;
	private float transStep = 1f;
	private float angle = (float)Math.PI / 4;
	
	
	public AdjustmentPane(World w, BrickPanel renderingArea){
		super();
		world = w;
		ra = renderingArea;
		
		//Camera position indicators
		xpos = new JTextField(5);
		xpos.addKeyListener(new FieldListener(xpos, this));
		ypos = new JTextField(5);
		ypos.addKeyListener(new FieldListener(ypos, this));
		zpos = new JTextField(5);
		zpos.addKeyListener(new FieldListener(zpos, this));
		
		//Camera direction indicators
		//That is, these three form the vector that points in the same direction as the camera
		xrot = new JTextField(5);
		xrot.addKeyListener(new FieldListener(xrot, this));
		yrot = new JTextField(5);
		yrot.addKeyListener(new FieldListener(yrot, this));
		zrot = new JTextField(5);
		zrot.addKeyListener(new FieldListener(zrot, this));
		
		//Selected object indicators (index in objs, then position)
		obj = new JTextField(5);
		obj.addKeyListener(new FieldListener(obj, this));
		objx = new JTextField(5);
		objx.addKeyListener(new FieldListener(objx, this));
		objy = new JTextField(5);
		objy.addKeyListener(new FieldListener(objy, this));
		objz = new JTextField(5);
		objz.addKeyListener(new FieldListener(objz, this));
		
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(new JLabel("Camera Position: "));
		add(xpos); add(ypos); add(zpos);
		
		add(new JLabel("Camera Rotation: "));
		add(xrot); add(yrot); add(zrot);
		
		add(new JLabel("Edit Object3D:"));
		add(obj); add(objx); add(objy); add(objz);
		obj.setText("0");
		obj.setName("object");
		
		addButton = new JButton("add");
		addButton.setActionCommand("add");
		add(addButton);
		addButton.addActionListener(this);
		
		objrx = new JButton("Rotate X"); objry = new JButton("Rotate Y"); objrz = new JButton("Rotate Z");
		objrx.setActionCommand("xrot"); objry.setActionCommand("yrot"); objrz.setActionCommand("zrot");
		add(objrx); add(objry); add(objrz);
		objrx.addActionListener(this); objry.addActionListener(this); objrz.addActionListener(this);
		
		shiftx = new JButton("Translate X"); shifty = new JButton("Translate Y"); shiftz = new JButton("Translate Z");
		shiftx.setActionCommand("xtran"); shifty.setActionCommand("ytran"); shiftz.setActionCommand("ztran");
		add(shiftx); add(shifty); add(shiftz);
		shiftx.addActionListener(this); shifty.addActionListener(this); shiftz.addActionListener(this);
		
		takeFrame = new JButton("Take Frame"); restoreFrame = new JButton("Restore Last Frame"); clear = new JButton("Clear");
		takeFrame.setActionCommand("take"); restoreFrame.setActionCommand("restore"); clear.setActionCommand("clear");
		add(takeFrame); add(restoreFrame); add(clear);
		takeFrame.addActionListener(this);
		restoreFrame.addActionListener(this);
		clear.addActionListener(this);
		
		play = new JButton("Play");
		play.setActionCommand("play");
		add(play);
		play.addActionListener(this);
		
		
	}
	
	public void update(){
		SimpleVector pos = world.getCamera().getPosition();
		numBricks = ra.numBricks();
		
		xpos.setText("" + pos.x);
		ypos.setText("" + pos.y);
		zpos.setText("" + pos.z);
		
		SimpleVector dir = world.getCamera().getDirection();
		
		xrot.setText("" + dir.x);
		yrot.setText("" + dir.y);
		zrot.setText("" + dir.z);
		
		int val = Integer.parseInt(obj.getText());
		if(val >= numBricks) val = numBricks - 1;
		if(numBricks != 0){
			
			chosen = ra.getObject(val);
			//ra.setSelectedBrick(chosen);
		
			SimpleVector objTrans = chosen.getTranslation();
		
			objx.setText("" + objTrans.x);
			objy.setText("" + objTrans.y);
			objz.setText("" + objTrans.z);
		} else {
			System.out.println("Stuff! BLARHJHAJHD");
		}
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
	public float getObjX(){
		return Float.parseFloat(objx.getText());
	}
	public float getObjY(){
		return Float.parseFloat(objy.getText());
	}
	public float getObjZ(){
		return Float.parseFloat(objz.getText());
	}
	public int getObj(){
		return Integer.parseInt(obj.getText());
	}
	
	public void updateSelectedObject(BrickObject brick){
		obj.setText("" + ra.indexOf(brick));
		update();
		updateCamera();
	}
	
	public void updateCamera(){
		//SimpleVector newRot = new SimpleVector(getXRot(), getYRot(), getZRot());
		SimpleVector camPos = new SimpleVector(getXPos(), getYPos(), getZPos());
		world.getCamera().setPosition(camPos);
		//System.out.println("Updated Camera.");
		//world.getCamera().lookAt(orig);
		
		SimpleVector curTrans = chosen.getTranslation();
		SimpleVector posDelta = new SimpleVector(getObjX() - curTrans.x, getObjY() - curTrans.y, getObjZ() - curTrans.z);
		chosen.translate(posDelta);
		ra.repaint();
		//System.out.println("Updated Object " + getObj() +  ".");
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
				ap.updateCamera();
			} else if (ke.getKeyCode() == KeyEvent.VK_UP) {
				if(val.getName() != null){
					setObjectVal(1);
				} else if (ke.isControlDown()){
					setVal(10.0f);
				} else if (ke.isShiftDown()){
					setVal(0.1f);
				} else {
					setVal(1.0f);
				}
				ap.updateCamera();
			} else if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
				if(val.getName() != null){
					setObjectVal(-1);
				} else if(ke.isControlDown()){
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
		
		private void setObjectVal(int i){
			int t = Integer.parseInt(val.getText());
			if(t <= 0 && i < 0) val.setText("0");
			else if(t >= numBricks-1 && i > 0) val.setText("" + (numBricks - 1));
			else val.setText(Integer.toString(Integer.parseInt(val.getText()) + i));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		Boolean isCtrl = (e.getModifiers() & ActionEvent.CTRL_MASK) != 0;
		
		if(command.startsWith("xt")){
			chosen.translateX(isCtrl ? -transStep : transStep);
		} else if(command.startsWith("yt")){
			chosen.translateY(isCtrl ? -transStep : transStep);
		} else if(command.startsWith("zt")){
			chosen.translateZ(isCtrl ? -transStep : transStep);
		} else if(command.startsWith("xr")){
			chosen.rotateX(isCtrl ? -angle : angle);
		} else if(command.startsWith("yr")){
			chosen.rotateY(isCtrl ? -angle : angle);
		} else if(command.startsWith("zr")){
			chosen.rotateZ(isCtrl ? -angle : angle);
		} else if(command.equals("take")){
			if(frame1 == null) frame1 = new Keyframe(ra, world, 0);
			else frame2 = new Keyframe(ra, world, 0);
		} else if(command.equals("restore")){
			Animator.restoreFromFrame(ra, world, frame1);
		} else if(command.equals("clear")){
			ra.removeAllObjects();
		} else if(command.equals("play")){
			new Thread(new Runnable() {
	            public void run() { Animator.moveBetweenFrames(ra, world, frame1, frame2); }
	        }).start();
		} else if("add".equals(command)){
			JFileChooser jfc = new JFileChooser(new File(BrickViewer.ldrawPath));
			BrickPreviewWindow bpw = new BrickPreviewWindow(jfc, 200, 200);
			bpw.setupListeners();
			jfc.setAccessory(bpw);
			FileNameExtensionFilter fnef = new FileNameExtensionFilter("LDraw model files (.dat, .ldr)", "dat", "ldr");
			jfc.setFileFilter(fnef);
			int ret = jfc.showOpenDialog(null);
			if(ret == JFileChooser.APPROVE_OPTION){
				String loc = jfc.getSelectedFile().getName();
				try {
					ra.addNewBrick(loc);
				} catch (PartNotFoundException pnfe) {
					JOptionPane.showMessageDialog(null, "Cannot find model \"" + loc + "\"");
				} catch (FileNotFoundException fnfe) {
					JOptionPane.showMessageDialog(null, "Cannot locate ldconfig.ldr");
				} 
			}
			
		}
		ra.repaint();
		
	}
}
