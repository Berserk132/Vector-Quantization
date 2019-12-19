import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;


public class VectorQuantization {
    private JButton imagePathButton;
    private JPanel panel1;
    private JTextField textField2;
    private JButton compressButton;
    private JButton decompressButton;
    private JTextField textField1;
    private JTextField textField3;
    String imagePath = "C:\\Users\\Berserk\\Desktop\\VectorQuantization\\";

    public VectorQuantization() {

        imagePathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PickAFile();
            }
        });
        compressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Code.load_imagePath = imagePath;
                Code.blockWidth =  Integer.parseInt(textField2.getText());
                Code.blockHeight = Integer.parseInt(textField1.getText());
                Code.blocksNumber = Integer.parseInt(textField3.getText());

                try {
                    Code.compress();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });
        decompressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    Code.deCompress();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public void PickAFile(){
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JPG & GIF Images", "jpg", "gif");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " +
                    chooser.getSelectedFile().getName());
        }
        imagePath += chooser.getSelectedFile().getName();
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Vector Quantization");
        frame.setContentPane(new VectorQuantization().panel1);
        frame.setPreferredSize( new Dimension( 600, 600 ) );
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}




