// import java.util.Arrays;
// import java.util.concurrent.ForkJoinPool;
// import java.util.concurrent.RecursiveAction;
// import javax.imageio.ImageIO;
// import java.awt.image.BufferedImage;
// import java.io.File;
// import java.io.IOException;

// public class MedianFilterParallel extends RecursiveAction{
//     private static int k;
//     private static  int h;
//     private static int w;
//     private static int startX;
//     private static int startY;
//     public static BufferedImage img;
//     static final int SEQUENTIAL_CUTOFF= 3000000;
//     // Processing window size; should be odd.
//     public static int window;
//     public static int tH;
//     public static int tW;
  
//     public MedianFilterParallel(int stX, int StY ,int height,int width,int key, BufferedImage image) {
//        k = key;
//        h = height;
//        w = width;
//        img = image;
//        startX=stX;
//        startY=StY;

//       //  System.out.println(" h "+h+" w "+w);
// //        System.out.println("starty "+startY+" startx "+startX);
// //        System.out.println("image.getHeight()-starty "+(image.getHeight()-startY)+" image.getWidth()-startx "+(image.getWidth()-startX));

   
//     }
//     protected static void meanFilter(){
//         int window =(k-1)/2;
//         //System.out.println("w "+w+" h "+h);
//         for(int x =startX-window; x<img.getWidth()-startX-window ;x++){
//         //System.out.println("startX "+startX+" startY "+startY);
//             for(int y = startY-window;y<img.getHeight()-startY-window;y++){    
//                 //System.out.println("X "+x+" Y "+y);
//                 int[] alphaArr = new int[k*k];
//                 int[] redArr =new int[k*k];
//                 int[] greenArr= new int[k*k];
//                 int[] blueArr =new int[k*k];;
//                 int Newp =0;
//                 int index =0;
//                 for(int i =x-window;i<=x+window;i++){
//                     for(int j=y-window;j<=y+window;j++){
//                         int p = img.getRGB(i,j);
//                         alphaArr[index] = ((p>>24)& 0xff);
//                         redArr[index] = ((p>>16)& 0xff);
//                         greenArr[index] = ((p>>8)& 0xff);
//                         blueArr[index] = (p & 0xff);
//                         //System.out.print(" i "+i+" ");
//                         index++;
//                         //4System.out.println(" i "+i+" j "+j);
//                     }
//                  //System.out.println();
//                 }
//                 //new Pixel
//                 Arrays.sort(redArr);
//                 Arrays.sort(greenArr);
//                 Arrays.sort(blueArr);
//                 int middle= (k*k)/2;
//                 ///System.out.println("k => "+k);
//                 Newp = (alphaArr[middle]<<24)|(redArr[middle]<<16)| (greenArr[middle]<<8)|blueArr[middle];
//                 img.setRGB(x, y, Newp);
        
//             }
//         }
//       }
//     protected void compute() {
//        //  System.out.println("Compute method => h*w "+(w*h)+" squential_cutoff "+SEQUENTIAL_CUTOFF);
//         if ((h*w) < SEQUENTIAL_CUTOFF) {
//            // System.out.println("Compute method => w "+w+" h "+h);
//             meanFilter();
//         }

//         //get an array
//         else{
//          //System.out.println(" h/2 "+h/2+" w/2 "+w/2);
//          MedianFilterParallel box1 = new MedianFilterParallel(startX, startY, h/2, w/2, k ,img);
//          MedianFilterParallel box2 = new MedianFilterParallel((startX+w)/2, startY, h, w/2, k, img);
//          MedianFilterParallel box3 = new MedianFilterParallel(startX, (startY+h)/2, h/2, w, k, img);
//          MedianFilterParallel box4 = new MedianFilterParallel((startX+w)/2, (startY+h)/2, h, w, k ,img);
//          invokeAll(box1,box2,box3,box4);
//          //System.out.println("startX "+startX+" startY "+startY);

//         }}
//         public static void main(String[] args) {
//             BufferedImage img = null;
//             File f = null;
//             // System.out.println("Enter input file name :");
//             // Scanner sc = new Scanner(System.in);
//             // String inputImage = sc.nextLine();
//             // System.out.println("Enter output file name :");
//             // String outputImage = sc.nextLine();
//             // System.out.println("Enter windowWidth");
//             // int k = sc.nextInt();
        
//             try{
//               f = new File("C:\\Users\\MTHTSH062\\Downloads\\pictures\\image1CSC.jpg");
//               img = ImageIO.read(f);
//             }
//             catch(IOException e){
//               System.out.println(e);  
//             }
//             int w = img.getWidth();
//             int h = img.getHeight();
//             //System.out.println(" w "+w);
//             //System.out.println(" h "+h);
//             int k = 5;
//             int stX = 0;
//             int stY = 0;

//             MedianFilterParallel mn = new MedianFilterParallel(stX, stY, h, w, k, img);
            
//              //ForkBlur fb = new ForkBlur(src, 0, src.length, dst);
 
//             ForkJoinPool pool = new ForkJoinPool();
 
//             long startTime = System.currentTimeMillis();
//             pool.invoke(mn);
//             long endTime = System.currentTimeMillis();
//             System.out.println("Image blur parallel took " + (endTime - startTime) + 
//                 " milliseconds.");
          
//             try{
//               f = new File("C:\\Users\\MTHTSH062\\Downloads\\pictures\\ output.jpg");
//               ImageIO.write(img, "jpg", f);
//               }
//             catch(IOException e){
//                System.out.println(e);
//               }
//            }
//     }