package brick;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Interact2D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;
import com.threed.jpct.Object3D;

public class BrickMouseController extends MouseAdapter {
	private BrickPanel ra;
	private FrameBuffer buffer;
	private World world;
	private float pad;
	
	public BrickMouseController(BrickPanel renderingArea, World world, FrameBuffer buffer, float pad) {
		this.world = world;
		this.buffer = buffer;
		ra = renderingArea;
		this.pad = pad;
	}
	
	public void mouseClicked(MouseEvent me){
		//System.out.println("Clicked!");
		Point clicked = me.getPoint();
		SimpleVector dir =
			Interact2D.reproject2D3DWS(world.getCamera(), buffer, clicked.x, clicked.y).normalize();
		Object[] res = world.calcMinDistanceAndObject3D(world.getCamera().getPosition(), dir, pad);
		if(res[1] != null){
			Object3D ob = (Object3D)res[1];
			Object3D[] pars = ((Object3D)res[1]).getParents();
			while(pars.length != 0){
				pars = (ob = pars[0]).getParents();
				//System.out.println("Up one.");
			}
			ra.setSelectedBrick((BrickObject)ob);
			//System.out.println("...And a hit!");
		} else {
			//System.out.println("...and a miss.");
		}
		
	}
}
