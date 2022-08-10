import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
//I need to get the pixels store them in a matrix
public class MeanFilterSerial{
  public static void meanFilter(int k,int h,int w, BufferedImage img){
    int window =(k-1)/2;
    for(int x =window;x<w-window;x++){
      for(int y = window;y<h-window;y++){
        int alphaT =0;
        int redT = 0;
        int greenT =0;
        int blueT =0;
        int Newp =0;
        //create a k by k window size
        for(int i =x-window;i<=x+window;i++){
          for(int j=y-window;j<=y+window;j++){
            int p = img.getRGB(i,j);
            alphaT = alphaT+((p>>24)& 0xff);
            redT = redT+((p>>16)& 0xff);
            greenT = greenT+((p>>8)& 0xff);
            blueT = blueT+(p & 0xff);
            //System.out.print("Red "+redT+" green "+greenT+" blue "+blueT);
          }
         // System.out.println();
        }
        //new Pixel

        Newp = (alphaT/(k*k)<<24)|(redT/(k*k)<<16)| (greenT/(k*k)<<8)|blueT/(k*k);
        img.setRGB(x, y, Newp);

      }
    }
  }
  public static void main(String args[])throws IOException{
    BufferedImage img = null;
    File f = null;
  //   System.out.println("Enter input file name :");
//     Scanner sc = new Scanner(System.in);
//     String inputImage = sc.nextLine();
//     System.out.println("Enter output file name :");
//     String outputImage = sc.nextLine();
//     System.out.println("Enter windowWidth");
//     int k = sc.nextInt();

    try{
      f = new File("C:\\Users\\MTHTSH062\\Downloads\\pictures\\image1CSC.jpg");
      img = ImageIO.read(f);
    }
    catch(IOException e){
      System.out.println(e);  
    }
    int w = img.getWidth();
    int h= img.getHeight();
    int k =11;
    long startTime = System.currentTimeMillis();
    meanFilter(k, h, w, img);
    long endTime = System.currentTimeMillis();
    System.out.println("Image blur serial took " + (endTime - startTime) + " milliseconds.");
            
    try{
      f = new File("C:\\Users\\MTHTSH062\\Downloads\\pictures\\+outputImage.jpg");
      ImageIO.write(img, "jpg", f);
      }
    catch(IOException e){
       System.out.println(e);
      }
   }
}