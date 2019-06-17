import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;


public class Main {
	
	private static JMenuBar makeMenuBar(final JFrame frame){
		
		JMenuBar bar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem save = new JMenuItem(new AbstractAction("Save"){
			
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e){
				
				JFileChooser chooser = new JFileChooser("Save");
				
				chooser.showSaveDialog(frame);
				
				File file = chooser.getSelectedFile();
				
				try{
					
					StructureIO.save(file);
					
				}
				catch(FileNotFoundException ex){
					
					ex.printStackTrace();
					
				}
				
			}
			
		});
		
		JMenuItem load = new JMenuItem(new AbstractAction("Load"){
			
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e){
				
				JFileChooser chooser = new JFileChooser("Load");
				
				chooser.showOpenDialog(frame);
				
				File file = chooser.getSelectedFile();
				
				try{
					
					StructureIO.load(file);
					
				}
				catch(FileNotFoundException ex){
					
					ex.printStackTrace();
					
				}
				
			}
			
		});
		
		fileMenu.add(save);
		
		fileMenu.add(load);
		
		bar.add(fileMenu);
		
		return bar;
		
	}
	
	public static void main(String[] args){
		
		try{
			
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
		}
		catch(Exception e){
			
			e.printStackTrace();
			
		}
		
		final GLProfile profile = GLProfile.get(GLProfile.GL2);
		
		GLCapabilities capabilities = new GLCapabilities(profile);
		
		final GLCanvas canvas = new GLCanvas(capabilities);
		
		World.initialize(canvas);
		
		canvas.setSize(400, 400);
		
		Input.initialize(canvas);
		
		final JFrame frame = new JFrame("Structure Editor");
		
		frame.setJMenuBar(makeMenuBar(frame));
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(canvas);
		
		frame.setSize(640, 480);
		
		frame.setVisible(true);
		
		final FPSAnimator anim = new FPSAnimator(canvas, 60, true);
		
		anim.start();
		
	}
	
}
