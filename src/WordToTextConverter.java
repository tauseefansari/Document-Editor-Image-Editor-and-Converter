import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class WordToTextConverter{
	static JTextField path;
	static JButton select,convert;
 public static void main(String[] args){
	 JFrame f=new JFrame("Browse to Select a File");
	 f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JToolBar tool=new JToolBar();
		tool.setVisible(true);
		f.setSize(700,200);
		f.setVisible(true);
		f.setLayout(null);
		f.setResizable(false);
		f.setLocationRelativeTo(null);
		Container contentPane = f.getContentPane();
		tool.setSize(new Dimension(30,400));
		contentPane.add(tool, BorderLayout.CENTER);
		  
		tool.setBounds(30, 60, 600, 40);
		path=new JTextField();
		path.setFont(new Font("Times New Roman",Font.PLAIN,14));
		select=new JButton("Browse...");
		convert=new JButton("Convert");
		path.setBounds(30, 60, 00, 30);
		select.setBounds(250, 60, 100, 30);
		convert.setBounds(350, 100, 150, 30);
		tool.add(path);tool.addSeparator();tool.add(select);tool.addSeparator();tool.add(convert);
		
		select.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				selectWordFiles();
			}
		});
	 
 }
 
 public static void selectWordFiles(){

	 JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
      FileNameExtensionFilter filter = new FileNameExtensionFilter("doc","docx");
      chooser.setFileFilter(filter);
      chooser.setMultiSelectionEnabled(true);
      int returnVal = chooser.showOpenDialog(null);
      path.setText(chooser.getSelectedFile().getAbsolutePath());
      convert.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent e)
    	  {
    		  if(returnVal == JFileChooser.APPROVE_OPTION) {
    	               File[] Files=chooser.getSelectedFiles();
    	               
    	               for( int i=0;i<Files.length;i++){     
    	                convertWordToText(Files[i].toString(),"Extracted Text From Word "+(i+1)+".txt");
    	                }
    	               		JOptionPane.showMessageDialog(null, "Successfully Converted");
    	        }
    	   
    	  }
  
    	  });
      }

 public static void convertWordToText(String src,String desc)
 { 
	 try {
		FileInputStream fis= new FileInputStream(src);
		XWPFDocument doc=new XWPFDocument(fis);
		XWPFWordExtractor extractor=new XWPFWordExtractor(doc);
		FileWriter fw=new FileWriter(desc);
		fw.write(extractor.getText());
		fw.flush();
		fw.close();
	 }
		catch(Exception e)
		{
			e.printStackTrace();
		}
	 }
 }

