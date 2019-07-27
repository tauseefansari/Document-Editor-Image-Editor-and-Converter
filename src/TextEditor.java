import java.net.*;
import java.text.MessageFormat;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.stark.*;
import java.io.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.print.*;
import javax.imageio.*;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import com.itextpdf.text.pdf.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultEditorKit;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

class TextEditor extends JFrame 
{
	static int i=0,j=0,k=0;
	private static JTextPane[] area = new JTextPane[99]; 
	static {area[j] = new JTextPane();}
	private JPanel[] panel = new JPanel[99];
	private JScrollPane[] scroll = new JScrollPane[99];
	private CloseableTabbedPane tabbedPane= new CloseableTabbedPane();
	private JFileChooser dialog = new JFileChooser(System.getProperty("user.dir"));
	private String currentFile = "New Text Editor.txt";
	String files[] = new String[99];
	private boolean changed = false;
	PrinterJob pPrinterjob = PrinterJob.getPrinterJob();
	PageFormat pPageFormat = pPrinterjob.defaultPage();
	
	Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	int height=d.height;
	int width=d.width;
	
	
	
	public TextEditor()
	{
		files[j]="New Text Editor.txt";
		panel[j]=new JPanel();
		area[j].setFont(new Font("Times New Roman",Font.PLAIN,20));
		scroll[j]= new JScrollPane(area[j],JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		panel[j].add(scroll[j]);
		tabbedPane.add(currentFile,panel[j]);
		getContentPane().add(tabbedPane,BorderLayout.CENTER);
		
		JMenuBar JMB = new JMenuBar();
		setJMenuBar(JMB);
		JMenu file = new JMenu("File");
		JMenu edit = new JMenu("Edit");
		JMenu help = new JMenu("Help");
		JMB.add(file);JMB.add(edit);JMB.add(help);
		file.add(NewFile);file.add(Open);file.add(Save);file.add(SaveAs);file.add(Quit);
		file.addSeparator();
		file.add(PageSetup);
		file.add(PrintPreview);
		file.add(Print);
		file.getItem(6).setText("Page Setup");
		file.getItem(7).setText("Print Preview");
		file.getItem(8).setText("Print    Alt+P");
		
		for(int i=0;i<=7;i++)
		{
			if(i!=5)
				file.getItem(i).setIcon(null);
		}
		
		edit.add(Cut);edit.add(Copy);edit.add(Paste);
		
		edit.getItem(0).setText("Cut      Ctrl+X");
		edit.getItem(1).setText("Copy     Ctrl+C");
		edit.getItem(2).setText("Paste    Ctrl+V");
		
		help.add(Contents);help.addSeparator();help.add(About);
		
		help.getItem(0).setText("Help Contents");
		help.getItem(2).setText("About");
		
		JToolBar tool = new JToolBar();
		add(tool,BorderLayout.NORTH);
		JButton cut = tool.add(Cut),cop = tool.add(Copy),pas = tool.add(Paste);
		JButton nw = tool.add(NewFile);
		JButton opn = tool.add(Open),sav = tool.add(Save);
		JButton prnt = tool.add(Print);
		tool.addSeparator();tool.addSeparator();
		//JButton qut = tool.add(Quit);
		
		tool.addSeparator();
		prnt.setMnemonic('p');
		sav.setMnemonic('s');
		opn.setMnemonic('o');
		nw.setMnemonic('n');
		prnt.setMnemonic('P');
		sav.setMnemonic('S');
		opn.setMnemonic('O');
		nw.setMnemonic('N');
		//qut.setMnemonic('x');
		//qut.setMnemonic('X');
		
		cut.setText("  Cut  "); cut.setIcon(new ImageIcon(getClass().getResource("cut.gif")));
		cop.setText("Copy"); cop.setIcon(new ImageIcon(getClass().getResource("copy.gif")));
		pas.setText("Paste"); pas.setIcon(new ImageIcon(getClass().getResource("paste.gif")));
		nw.setText(" New ");  nw.setIcon(new ImageIcon(getClass().getResource("new.gif")));
		opn.setText("Open"); opn.setIcon(new ImageIcon(getClass().getResource("open.gif")));
		sav.setText("Save"); sav.setIcon(new ImageIcon(getClass().getResource("save.gif")));
		prnt.setText("Print"); prnt.setIcon(new ImageIcon(getClass().getResource("print.gif")));

		
		Save.setEnabled(false);
		SaveAs.setEnabled(false);
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() 
				{
				public void windowClosing(WindowEvent e)
				{
					JFrame frame = (JFrame)e.getSource();
					if(changed)
					{
						saveOld();
						System.exit(0);
					}
					if(JOptionPane.showConfirmDialog(frame, "Are you sure want to Exit the Application ?","Exit Application",
							JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
					frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				}
				});
		
		area[j].setPreferredSize(new Dimension(width,height));
		pack();
		tabbedPane.addChangeListener(changeListener);
		area[j].addKeyListener(k1);
		setTitle("Untitled");
		area[j].requestFocusInWindow();
		
		
		setVisible(true);
		setSize(1366,768);
		setLayout(null);
		
		
	}
	
		Action NewFile = new AbstractAction("New",new ImageIcon("new.gif"))
			{
				public void actionPerformed(ActionEvent e)
				{
					saveOld();
					j++;
					area[j] = new JTextPane();
					panel[j] = new JPanel();
					i++;
					currentFile = "New Text Editor "+i+".txt";
					files[j] = currentFile;
					area[j].setFont(new Font("Times New Roman",Font.PLAIN,20));
					scroll[j] = new JScrollPane(area[j],JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
							JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
							panel[j].add(scroll[j]);
							tabbedPane.add(currentFile,panel[j]);
							area[j].addKeyListener(k1);
							setVisible(true);
							area[j].setPreferredSize(new Dimension(width,height));
							area[j].setContentType("text");
							area[j].setText("");
							area[j].requestFocusInWindow();
							setTitle("Untitled");
				}
			};
	
		Action Save = new AbstractAction("Save",new ImageIcon("save.gif"))
			{
				public void actionPerformed(ActionEvent e)
				{
					if(!currentFile.equals("Untitled"))
						saveFile(currentFile,k);
					else
						saveFileAs();
				}
			};
			
		Action SaveAs = new AbstractAction("Save As")
			{
				public void actionPerformed(ActionEvent e)
				{
					saveFileAs();
				}
			};
			
			Action Quit = new AbstractAction("Quit")
					{
						public void actionPerformed(ActionEvent e) 
						{
							saveOld();
							System.exit(0);
						}
					};
			
		Action Open = new AbstractAction("Open",new ImageIcon("open.gif"))
			{
				public void actionPerformed(ActionEvent e)
				{
					saveOld();
					if(dialog.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
					{
						j++;
						area[j] = new JTextPane();
						panel[j] = new JPanel();
						area[j].setFont(new Font("Times New Roman",Font.PLAIN,20));
						scroll[j] = new JScrollPane(area[j],JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
						panel[j].add(scroll[j]);
						
						tabbedPane.add(getFileName(dialog.getSelectedFile().getAbsolutePath()),panel[j]);
						area[j].addKeyListener(k1);
						setVisible(true);
						area[j].setPreferredSize(new Dimension(width,height));
						area[j].requestFocusInWindow();
						area[j].setText("");
						files[j] = dialog.getSelectedFile().getAbsolutePath();
						readInFile(dialog.getSelectedFile().getAbsolutePath());
					}
					SaveAs.setEnabled(true);
				}
			};
			
			Action PageSetup = new AbstractAction("Page Setup")
					{
						public void actionPerformed(ActionEvent e)
						{
							try
							{
								Paper p = pPageFormat.getPaper();
								pPageFormat.setPaper(p);
								int o = pPageFormat.getOrientation();
								pPageFormat.setOrientation(o);
								pPageFormat = pPrinterjob.pageDialog(pPageFormat);
							}
							catch(Exception pex)
							{
								JOptionPane.showMessageDialog(null,"Error while Showing Page Setup");
								pex.printStackTrace();
							}
						}
					};
					
			Action Print = new AbstractAction("Print",new ImageIcon("print.gif"))
			{
				public void actionPerformed(ActionEvent e)
				{
					try
					{
						boolean done = area[k].print();
						if(done)
						{
							JOptionPane.showMessageDialog(null, "Printing is Done");
						}
					}
					catch(Exception pex)
					{
						JOptionPane.showMessageDialog(null, "Error while Printing");
						pex.printStackTrace();
					}
				}
			}; 
			
			Action PrintPreview = new AbstractAction("PrintPreview") {
	            public void actionPerformed(ActionEvent e) {
	                try {
						PrinterJob pj = PrinterJob.getPrinterJob();
						javax.print.attribute.HashPrintRequestAttributeSet att = new javax.print.attribute.HashPrintRequestAttributeSet();
						new PrintPreview(area[k].getPrintable(new MessageFormat(""), new MessageFormat("{0}")), pj.getPageFormat(att));
	                } catch (Exception pex) {
	                    //JOptionPane.showMessageDialog(null, "Error while Showing Print Preview");
	                    pex.printStackTrace();
	                }
	            }
			};
			
			Action Contents = new AbstractAction("Contents")
			{
				public void actionPerformed(ActionEvent e)
				{
					try
					{
						area[j].setFont(new Font("Arial",Font.BOLD,30));
						area[j].setText("\t\t\t\t\t\t\t\t     Universal Document Edior Basic Help \n\n\n"
								+ "\t\t\t\t\t\t\t\t\tSteps how to edit a document \n\n"
								+ "\n1: Browse a file and select it \n"
								+ "\n2: Editor will automatically detect the file format otherwise it will display that file format is not suported \n"
								+ "\n3: It will open the selected file \n"
								+ "\n4: It will ask wheater you have to edit or open for an HTML file \n"
								+ "\n5: After opening the file you can edit the content of the file as your wish \n"
								+ "\n6: In case of PDF fle it will automatically generate the modified file \n"
								+ "\n7: And in case of other then PDF file it will show you the two options whether you have to SAVE or SAVE_AS \n"
								+ "\n8: Then the edited file will  be a modified file ");
						currentFile = "Editor Help";
						changed = false;
						area[j].setEditable(false);
						setTitle(currentFile);
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
			};
			
			Action About = new AbstractAction("About")
			{
				public void actionPerformed(ActionEvent e)
				{
					try
					{
						area[j].setContentType("text");
						area[j].setFont(new Font("Arial",Font.BOLD,30));
						area[j].setText("\t\t\tUniversal Document Editor (UDE) is an Editor that can open almost all different kinds of files.\n\t\t\t\t\t\tCreated by \n\t\t\t\t\tTauseef Ansari (SS15IF015)\n\t\t\t\t\tTaha Shaikh(SS15IF012)\n\t\t\t\t\tSahil Shaikh(SS15IF017)");
						currentFile = "About US";
						changed = false;
						area[j].setEditable(false);
						setTitle(currentFile);
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
			};
	
			private KeyListener k1 = new KeyAdapter()
			{
				public void keyPressed(KeyEvent e)
				{
					changed = true;
					Save.setEnabled(true);
					SaveAs.setEnabled(true);
				}
			};
			
			ActionMap m = area[j].getActionMap();
			Action Cut = m.get(DefaultEditorKit.cutAction);
			Action Copy = m.get(DefaultEditorKit.copyAction);
			Action Paste = m.get(DefaultEditorKit.pasteAction);
			
			ChangeListener changeListener = new ChangeListener() 
			{
				public void stateChanged(ChangeEvent changeEvent)
				{
					JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
					int index = sourceTabbedPane.getSelectedIndex();
					k = 
							Arrays.asList(panel).indexOf((JPanel)sourceTabbedPane.getComponentAt(index));
							currentFile = files[k];
							setTitle(currentFile);
				}
			};
			
			private void saveOld()
			{
				if(changed)
				{
					if(JOptionPane.showConfirmDialog(this,"Would you like to Save this to "+currentFile+" ? ","Save Changes",JOptionPane.YES_NO_OPTION)==
							JOptionPane.YES_OPTION)
							saveFile(currentFile,k);
				}
			}
			
			private void saveFileAs()
			{
				if(dialog.showSaveDialog(null)==JFileChooser.APPROVE_OPTION)
					saveFile(dialog.getSelectedFile().getAbsolutePath(),k);
			}
			
			String readFile(String fileName) throws IOException
			{
				BufferedReader br = new BufferedReader(new FileReader(fileName));
				try
				{
					StringBuilder sb = new StringBuilder();
					String line = br.readLine();
					
					while(line != null)
					{
						sb.append(line);
						sb.append("\n");
						line = br.readLine();
					}
					return sb.toString();
				}
				finally
				{
					br.close();
				}
			}
			
			public void unzip(String zipFilePath,String destDirectory) throws IOException
			{
				try
				{
					ZipFile zipFile = new ZipFile(zipFilePath);
					Enumeration<?> enu = zipFile.entries();
					while(enu.hasMoreElements())
					{
						ZipEntry zipEntry = (ZipEntry) enu.nextElement();
						String name = zipEntry.getName();
						long size = zipEntry.getSize();
						long compressedSize = zipEntry.getCompressedSize();
						File file = new File(destDirectory+"/"+name);
						if(name.endsWith("/"))
						{
							file.mkdirs();
							continue;
						}
						File parent = file.getParentFile();
						if(parent != null)
						{
							parent.mkdirs();
						}
						InputStream is = zipFile.getInputStream(zipEntry);
						FileOutputStream fos = new FileOutputStream(file);
						byte[] bytes = new byte[1024];
						int length;
						while((length = is.read(bytes)) >= 0)
						{
							fos.write(bytes,0,length);
						}
						is.close();
						fos.close();
					}
					zipFile.close();
					area[j].setText("Successfully Extracted");
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			
			private void saveFile(String fileName,int k)
			{
				try 
				{
					area[j].getDocument().putProperty("ZOOM FACTOR", new Double(1.5));
					String ext = getFileExtension(fileName);
					FileWriter w;
					if(ext.equals(""))
						w = new FileWriter(fileName=fileName+".txt");
					else
						w = new FileWriter(fileName);
					if(ext.toLowerCase().equals("pdf"))
					{
						com.itextpdf.text.Document document = new com.itextpdf.text.Document();
						PdfWriter.getInstance(document, new FileOutputStream("Modified.pdf"));
						document.open();
						document.add(new Paragraph(area[k].getText()));
						document.close();
					}
					else
					{
						area[k].write(w);
						w.close();
						setTitle(fileName);
						changed = false;
						Save.setEnabled(false);
					}
				}
				catch(Exception e)
				{
				}
			}
			
			private void readInFile(String fileName)
			{
				try
				{
					FileReader r = new FileReader(fileName);
					String ext = getFileExtension(fileName);
					area[j].setEditable(true);
					if(ext.toLowerCase().equals("txt") || ext.toLowerCase().equals("java") || ext.toLowerCase().equals("c") ||
							 ext.toLowerCase().equals("cpp") || ext.toLowerCase().equals("cs") || ext.toLowerCase().equals("class") ||
							 ext.toLowerCase().equals("csv") || ext.toLowerCase().equals("css") || ext.toLowerCase().equals("ser"))
					{
						area[j].setContentType("text");
						area[j].read(r, null);
						r.close();
						currentFile = fileName;
						changed = false;
						setTitle(currentFile);
					}
					else if(ext.toLowerCase().equals("rtf"))
					{
						area[j].setContentType("text/rtf");
						area[j].read(r, null);
						r.close();
						currentFile = fileName;
						changed = false;
						setTitle(currentFile);
					}
					else if(ext.toLowerCase().equals("html") ||ext.toLowerCase().equals("htm") ||ext.toLowerCase().equals("xml"))
					{
						if(JOptionPane.showConfirmDialog(this, "Would you like to open it like a Web page ?",
								"Web page OR Code",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
						{
							area[j].setContentType("text/html");
							currentFile = fileName;
							String str = readFile(fileName);
							area[j].setText(str);
							area[j].setEditable(false);
							setTitle(currentFile);
						}
						else
						{
							area[j].setContentType("text");
							area[j].read(r, null);
							r.close();
							currentFile = fileName;
							changed = false;
							setTitle(currentFile);
						}
					}
					else if(ext.toLowerCase().equals("jpeg") || ext.toLowerCase().equals("jpg") || ext.toLowerCase().equals("png") ||
							 ext.toLowerCase().equals("gif"))
					{
						area[j].setContentType("text/html");
						currentFile = fileName;
						area[j].insertIcon(new ImageIcon(fileName));
						area[j].setEditable(false);
						setTitle(currentFile);
					}
					else if(ext.toLowerCase().equals("bmp"))
					{
						area[j].setContentType("text/html");
						currentFile = fileName;
						File input = new File(fileName);
						BufferedImage image = ImageIO.read(input);
						fileName.replace(ext, ".jpg");
						File output = new File(fileName);
						ImageIO.write(image, "jpg", output);
						area[j].insertIcon(new ImageIcon(fileName));
						area[j].setEditable(false);
						setTitle(currentFile);
					}
					
					/*else if(ext.toLowerCase().equals("xls") || ext.toLowerCase().equals("xlsx")) 
					{
						//area[j].setContentType("application/msword");
						currentFile = fileName;
						OutputStream outputstream = new org.apache.tika.io.ByteArrayOutputStream();
						Metadata metadata = new Metadata();
						File file = new File(fileName.replace("\\", "/"));
						URL url = file.toURI().toURL();
						InputStream input = TikaInputStream.get(url,metadata);
						ParseContext pcontext = new ParseContext();
						OOXMLParser parser = new OOXMLParser();
						BodyContentHandler handler = new BodyContentHandler(outputstream);
						parser.parse(input, handler, metadata, pcontext);
						area[j].setText(outputstream.toString());
						setTitle(currentFile);
					}*/
					
					else if(ext.toLowerCase().equals("doc") || ext.toLowerCase().equals("docx") || ext.toLowerCase().equals("xls") ||
							 ext.toLowerCase().equals("xlsx") || ext.toLowerCase().equals("ppt") || ext.toLowerCase().equals("pptx") ||
							 ext.toLowerCase().equals("mdb") || ext.toLowerCase().equals("accdb") || ext.toLowerCase().equals("odt") ||
							 ext.toLowerCase().equals("ods") || ext.toLowerCase().equals("odp") || ext.toLowerCase().equals("odb"))
					{
						area[j].setContentType("application/msword");
						currentFile = fileName;
						OutputStream outputstream;
						ParseContext context;
						AutoDetectParser parser;
						Metadata metadata;
						context = new ParseContext();
						parser = new AutoDetectParser();
						context.set(Parser.class, parser);
						outputstream = new ByteArrayOutputStream();
						metadata = new Metadata();
						File file = new File(fileName.replace("\\", "/"));
						URL url = file.toURI().toURL();
						InputStream input = TikaInputStream.get(url, metadata);
						ContentHandler handler = new BodyContentHandler(outputstream);
						parser.parse(input, handler, metadata, context);
						area[j].setText(outputstream.toString());
						setTitle(currentFile);
					}
					else if(ext.toLowerCase().equals("pdf"))
					{
						area[j].setContentType("application/pdf");
						PdfReader reader = new PdfReader(fileName);
						StringBuilder page = new StringBuilder();
						for(int i=1;i<=reader.getNumberOfPages();i++)
							page.append(PdfTextExtractor.getTextFromPage(reader, i));
						area[j].setText(page.toString());
						currentFile = fileName;
						area[j].setEditable(true);
						setTitle(currentFile);
					}
					else if(ext.toLowerCase().equals("zip"))
					{
						String zipFilePath = fileName.replace("\\", "/");
						String destDirectory = null;
						JFileChooser chooser = new JFileChooser();
						chooser.setCurrentDirectory(new java.io.File("."));
						chooser.setDialogTitle("Select Destination for ZIP file");
						chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						chooser.setAcceptAllFileFilterUsed(false);
						if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
						{
							destDirectory = chooser.getSelectedFile().toString().replace("\\", "/");
						}
						else
						{
							JOptionPane.showMessageDialog(this,"Destination Not Selected; Selecting Current Directory by default");
							destDirectory = fileName.substring(0, fileName.lastIndexOf("\\")).replace("\\", "/");
						}
						this.unzip(zipFilePath, destDirectory);
					}
					else
					{
						JOptionPane.showMessageDialog(this, "File Format not Supported","File Format Error",JOptionPane.ERROR_MESSAGE);
					}
				}
				catch(Exception e)
				{
					Toolkit.getDefaultToolkit().beep();
					e.printStackTrace();
					JOptionPane.showMessageDialog(this,"Editor can't find the file called "+fileName);
				}
			}
			
			private static String getFileExtension(String fileName)
			{
				if(fileName.lastIndexOf(".")!=-1 && fileName.lastIndexOf(".")!=0)
				return fileName.substring(fileName.lastIndexOf(".")+1);
				else
				return "";
			}
			
			private static String getFileName(String fileName)
			{
				if(fileName.lastIndexOf("\\")!=-1 && fileName.lastIndexOf("\\")!=0)
				return fileName.substring(fileName.lastIndexOf("\\")+1);
				else
				return "";
			}	
			
	public static void main(String[] arg)
	{
		 new TextEditor();
	}
}