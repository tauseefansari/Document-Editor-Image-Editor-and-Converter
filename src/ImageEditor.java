import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.image.*;
import javax.swing.filechooser.*;
import java.io.*;
import javax.imageio.*;

public class ImageEditor extends Canvas 
{ 
	 Image orImg;
	 BufferedImage orBufferedImage,bimg,bimg1; 
	 float e,radian;
	 Dimension ds;
	 int mX,mY,x,y;
	 static boolean imageLoaded;
	 boolean actionSlided,actionResized,actionTransparent,actionRotated,actionDraw,drawn;
	 MediaTracker mt;
	 static Color c;
	 Color colorTextDraw;
	 Robot rb;
	 boolean dirHor;
	 String imgFileName,fontName,textToDraw;
	 int fontSize;
	 public ImageEditor(){
	   addMouseListener(new Mousexy());
	   addKeyListener(new KList());
	   try{
	    rb=new Robot();
	   }catch(AWTException e){}
	   
	   ds=getToolkit().getScreenSize();
	   mX=(int)ds.getWidth()/2;
	   mY=(int)ds.getHeight()/2;
	  }
	  
	  public void paint(Graphics g){
	   Graphics2D g2d=(Graphics2D)g;   
	  
	   if(imageLoaded){
	    if(actionSlided || actionResized || actionTransparent || actionRotated || drawn ){
	     x=mX-bimg.getWidth()/2;
	     y=mY-bimg.getHeight()/2;
	     g2d.translate(x,y);  
	     g2d.drawImage(bimg,0,0,null);
	     
	     }
	    else{
	     x=mX-orBufferedImage.getWidth()/2;
	     y=mY-orBufferedImage.getHeight()/2;
	     g2d.translate(x,y);
	     g2d.drawImage(orBufferedImage,0,0,null);
	     }
	   }
	   g2d.dispose();
	  }

	  class Mousexy extends MouseAdapter{
	   
	   public void mousePressed(MouseEvent e){
	    Color color=rb.getPixelColor(e.getX(),e.getY());
	    try{    
	    setColor(color);
	    if(actionDraw){
	     if(actionSlided || actionResized || actionTransparent || actionRotated || drawn)
	      addTextToImage(e.getX()-x,e.getY()-y, bimg);
	     else
	      addTextToImage(e.getX()-x,e.getY()-y, orBufferedImage);
	     }
	    }catch(Exception ie){}
	   }
	  }
	  
	 class KList extends KeyAdapter{
	  public void keyPressed(KeyEvent e){
	   if(e.getKeyCode()==27){ //ESC Key
	    actionDraw=false;
	    textToDraw="";
	    fontName="";
	    fontSize=0;
	    }
	   }
	  }
	 
	 public void addTextToImage(int x,int y, BufferedImage img){
	  BufferedImage bi=(BufferedImage)createImage(img.getWidth(),img.getHeight());  
	  Graphics2D  g2d=(Graphics2D)bi.createGraphics();
	  g2d.setFont(new Font(fontName,Font.BOLD,fontSize));
	  g2d.setPaint(colorTextDraw);
	  g2d.drawImage(img,0,0,null);
	  g2d.drawString(textToDraw,x,y);
	  bimg=bi;
	  drawn=true;  
	  g2d.dispose();
	  repaint(); 
	  }

	  public void setColor(Color color){
	   c=color;   
	  }
	  
	  public void setImgFileName(String fname){
	   imgFileName=fname;
	  }
	  
	  public void initialize(){
	   imageLoaded=false; 
	   actionSlided=false;
	   actionResized=false;
	   actionTransparent=false;
	   actionRotated=false;
	   actionDraw=false;
	   drawn=false;
	   dirHor=false;
	   c=null;
	   radian=0.0f;
	   e=0.0f;
	   }

	  public void reset(){
	   if(imageLoaded){
	   prepareImage(imgFileName);
	   repaint();
	   }
	  }

