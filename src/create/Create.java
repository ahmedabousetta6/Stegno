/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package create;
import javax.swing.JFrame;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.DataBufferByte;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
/**
 *
 * @author PC
 */ 

class MyButton extends JButton {
    private boolean isLastButton;
    public MyButton() {
        super();
        initUI();
        
    }
    public MyButton(Image image) {
        super(new ImageIcon(image));
        initUI();
    }
    public MyButton( BufferedImage image) {
        super(new ImageIcon(image));
        initUI();
    }
    private void initUI() {
        isLastButton = false;
        BorderFactory.createLineBorder(Color.red);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.yellow));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.green));
            }
        });
    }
    public void setLastButton() {
        
        isLastButton = true;
    }

    public boolean isLastButton() {

        return isLastButton;
    }
}
  class Steganography{
           //public boolean encode(String path, String original, String ext1, String stegan, String message)
                  //public boolean encode(String path, String stegan, String message)
                           public boolean encode(BufferedImage b,String stegan, String message)
	{
		//String	file_name = path;
	//	BufferedImage 	image_orig	= getImage(file_name);
                //BufferedImage 	image_orig	= getImage(path);
		
		BufferedImage image = user_space(b);
		image = add_text(image,message);
		
		return(setImage(image,new File(stegan),"png"));
	}
	
	
	
	//public String decode(String path, String name)
                public String decode(String path)
	{
		byte[] decode;
		try
		{
			
			BufferedImage image  = user_space(getImage(path));
			decode = decode_text(get_byte_data(image));
			return(new String(decode));
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, 
				"There is no hidden message in this image!","Error",
				JOptionPane.ERROR_MESSAGE);
                        System.out.println(e.getClass());
			return "";
		}
	}
	
	
	private String image_path(String path, String name, String ext)
	{
		return path + "/" + name + "." + ext;
	}
	
	
	private BufferedImage getImage(String f)
	{
		BufferedImage 	image	= null;
		File 		file 	= new File(f);
		
		try
		{
			image = ImageIO.read(file);
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null, 
				"Image could not be read!","Error",JOptionPane.ERROR_MESSAGE);
                        System.out.println(ex.getClass());
		}
		return image;
	}
	
	
	private boolean setImage(BufferedImage image, File file, String ext)
	{
		try
		{
			file.delete(); //delete resources used by the File
			ImageIO.write(image,ext,file);
			return true;
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, 
				"File could not be saved!","Error",JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	
	
	private BufferedImage add_text(BufferedImage image, String text)
	{
		//convert all items to byte arrays: image, message, message length
		byte img[]  = get_byte_data(image);
		byte msg[] = text.getBytes();
		byte len[]   = bit_conversion(msg.length);
		try
		{
			encode_text(img, len,  0); //0 first positiong
			encode_text(img, msg, 32); //4 bytes of space for length: 4bytes*8bit = 32 bits
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, 
"Target File cannot hold message!", "Error",JOptionPane.ERROR_MESSAGE);
		}
		return image;
	}
	
	
	private BufferedImage user_space(BufferedImage image)
	{
		//create new_img with the attributes of image
		BufferedImage new_img  = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D	graphics = new_img.createGraphics();
		graphics.drawRenderedImage(image, null);
		graphics.dispose(); //release all allocated memory for this image
		return new_img;
	}
	
	
	private byte[] get_byte_data(BufferedImage image)
	{
		WritableRaster raster   = image.getRaster();
		DataBufferByte buffer = (DataBufferByte)raster.getDataBuffer();
		return buffer.getData();
	}
	
	
	private byte[] bit_conversion(int i)
	{
		//originally integers (ints) cast into bytes
		//byte byte7 = (byte)((i & 0xFF00000000000000L) >>> 56);
		//byte byte6 = (byte)((i & 0x00FF000000000000L) >>> 48);
		//byte byte5 = (byte)((i & 0x0000FF0000000000L) >>> 40);
		//byte byte4 = (byte)((i & 0x000000FF00000000L) >>> 32);
		
		//only using 4 bytes
		byte byte3 = (byte)((i & 0xFF000000) >>> 24); //0
		byte byte2 = (byte)((i & 0x00FF0000) >>> 16); //0
		byte byte1 = (byte)((i & 0x0000FF00) >>> 8 ); //0
		byte byte0 = (byte)((i & 0x000000FF)	   );
		//{0,0,0,byte0} is equivalent, since all shifts >=8 will be 0
		return(new byte[]{byte3,byte2,byte1,byte0});
	}
	
	
	private byte[] encode_text(byte[] image, byte[] addition, int offset)
	{
		//check that the data + offset will fit in the image
		if(addition.length + offset > image.length)
		{
			throw new IllegalArgumentException("File not long enough!");
		}
		//loop through each addition byte
		for(int i=0; i<addition.length; ++i)
		{
			//loop through the 8 bits of each byte
			int add = addition[i];
			for(int bit=7; bit>=0; --bit, ++offset) //ensure the new offset value carries on through both loops
			{
				//assign an integer to b, shifted by bit spaces AND 1
				//a single bit of the current byte
				int b = (add >>> bit) & 1;
				//assign the bit by taking: [(previous byte value) AND 0xfe] OR bit to add
				//changes the last bit of the byte in the image to be the bit of addition
				image[offset] = (byte)((image[offset] & 0xFE) | b );
			}
		}
		return image;
	}
	
	
	private byte[] decode_text(byte[] image)
	{
		int length = 0;
		int offset  = 32;
		//loop through 32 bytes of data to determine text length
		for(int i=0; i<32; ++i) //i=24 will also work, as only the 4th byte contains real data
		{
			length = (length << 1) | (image[i] & 1);
		}
		
		byte[] result = new byte[length];
		
		//loop through each byte of text
		for(int b=0; b<result.length; ++b )
		{
			//loop through each bit within a byte of text
			for(int i=0; i<8; ++i, ++offset)
			{
				//assign bit: [(new byte value) << 1] OR [(text byte) AND 1]
				result[b] = (byte)((result[b] << 1) | (image[offset] & 1));
			}
		}
		return result;
	}}
