import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MedianFilterParallel extends RecursiveAction{
    public static int window;
    private static int k;
    private static  int h;
    private static int w;
    private static int sx;
    private static int sy;
    public static BufferedImage i1;
    public static BufferedImage i2;
    static final int SEQUENTIAL_CUTOFF= 300000;
    // Processing window size; should be odd.

    public MedianFilterParallel(int stX, int StY ,int height,int width,int key, BufferedImage image) {
        k = key;
        h = height;
        w = width;
        i1 = image;
        sx = stX;
        sy = StY;
    }

    protected static void medianFilter(){
        int window =(k-1)/2;
        //System.out.println("w "+w+" h "+h);
        //System.out.println("sx "+sx+" sy "+sy);
        for(int y = (sy+window); y < (sy+h-window); y++) {  
            for(int x = (sx+window); x < (sx+w-window); x++) {
                //System.out.println("x => "+x+" and y => "+y);
                //System.out.println("sx => "+sx+" and "+sy+" sy => "+sy);
                //System.out.println("Width => "+w+" and Height => "+h);
                //int[] alphaArr = new int[k*k];
                if (x >= i1.getWidth()|| y >= i1.getHeight()) {
                    continue;
                }
                else {
                    int[] redArr =new int[k*k];
                    int[] greenArr= new int[k*k];
                    int[] blueArr =new int[k*k];
                    int Newp;// = i1.getRGB(x, y);
                    int index = 0;

                    for(int j= (y-window); j<= (y+window); j++) {
                        for(int i = (x-window); i <= (x+window); i++) {
                            //System.out.println(" i => "+i+" and j => "+j);
                            int p = i1.getRGB(i,j);
                            //alphaArr[index] = ((p>>24)& 0xff);
                            redArr[index] = ((p>>16)& 0xff);
                            greenArr[index] = ((p>>8)& 0xff);
                            blueArr[index] = (p & 0xff);
                            //System.out.print(" i "+i+" ");
                            index++;
                            if (index == ((k*k)-1)) {
                                index = 0;
                            }
                        }
                    }
                    //new Pixel
                    Arrays.sort(redArr);
                    Arrays.sort(greenArr);
                    Arrays.sort(blueArr);
                    int middle= (k*k)/2;
                    ///System.out.println("k => "+k);
                    Newp = (redArr[middle]<<16)| (greenArr[middle]<<8)|blueArr[middle];
                    i2.setRGB(x, y, Newp);
                }
               
            }
        }
    }
    protected void compute() {
       //  System.out.println("Compute method => h*w "+(w*h)+" squential_cutoff "+SEQUENTIAL_CUTOFF);
        if ((h*w) < SEQUENTIAL_CUTOFF) {
            //System.out.println("sx => "+sx+" and sy => "+sy);
            //System.out.println("Splitheight "+h/2+" and Splitwidth "+w/2);
            //System.out.println("Width => "+w+" and Height => "+h);
            //System.out.println("Compute method => sx "+sx+" sy "+sy);
            medianFilter();
        }
        else {

            MedianFilterParallel box1 = new MedianFilterParallel(sx, sy, h/2, w/2, k ,i1);
            MedianFilterParallel box2 = new MedianFilterParallel((sx + w/2), sy, h/2, w/2, k, i1);
            MedianFilterParallel box3 = new MedianFilterParallel(sx, (sy + h/2), h/2, w/2, k, i1);
            MedianFilterParallel box4 = new MedianFilterParallel((sx + w/2), (sy + h/2), h/2, w/2, k ,i1);
            //invokeAll(box1,box2,box3,box4);
            box1.fork();
            box2.compute();
            box1.join();

            box3.fork();
            box4.compute();
            box3.join();
            
         //System.out.println("sx "+sx+" sy "+sy);
        }
    }
    public static void main(String[] args) {
        BufferedImage i1 = null;
        File f = null;
        // System.out.println("Enter input file name :");
        // Scanner sc = new Scanner(System.in);
        // String inputImage = sc.nextLine();
        // System.out.println("Enter output file name :");
        // String outputImage = sc.nextLine();
        // System.out.println("Enter windowWidth");
        // int k = sc.nextInt();
    
        try{
          f = new File("./WurricaneDrawing.png");
          i1 = ImageIO.read(f);
        }
        catch(IOException e){
          System.out.println("Try again............");  
        }
        
        int w = i1.getWidth();
        int h = i1.getHeight();
        //System.out.println(" w "+w);
        //System.out.println(" h "+h);
        int k = 5;
        //int stX = 0;
        //int stY = 0;
        //MedianFilterParallel mn = new MedianFilterParallel(0, 0, h, w, k, i1);
     
         //ForkBlur fb = new ForkBlur(src, 0, src.length, dst);
        i2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        ForkJoinPool pool = new ForkJoinPool();
        long startTime = System.currentTimeMillis();
        pool.invoke(new MedianFilterParallel(0, 0, h, w, k, i1));
        long endTime = System.currentTimeMillis();
        System.out.println("Image blur parallel took " + (endTime - startTime) + " milliseconds.");
    
        try {
          f = new File("./output.jpg");
          ImageIO.write(i2, "jpg", f);
        }
        catch(IOException e) {
           System.out.println("Try again............");
        }
    }
}