	  public void makeImageRotate(BufferedImage image,int w,int h){
	   BufferedImage bi=(BufferedImage)createImage(w,h);
	   Graphics2D  g2d=(Graphics2D)bi.createGraphics(); 
	   radian=(float)Math.PI/2; //angle     
	   g2d.translate(w/2,h/2);
	   g2d.rotate(radian);
	   g2d.translate(-h/2,-w/2);
	   g2d.drawImage(image,0,0,null);
	   bimg=bi;
	   g2d.dispose();
	  }

	  public void rotateImage(){
	    BufferedImage bi;
	    if(actionSlided || actionResized || actionTransparent || actionRotated || drawn){
	     bi=bimg;     
	    } 
	    else{
	     bi=orBufferedImage;
	    }
	    makeImageRotate(bi,bi.getHeight(),bi.getWidth());
	    actionRotated=true;
	    repaint();
	   }

	  public void resizeImage(int w,int h){
	    BufferedImage bi=(BufferedImage)createImage(w,h);
	    Graphics2D g2d=(Graphics2D)bi.createGraphics();
	    if(actionSlided || actionTransparent || actionRotated ||drawn)
	     g2d.drawImage(bimg,0,0,w,h,null);
	    else
	     g2d.drawImage(orImg,0,0,w,h,null);
	    bimg=bi;
	    g2d.dispose();  
	  }

