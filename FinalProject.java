
// a lot of imported stuff that I don't really understand.
import java.util.Scanner;
import java.io.*;
import javax.swing.*;
import javax.sound.sampled.Clip;
import javax.swing.event.*;
public class FinalProject{
   // just a normal colour array
   public static final String [] COLOUR = {"yellow", "blue", "cyan", "green", "pink", "white", "red", "gray", "black"};
   // Board to pick the colour
   public static Board1 pick = new Board1 (10);
   // Board for the actual game
   private static Board game = new Board (9,9);
   // Array that keeps what user put
   private static String act [] [] = new String [9] [9];
   // Array that has the answers
   private static String real [] [] = new String [9] [9];
   // Array that has the original pegs that were on the Board
   public static String og [] [] = new String [9] [9];
   // Color that User picked
   static String clr = "";
   // 3D Array that keeps track of 9 boxes in Sudoku
   static String [] [] [] sq = new String [9] [3] [3];
   // Some Booleans needed for cheating
   static boolean red = false, aut = false, mac = false, blb = false, beg = false;
   // Just Row and Colomn
   static int row = 0, col = 0;
   // Boolean to check whether the game should end or not
   static boolean gameOver = false;
   // method to set up the Board
   public static void setUp (String map) throws IOException{
      game.drawLine(0, 3, 8, 3);          // drawing lines for the Board
      game.drawLine(0, 6, 8, 6);
      game.hdrawLine(3, 0, 3, 8);         // drawing horizontal lines for the Board
      game.hdrawLine(6, 0, 6, 8);
      File dataFile = new File(map);      // inputting file
      Scanner scanFile = new Scanner(dataFile);
      int n = 0, value = 0, n1 = 0, n2 = 0;
      for (int i = 0; i < 9; i++){        // put 9 colours on the Board.
         pick.putPeg(COLOUR [i], i);
      }
      // This is Beginner Level
      if (beg){
         int count = (int)(Math.random()*8);
         int num = 0, c = 0, o = count;
         for (int j = 0; j < 9; j++){
            for (int i = 0; i < 9; i++){
               if (count > 8){
                  count = count - 9;
               }
               real [j] [i] = COLOUR [count];
               n = (int)(Math.random() * 2 + 1);
               if (n%2 == 0){
                  game.putPeg(COLOUR[count], j, i);
                  act [j] [i] = COLOUR [count];
                  og [j] [i] = COLOUR [count];
               }
               count++;
            }
            if (c == 2){
               num = 1;
               count = o + num;
            }
            else if (c == 5){
               num = 2;
               count = o + num;
            }
            else {
               count += 3;
            }
            c++;
         }
      }
      // Actual Game
      else{
         while (scanFile.hasNext()){
            value = scanFile.nextInt();
            if (value == 10){
               break;
            }
            else if (value > 0){
               game.putPeg(COLOUR [value - 1], n1, n2);
               act [n1] [n2] = COLOUR [value - 1];
               og [n1] [n2] = COLOUR [value - 1];
               n2++;
            }
            else {
               n2++;
            }
            if (n2 == 9){
               n1++;
               n2 = 0;
            }
         }
         n1 = 0;
         n2 = 0;
         while (scanFile.hasNext()){
            value = scanFile.nextInt();
            if (n1 == 10){
               break;
            }
            real [n1] [n2] = COLOUR [value - 1];
            n2++;
            if (n2 == 9){
               n1++;
               n2 = 0;
            }
         }
      }
   }
   // method to play the music
   public static void music (){
      Clip musica = JCanvas.loadClip("nf.wav");
      JCanvas.loopClip(musica, 200);
   }
   // Filling the 3D Array in squares
   static void fill (){
      int c1 = 0, c2 = 0, r = 0, count = 0, n = 0, c = 0;
      while (c != 27){
         for (int k = 0; k < 3; k++){
            sq [r] [n] [k] = act [c1] [c2];
            c2++;
            c++;
            if (c2 == 9){
               c2 = 0;
               c1++;
            }
            if (c2%3 == 0){
               r++;
            }
            if (count == 3){
               count = 0;
               break;
            }
            if (r == 3){
               r = 0;
               count++;
               n++;
            }
         }
      }
      c2 = 0; r = 3; count = 0; n = 0; c = 0;
      while (c != 27){
         for (int k = 0; k < 3; k++){
            sq [r] [n] [k] = act [c1] [c2];
            c2++;
            c++;
            if (c2 == 9){
               c2 = 0;
               c1++;
            }
            if (c2%3 == 0){
               r++;
            }
            if (count == 3){
               count = 0;
               break;
            }
            if (r == 6){
               r = 3;
               count++;
               n++;
            }
         }
      }     
      c2 = 0; r = 6; count = 0; n = 0; c = 0;
      while (c != 27){
         for (int k = 0; k < 3; k++){
            sq [r] [n] [k] = act [c1] [c2];
            c2++;
            c++;
            if (c2 == 9){
               c2 = 0;
               c1++;
            }
            if (c2%3 == 0){
               r++;
            }
            if (count == 3){
               count = 0;
               break;
            }
            if (r == 9){
               r = 6;
               count++;
               n++;
            }
         }
      }   
   }
   // Getting colour from the colour Board
   static void colour (){
      Coordinate clickSpot;
      clickSpot = pick.getClick();
      int col = clickSpot.getCol();
      if (col == 9){          // when you want to remove the peg you put
         game.displayMessage("You want to remove something!");
         clr = null;
      }
      else{                // when you picked the colour
         clr = COLOUR [col];
         game.displayMessage("You picked " + COLOUR [col] + "!");
      }
   }
   // method to check if the user can put the peg on the spot they picked
   public static boolean check (String c){
      int box = 0;
      boolean rite = true;
      for (int i = 0; i < 9; i++){     // checking horizontally and vertically to see if there is a peg of same colour
         if (c.equals(act [row] [i]) || c.equals(act [i] [col])){
            rite = false;
         }
      }
      // checking the boxes
      if (row < 3 && col < 3){
         box = 0;
      }
      else if (row < 3 && col < 6){
         box = 1;
      }
      else if (row < 3 && col < 9){
         box = 2;
      }
      else if (row < 6 && col < 3){
         box = 3;
      }
      else if (row < 6 && col < 6){
         box = 4;
      }
      else if (row < 6 && col < 9){
         box = 5;
      }
      else if (row < 9 && col < 3){
         box = 6;
      }
      else if (row < 9 && col < 6){
         box = 7;
      }
      else if (row < 9 && col < 9){
         box = 8;
      }
      for (int i = 0; i < 3; i++){
         for (int j = 0; j < 3; j++){
            if (c.equals(sq [box] [i] [j])){
               rite = false;
            }
         }
      }
      return rite;      // return the boolean that tells if the move was valid or not
   }
   public static void finalmath () throws IOException, InterruptedException{
      int num = 0, no = 0;
      boolean sec = false;
      music();       // run the music method
      String m = "", ch = "";
      int n = (int)(Math.random()*18) + 1;
      // importing a random map from the text file
      String numb = String.valueOf(n);
      m = "Map" + 1 + ".txt";
      // JPanel things to see if user wants to play the game
      int opt = JOptionPane.showConfirmDialog(null, "Do you want to play Sudoku?", "Click Yes", JOptionPane.YES_NO_OPTION);
      if (opt == 0){
         JOptionPane.showMessageDialog(null, "Good Choice!");
      }
      else if (opt == 1){
         while (sec != true){       // But I will force them to play        
            opt = JOptionPane.showConfirmDialog(null, "Liar, you want to play Sudoku, right?", "Click Yes",JOptionPane.YES_NO_OPTION);
            if (opt == 0){
               JOptionPane.showMessageDialog(null, "Good Choice!");
               sec = true;
            }
            else {
               JOptionPane.showMessageDialog(null, "Wow, I am sad.");
               no++;
               Thread.sleep(500);
            }
            if (no%3 == 0){         // Some cheating codes
               while (sec != true){
                  ch = JOptionPane.showInputDialog(null, "You sure you want to quit? (Say No!)");
                  ch = ch.toLowerCase();
                  switch(ch){
                     case "yes":    // This case will keep looping
                        JOptionPane.showMessageDialog(null, "Stop Lying!");
                        break;
                     case "no":     // This case will break the loop
                        JOptionPane.showMessageDialog(null, "Good Choice!");
                        sec = true;
                        break;
                     case "redmoon":      // This case will give you some hints
                        JOptionPane.showMessageDialog(null, "Shhhhh......");
                        red = true;
                        break;
                     case "authentication":     // This case will let you remove anything and put anything
                        JOptionPane.showMessageDialog(null, "How did you know that word?");
                        aut = true;
                        break;
                     case "macbeth":      // This case will tell you whether it is right or wrong
                        JOptionPane.showMessageDialog(null, "Be quiet!");
                        mac = true;
                        break;
                     case "bloodborne":      // this will make a completely new cheating loop
                        JOptionPane.showMessageDialog(null, "Stop!!");
                        blb = true;
                        sec = true;
                        break;
                     default:
                        JOptionPane.showMessageDialog(null, "Not the word I wanted.");
                        break;
                  }
               }
            }
         }
      }
      Thread.sleep(500);
               // Checking if the user is a beginner Sudoku player
      int cho = JOptionPane.showConfirmDialog(null, "Are you a beginner?", "Are you bad at playing Sudoku?", JOptionPane.YES_NO_OPTION);
      if (cho == 0){
         JOptionPane.showMessageDialog(null, "Then you should play a beginner level!");
         beg = true;
      }
      else {
         JOptionPane.showMessageDialog(null, "Let's play some hard levels then!");
      }
      setUp(m); // Setting up the Board
      fill();  // And filling it
      // This is the cheating main
      while(blb){
         Coordinate clickSpot;
         // Pick Colour
         colour();
         // Get the Coordinate
         clickSpot = game.getClick();
         row = clickSpot.getRow();
         col = clickSpot.getCol();
         for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
            // It puts peg on all 9 spots
               if (clr != null && clr.equals(real [i] [j])){
                  game.putPeg(clr, i , j);
                  act [row] [col] = clr;
                  fill();
                  game.displayMessage("Good Job");
               }
            }
         }
         for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
               if (act [i] [j] != null){
                  num++;      // Checks to see if it ends
               }
            }
         }
         if (num == 82){
            game.displayMessage("Congratulations!");     // GG!
            return;
         }
         else {
            num = 0;       // Or not.
         }
      }
      // Actual game
      while (!gameOver){
         Coordinate clickSpot;
         // Pick Colour
         colour();
         // Get the Coordinate
         clickSpot = game.getClick();
         row = clickSpot.getRow();
         col = clickSpot.getCol();
         if (clr == null){                       // Removing the peg the user put it
            if (og [row] [col] == null){         // Making sure the user is not removing the original peg
               if (act [row] [col] == null){     // When user tries to remove and empty spot
                  if (red){                      // With Cheat nothing is possible
                     game.displayMessage("That spot is supposed to be " + real [row] [col]); 
                  }
                  else {
                     game.displayMessage("There is nothing to remove!");
                  }
               }
               else {
                  game.removePeg(row, col);
                  act [row] [col] = clr;
                  fill();
                  game.displayMessage("Good Job!");
               }
            }
            else if (og [row] [col] != null){   // you cant't remove the original peg!
               if (aut){                        // but if you activate the cheat you can...
                  game.removePeg(row, col);
                  act [row] [col] = clr;
                  fill();
                  game.displayMessage("Good Job!");
               }
               else{
                  game.displayMessage("Can't do that young kid.");
               }
            }
         }
         else if (clr != null){              // putting some colour on the Board.
            if (og [row] [col] != null){     // checking to see if the spot user clicked is empty
               if (aut){                     // With the aids of some cheats, you can put the peg anywhere you want.
                  game.putPeg(clr, row, col);
                  act [row] [col] = clr;
                  fill();
                  game.displayMessage("Good Job!");
               }
               else{                         // Without Cheat, it won't let you do that
                  game.displayMessage("Can't do that young kid.");
               }
            }
            else if (check(clr)){            // Checking to see if there is same colour on its box, horizontally and vertically
               game.putPeg(clr, row, col);
               act [row] [col] = clr;
               fill();
               game.displayMessage("Good Job!");
            }
            else{
               game.displayMessage("Can't put it there!");
            }
         }
         if (mac){                                                // Cheat tells you right answer
            if (clr != null && clr.equals(real [row] [col])){     // If you put the peg in a correct place.
               game.displayMessage("That is Correct!");
            }
            else{
               game.displayMessage("I think that one is supposed to be " + real [row] [col]);   //If you put it in an incorrect spot, it tells you the answer.
            }
         }
         for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
               if (act [i] [j] != null){
                  num++;                  // counting to see if the Board is full
               }
            }
         }
         if (num == 82){
            gameOver = true;           // If it is, break the while loop.
         }
         else {
            num = 0;                   // Or not.
         }
      }
      game.displayMessage("Congratulations!!!");      //Congratulations!!!
   }
   // MAIN
   public static void main (String [] args) throws IOException, InterruptedException{
      finalmath();
   }
}