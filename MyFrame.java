// I certify, that this computer program submitted by me is all of my own work. Signed: Elisha Bjerkeset
//Bjerkeset CSC 322 Final Project

/*
 * Features: 
 * Java Swing menu and menu items
 * Action Listener
 * Serialization/Deserialization
 */

import javax.swing.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MyFrame extends JFrame implements ActionListener {

    //Creating variables
    MyPanel myPanel = new MyPanel();
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem loadItem;
    JMenuItem saveItem;
    JMenuItem restartItem;

    MyFrame() {

        //Creating Frame
        this.add(myPanel);
        this.setTitle("Checkers Game");
        this.setSize(815,865);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Adding a JMenu and it's options
        menuBar = new JMenuBar();
        fileMenu = new JMenu("file");

        loadItem = new JMenuItem("Load Game");
        saveItem = new JMenuItem("Save Game");
        restartItem = new JMenuItem("Restart Game");

        loadItem.addActionListener(this);
        saveItem.addActionListener(this);
        restartItem.addActionListener(this);

        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        fileMenu.add(restartItem);

        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //If the load button is clicked, it will get input and then call the load method
        if(e.getSource() == loadItem) {
            
            String userInput = JOptionPane.showInputDialog(this, "Enter the file name:", "Load File", JOptionPane.QUESTION_MESSAGE);

            if(userInput != null) {
                System.out.println("User input: " + userInput);
                ArrayList<Pieces> finalList = loadList(userInput);
                if(finalList != null) {
                    myPanel.setBoardPieces(finalList);
                }
            }
            else {
                System.out.println("Cancel");
            }
        }

        //If save button is pressed, then it will get input and then call the save method
        if(e.getSource() == saveItem) {
            String userInput = JOptionPane.showInputDialog(this, "Enter a file name:", "Save File", JOptionPane.QUESTION_MESSAGE);

            if(userInput != null) {
                System.out.println("User input: " + userInput);
                saveList(myPanel.getBoardPieces(), userInput);
            }
            else {
                System.out.println("Cancel");
            }
        }

        //If the restart button is pressed, then it will reset the boardPieces and repaint
        if(e.getSource() == restartItem) {
            //System.exit(0);
            myPanel.setBoardPieces(null);
            myPanel.initializePieces();
            myPanel.repaint();
            myPanel.setPlayer(true);
        }
    }
       
    //Deserializing the specified file
    public ArrayList<Pieces> loadList(String file) {
        file += ".ser";
        try (ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(file))) {
            Object deserializedObject = inFile.readObject();
            boolean currentPlayer = inFile.readBoolean();

            myPanel.setPlayer(currentPlayer);

            if(deserializedObject == null) {
                System.out.println("Returning empty list");
                return null;
            }
            else if(deserializedObject instanceof ArrayList<?>) {
                //Creating an arraylist with the object
                ArrayList<?> defaultArrayList = (ArrayList<?>) deserializedObject;
                ArrayList<Pieces> finalList = new ArrayList<Pieces>();

                //Adding the Pieces Objects to an observable array that cna be returned
                for (int i = 0; i < defaultArrayList.size(); i++) {
                    if (defaultArrayList.get(i) instanceof Pieces) {
                        finalList.add((Pieces) defaultArrayList.get(i));
                    }
                    else {
                        System.out.println("Deserialized list is not valid");
                        return null;
                    }
                }
            System.out.println("Returning serialized list");
            return finalList;
            }
        }
        catch(Exception e) {
            System.out.println("Error 1" + e.getMessage());
        }
        return null;
    }

    //Serializing arraylist
    private void saveList(ArrayList<Pieces> saveBoardPieces, String file) {
        file += ".ser";
        try (ObjectOutputStream outFile = new ObjectOutputStream(new FileOutputStream(file))) {
            outFile.writeObject(saveBoardPieces);
            outFile.writeBoolean(myPanel.getPlayer());
            System.out.println("Saved the info");
        }
        catch (Exception e) {
            System.out.println(e.getMessage() + "BIG PROBLEM");
        }
    }
}