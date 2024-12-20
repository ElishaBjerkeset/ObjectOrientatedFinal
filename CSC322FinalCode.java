// I certify, that this computer program submitted by me is all of my own work. Signed: Elisha Bjerkeset
//Bjerkeset CSC 322 Final Project

/*
 * Sources:
 * https://www.youtube.com/watch?v=Kmgo00avvEw&t=14542s
 * https://medium.com/@michael71314/java-lesson-21-drawing-and-coloring-shapes-on-the-jframe-d740970e1d68
 * https://www.geeksforgeeks.org/java-joptionpane/
 * https://www.geeksforgeeks.org/introduction-to-java-swing/
 */

import javax.swing.JOptionPane;

public class CSC322FinalCode {
    public static void main(String[] args) {
        
        MyFrame myFrame = new MyFrame();
        showStartup();
    }

    //Start up message
    private static void showStartup() {
        JOptionPane.showMessageDialog(
            null,
            "Welcome to the Checkers Game",
            "Start Game",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}