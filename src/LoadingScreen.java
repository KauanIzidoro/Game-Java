import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*

    Classe LoadingScreen possui uma explicação sobre a história do mapa
    e o objetivo do jogo.

 */
public class LoadingScreen extends Application {
    private Image portalImage;

    public LoadingScreen() {
            portalImage = new Image("res/screens/screen_loading.png");
    }

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: black;");

        if (portalImage != null) {
            ImageView imageView = new ImageView(portalImage);
            root.getChildren().add(imageView);
        }

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setTitle("Atemporal");
        primaryStage.setScene(scene);
        primaryStage.show();
        SoundsFX.playBackgroundMusic();
        setupKeyHandlers(scene,primaryStage);
    }

    private void setupKeyHandlers(Scene scene, Stage primaryStage) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    // Tecla espaço é a condição para troca a tela
                   GameEnvironment gameE = new GameEnvironment();
                   gameE.start(primaryStage);
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