public class Create extends JFrame{

Steganography s=new Steganography() ;
private JPanel panel;
private JPanel panel_2;
    private BufferedImage source,first_source;
    private BufferedImage resized;    
    private BufferedImage resized_2; 
    BufferedImage bimg_2;
    private Image image;
     private Image image_2;
    private MyButton lastButton;
    private MyButton lastButton_2;
    private int width, height;    
    int h ;
    private List<MyButton> buttons;
    private List<MyButton> buttons_2;
    private List<Point> solution;
    private List<Point> solution_1 = new ArrayList<>();
    private List<Point> check= new ArrayList<>();
    Point current;
    Image img,img_2;
       JButton button2 ;
       JPanel buttonCenter;
    private final int NUMBER_OF_BUTTONS = 36;//12
    private final int NUMBER_OF_BUTTONS_2 = 12;
    private final int DESIRED_WIDTH = 650;//1650
     int tries=7;
 public static String line1;
ArrayList<Integer> random_arr=new ArrayList<Integer>();
Boolean random_bool=false;
Boolean passed;
boolean encr_path=true;
String encr;
String hidden;
     

 
    public Create(){
        //this.passed = false;

        initUI();
        
        //f2.setVisible(true);
        // initUI_2();
      
    }

    private void initUI() {

        buttons = new ArrayList<>();
        

        panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        panel.setLayout(new GridLayout(12,9, 0, 0));

     
for(int a=1;a<4;a++){
    try {
            source = loadImage();
             h = getNewHeight(source.getWidth(), source.getHeight());
            if(a==1)
                first_source=source;          

  
            resized = resizeImage(source, DESIRED_WIDTH, h,
                    BufferedImage.TYPE_INT_ARGB);}

        catch (IOException ex) {
            Logger.getLogger(Create.class.getName()).log(
                    Level.SEVERE, null, ex);
        }

        width = resized.getWidth(null);
        height = resized.getHeight(null);

        add(panel, BorderLayout.CENTER);
        for (int i = 0; i < 4; i++) {

            for (int j = 0; j < 3; j++) {

                image = createImage(new FilteredImageSource(resized.getSource(),
                        new CropImageFilter(j * width / 3, i * height / 4,
                                (width / 3), height / 4)));
                
                MyButton button = new MyButton(image);
                int rand_1,rand_2;
                int random=(int)(Math.random()*3);
                rand_1=random;
                random=(int)(Math.random()*3);
                rand_2=random;
                
                button.putClientProperty("position", new Point(i*(rand_1+1), j*(rand_2+1)));
                current=(Point)button.getClientProperty("position");
                if(a==1){
                        
                        solution_1.add(current);

                }

                if (i == 13 && j == 2) {
                    lastButton = new MyButton();
                    lastButton.setBorderPainted(false);
                    lastButton.setContentAreaFilled(false);
                    lastButton.setLastButton();
                    lastButton.putClientProperty("position", new Point(i, j));
                } else {
                    buttons.add(button);
                   
                }
            }
        }}
          
        
        Collections.shuffle(buttons);
        buttons.add(lastButton);
      

        for (int i = 0; i < NUMBER_OF_BUTTONS; i++) {

            MyButton btn = buttons.get(i);
            panel.add(btn);
            btn.setBorder(BorderFactory.createLineBorder(Color.blue));
            btn.addActionListener(new ClickAction());
        }
       
       

       JButton button1=new JButton();
          Icon str1 = null;
        //JButton
                button2 = new JButton(str1);
        //JPanel
                buttonCenter = new JPanel( new FlowLayout(FlowLayout.CENTER) );
     //   JPanel counter = new JPanel( new FlowLayout(FlowLayout.RIGHT) );
        
          button1.setIcon(new ImageIcon(img));
    
        buttonCenter.add(button1);
       // counter.add(button2);
        add(buttonCenter, BorderLayout.EAST);
        //add(counter, BorderLayout.AFTER_LAST_LINE);
       // add(panel, 10,20);
       
        pack();        
        setTitle("Puzzle");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     
        
       
      
        
    }
    //puzzle 
    private void initUI_2() {
  
        button2.removeAll();
        buttonCenter.removeAll();
        panel.removeAll();
        panel_2 = new JPanel();
        panel_2.setBorder(BorderFactory.createLineBorder(Color.black));
        panel_2.setLayout(new GridLayout(4, 3, 0, 0));
        solution = new ArrayList<>();
        
        solution.add(new Point(0, 0));
        solution.add(new Point(0, 1));
        solution.add(new Point(0, 2));
        solution.add(new Point(1, 0));
        solution.add(new Point(1, 1));
        solution.add(new Point(1, 2));
        solution.add(new Point(2, 0));
        solution.add(new Point(2, 1));
        solution.add(new Point(2, 2));
        solution.add(new Point(3, 0));
        solution.add(new Point(3, 1));
        solution.add(new Point(3, 2));

        buttons_2 = new ArrayList<>();

        
       

        try {           
            int h = getNewHeight(bimg_2.getWidth(), first_source.getHeight());
            resized_2 = resizeImage(bimg_2, DESIRED_WIDTH, h,
                    BufferedImage.TYPE_INT_ARGB);

        } catch (IOException ex) {
            Logger.getLogger(Create.class.getName()).log(
                    Level.SEVERE, null, ex);
        }

        width = resized_2.getWidth(null);
        height = resized_2.getHeight(null);

        add(panel_2, BorderLayout.CENTER);

        for (int i = 0; i < 4; i++) {

            for (int j = 0; j < 3; j++) {

                image_2 = createImage(new FilteredImageSource(resized_2.getSource(),
                        new CropImageFilter(j * width / 3, i * height / 4,
                                (width / 3), height / 4)));
                
                MyButton button = new MyButton(image_2);
                button.putClientProperty("position", new Point(i, j));

                if (i == 3 && j == 2) {
                    lastButton_2 = new MyButton();
                    lastButton_2.setBorderPainted(false);
                    lastButton_2.setContentAreaFilled(false);
                    lastButton_2.setLastButton();
                    lastButton_2.putClientProperty("position", new Point(i, j));
                } else {
                    buttons_2.add(button);
                }
            }
        }

        Collections.shuffle(buttons_2);
        buttons_2.add(lastButton_2);

        for (int i = 0; i < 11; i++) {

            MyButton btn = buttons_2.get(i);
            panel_2.add(btn);
            btn.setBorder(BorderFactory.createLineBorder(Color.blue));
            btn.addActionListener(new ClickAction_2());
            
        }

        pack();
        setTitle("Puzzle");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
    
    
    

    private int getNewHeight(int w, int h) {

        double ratio = DESIRED_WIDTH / (double) w;
        int newHeight = (int) (h * ratio);
        return newHeight;
    }

   private BufferedImage loadImage() throws IOException {

      int random=(int)(Math.random()*3);
      while(random_bool.equals(false))
      {for(int i=0;i<random_arr.size();i++){
          
          if(random_arr.get(i).equals(random))
          {
            random_bool=true;  
          break;
          }}
      if(random_bool==false){
          random_arr.add(random);
          random_bool=true;
          }
      else{
      random=(int)(Math.random()*3);
      random_bool=false;}
      
      
      }
      random_bool=false;
       
          int random1=random+1;
  
       String l=random1+".jpg";
             if( encr_path==true)
             {encr=random1+".png";
             ClassLoader cl = getClass().getClassLoader();
    File file = new File(cl.getResource(encr).getFile());
           bimg_2 =ImageIO.read( file);
           s.encode(bimg_2,cl.getResource(encr).getFile() , line1);
            hidden= s.decode(cl.getResource(encr).getFile());
            encr_path=false;
             }
              
    
       BufferedImage bimg = ImageIO.read(new File(l));
       
      
       String wanted=random_arr.get(0)+1+".jpg";
       String wanted_2=random_arr.get(0)+1+".png";
      
       img = ImageIO.read(new File(wanted));
    
       
      
      
        return bimg;
   
        
   }
   
    private BufferedImage resizeImage(BufferedImage originalImage, int width,
            int height, int type) throws IOException {

        BufferedImage resizedImage = new BufferedImage(width, height, type);
        
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
         g.dispose();
        return resizedImage;
    }

    private class ClickAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(tries>0){
           
           checkSolution(e);}
           
            else{ JOptionPane.showMessageDialog(panel, "unfortunately uou chosed wrong pieces 7 times so, the game is over",
                    "Next time",JOptionPane.INFORMATION_MESSAGE);
           System.exit(0);
            }
            
        }
       

      

