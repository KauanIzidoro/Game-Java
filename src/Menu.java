import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Objects;

/*

    Classe Menu possui a primeira tela do jogo, aqui temos os botões 'Iniciar', 'Opções' e 'Sair', junto disso
    cada botão possui um método atribuido que direciona para a tela de seleção de personagem.

 */

public class Menu extends Application {

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Menu do Jogo");

        // Instância da imagem do logo
        Image logo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("res/screens/logo.png")));
        ImageView logoView = new ImageView(logo);

        // Ajuste da dimensão da imagem
        logoView.setFitWidth(300); //Ajuste de largura
        logoView.setPreserveRatio(true); //Mantém a proporção da imagem

        // Configurar a animação de transição para movimentar o logo
        TranslateTransition transition = new TranslateTransition(Duration.seconds(2), logoView);

        transition.setToY(-20); // Mover para cima 20 pixels

        transition.setCycleCount(TranslateTransition.INDEFINITE); // Repetir infinitamente
        transition.setAutoReverse(true); // Alternar a direção da animação

        // Iniciar a animação
        transition.play();

        // Instância do botão 'Iniciar Jogo'
        Button startButton = new Button("Iniciar Jogo");
        startButton.getStyleClass().add("menu-button"); //Importa a classe de estilo do arquivo css

        // Configura a ação que o botão vai realizar quando pressionado
        startButton.setOnAction(e -> {
            startGame(primaryStage);
            SoundsFX.playClique();
        });

        // Instância do botão 'Opções'
        Button settingsButton = new Button("Opções");
        settingsButton.getStyleClass().add("menu-button");
        settingsButton.setOnAction(e -> {
            openSettings(primaryStage);
            SoundsFX.playClique();
        });

        // Instância do botão 'Sair'
        Button exitButton = new Button("Sair");
        exitButton.getStyleClass().add("menu-button");
        exitButton.setOnAction(e -> {
            SoundsFX.playClique();
            primaryStage.close();
        });

        // Ajuste de layout da tela de menu
        VBox menuLayout = new VBox(20);

        // Atribuindo os botões à Vbox
        menuLayout.getChildren().addAll(logoView, startButton, settingsButton, exitButton);
        menuLayout.getStyleClass().add("menu-layout"); //Recebe o estilo do arquivo css

        // Instância da cena principal
        Scene mainScene = new Scene(menuLayout, 1000, 600);
        // Atribuindo classes de estilo à cena principal
        mainScene.getStylesheets().add("styleMain.css");
        // Atribuindo cena ao primaryStage
        primaryStage.setScene(mainScene);
        SoundsFX.playBackgroundMusic();
        primaryStage.show();
    }

    private void openSettings(Stage primaryStage) {
        //Aqui devemos implementar a próxima tela que deve ser chamada quando o botão
        // de opções for pressionado
        System.out.println("Tela de opções");
        OptionScreen optionScreen = new OptionScreen();
        optionScreen.start(primaryStage);
    }

    // Este método é responsável por conectar este 'Stage' ao 'Stage' da Classe Move que por sua vez é a classe que roda o jogo
    private void startGame(Stage primaryStage) {
//        // Acessar o método start() da Classe Move e faz com que o primaryStage seja chamado
//        // assim que for obeservado a ação do botão 'Iniciar Jogo' for pressionado
        GameEnvironment gameE = new GameEnvironment();
        gameE.start(primaryStage);


    }

    public static void main(String[] args) {
        launch(args);
    }

}