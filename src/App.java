import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Dinosaur Game by ANK ");
        int boardheight = 400;
        int boaradwidth = 900;
       //setting all the attrinbute of this frame..
       frame.setSize(boaradwidth,boardheight);
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setLocationRelativeTo(null);
       frame.setResizable(false); 
       frame.setVisible(true);
             
       ChromeDinosaur dino = new ChromeDinosaur();
       frame.add(dino);
       frame.pack();
       dino.requestFocus();
       frame.setVisible(true);

    }
}
