import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.*;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class PDFToWordConverter {
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
				selectPDFFiles();
			}
		});
	 
 }

	
	
	public static void selectPDFFiles()
	{
			
			JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
		      FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF","pdf");
		      chooser.setFileFilter(filter);
		      chooser.setMultiSelectionEnabled(false);
		      int returnVal = chooser.showOpenDialog(null);
		      path.setText(chooser.getSelectedFile().getAbsolutePath());
		      convert.addActionListener(new ActionListener() {
		    	  public void actionPerformed(ActionEvent e)
		    	  {
		    		  if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	               File Files=chooser.getSelectedFile();
		    	       			
		    	                convertPDFToWord(Files.toString(),"Extracted Word From PDF "+".doc");
		    	               		JOptionPane.showMessageDialog(null, "Successfully Converted");
		    	        }
		    	  }
		     
		    	  });
			
	
		
	}
	
	
	 public static void convertPDFToWord(String src,String desc){
		  try{
			  XWPFDocument doc = new XWPFDocument();
			  String pdf = src;
			  PdfReader reader = new PdfReader(pdf);
			  PdfReaderContentParser parser = new PdfReaderContentParser(reader);
			  for (int i = 1; i <= reader.getNumberOfPages(); i++) {
			    TextExtractionStrategy strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
			    	    String text = strategy.getResultantText();
			    	    XWPFParagraph p = doc.createParagraph();
			    	    XWPFRun run = p.createRun();
			    	    run.setText(text);
			    	    run.addBreak();
			    	}
			    	FileOutputStream out = new FileOutputStream(desc);
			    	doc.write(out);
		  }catch(Exception e){e.printStackTrace();}
		  
		 }
}
