import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.random.*;
import javax.swing.*;


public class Flappy extends JPanel implements ActionListener, KeyListener{ 
     int boardWidth =360;
     int boardHeight = 640;

     //Images
     Image backgroundImg;
     Image birdImg;
     Image topPipeImg;
     Image bottonPipeImg;


     //Bird
     int birdX = boardWidth/8;
     int birdY = boardHeight/2;
     int birdWidth = 34;
     int birdHeight = 24;

    class Bird{
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img){
            this.img = img;
        }
    }


    //Pipes
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;   //scaled by 1/6
    int pipeHeight = 500;


    class Pipe{
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;


        Pipe(Image img){
            this.img = img;
        }
    }

    //game logic
    Bird bird;
    int velocityX = -4;
    int velocityY = 0; 
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipesTimer;

    boolean gameOver = false;

    double score = 0;


     Flappy(){
        setPreferredSize(new Dimension( boardWidth , boardHeight));
       // setBackground(Color.BLUE);

       setFocusable(true);
       addKeyListener(this);

       // Debug: Check if files are accessible
       System.out.println("Background: " + getClass().getResource("/resources/background.jpg"));
       System.out.println("Bird: " + getClass().getResource("/resources/iwaza.png"));
       System.out.println("Top Pipe: " + getClass().getResource("/resources/pipes1.png"));
       System.out.println("Bottom Pipe: " + getClass().getResource("/resources/pipes2.png"));

        //load images
        backgroundImg = new ImageIcon(getClass().getResource("/resources/background.jpg")).getImage();
        birdImg = new ImageIcon(getClass().getResource("/resources/iwaza.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("/resources/pipes1.png")).getImage();
        bottonPipeImg = new ImageIcon(getClass().getResource("/resources/pipes2.png")).getImage();
    

        //bird
        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();


        //place pipes timer
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                placePipes();
            }
        });

        placePipesTimer.start();

        //game timer
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    public void placePipes(){
        //(0-1)* pipeheight/2 ->  (0-256)
        //128
        //0 - 128 - (0-256) --> 
        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = boardHeight/4;
        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe (bottonPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);


    }


    public void draw(Graphics g){
        System.out.println("draw");
        //background
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

        //bird
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);


        //pipes
        for (int i = 0 ; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width,pipe.height, null);
        }

        //score
        g.setColor(Color.red);
        g.setFont(new Font("Ariel" , Font.PLAIN, 32));
        if(gameOver){
            g.drawString("Game Over : " + String.valueOf((int) score), 10, 35);
        }
        else{
            g.drawString(String.valueOf((int) score), 10, 35);
        }

    }


    public void move(){
        //bird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y,0);


        //pipes
        for(int i = 0 ; i <pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if(!pipe.passed && bird.x > pipe.x + pipe.width){
                pipe.passed = true;
                score += 0.5;
            }

            if (collision(bird, pipe)){
                gameOver = true;
            }
        }

        if (bird.y >boardHeight){
            gameOver = true;
        }
    }

    public boolean collision (Bird a, Pipe b){
        return a.x < b.x + b.width && 
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y ;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
      repaint();
      if (gameOver){
        placePipesTimer.stop();
        gameLoop.stop();
      }
    }





    @Override
    public void keyPressed(KeyEvent e) {
       if (e.getKeyCode() == KeyEvent.VK_SPACE){
        velocityY = -9;
        if (gameOver){
            //restart the game 
            bird.y = birdY;
            velocityY = 0;
            pipes.clear();
            score = 0 ;
            gameOver = false;
            gameLoop.start();
            placePipesTimer.start();
        }
       }
    }


    @Override
    public void keyTyped(KeyEvent e) {
       
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
    
}
