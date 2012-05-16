package brick;

import java.io.FileNotFoundException;

import javax.swing.*;
import com.threed.jpct.*;


@SuppressWarnings("serial")
public class BrickViewer extends JFrame {
	private final static String myPath = "~/books/robotics";
	private final static String ldrawPath = "/Applications/LDRAW";

	public BrickViewer(String filename) throws PartNotFoundException, FileNotFoundException {
		PartFactory fact = new PartFactory(ldrawPath);
		fact.addPath(myPath);
		PartSpec model = fact.getPart(filename);
		
		setTitle("Brick Viewer");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		BrickPanel bp = new BrickPanel();
		getContentPane().add(bp);
		BrickObject obj = model.toBrickObject(new Matrix(), bp);
		obj.setColorCode(2);
		bp.buildAll();
		addKeyListener(new BrickKeyController(obj, (float)Math.PI/48, bp));
	}
	
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: BrickViewer model");
			System.exit(1);
		}
		try {
			new BrickViewer(args[0]).setVisible(true);
		} catch (PartNotFoundException pnfe) {
			JOptionPane.showMessageDialog(null, "Cannot find model \"" + args[0] + "\"");
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Cannot locate ldconfig.ldr");
		} 
	}
}