    private void checkSolution(ActionEvent e) {

        boolean a=false,b=false;
         JButton button = (JButton) e.getSource();
         current = (Point)button.getClientProperty("position");
             
  
        for(int i=0;i<solution_1.size();i++)
        {  if(current==solution_1.get(i)){
             //System.out.println(current);
            a=true;
            break;  
            }}
        if(a==true){
            for(int i=0;i<check.size();i++){
                if(current==check.get(i))
                {
                   
                    b=true;
                break;
                }}
                if(b==true)
                    a=false;
                            else{
            check.add(current);
            a=false;}
        }
        else{
            tries--;
             JOptionPane.showMessageDialog(panel,"You have: "+tries+"  wrong choices left"+"\n"+"Good luck" ,
                   "Wrong choice"  ,JOptionPane.INFORMATION_MESSAGE);}
            
        if(check.size()==solution_1.size()){
            
            JOptionPane.showMessageDialog(panel, "Finished" ,
                    "Congratulation",JOptionPane.INFORMATION_MESSAGE);
            initUI_2();

            }
       
  
        }
 
    
    }
      private class ClickAction_2 extends AbstractAction {
         
        
        @Override
        public void actionPerformed(ActionEvent e) {

            checkButton(e);
            checkSolution();
        }

        private void checkButton(ActionEvent e) {

            int lidx = 0;
            
            for (MyButton button : buttons_2) {
                if (button.isLastButton()) {
                    lidx = buttons_2.indexOf(button);
                }
            }

            JButton button = (JButton) e.getSource();
            int bidx = buttons_2.indexOf(button);

            if ((bidx - 1 == lidx) || (bidx + 1 == lidx)
                    || (bidx - 3 == lidx) || (bidx + 3 == lidx)) {
                Collections.swap(buttons_2, bidx, lidx);
                updateButtons();
            }
        }

        private void updateButtons() {

            panel_2.removeAll();

            buttons_2.forEach((btn) -> {
                panel_2.add(btn);
            });

            panel_2.validate();
        }
    }

    private void checkSolution() {

        List<Point> current_2 = new ArrayList<>();

        buttons_2.forEach((btn) -> {
            current_2.add((Point) btn.getClientProperty("position"));
        });

        if (compareList(solution, current_2)) {
            JOptionPane.showMessageDialog(panel, "Finished"+"\n"+"The hidden ward was:"+hidden,
                    "Congratulation",JOptionPane.INFORMATION_MESSAGE);
            encr_path=false;
            this.hide();
             Category start=new Category();
         start.setVisible(true);
            
            
        }
    }
    
    
    
    

    public static boolean compareList(List ls1, List ls2) {
        
        return ls1.toString().contentEquals(ls2.toString());
    }
    
   static  JFrame f1=new NewCategory();
   
    
   
    public static void main(String[] args) {

       f1.setVisible(true);
       
       
        
        
    }}
    

