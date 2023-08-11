package mareza;


import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class StartController {
@FXML
    public TextField boN;
@FXML
    public ProgressBar progress;
    @FXML
    private AnchorPane pane;
    KeyCode keyCode;
    @FXML
    private Rectangle rect;
    public int speed;



    @FXML
    public Label txto;




    private static final int SIDE_SIZE = 20;
    private static final int CELL_SIZE = 40;

    private int currentX ;
    private int currentY ;

    int direction;
    int aa;

    private GridPane gridPane = new GridPane();
    private Rectangle player = new Rectangle(SIDE_SIZE / 2 * CELL_SIZE, SIDE_SIZE / 2 * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    PlayerLogic nodes =PlayerLogic.getInstance(SIDE_SIZE,CELL_SIZE);
    Weapons weapons=new Weapons(nodes);
    public void start(ActionEvent event) throws InterruptedException {
        Stage primaryStage= new Stage();
        nodes.fillGridPane(gridPane,0,0);
        player.setFill(nodes.getMainPlayer().getColor());
        speed=200;

        progress.setProgress(1);
        aa = Integer.parseInt(boN.getText());

        if(aa>4 || aa<0){ txto.setText("invalid");
        }

        else{





            //for bots---------------------------------------------------------
               switch (aa){
                   case 0:
                    break;
                   case 1:BotLogic bN= new BotLogic(SIDE_SIZE,CELL_SIZE,Level.MEDIUM);
                       Thread thread = new Thread(bN);
                       thread.start();
                       break;
                   case 2:
                       BotLogic bN1= new BotLogic(SIDE_SIZE,CELL_SIZE,Level.MEDIUM);
                       Thread thread1 = new Thread(bN1);
                       thread1.start();
                       BotLogic bN2= new BotLogic(SIDE_SIZE,CELL_SIZE,Level.MEDIUM);
                       Thread thread2 = new Thread(bN2);
                       thread2.start();
                       break;
                   case 3:BotLogic bN4= new BotLogic(SIDE_SIZE,CELL_SIZE,Level.MEDIUM);
                       Thread thread4 = new Thread(bN4);
                       thread4.start();
                       BotLogic bN3= new BotLogic(SIDE_SIZE,CELL_SIZE,Level.MEDIUM);
                       Thread thread3 = new Thread(bN3);
                       thread3.start();
                       BotLogic bN5= new BotLogic(SIDE_SIZE,CELL_SIZE,Level.MEDIUM);
                       Thread thread5 = new Thread(bN5);
                       thread5.start();
                       break;

            }
            Pane root = new Pane(gridPane,player);
            Scene scene = new Scene(root, SIDE_SIZE * CELL_SIZE, SIDE_SIZE * CELL_SIZE);
            primaryStage.setScene(scene);
            primaryStage.show();

            scene.setOnKeyPressed(kEvent -> {
                handleKeyPress(kEvent);
            });
        }



        //for manual move----------------------------------------

      /*  scene.setOnKeyPressed(kEvent -> {
            KeyCode keyCode = kEvent.getCode();
            switch (keyCode) {
                case W:
                    direction=1;
                    currentX--;
                    nodes.generateRow(currentX, false);
                    break;
                case S:
                    direction=3;
                    currentX++;
                    nodes.generateRow(currentX, true);
                    break;
                case D:
                    direction=0;
                    currentY++;
                    nodes.generateColumn(currentY, true);
                    break;
                case A:
                    direction=2;
                    currentY--;
                    nodes.generateColumn(currentY, false);
                    break;
                case ENTER:
                    weapons.weaponA(direction);
                case SPACE:
                    weapons.weaponB(direction);
            }
            nodes.fillGridPane(gridPane,currentX,currentY);
        });
*/

    }



    AnimationTimer timer=new AnimationTimer() {
        @Override
        public void handle(long now) {
            switch (keyCode) {
                case W:
                    direction=1;
                    currentX--;
                    nodes.generateRow(currentX, false);
                    break;
                case S:
                    direction=3;
                    currentX++;
                    nodes.generateRow(currentX, true);
                    break;
                case D:
                    direction=0;
                    currentY++;
                    nodes.generateColumn(currentY, true);
                    break;
                case A:
                    direction=2;
                    currentY--;
                    nodes.generateColumn(currentY, false);
                    break;
                case ENTER:
                    weapons.weaponA(direction);

                case SPACE:
                    weapons.weaponB(direction);

            }
            nodes.fillGridPane(gridPane,currentX,currentY);
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    };




    void handleKeyPress(KeyEvent e){
        keyCode=e.getCode();
        timer.start();
    }

}
