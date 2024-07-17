import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/*

 Classe ControlScreen é responsável por iniciar a tela
 de controles básicos de movimentação e ataque do player

 */
public class OptionScreen extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Control Screen");

        // Carregar a imagem
        Image image = new Image("res/screens/screen_controls_tips.png");
        ImageView imageView = new ImageView(image);

        // Instância do botão 'Sair'
        Button menuButton = new Button("Sair");
        menuButton.getStyleClass().add("menu-button");
        menuButton.setOnAction(e -> {
            SoundsFX.playClique();
            backToMenu(primaryStage);
        });

        // Configurar o layout para o botão
        VBox topLeftContainer = new VBox();
        topLeftContainer.setAlignment(Pos.TOP_LEFT);
        topLeftContainer.getChildren().add(menuButton);

        // Configurar o layout principal
        BorderPane root = new BorderPane();
        root.setTop(topLeftContainer);
        StackPane centerPane = new StackPane();
        centerPane.getChildren().add(imageView);
        root.setCenter(centerPane);
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

    private void backToMenu(Stage primaryStage) {
        System.out.println("Voltando para o menu principal!");
        GameInit gameI = new GameInit();
        gameI.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}