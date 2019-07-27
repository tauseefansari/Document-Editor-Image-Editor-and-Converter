import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

public class PDFToTextConverter{
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

 
 //allow pdf files selection for converting
 public static void selectPDFFiles(){

	 JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
      FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF","pdf");
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
    	                convertPDFToText(Files[i].toString(),"Extracted Text From PDF "+(i+1)+".txt");
    	                }
    	               		JOptionPane.showMessageDialog(null, "Successfully Converted");
    	        }
    	   
    	  }
  
    	  });
      }
       
 public static void convertPDFToText(String src,String desc){
  try{
   //create file writer
   FileWriter fw=new FileWriter(desc);
   //create buffered writer
   BufferedWriter bw=new BufferedWriter(fw);
   //create pdf reader
   PdfReader pr=new PdfReader(src);
   //get the number of pages in the document
   int pNum=pr.getNumberOfPages();
   //extract text from each page and write it to the output text file
   for(int page=1;page<=pNum;page++){
    String text=PdfTextExtractor.getTextFromPage(pr, page);
    bw.write(text);
    bw.newLine();
   }
   bw.flush();
   bw.close();
  }catch(Exception e){e.printStackTrace();}
  
 }
}