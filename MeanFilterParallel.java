
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MeanFilterParallel extends RecursiveAction{
    private static int k;
    private static  int h;
    private static int w;
    private static int hi;
    private static int lo;
    public static BufferedImage img;
    static final int SEQUENTIAL_CUTOFF= 100;
    // Processing window size; should be odd.
    public static int window;
    public static int tH;
    public static int tW;
    public static int width;
  
    public MeanFilterParallel (int low, int high ,int height,int key, BufferedImage image) {
       k = key;
       h = height;
       img = image;
       hi=high;
       lo =low;
       width =img.getWidth();
    }
      //  System.out.println(" h "+h+" w "+w);
//        System.out.println("starty "+startY+" startx "+startX);
//        System.out.println("image.getHeight()-starty "+(image.getHeight()-startY)+" image.getWidth()-startx "+(image.getWidth()-startX));
  
    protected void compute() {
        System.out.println("Compute method => img-hi "+(img.getWidth()-hi)+" squential_cutoff "+SEQUENTIAL_CUTOFF);
        if ((hi*h) < SEQUENTIAL_CUTOFF) {
          int window =(k-1)/2;
          System.out.println("lo "+lo+" hi "+hi);
          for(int x =lo; x<hi; x++){
          //System.out.println("startX "+startX+" startY "+startY);
              for(int y = 0; y<h; y++){    
                  //System.out.println("X "+x+" Y "+y);
                  int alphaT =0;
                  int redT = 0;
                  int greenT =0;
                  int blueT =0;
                  int Newp =0;
    
                  for(int i =0;i<=window;i++){
                      for(int j=0;j<=window;j++){
                        if (x + i < width && y + j < h) {
                          int p = img.getRGB(i,j);
                          alphaT = alphaT+((p>>24)& 0xff);
                          redT = redT+((p>>16)& 0xff);
                          greenT = greenT+((p>>8)& 0xff);
                          blueT = blueT+(p & 0xff);
                        }
                          //System.out.print(" i "+i+" ");
                          //System.out.println(" i "+i+" j "+j);
                      }
                   //System.out.println();
                  }
                  //new Pixel
                  ///System.out.println("k => "+k);
                  Newp = (alphaT/(k*k)<<24)|(redT/(k*k)<<16)| (greenT/(k*k)<<8)|blueT/(k*k);
                  img.setRGB(x, y, Newp);
          
              }
          }
        }

        //get an array
        else{
         System.out.println(" hi "+hi+" lo "+lo);
         int mid= (hi-lo)/2;
         MeanFilterParallel left = new MeanFilterParallel(lo, mid, h, k ,img);
         MeanFilterParallel right = new MeanFilterParallel(mid, hi, h, k, img);
         left.fork();
         right.compute();
         left.join();
         //System.out.println("startX "+startX+" startY "+startY);

        }}
        public static void main(String[] args) {
            BufferedImage img = null;
            File f = null;
            // System.out.println("Enter input file name :");
            // Scanner sc = new Scanner(System.in);
            // String inputImage = sc.nextLine();
            // System.out.println("Enter output file name :");
            // String outputImage = sc.nextLine();
            // System.out.println("Enter windowWidth");
            // int k = sc.nextInt();
        
            try{
              f = new File("C:\\Users\\MTHTSH062\\Downloads\\pictures\\image1CSC.jpg");
              img = ImageIO.read(f);
            }
            catch(IOException e){
              System.out.println(e);  
            }
            int w = img.getWidth();
            int h = img.getHeight();
            //System.out.println(" w "+w);
            //System.out.println(" h "+h);
            int k = 11;
            int hi_w = w;

            MeanFilterParallel mn = new MeanFilterParallel(0, hi_w, h, k, img);
            
             //ForkBlur fb = new ForkBlur(src, 0, src.length, dst);
 
            ForkJoinPool pool = new ForkJoinPool();
 
            long startTime = System.currentTimeMillis();
            pool.invoke(mn);
            long endTime = System.currentTimeMillis();
            System.out.println("Image blur parallel took " + (endTime - startTime) + 
                " milliseconds.");
          
            try{
              f = new File("C:\\Users\\MTHTSH062\\Downloads\\pictures\\ output.jpg");
              ImageIO.write(img, "jpg", f);
              }
            catch(IOException e){
               System.out.println(e);
              }
           }
    }