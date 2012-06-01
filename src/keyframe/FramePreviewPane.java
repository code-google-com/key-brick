package keyframe;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import brick.BrickPanel;

@SuppressWarnings("serial")
public class FramePreviewPane extends JFrame implements ActionListener {
	private JPanel content;
	private ArrayList<Keyframe> frames;
	private JButton first, prev, next, last;
	private BrickPanel preview;
	private int curFrame = -1;

	public FramePreviewPane(){
		
		frames = new ArrayList<Keyframe>();
		content = new JPanel();
		add(content);
		
		first = new JButton("First"); first.setActionCommand("first");
		first.addActionListener(this);
		prev = new JButton("Previous"); prev.setActionCommand("prev");
		prev.addActionListener(this);
		next = new JButton("Next"); next.setActionCommand("next");
		next.addActionListener(this);
		last = new JButton("Last"); last.setActionCommand("last");
		last.addActionListener(this);
		JPanel topButtons = new JPanel();
		topButtons.setLayout(new BoxLayout(topButtons, BoxLayout.X_AXIS));
		topButtons.add(first); topButtons.add(prev);
		topButtons.add(next); topButtons.add(last);
		first.setEnabled(false); prev.setEnabled(false);
		next.setEnabled(false); last.setEnabled(false);
		
		//calculated this in one run, since the ordering to get button sizes is frustrating.
		int size = 262;
		//Same as before, the height of the buttons turn out to be 26;
		int buttonHeight = 26;
		
		preview = new BrickPanel(size, size);
		preview.buildAll();
		content.add(preview);
		
		content.add(topButtons);
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(size + 10, size + buttonHeight + 10));
		setMinimumSize(getPreferredSize());
		setMaximumSize(getPreferredSize());
		setVisible(true);
		//int size = first.getWidth() + last.getWidth() + next.getWidth() + prev.getWidth();
		System.out.println("SIZE IS " + first.getHeight());
		
	}
	
	public void updateImage(){
		//preview.
	}
	
	public BrickPanel getPreviewPanel(){
		return preview;
	}
	
	private void next(){
		curFrame++;
		change();
		if(frames.size() > 0){
			prev.setEnabled(true);
			first.setEnabled(true);
		}
	}
	
	private void prev(){
		curFrame--;
		change();
		if(frames.size() > 1){
			next.setEnabled(true);
			last.setEnabled(true);
		}
	}
	
	private void first(){
		curFrame = 0;
		change();
		if(frames.size() > 1){
			next.setEnabled(true);
			last.setEnabled(true);
		}
	}
	
	public void toStart(){
		first();
	}
	
	public void addFrame(Keyframe kf){
		frames.add(kf);
		last();
	}
	
	public Keyframe getCurrentFrame(){
		return frames.get(curFrame);
	}
	
	public ArrayList<Keyframe> getAllFrames(){
		return frames;
	}
	
	//I find this funny, because it's faster by a tiny, tiny bit than setting it directly.
	private void last(){
		curFrame = Integer.MAX_VALUE;
		change();
		if(frames.size() > 1){
			first.setEnabled(true);
			prev.setEnabled(true);
		}
	}
	
	private void change(){
		if(curFrame <= 0){
			curFrame = 0;
			first.setEnabled(false);
			prev.setEnabled(false);
		}
		if(curFrame >= frames.size() - 1){
			curFrame = frames.size() - 1;
			next.setEnabled(false);
			last.setEnabled(false);
		}
		Animator.restoreFromFrame(preview, frames.get(curFrame));
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		String command = arg0.getActionCommand();
		if(command.equals("first")){
			first();
		} else if(command.equals("next")){
			next();
		} else if(command.equals("prev")){
			prev();
		} else if(command.equals("last")){
			last();
		}
	}

}
