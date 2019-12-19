import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Code {

    static SaveRead xd = new SaveRead();
    public static int width , height;
    public static int[][] img;
    public static int imageType = 10;
    public static double[][][][] blocks;
    public static ArrayList<BookCodes> dicompressArray;
    public static double [][] blocksAvg;
    public static int [][] output;
    public static ArrayList<Blocks> blocksArray = new ArrayList<>();
    public static int blockWidth, blockHeight,blocksNumber;
    public static ArrayList<BookCodes> bookCodes = new ArrayList<>();
    public static ArrayList<Integer> codes = new ArrayList<>();
    public static int[][] finalCode;
    public static HashMap<BookCodes,Integer> compress = new HashMap<>();
    public static HashMap<Integer,BookCodes> diCompress = new HashMap<>();
    public static String load_imagePath = "";
    public static int wc = 0, hc = 0;
    public static void main(String[] args) throws IOException, ClassNotFoundException {



        /*String load_imagePath 		= "LudensTee_Detail04_grande.jpg";
        readImage_grayScale( load_imagePath );*/


        Algorithm();


    }
    public static void Algorithm() throws IOException, ClassNotFoundException {
        // 7  5
        // codeBook = 2;

        /*System.out.println("Compress(1) or Decompress(2) : ");
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();*/
        /*if(n==1){
            //getInput_fromUser();
            compress();
        }
        else {
            deCompress();
        }*/

    }
    public static void compress() throws IOException {


        readImage_grayScale( load_imagePath );

        constructBlocks(img,blocksArray);
        blocksAvg();
        get_codeBook();
        enhanced_codeBook();
        distanceComp();
        writeUsingFileWriter();
        xd.saveFile(diCompress);
    }
    public static void enhanced_codeBook(){
        for (int i = 0; i < 10; i++){

            for (int j = 0; j < bookCodes.size(); j++){


                bookCodes.get(j).bookCode = bookCodes.get(j).bookCodeAvg;
                bookCodes.get(j).blocks.clear();
            }

            distance();
            codeBookAvg();
        }

        for (int i = 0; i < bookCodes.size(); i++){

            compress.put(bookCodes.get(i), i);
            diCompress.put(i, bookCodes.get(i));
        }
    }
    public static void get_codeBook(){

        double [][] arr1 = new double[blockWidth][blockHeight];
        double [][] arr2 = new double[blockWidth][blockHeight];

        arr1 = subOne(arr1,blocksAvg);
        bookCodes.add(new BookCodes(arr1));
        arr2 = addOne(arr2,blocksAvg);
        bookCodes.add(new BookCodes(arr2));
        ArrayList<BookCodes> tmp = new ArrayList<>();

        for (int i = 0; i < (int)(Math.log(blocksNumber)/Math.log(2)); i++){

            if (i == 0){

                distance();
                codeBookAvg();

            }
            else{

                for (int j = 0; j < bookCodes.size(); j++){


                    double[][] bookAvgTmp = bookCodes.get(j).bookCodeAvg;
                    double [][] arr3 = new double[blockWidth][blockHeight];
                    double [][] arr4 = new double[blockWidth][blockHeight];
                    arr3 = subOne(arr3, bookAvgTmp);
                    tmp.add(new BookCodes(arr3));
                    arr4 = addOne(arr4, bookAvgTmp);
                    tmp.add(new BookCodes(arr4));
                }

                bookCodes.clear();
                bookCodes.addAll(tmp);
                tmp.clear();
                distance();
                codeBookAvg();

            }
        }
    }
    public static void distance(){


        for (int i = 0; i < blocksArray.size(); i++){
            ArrayList<Double> dis = new ArrayList<>();
            for (int j = 0; j < bookCodes.size(); j++){

                double distance = 0;
                for (int m = 0; m < blockWidth; m++){

                    for (int n = 0; n < blockHeight; n++){

                        distance += Math.abs(bookCodes.get(j).bookCode[m][n] - blocksArray.get(i).block[m][n]) ;
                    }
                }
                dis.add(distance);
            }

            double min = Collections.min(dis);
            int index = dis.indexOf(min);
            //System.out.println(index+" Index");
            bookCodes.get(index).blocks.add(blocksArray.get(i));
            //System.out.println(dis.get(0)+"    "+dis.get(1));

        }


    }
    public static void distanceComp(){

        codes = new ArrayList<>(blocksArray.size());

        for (int i = 0; i < blocksArray.size(); i++){
            ArrayList<Double> dis = new ArrayList<>();
            for (int j = 0; j < bookCodes.size(); j++){

                double distance = 0;
                for (int m = 0; m < blockWidth; m++){

                    for (int n = 0; n < blockHeight; n++){

                        distance += Math.abs(bookCodes.get(j).bookCode[m][n] - blocksArray.get(i).block[m][n]) ;
                    }
                }
                dis.add(distance);
            }

            double min = Collections.min(dis);
            int index = dis.indexOf(min);

            codes.add(index);


        }


    }
    public static void converdArray(){

        finalCode = new int[width/blockWidth][height/blockHeight];
        for(int i=0; i<width/blockWidth;i++) {
            for (int j = 0; j < height/blockHeight; j++) {

                finalCode[i][j] = codes.get((j*width/blockWidth) + i);
            }
        }

    }
    public static void readImage_grayScale( String loadPath ) {
        try {
            BufferedImage image;
            File input 	= new File( loadPath );

            image 		= ImageIO.read(input);
            width 		= image.getWidth();
            height 		= image.getHeight();
            //imageType 	= BufferedImage.TYPE_BYTE_GRAY;

            if((width % blockWidth!=0)||(height%blockHeight!=0)){
                int wTmp = width;
                int hTmp = height;
                while (wTmp % blockWidth!=0){

                    wTmp++;
                    wc++;
                }
                while (hTmp % blockHeight!=0){
                    hTmp++;
                    hc++;
                }
                img = new int[wTmp][hTmp];
                for ( int i = 0; i < wTmp; ++i ) {
                    for ( int j = 0; j < hTmp; ++j ) {
                        if ((i>=width)||(j>=height)){
                            img[i][j] = 0;
                        }
                        else {
                            Color pixelColor = new Color( image.getRGB(i, j) );
                            int red 	= (int)( 0.299 * pixelColor.getRed() 	);
                            int green 	= (int)( 0.587 * pixelColor.getGreen() 	);
                            int blue 	= (int)( 0.114 * pixelColor.getBlue() 	);
                            img[i][j] = red + green + blue;
                        }

                    }
                }
                width = wTmp;
                height = hTmp;
            }
            else {
                img = new int[width][height];
                for ( int i = 0; i < width; ++i ) {
                    for ( int j = 0; j < height; ++j ) {
                        Color pixelColor = new Color( image.getRGB(i, j) );

                        int red 	= (int)( 0.299 * pixelColor.getRed() 	);
                        int green 	= (int)( 0.587 * pixelColor.getGreen() 	);
                        int blue 	= (int)( 0.114 * pixelColor.getBlue() 	);
                        img[i][j] = red + green + blue;
                    }
                }
            }

        }
        catch (Exception e) {
            System.out.println( "Image can't be readed" );
        }
    }
    public static void saveImage_grayScale( String savePath , String imageName , String imageFormat ) {
        File output1 = new File( savePath + "\\" + imageName + "." + imageFormat );

        width-=wc;
        height-=hc;

        BufferedImage image = new BufferedImage( width , height, imageType );

        for ( int i = 0; i < width; ++i ) {
            for ( int j = 0; j < height; ++j ) {
                int grayValue = (int)output[i][j];
                Color pixelColor = new Color( grayValue , grayValue , grayValue );
                image.setRGB( i , j , pixelColor.getRGB() );
            }
        }

        try {
            ImageIO.write( image , imageFormat , output1 );
        } catch (IOException e) {
            System.out.println( "Error in save the image" );
        }
    }
    private static void printImageMatrix(int[][] img){

        for (int i = 0; i < width; i++) {

            for (int j = 0; j < height; j++){

                /*System.out.println(img.length);
                System.out.println(img[0].length);*/
                System.out.print(img[i][j] + " ");
            }
            System.out.println();
        }
    }
    private static void printBlocks(int[][][][] img){

        for (int i = 0; i < img.length; i+=7) {

            for (int j = 0; j < img[0].length; j+=5){

                for (int m = i; m < i + 7; m++){

                    for (int n = j; n < j + 5; n++){

                        System.out.print(img[i][j][m % 7][n % 5] + " ");
                    }
                    System.out.println();
                }
                System.out.println();
            }
            System.out.println();
        }
    }
    private static void printBlocks(ArrayList<Blocks> blocksArrayList){

        for (int i = 0; i < blocksArrayList.size(); i+=1) {

            for (int m = 0; m < blockWidth; m++){

                for (int n = 0; n < blockHeight ; n++){

                    System.out.print(blocksArrayList.get(i).block[m][n] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
    private static void printAVG(){


        for (int m = 0; m < blockWidth; m++){

            for (int n = 0; n < blockHeight ; n++){

                System.out.print(blocksAvg[m][n] + " ");
            }
            System.out.println();
        }
    }
    private static void printCodeBookAvg(){

        for (int i = 0; i < bookCodes.size(); i++) {

            for (int m = 0; m < blockWidth; m++) {

                for (int n = 0; n < blockHeight; n++) {

                    System.out.print(bookCodes.get(i).bookCodeAvg[m][n] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
    private static void printBookCode(){

        for (int i = 0; i < bookCodes.size(); i+=1) {

            for (int m = 0; m < blockWidth; m++){

                for (int n = 0; n < blockHeight ; n++){

                    System.out.print(bookCodes.get(i).bookCode[m][n] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
    private static void printBook(double[][] tmp){

        for (int m = 0; m < blockWidth; m++){

            for (int n = 0; n < blockHeight ; n++){

                System.out.print(tmp[m][n] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    private static void constructBlocks(int[][] img,ArrayList<Blocks> blocksArray){

        blocks = new double[img.length][img[0].length][blockWidth][blockHeight];

        for (int i = 0; i < img.length; i+=blockWidth) {

            for (int j = 0; j < img[0].length; j+=blockHeight){

                double[][] tmp = new double[blockWidth][blockHeight];

                for (int m = i; m < i + blockWidth; m++){

                    for (int n = j; n < j + blockHeight; n++){

                        blocks[i][j][m % blockWidth][n % blockHeight] = img[m][n];
                        tmp[m % blockWidth][n % blockHeight] = img[m][n];

                    }
                }
                Blocks b = new Blocks(tmp);
                blocksArray.add(b);
            }

        }
    }
    public static void deCompress() throws IOException, ClassNotFoundException {
        /*constructBlocks(img,blocksArray);
        blocksAvg();
        get_codeBook();
        enhanced_codeBook();
        distanceComp();*/
        readFromFile();
        converdArray();
        dicompressArray = new ArrayList<>();
        diCompress.clear();
        diCompress = xd.readFile();
        for (int i = 0; i < codes.size(); i++){

            BookCodes bookCodes;
            bookCodes = diCompress.get(codes.get(i));
            dicompressArray.add(bookCodes);
        }

        output = new int[width][height];

        int i = 0;
        for (int j = 0; j < width; j+=blockWidth) {

            for (int m = 0; m < height; m+=blockHeight){

                for (int n = j; n < j + blockWidth; n++){

                    for (int o = m; o < m + blockHeight; o++){

                        output[n][o] = (int)dicompressArray.get(i).bookCode[n % blockWidth][o % blockHeight];

                    }
                }
                i++;
            }
        }


        String save_imagePath 		= "tmp";
        String save_imageName 		= "Out";
        String save_imageFormat		= "jpg";
        saveImage_grayScale( save_imagePath , save_imageName , save_imageFormat );

    }
    private static void readFromFile() throws IOException {

        FileReader fr = new FileReader(new File("C:\\Users\\Berserk\\Desktop\\VectorQuantization\\Compressed.txt"));
        codes.clear();
        width = fr.read();
        height = fr.read();
        blockWidth = fr.read();
        blockHeight = fr.read();
        blocksNumber = fr.read();

        int i;
        while((i=fr.read())!=-1)
            codes.add(i);
        fr.close();
    }
    private static void writeUsingFileWriter() {
        File file = new File("C:\\Users\\Berserk\\Desktop\\VectorQuantization\\Compressed.txt");
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            fr.write(width);
            fr.write(height);
            fr.write(blockWidth);
            fr.write(blockHeight);
            fr.write(blocksNumber);

            for (int i = 0; i < codes.size(); i++){

                fr.write(codes.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //close resources
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static class SaveRead implements Serializable {


        private static final String PATH = "C:\\Users\\Berserk\\Desktop\\VectorQuantization\\bookcodes.txt";

        public void saveFile(HashMap<Integer, BookCodes> users) throws IOException {

            try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(PATH))) {
                os.writeObject(users);
            }
        }

        public HashMap<Integer, BookCodes> readFile () throws ClassNotFoundException, IOException
        {
            try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(PATH))) {
                return (HashMap<Integer, BookCodes>) is.readObject();
            }
        }
    }
    private static void blocksAvg(){

        blocksAvg = new double[blockWidth][blockHeight];
        for (int i = 0; i < width; i+=blockWidth) {

            for (int j = 0; j <height; j+=blockHeight){

                for (int m = i; m < i + blockWidth; m++){

                    for (int n = j; n < j + blockHeight; n++){

                        blocksAvg[m % blockWidth][n % blockHeight] += blocks[i][j][m % blockWidth][n % blockHeight];

                    }
                }
            }
        }
        for(int i=0;i<blockWidth;i++){
            for(int j=0;j<blockHeight;j++){
                blocksAvg[i][j] /= blocksArray.size() ;
            }
        }

    }
    private static void codeBookAvg(){

        for (int i = 0; i < bookCodes.size(); i++) {

            bookCodes.get(i).bookCodeAvg = new double[blockWidth][blockHeight];

            for (int j = 0; j < bookCodes.get(i).blocks.size(); j++) {

                for (int m = 0; m < blockWidth; m++) {

                    for (int n = 0; n < blockHeight; n++) {

                        bookCodes.get(i).bookCodeAvg[m][n] += bookCodes.get(i).blocks.get(j).block[m][n];

                    }
                }
            }

            for(int x=0;x<blockWidth;x++){
                for(int l=0;l<blockHeight;l++){

                    bookCodes.get(i).bookCodeAvg[x][l] /= bookCodes.get(i).blocks.size();
                }
            }
        }




    }
    private static double[][] subOne(double[][] arr,double[][] matAvg){

        for (int i = 0; i < blockWidth; i++) {

            for (int j = 0; j < blockHeight; j++) {

                arr[i][j] = Math.floor(matAvg[i][j]);
            }
        }

        return arr;
    }
    private static double[][] addOne(double[][] arr, double[][] matAvg){

        for (int i = 0; i < blockWidth; i++) {

            for (int j = 0; j < blockHeight; j++) {

                arr[i][j] = Math.ceil(matAvg[i][j]);
            }
        }

        return arr;
    }
    /*public static void  getInput_fromUser(int bWidth, int bHeight, int bNumber){

     *//*int blockWidth, blockHeight,blocksNumber;
        Scanner in  = new Scanner(System.in);

        System.out.println("Enter Block Width then Height :");

        blockWidth = in.nextInt();
        blockHeight = in.nextInt();

        System.out.println("Enter Number of Blocks :");
        blocksNumber = in.nextInt();*//*

        Code.blockHeight = bWidth;
        Code.blockWidth = bHeight;
        Code.blocksNumber = bNumber;


    }*/

}