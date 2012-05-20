package brick;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.threed.jpct.Config;
import com.threed.jpct.Matrix;


@SuppressWarnings("serial")
public class BrickViewer extends JFrame {
	//private final static String myPath = "~/books/robotics";
	private final static String ldrawPath = "C:\\Program Files (x86)\\LDraw";
	private final JFrame helper;
	
	public BrickViewer(String filename) throws PartNotFoundException, FileNotFoundException {
		PartFactory fact = new PartFactory(ldrawPath);
		//fact.addPath(myPath);
		PartSpec model = fact.getPart(filename);
		
		Config.maxPolysVisible = 100000;
		
		setTitle("Brick Viewer");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		BrickPanel bp = new BrickPanel();
		getContentPane().add(bp);
		BrickObject obj = model.toBrickObject(new Matrix(), bp);
		obj.setColorCode(2);
		bp.buildAll();
		addKeyListener(new BrickKeyController(obj, (float)Math.PI/48, bp));
		helper = new JFrame();
		helper.add(bp.ap);
		helper.setSize(400, 150);
		
		
	}
	
	public void setVisible(boolean b){
		super.setVisible(b);
		helper.setVisible(b);
	}
	
	public static void main(String[] args) {
		String loc = "";
		if (args.length < 1) {
			System.out.println("Usage: BrickViewer model");
			JFileChooser jfc = new JFileChooser(new File(ldrawPath));
			FileNameExtensionFilter fnef = new FileNameExtensionFilter("LDraw model files (.dat)", "dat");
			jfc.setFileFilter(fnef);
			int ret = jfc.showOpenDialog(null);
			if(ret == JFileChooser.APPROVE_OPTION){
				loc = jfc.getSelectedFile().getName();
				System.out.println(loc);
			}else{
				System.exit(1);
			}
		}else{
			loc = args[0];
		}
		try {
			new BrickViewer(loc).setVisible(true);
		} catch (PartNotFoundException pnfe) {
			JOptionPane.showMessageDialog(null, "Cannot find model \"" + loc + "\"");
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Cannot locate ldconfig.ldr");
		} 
	}
}
