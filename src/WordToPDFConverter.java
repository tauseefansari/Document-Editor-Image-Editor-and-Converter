import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class WordToPDFConverter{
	static JTextField path;
	static JButton select,convert;
 public static void main(String[] args){
	 JFrame f=new JFrame("Browse to Select a File");
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
    	                convertWordToPDF(Files[i].toString(),"Extracted PDF From Word "+(i+1)+".pdf");
    	                }
    	               		JOptionPane.showMessageDialog(null, "Successfully Converted");
    	        }
    	   
    	  }
  
    	  });
      }

 public static void convertWordToPDF(String src,String desc)
 { 
	 try {
		FileInputStream fis= new FileInputStream(src);
		Document document = new Document(PageSize.A4);
		try
		{
			PdfWriter.getInstance(document, new FileOutputStream(desc));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		XWPFDocument doc=new XWPFDocument(fis);
		List<XWPFParagraph> paragraph = doc.getParagraphs();
		XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
			document.open();
			Paragraph paragr = new Paragraph(extractor.getText());
			document.add(paragr);
			document.close();
	 }
		catch(Exception e)
		{
			e.printStackTrace();
		}
	 }
 }

