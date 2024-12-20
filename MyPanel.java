// I certify, that this computer program submitted by me is all of my own work. Signed: Elisha Bjerkeset
//Bjerkeset CSC 322 Final Project

/*
 * Features: 
 * Serialization and Deserialization
 * Java Swing Panes
 * Point class
 * New features like HashMaps
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;//setBoardPieces

//The aizen panel will be dragged, it will have to be used for each piece home and away
public class MyPanel extends JPanel {

    //Declaring images, booleans, and board Pieces array List
    private ImageIcon redPawn = new ImageIcon("red.png");
    private ImageIcon blackPawn = new ImageIcon("black.png");

    private ImageIcon redKing = new ImageIcon("redKing.png");
    private ImageIcon blackKing = new ImageIcon("blackKing.png");

    private Point currentDragPoint;
    private Pieces draggedPiece;
    private Pieces previousPiece;
    private Point previousSpace;

    private Image redPiece = redPawn.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
    private ImageIcon red = new ImageIcon(redPiece);
    private Image blackPiece = blackPawn.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
    private ImageIcon black = new ImageIcon(blackPiece);

    private Image redKingFin = redKing.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
    private ImageIcon reder = new ImageIcon(redKingFin);
    private Image blackKingFin = blackKing.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
    private ImageIcon blacker = new ImageIcon(blackKingFin);

    private boolean requiredUpLeftMove;
    private boolean requiredUpRightMove;
    private boolean requiredDownLeftMove;
    private boolean requiredDownRightMove;

    private boolean currentPlayer = true; // true is red
    private ArrayList<Pieces> boardPieces = new ArrayList<Pieces>();

    //Constructor declaring listners
    MyPanel() {
        //imageCorner = new Point(0,0);
        ClickListener clickListener = new ClickListener();
        DragListener dragListener = new DragListener();
        this.addMouseListener(clickListener);
        this.addMouseMotionListener(dragListener);
        initializePieces();
    }

    //Methods to change the arraylist
    public ArrayList<Pieces> getBoardPieces() {
        return this.boardPieces;
    }
    public void setBoardPieces(ArrayList<Pieces> boardPieces) {
        if(boardPieces == null) {
            this.boardPieces.clear();
            return;
        }
        this.boardPieces = boardPieces;
        repaint();
    }

    //For setting the turn when serializing
    public boolean getPlayer() {
        return currentPlayer;
    }
    public void setPlayer(boolean currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    //Show this after someone wins
    public void winnerMessage(String team) {
        Object[] options = {"Restart", "Close"};
        int response = JOptionPane.showOptionDialog (
            null,
            team + " has won the game. Would you like to start a new game or exit?",
            "Game Over",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
        if(response == 0) {
            currentPlayer = true;
            boardPieces.clear();
            initializePieces();
            repaint();
        }
        else {
            System.exit(0);
        }
    }

    //For setting up the pieces at the start of the game
    public void initializePieces() {
        for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 8; col++) {
                if((row + col) % 2 == 1) { 
                    if(row < 3) {
                        boardPieces.add(new Pieces("red", new Point(col * 100, row * 100)));
                    }
                    else if(row > 4) {
                        boardPieces.add(new Pieces("black", new Point(col * 100, row * 100)));
                    }
                }
            }
        }
    }

    //For painting 
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawCheckerboard(g);

        drawPieces(g);

    }

    //Printing the board
    private void drawCheckerboard(Graphics g) {
        boolean switcher = false;

        for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 8; col++) {
                if(switcher) {
                    g.setColor(Color.LIGHT_GRAY);
                }
                else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(col * 100, row * 100, 100, 100);
                switcher = !switcher;
            }
            switcher = !switcher;
        }
    }

    //Drawing the pieces in the board
    private void drawPieces(Graphics g) {
        for (Pieces piece : boardPieces) {
            if(piece.getSide().equals("red") && !piece.getKing()) {
                red.paintIcon(this, g, (int)piece.getPiecePoint().getX(), (int)piece.getPiecePoint().getY());
            } 
            else if(piece.getSide().equals("red") && piece.getKing()) {
                reder.paintIcon(this, g, (int)piece.getPiecePoint().getX(), (int)piece.getPiecePoint().getY());
            } 
            else if(piece.getSide().equals("black") && !piece.getKing()) {
                black.paintIcon(this, g, (int)piece.getPiecePoint().getX(), (int)piece.getPiecePoint().getY());
            }
            else if(piece.getSide().equals("black") && piece.getKing()) {
                blacker.paintIcon(this, g, (int)piece.getPiecePoint().getX(), (int)piece.getPiecePoint().getY());
            }
        }
    }

    //Checking if a player won
    private boolean[] checkGame() {

        boolean[] lostList = {true, true};

        for(int i = 0; i < boardPieces.size(); i++) {
            if(boardPieces.get(i).getSide().equals("red")) {
                lostList[0] = false;
            }
            else if(boardPieces.get(i).getSide().equals("black")) {
                lostList[1] = false;
            }
        }
        return lostList;
    }

    //Snapping the pieces to their closest square and making them kings
    private void snapToGrid(Pieces piece) {
        
        int x = (int) piece.getPiecePoint().getX();
        int y = (int) piece.getPiecePoint().getY();
    
        x = (x + 100 / 2) / 100 * 100;
        y = (y + 100 / 2) / 100 * 100;

        piece.setPiecePoint(new Point(x, y));

        if(piece.getPiecePoint().getY() == 700 && piece.getSide().equals("red") && piece.getKing() == false) {
            piece.setKing(true);
        }
        else if(piece.getPiecePoint().getY() == 0 && piece.getSide().equals("black") && piece.getKing() == false) {
            piece.setKing(true);
        }
    }

    //Click Listener
    private class ClickListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            currentDragPoint = e.getPoint();
            draggedPiece = pieceAtLocation(currentDragPoint);
            if(draggedPiece != null) {
                //Setting the previous space to where the piece was so that it can be returned
                previousSpace = new Point(draggedPiece.getPiecePoint());
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (draggedPiece != null) {

                //Placing the piece if it is in a valid place
                if(isValidMove(draggedPiece)) {
                    snapToGrid(draggedPiece);

                    //Checking if someone won
                    boolean[] checking = checkGame();
                    if(checking[0]) {
                        repaint();
                        winnerMessage("Black");
                    }
                    else if(checking[1]) {
                        repaint();
                        winnerMessage("Red");
                    }

                    previousPiece = draggedPiece;
                } 
                else {
                    // If the move is invalid, reset the piece to its original position
                    draggedPiece.setPiecePoint(previousSpace);
                }
                draggedPiece = null;
                repaint();
            }
        }
    }

    //Drag Listener for changing the piece's location for how much it moves
    private class DragListener extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent e) {
            if (draggedPiece != null) {
                int changeX = e.getX() - currentDragPoint.x;
                int changeY = e.getY() - currentDragPoint.y;
                draggedPiece.move(changeX, changeY);
                currentDragPoint = e.getPoint();
                repaint();
            }
        }
    }

    //Says if there is a piece there
    private Pieces pieceAtLocation(Point point) {
        for(Pieces piece : boardPieces) {
            if(piece.contains(point)) {
                return piece;
            }
        }
        return null;
    }

    //Checking if a move is valid
    private boolean isValidMove(Pieces draggedPiece) {

        //Seeing if it is their turn
        if(draggedPiece.getSide().equals("red") && currentPlayer) {
        }
        else if(draggedPiece.getSide().equals("black") && !currentPlayer) {
        }
        else {
            return false;
        }

        //Getting where the piece is going
        int x = (int) draggedPiece.getPiecePoint().getX();
        int y = (int) draggedPiece.getPiecePoint().getY();
        x = (x + 100 / 2) / 100 * 100;
        y = (y + 100 / 2) / 100 * 100;
    
        //Seeing if the space is occupied
        for(int i = 0; i < boardPieces.size(); i++) {
            if(boardPieces.get(i).getPiecePoint().equals(new Point(x,y))) {
                draggedPiece.setPiecePoint(previousSpace);
                return false;
            }
        }

        //Getting the change in X and Y
        int changeX = (int)(previousSpace.getX() - x);
        int changeY = (int)(previousSpace.getY() - y);
        
        //Seeing if a jump is possible
        if(requiredUpLeftMove || requiredUpRightMove || requiredDownLeftMove || requiredDownRightMove) {
            if(!draggedPiece.equals(previousPiece)) {
                return false;
            }
            boolean chance = true;
            if(requiredUpLeftMove && changeX != 200 && changeY != 200) {
                chance = false;
            }
            if(requiredUpRightMove && changeX != -200 && changeY != 200) {
                chance = false;
            }
            if(requiredDownLeftMove && changeX != 200 && changeY != -200) {
                chance = false;
            }
            if(requiredDownRightMove && changeX != -200 && changeY != -200) {
                chance = false;
            }

            if(!chance) {
                return false;
            }
        }
        
        
        //Resetting varaibles
        requiredUpLeftMove = false;
        requiredUpRightMove = false;
        requiredDownLeftMove = false;
        requiredDownRightMove = false;

        //Seeing if a red piece can move
        if(draggedPiece.getSide().equals("red") && !draggedPiece.getKing()) {
            HashMap<String, Object[]> jumpableDownHash = downHasJump((int)previousSpace.getX(), (int)previousSpace.getY());
            Object[] downLeftValues = jumpableDownHash.get("jumpableLeftDown");
            Object[] downRightValues = jumpableDownHash.get("jumpableRightDown");

            if(Math.abs(changeX) == 100 && changeY == -100 && x <= 700 && x >= 0 && y <= 700) {
                currentPlayer = !currentPlayer;
                return true;
            }
            else if(changeX == 200 && changeY == -200 && (boolean)downLeftValues[0]) {
                boardPieces.remove((int)downLeftValues[1]);
            }
            else if(changeX == -200 && changeY == -200 && (boolean)downRightValues[0]) {
                boardPieces.remove((int)downRightValues[1]);
            }
            else {
                return false;
            }
        }

        //Seeing if the black piece can move
        else if(draggedPiece.getSide().equals("black") && !draggedPiece.getKing()) {//curent player ???
            HashMap<String, Object[]> jumpableUpHash = upHasJump((int)previousSpace.getX(), (int)previousSpace.getY());
            Object[] upLeftValues = jumpableUpHash.get("jumpableLeftUp");
            Object[] upRightValues = jumpableUpHash.get("jumpableRightUp");

            if(Math.abs(changeX) == 100 && changeY == 100 && x <= 700 && x >= 0 && y >= 0) {
                currentPlayer = !currentPlayer;
                return true;
            }
            else if(changeX == 200 && changeY == 200 && (boolean)upLeftValues[0]) {
                boardPieces.remove((int)upLeftValues[1]);
            }
            else if(changeX == -200 && changeY == 200 && (boolean)upRightValues[0]) {
                boardPieces.remove((int)upRightValues[1]);
            }
            else {
                return false;
            }
        }

        //Seeing if the king can move
        else if(draggedPiece.getKing()) {
            HashMap<String, Object[]> jumpableKingHash = kingHasJump((int)previousSpace.getX(), (int)previousSpace.getY());
            Object[] downLeftValues = jumpableKingHash.get("jumpableLeftDown");
            Object[] downRightValues = jumpableKingHash.get("jumpableRightDown");
            Object[] upLeftValues = jumpableKingHash.get("jumpableLeftUp");
            Object[] upRightValues = jumpableKingHash.get("jumpableRightUp");

            if(Math.abs(changeX) == 100 && Math.abs(changeY) == 100 && x <= 700 && x >= 0 && y >= 0 && y <= 700) {
                currentPlayer = !currentPlayer;
                return true;
            }
            else if(changeX == 200 && changeY == -200 && (boolean)downLeftValues[0]) {
                boardPieces.remove((int)downLeftValues[1]);
            }
            else if(changeX == -200 && changeY == -200 && (boolean)downRightValues[0]) {
                boardPieces.remove((int)downRightValues[1]);
            }
            else if(changeX == 200 && changeY == 200 && (boolean)upLeftValues[0]) {
                boardPieces.remove((int)upLeftValues[1]);
            }
            else if(changeX == -200 && changeY == 200 && (boolean)upRightValues[0]) {
                boardPieces.remove((int)upRightValues[1]);
            }
            else {
                return false;
            }
        }
        
        //Seeing if a piece can jump down again
        if(draggedPiece.getSide().equals("red") || draggedPiece.getKing() || y == 0) {
            HashMap<String, Object[]> agaJumpableDownHash = downHasJump(x, y);
            Object[] againDownLeftValues = agaJumpableDownHash.get("jumpableLeftDown");
            Object[] againDownRightValues = agaJumpableDownHash.get("jumpableRightDown");

            if((boolean)againDownLeftValues[0]) {
                requiredDownLeftMove = true;
                System.out.println("Down Left Jump Required");
            }
            if((boolean)againDownRightValues[0]) {
                requiredDownRightMove = true;
                System.out.println("Down Right Jump Required");
            }
        }

        //Seeing if a piece can jump up again
        if(draggedPiece.getSide().equals("black") || draggedPiece.getKing() || y == 700) {
            HashMap<String, Object[]> agaJumpableUpHash = upHasJump(x, y);
            Object[] againUpLeftValues = agaJumpableUpHash.get("jumpableLeftUp");
            Object[] againUpRightValues = agaJumpableUpHash.get("jumpableRightUp");

            if((boolean)againUpLeftValues[0]) {
                requiredUpLeftMove = true;
                System.out.println("Up Left Jump Required");
            }
            if((boolean)againUpRightValues[0]) {
                requiredUpRightMove = true;
                System.out.println("Up Right Jump Required");
            }
        }

        //Not switching players if the player has a jump available
        if(!requiredDownLeftMove && !requiredDownRightMove && !requiredUpLeftMove && !requiredUpRightMove) {
            currentPlayer = !currentPlayer;
        }

        return true;
    }

    //Seeing if the king has a jump by using the other methods and returning a HashMap
    private HashMap<String, Object[]> kingHasJump(int x, int y) {
        HashMap<String, Object[]> jumpableHash = downHasJump(x, y);
        HashMap<String, Object[]> jumpableUpHash = upHasJump(x, y);

        jumpableHash.putAll(jumpableUpHash);

        return jumpableHash;
    }

    //Checking if a player can jump down and returning a HashMap with if it can jump and which piece to remove
    private HashMap<String, Object[]> downHasJump(int x, int y) {

        //Creating the HashMap
        HashMap<String, Object[]> jumpableHash = new HashMap<String, Object[]>();
        jumpableHash.put("jumpableRightDown", new Object[]{false, 0});
        jumpableHash.put("jumpableLeftDown", new Object[]{false, 0});

        //Setting the enemy
        String enemy = null;
        if(draggedPiece.getSide().equals("red")) {
            enemy = "black";
        }
        if(draggedPiece.getSide().equals("black")) {
            enemy = "red";
        }

        //If there is a piece below and to the left, the next for loop enters
        outerLoop:
        for (int i = 0; i < boardPieces.size(); i++) {
            if(boardPieces.get(i).getPiecePoint().getX() == x - 100 &&
                boardPieces.get(i).getPiecePoint().getY() == y + 100 &&
                boardPieces.get(i).getSide().equals(enemy)) {

                //If there are no pieces where it wants to jump and the jump is in bounds
                for (int j = 0; j < boardPieces.size(); j++) {
                    if (boardPieces.get(j).getPiecePoint().equals(new Point(x - 200, y + 200))) {
                        break outerLoop;
                    }
                    if (x - 200 < 0 || y + 200 > 700) {
                        break outerLoop;
                    }
                }
            
            //Executes if all the conditinals are correct and return the Hash Map
            jumpableHash.replace("jumpableLeftDown", new Object[]{true, i});
            break outerLoop;
            }
        }

        //If there is a piece below and to the right, the next for loop enters
        outerLoop:
        for(int i = 0; i < boardPieces.size(); i++) {
            if(boardPieces.get(i).getPiecePoint().getX() == x + 100 && 
            boardPieces.get(i).getPiecePoint().getY() == y + 100 && 
            boardPieces.get(i).getSide().equals(enemy)) {

                //If there are no pieces where it wants to jump and the jump is in bounds
                for(int j = 0; j < boardPieces.size(); j++) {
                    if(boardPieces.get(j).getPiecePoint().equals(new Point(x + 200, y + 200))) {
                        break outerLoop;
                    }
                    if(x + 200 > 700 || y + 200 > 700) {
                        break outerLoop;
                    }
                }

            //Executes if all the conditinals are correct and return the Hash Map
            jumpableHash.replace("jumpableRightDown", new Object[]{true, i});
            break outerLoop;
            }
        }

        return jumpableHash;
    }

    //Checking if a player can jump up and returning a HashMap with if it can jump and which piece to remove
    private HashMap<String, Object[]> upHasJump(int x, int y) {

        //Creating the Hash Map
        HashMap<String, Object[]> jumpableHash = new HashMap<String, Object[]>();
        jumpableHash.put("jumpableRightUp", new Object[]{false, 0});
        jumpableHash.put("jumpableLeftUp", new Object[]{false, 0});

        //Setting the enemy
        String enemy = null;
        if(draggedPiece.getSide().equals("red")) {
            enemy = "black";
        }
        if(draggedPiece.getSide().equals("black")) {
            enemy = "red";
        }

        //If there is a piece above and to the left, the next for loop enters
        outerLoop:
        for(int i = 0; i < boardPieces.size(); i++) {
            if(boardPieces.get(i).getPiecePoint().getX() == x - 100 && 
            boardPieces.get(i).getPiecePoint().getY() == y - 100 && 
            boardPieces.get(i).getSide().equals(enemy)) {

                //If there are no pieces where it wants to jump and the jump is in bounds
                for(int j = 0; j < boardPieces.size(); j++) {
                    if(boardPieces.get(j).getPiecePoint().equals(new Point(x - 200, y - 200))) {
                        break outerLoop;
                    }
                    if(x - 200 < 0 || y - 200 < 0) {
                        break outerLoop;
                    }
                }

            //Executes if all the conditinals are correct and return the Hash Map
            jumpableHash.replace("jumpableLeftUp", new Object[]{true, i});
            break outerLoop;
            }
        }

        //If there is a piece above and to the right, the next for loop enters
        outerLoop:
        for(int i = 0; i < boardPieces.size(); i++) {
            if(boardPieces.get(i).getPiecePoint().getX() == x + 100 && 
            boardPieces.get(i).getPiecePoint().getY() == y - 100 && 
            boardPieces.get(i).getSide().equals(enemy)) {

                //If there are no pieces where it wants to jump and the jump is in bounds
                for(int j = 0; j < boardPieces.size(); j++) {
                    if(boardPieces.get(j).getPiecePoint().equals(new Point(x + 200, y - 200))) {
                        break outerLoop;
                    }
                    if(x + 200 > 700 || y - 200 < 0) {
                        break outerLoop;
                    }
                }

            //Executes if all the conditinals are correct and return the Hash Map
            jumpableHash.replace("jumpableRightUp", new Object[]{true, i});
            break outerLoop;
            }
        }

        return jumpableHash;
    }
}