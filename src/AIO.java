import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;

public class AIO{
	public AIO(){
		JFrame frame = new JFrame("All In One");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JToolBar tool = new JToolBar();
		tool.setVisible(true);
		Container contentPane = frame.getContentPane();
		frame.setSize(1021,366);
		frame.setResizable(false);
		/*Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		  int height = screenSize.height;
		  int width = screenSize.width;
		  frame.setSize(width/2, height/2);*/
		  frame.setLocationRelativeTo(null);
		tool.setSize(new Dimension(1021,366));
		contentPane.add(tool,BorderLayout.NORTH);
		JButton editor = new JButton("");editor.setIcon(new ImageIcon(getClass().getResource("Editor.jpg")));
		tool.add(editor);
		
		editor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new TextEditor();
			}
		});
		
		tool.addSeparator();
		tool.addSeparator();
		
		JButton imageEditor = new JButton("");imageEditor.setIcon(new ImageIcon(getClass().getResource("Image Editor.jpg")));
		tool.add(imageEditor);
		
		imageEditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Main();
			}
		});
		
		tool.addSeparator();
		tool.addSeparator();
		
		JButton converter = new JButton("");converter.setIcon(new ImageIcon(getClass().getResource("Convertor.jpg")));
		tool.add(converter);
		
		converter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Converters conv=new Converters();
				conv.main(null);
			}
		});
		
		
		
		frame.setVisible(true);
		frame.setLayout(null);
	}
	
	public static void main(String args[]) {
		new AIO();
	}
}
