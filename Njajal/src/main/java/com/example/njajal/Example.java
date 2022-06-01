package com.example.njajal;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;

public class Example extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Example.fxml"));
        stage.setTitle("Test Background");

        Group root =new Group();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        Canvas canvas = new Canvas(711,400);
        root.getChildren().add(canvas);

        ArrayList<String> input = new ArrayList<String>();

        scene.setOnKeyPressed(
                new EventHandler<KeyEvent>()
                {
                    public void handle(KeyEvent e)
                    {
                        String code = e.getCode().toString();
                        if ( !input.contains(code) )
                            input.add( code );
                    }
                });

        scene.setOnKeyReleased(
                new EventHandler<KeyEvent>()
                {
                    public void handle(KeyEvent e)
                    {
                        String code = e.getCode().toString();
                        input.remove( code );
                    }
                });

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Font theFont = Font.font( "Helvetica", FontWeight.BOLD, 24 );
        gc.setFont( theFont );
        gc.setFill( Color.GREEN );
        gc.setStroke( Color.BLACK );
        gc.setLineWidth(1);

        Sprite jaring = new Sprite();
        jaring.setImage("file:D:/Kelas E Informatika UAD/Semester 3/PBO/Tugas Akhir/Njajal/assets/jaring.png");
        jaring.setPosition(200, 0);

        ArrayList<Sprite> jellyfishList = new ArrayList<Sprite>();

        for (int i = 0; i < 15; i++)
        {
            Sprite jellyfish = new Sprite();
            jellyfish.setImage("file:D:/Kelas E Informatika UAD/Semester 3/PBO/Tugas Akhir/Njajal/assets/jellyfish1.png");
            double px = 350 * Math.random() + 50;
            double py = 350 * Math.random() + 50;
            jellyfish.setPosition(px,py);
            jellyfishList.add( jellyfish );
        }

        LongValue lastNanoTime = new LongValue( System.nanoTime() );

        IntValue score = new IntValue(0);

        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                lastNanoTime.value = currentNanoTime;

                // game logic

                jaring.setVelocity(0,0);
                if (input.contains("LEFT"))
                    jaring.addVelocity(-70,0);
                if (input.contains("RIGHT"))
                    jaring.addVelocity(70,0);
                if (input.contains("UP"))
                    jaring.addVelocity(0,-70);
                if (input.contains("DOWN"))
                    jaring.addVelocity(0,70);

                jaring.update(elapsedTime);

                // collision detection

                Iterator<Sprite> moneybagIter = jellyfishList.iterator();
                while ( moneybagIter.hasNext() ) {
                    Sprite moneybag = moneybagIter.next();
                    if ( jaring.intersects(moneybag) ) {
                        moneybagIter.remove();
                        score.value++;
                    }
                }

                //render
                gc.clearRect(0,0,711,400);
                Image bg = new Image("file:D:/Kelas E Informatika UAD/Semester 3/PBO/Tugas Akhir/Njajal/assets/bg2.jpg");
                gc.drawImage(bg,0,0);
                jaring.render(gc);


                for (Sprite jellyfish : jellyfishList)
                    jellyfish.render(gc);

                String pointsText = "Cash: $" + (100 * score.value);
                gc.fillText( pointsText, 360, 36 );
                gc.strokeText( pointsText, 360, 36 );
            }
        }.start();



        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
