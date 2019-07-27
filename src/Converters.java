import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Converters {
	public static void main(String args[]) {
		JFrame f=new JFrame("Converters");
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setSize(400,400);
		f.setVisible(true);
		f.setLayout(null);
		f.setResizable(false);
		f.setLocationRelativeTo(null);
		JButton pdftoword=new JButton("PDF to Word Converter");
		JButton pdftotext=new JButton("PDF to Text Converter");
		JButton wordtotext=new JButton("Word to Text Converter");
		JButton wordtopdf=new JButton("Word to PDF Converter");
		pdftoword.setBounds(50, 30, 300, 40);
		pdftotext.setBounds(50,100,300,40);
		wordtotext.setBounds(50,170,300,40);
		wordtopdf.setBounds(50,240,300,40);
		f.add(pdftoword);f.add(pdftotext);f.add(wordtotext);f.add(wordtopdf);
		
		pdftotext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PDFToTextConverter ptt = new PDFToTextConverter();
				ptt.main(null);
			}
		});
		
		pdftoword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PDFToWordConverter ptw = new PDFToWordConverter();
				ptw.main(null);
			}
		});
		
		wordtotext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WordToTextConverter wtt = new WordToTextConverter();
				wtt.main(null);
			}
		});
		
		wordtopdf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WordToPDFConverter wtp = new WordToPDFConverter();
				wtp.main(null);
			}
		});
	}

}
