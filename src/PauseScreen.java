import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Objects;

/*

    Classe PauseScreen possui a tela que será exibida quando o
    player pausar o jogo.

 */
public class PauseScreen extends Application {

    private static final double WIDTH = 1000;
    private static final double HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Atemporal");

        // Logo de Atemporal
        Image gamepause = new Image(Objects.requireNonNull(getClass().getResourceAsStream("res/screens/gamepause.gif")));
        ImageView gamePause = new ImageView(gamepause);
        gamePause.setFitWidth(300); // Ajuste de largura da logo
        gamePause.setPreserveRatio(true); // Mantém a proporção da imagem

        // Configurar a animação de transição para movimentar o logo
        TranslateTransition transition = new TranslateTransition(Duration.seconds(3), gamePause);
        transition.setToY(-10); // Mover para cima 20 pixels
        transition.setCycleCount(TranslateTransition.INDEFINITE); // Repetir infinitamente
        transition.setAutoReverse(true); // Alternar a direção

        // Iniciar a animação
        transition.play();

        // Botão continuar
        Button continueButton = new Button("Continue");
        continueButton.getStyleClass().add("menu-button");
        continueButton.setOnAction(e -> {
            game_Loop(primaryStage);
            SoundsFX.playClique();
        });

        // Botão BackToMenu
        Button menuButton = new Button("Menu");
        menuButton.getStyleClass().add("menu-button");
        menuButton.setOnAction(e -> {
            backMenu(primaryStage);
            SoundsFX.playClique();
        });

        // Organize components in a layout container
        VBox layout = new VBox(20); // VBox with 20px spacing
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(gamePause, continueButton, menuButton);

        // Cena principal
        Scene mainScene = new Scene(layout, WIDTH, HEIGHT);
        mainScene.setFill(Color.BLACK);
        mainScene.getStylesheets().add("styleMain.css"); // arquivo css com as estilizações
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private void game_Loop(Stage primaryStage) {
        System.out.println("Continuando a fase...");
        GameEnvironment gameE = new GameEnvironment();
        gameE.start(primaryStage);
    }

    private void backMenu(Stage primaryStage) {
        System.out.println("Voltando para o menu principal!");
        GameInit gameI = new GameInit();
        gameI.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}