import javax.swing.*;

public class App {
    public static void main(String[] args)  {
       int boardWidth = 360;
       int boardHeight = 640;

       JFrame frame = new JFrame("Flappy Bird");
       frame.setVisible(true);
       frame.setSize(boardWidth, boardHeight);
       frame.setLocationRelativeTo(null);
       frame.setResizable(false);
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       Flappy flappy = new Flappy();
       frame.add(flappy);
       frame.pack();
       flappy.requestFocus();
       frame.setVisible(true);
       
    }
}
