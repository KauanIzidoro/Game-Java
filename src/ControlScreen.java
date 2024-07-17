import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/*

 Classe ControlScreen é responsável por iniciar a tela
 de controles básicos de movimentação e ataque do player

 */
public class ControlScreen extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Control Screen");

        // Carregar a imagem
        Image image = new Image("res/screens/screen_controls_tips.png");
        ImageView imageView = new ImageView(image);

        // Configurar o layout
        VBox root = new VBox();
        // Centraliza o conteúdo dentro do 'root'
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(imageView);
        root.getStylesheets().add("styleMain.css");

        // Configurar a cena
        Scene scene = new Scene(root, 997, 597);
        scene.setFill(Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.show();
        SoundsFX.playBackgroundMusic();

        // método que faz a chamada da próxima tela
        setupKeyHandlers(scene, primaryStage);
    }

    private void setupKeyHandlers(Scene scene, Stage primaryStage) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    // Tecla espaço é a condição para troca a tela
                    LoadingScreen loadingScreen = new LoadingScreen();
                    loadingScreen.start(primaryStage);
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}