	  public void prepareImage(String filename){
	   initialize();
	   try{
	   mt=new MediaTracker(this);    
	   orImg=Toolkit.getDefaultToolkit().getImage(filename); 
	   mt.addImage(orImg,0);
	    mt.waitForID(0); 
	   int width=orImg.getWidth(null);
	   int height=orImg.getHeight(null);
	   orBufferedImage=createBufferedImageFromImage(orImg,width,height,false);  
	   bimg = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);  
	   imageLoaded=true;
	   }catch(Exception e){System.exit(-1);}
	  }

	  public void filterImage(){
	   float[] elements = {0.0f, 1.0f, 0.0f, -1.0f,e,1.0f,0.0f,0.0f,0.0f}; 
	   Kernel kernel = new Kernel(3, 3, elements);
	   ConvolveOp cop = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
	   bimg= new BufferedImage(orBufferedImage.getWidth(),orBufferedImage.getHeight(),BufferedImage.TYPE_INT_RGB);
	   cop.filter(orBufferedImage,bimg); 	   
	  }

	  public void setValue(float value){ 
	   e=value;
	  }

	  public void setActionSlided(boolean value ){ 
	   actionSlided=value;
	  } 
	  
	  public void setActionResized(boolean value ){ 
	   actionResized=value;
	  }
	  
	  public void setActionDraw(boolean value ){ 
	   actionDraw=value; 
	  }

	 public BufferedImage createBufferedImageFromImage(Image image, int width, int height, boolean tran)
	   { BufferedImage dest ;
	  if(tran) 
	       dest = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	  else
	   dest = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	       Graphics2D g2 = dest.createGraphics();
	       g2.drawImage(image, 0, 0, null);
	       g2.dispose();
	       return dest;
	   }

	 public void makeTransparency(final Color col){
	  ImageFilter filter = new RGBImageFilter() {
	         int imageRGB = col.getRGB();

	         public final int filterRGB(int x, int y, int rgb) {
	            if (rgb==imageRGB ) {
	              
	               return 0x00FFFFFF & rgb;
	              }
	            else {
	               return rgb;
	              }
	          }
	       }; 
	  ImageProducer ip;
	  if(actionSlided || actionResized)
	   ip = new FilteredImageSource(bimg.getSource(), filter);
	  else
	   ip = new FilteredImageSource(orImg.getSource(), filter);
	     Image img=getToolkit().createImage(ip);
	  try{
	   mt.addImage(img,0);
	   mt.waitForID(0);
	   bimg=createBufferedImageFromImage(img,img.getWidth(null),img.getHeight(null),true);
	   actionTransparent=true;
	   repaint();
	  }catch(Exception e){} 
	 }

	 public void saveToFile(String filename){
	  String ftype=filename.substring(filename.lastIndexOf('.')+1);
	  try{
	   if(actionSlided || actionResized || actionTransparent || actionRotated || drawn)
	    ImageIO.write(bimg,ftype,new File(filename));
	     }catch(IOException e){System.out.println("Error in saving the file");}
	  }

	 public void setText(String text,String fName, int fSize, Color color){
	   textToDraw=text;
	   fontName=fName;
	   fontSize=fSize;
	   if(color==null)
	    colorTextDraw=new Color(0,0,0);
	   else
	    colorTextDraw=color;
	  }
	}

	class  Main extends JFrame implements ActionListener{ 
	 ImageEditor ie;
	 JFileChooser chooser; 
	 JMenuBar mainmenu;
	 JMenu menu;
	 JMenu editmenu;
	 JMenuItem mopen;
	 JMenuItem msaveas;
	 JMenuItem msave;
	 JMenuItem mexit; 
	 JMenuItem mbright; 
	 JMenuItem mcrop;
	 JMenuItem mresize;
	 JMenuItem mrotate;
	 JMenuItem mtransparent;
	 JMenuItem maddtext;
	 JMenuItem mcancel;
	 String filename;
	 Main(){
	  ie=new ImageEditor();
	  Container cont=getContentPane();
	  cont.add(ie,BorderLayout.CENTER );  
	  mainmenu=new JMenuBar();
	  menu=new JMenu("File");
	  menu.setMnemonic(KeyEvent.VK_F);

	  mopen=new JMenuItem("Open...");
	  mopen.setMnemonic(KeyEvent.VK_O);
	  mopen.addActionListener(this);

	  msaveas=new JMenuItem("Save as...");
	  msaveas.setMnemonic(KeyEvent.VK_S);
	  msaveas.addActionListener(this);

	  msave=new JMenuItem("Save");
	  msave.setMnemonic(KeyEvent.VK_V);
	  msave.addActionListener(this);  

	  mexit=new JMenuItem("Exit");
	  mexit.setMnemonic(KeyEvent.VK_X);
	  mexit.addActionListener(this);
	  menu.add(mopen);
	  menu.add(msaveas);
	  menu.add(msave);
	  menu.add(mexit);  

	  editmenu=new JMenu("Edit");
	  editmenu.setMnemonic(KeyEvent.VK_E);
	  
	  mbright=new JMenuItem("Image Brightness");
	  mbright.setMnemonic(KeyEvent.VK_B);
	  mbright.addActionListener(this);

	  maddtext=new JMenuItem("Add Text on Image");
	  maddtext.setMnemonic(KeyEvent.VK_A);
	  maddtext.addActionListener(this);  

	  mresize=new JMenuItem("Image Resize");
	  mresize.setMnemonic(KeyEvent.VK_R);
	  mresize.addActionListener(this);

	  mrotate=new JMenuItem("Image Rotation");
	  mrotate.setMnemonic(KeyEvent.VK_T);
	  mrotate.addActionListener(this);

	  mtransparent=new JMenuItem("Image Transparency");
	  mtransparent.setMnemonic(KeyEvent.VK_T);
	  mtransparent.addActionListener(this);
	 
	  mcancel=new JMenuItem("Cancel / Reset Editing");
	  mcancel.setMnemonic(KeyEvent.VK_L);
	  mcancel.addActionListener(this);

	  editmenu.add(maddtext);
	  editmenu.add(mbright);
	  editmenu.add(mresize);
	  editmenu.add(mrotate);
	  editmenu.add(mtransparent);
	  editmenu.add(mcancel);
	  
	  mainmenu.add(menu);
	  mainmenu.add(editmenu);
	  setJMenuBar(mainmenu);
	 
	  setTitle("Image Editor");
	  setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	  setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH);
	     setVisible(true); 

	  chooser = new JFileChooser(System.getProperty("user.dir"));
	      FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "jpg", "gif","bmp","png");
	      chooser.setFileFilter(filter);
	      chooser.setMultiSelectionEnabled(false);
	  enableSaving(false);
	  ie.requestFocus();
	  }

	 public class ImageBrightness extends JFrame implements ChangeListener{
	  JSlider slider;
	 
	  ImageBrightness(){
	  addWindowListener(new WindowAdapter(){
	     public void windowClosing(WindowEvent e){
	      dispose();
	     }
	    });
	  Container cont=getContentPane();  
	  slider=new JSlider(-10,10,0); 
	  slider.setEnabled(false);
	  slider.addChangeListener(this);
	  cont.add(slider,BorderLayout.CENTER); 
	  slider.setEnabled(true);
	  setTitle("Image brightness");
	  setPreferredSize(new Dimension(300,100));
	  setLocationRelativeTo(null);
	  setVisible(true);
	  pack();
	  enableSlider(false);
	  }
	  public void enableSlider(boolean enabled){
	   slider.setEnabled(enabled);
	  }
	  public void stateChanged(ChangeEvent e){
	    ie.setValue(slider.getValue()/10.0f);
	    ie.setActionSlided(true);   
	    ie.filterImage();
	    ie.repaint();
	    enableSaving(true);
	  }
	 }

	 public class ImageResize extends JFrame implements ActionListener {
	  JPanel panel;
	  JTextField txtWidth;
	  JTextField txtHeight;
	  JButton btOK;
	  ImageResize(){
	  setTitle("Image resize");
	  setPreferredSize(new Dimension(400,100));
	  
	  btOK=new JButton("OK");
	  btOK.setBackground(Color.BLUE);
	  btOK.setForeground(Color.RED);  
	  btOK.addActionListener(this);

	  txtWidth=new JTextField(4);
	  txtWidth.addKeyListener(new KeyList());
	  txtHeight=new JTextField(4);
	  txtHeight.addKeyListener(new KeyList());
	  panel=new JPanel();
	  panel.setLayout(new FlowLayout());
	  panel.add(new JLabel("Width:"));
	  panel.add(txtWidth);
	  panel.add(new JLabel("Height:"));
	  
	  panel.add(txtHeight);
	  panel.add(btOK);
	  panel.setBackground(Color.cyan);
	  add(panel, BorderLayout.CENTER);
	  setLocationRelativeTo(null);
	  setVisible(true);
	  pack();
	  enableComponents(false);
	  }

	  public void enableComponents(boolean enabled){
	   txtWidth.setEnabled(enabled);
	   txtHeight.setEnabled(enabled);
	   btOK.setEnabled(enabled);
	  }

	  public void actionPerformed(ActionEvent e){
	   if(e.getSource()==btOK){
	    ie.setActionResized(true);     
	    ie.resizeImage(Integer.parseInt(txtWidth.getText()),Integer.parseInt(txtHeight.getText()));
	    enableSaving(true);
	    ie.repaint();
	    }
	  }

	  public class KeyList extends KeyAdapter{
	     public void keyTyped(KeyEvent ke){
	 
	    char c = ke.getKeyChar(); 
	    int intkey=(int)c;
	    if(!(intkey>=48 && intkey<=57 || intkey==8 || intkey==127))
	     {
	     ke.consume();
	      }  
	   }
	  } 
	 }
	
	 public class TextAdd extends JFrame implements ActionListener {
	  JPanel panel;
	  JTextArea txtText;
	  JComboBox cbFontNames;
	  JComboBox cbFontSizes;
	  JButton btOK;
	  JButton btSetColor;
	  String seFontName;
	  Color colorText;
	  int seFontSize;
	  TextAdd(){
	  colorText=null;
	  setTitle("Add text to the image");
	  setPreferredSize(new Dimension(400,150));
	  
	  btOK=new JButton("OK");
	  btOK.setBackground(Color.LIGHT_GRAY);
	  btOK.setForeground(Color.RED);
	  btOK.addActionListener(this);

	  btSetColor=new JButton("Set text color");
	  btSetColor.setBackground(Color.BLACK);
	  btSetColor.setForeground(Color.WHITE);  
	  btSetColor.addActionListener(this);

	  txtText=new JTextArea(1,30);
	  cbFontNames=new JComboBox();
	  cbFontSizes=new JComboBox();
	  panel=new JPanel();
	  panel.setLayout(new GridLayout(4,1));
	  panel.add(new JLabel("Text:"));
	  panel.add(txtText);
	  panel.add(new JLabel("Font Name:"));  
	  panel.add(cbFontNames);
	  panel.add(new JLabel("Font Size:"));  
	  panel.add(cbFontSizes);
	  panel.add(btSetColor);
	  panel.add(btOK);
	  panel.setBackground(Color.cyan);
	  add(panel, BorderLayout.CENTER);
	  setLocationRelativeTo(null);
	  setVisible(true);
	  pack();
	  listFonts();
	  }

	  
	  public void actionPerformed(ActionEvent e){
	   if(e.getSource()==btOK){
	    ie.setActionDraw(true); 
	    String textDraw=txtText.getText(); 
	    String fontName=cbFontNames.getSelectedItem().toString();
	    int fontSize=Integer.parseInt(cbFontSizes.getSelectedItem().toString());
	    ie.setText(textDraw,fontName,fontSize,colorText);
	    dispose();
	    }
	   else if(e.getSource()==btSetColor){
	    JColorChooser jser=new JColorChooser();   
	    colorText=jser.showDialog(this,"Color Chooser",Color.BLACK); 
	   }
	  }
	   
	  public void listFonts(){
	   GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment(); 
	   String[] fonts=ge.getAvailableFontFamilyNames();
	   for(String f:fonts)
	    cbFontNames.addItem(f);
	   for(int i=8;i<50;i++)
	    cbFontSizes.addItem(i);
	  }
	 }

	 public void actionPerformed(ActionEvent e){

	  JMenuItem source = (JMenuItem)(e.getSource());
	  if(source.getText().compareTo("Open...")==0)
	    {
	    setImage();
	    ie.repaint();
	    validate();
	     }
	  else if(source.getText().compareTo("Save as...")==0)
	    {
	    showSaveFileDialog();
	     }
	  else if(source.getText().compareTo("Save")==0)
	    {
	    ie.saveToFile(filename);  
	     }
	  else if(source.getText().compareTo("Add Text on Image")==0)
	    {
	    new TextAdd(); 
	    }
	  else if(source.getText().compareTo("Image Brightness")==0)
	    {
	    ImageBrightness ib=new ImageBrightness(); 
	    if(ImageEditor.imageLoaded)
	     ib.enableSlider(true); 
	     }
	  else if(source.getText().compareTo("Image Resize")==0)
	    {
	    ImageResize ir=new ImageResize();
	    if(ImageEditor.imageLoaded)
	     ir.enableComponents(true);  
	     }
	  else if(source.getText().compareTo("Image Rotation")==0)
	    {
	    if(ImageEditor.imageLoaded){
	     ie.rotateImage();
	     enableSaving(true);
	     } 
	    }
	  else if(source.getText().compareTo("Image Transparency")==0){
	   if(ImageEditor.c==null){
	    JOptionPane dialog=new JOptionPane();
	    dialog.showMessageDialog(this,"Click the background area of the image first","Error",JOptionPane.ERROR_MESSAGE);
	   }
	   else if(ImageEditor.imageLoaded){
	    ie.makeTransparency(ImageEditor.c);
	    enableSaving(true);
	    }
	  } 
	  else if(source.getText().compareTo("Cancel / Reset Editing")==0) {
	    ie.setImgFileName(filename);
	    ie.reset();
	    }
	  else if(source.getText().compareTo("Exit")==0) 
	    System.exit(0);
	  } 

	 public void setImage(){
	  int returnVal = chooser.showOpenDialog(this);
	      if(returnVal == JFileChooser.APPROVE_OPTION) {   
	   filename=chooser.getSelectedFile().toString();
	   ie.prepareImage(filename);
	   }     
	  }

	 public void showSaveFileDialog(){
	       int returnVal = chooser.showSaveDialog(this);
	      if(returnVal == JFileChooser.APPROVE_OPTION) {  
	   String filen=chooser.getSelectedFile().toString(); 
	                ie.saveToFile(filen);
	            }
	   }

	 public void enableSaving(boolean f){
	  msaveas.setEnabled(f);
	  msave.setEnabled(f); 
	  }

	 public static void main(String args[]){
	       new Main();
	 }
	}