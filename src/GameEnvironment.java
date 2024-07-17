import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.util.Objects;

/*

    Classe GameEnvironment é responsável por criar os obstáculos do mapa
    nas devidas posições.

 */

public class GameEnvironment extends Application {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;
    MapObstacles[] obstacles;
    Hud[] hud;

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();
        Scene scene = new Scene(pane, WIDTH, HEIGHT);
        // Call playThemesong with a desired volume level (e.g., 0.75 for 75% volume)
        SoundsFX.playThemesong(0.15f);

        // Instância do mapa e do player
        GameMap gameMap = new GameMap(WIDTH, HEIGHT);
        Player player = new Player(200, HEIGHT/2);
        pane.getChildren().add(gameMap.getBackgroundImageView());
        pane.getChildren().add(player.getImageView());

        // Posicionamento dos obstáculos do mapa em uma camada acima a do player
        obstacles = new MapObstacles[5];
        // Imagem da pedra em uma camada acima do player
        obstacles[0] = new MapObstacles(216,416,0);
        // Árvore
        obstacles[1] = new MapObstacles(349,361,1);
        // Altar
        obstacles[2] = new MapObstacles(105,246,2);
        // Coluna
        obstacles[3] = new MapObstacles(483,520,3);
        

        obstacles[4] = new MapObstacles(0,0,4);

        obstacles[4].setWidth(1000, 600);


        for (int i = 0; i < obstacles.length; i++) {
            pane.getChildren().add(obstacles[i].getMap_obj(i));
            obstacles[i].toFront();
        }

        // Posicionamento do HUD na tela
        hud = new Hud[1];
        // Player HUD
        hud[0] = new Hud(0,0);

        for (int i = 0; i < hud.length; i++) {
            pane.getChildren().add(hud[i].getPlayer_life(0));
            hud[i].toFront();
        }

        // Instância da classe GameLoop para acessar o método start()
        GameLoop gameLoop = new GameLoop(scene, player, pane, primaryStage);
        gameLoop.start();

        // Configuração da tela 'primaryStage'
        primaryStage.setTitle("Atemporal");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("res/screens/logo.png"))));
        SoundsFX.stopBackgroundMusic();
        primaryStage.show();

        // Atribuir foco aos eventos do painel
        pane.requestFocus();
    }




    public int getSceneWidth(){
        return WIDTH;
    }
    public int getSceneHeight(){
        return HEIGHT;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
