import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Objects;

/*

    Classe mapa, usada para ajustar os detalhes do mapa:
    imagem, dimensões e demais características.

 */
public class GameMap {

    // Armazenar o imagem do mapa na instância da classe ImageView
    private ImageView backgroundImageView;

    public GameMap(double width, double height) {
        Image mapa = new Image(Objects.requireNonNull(getClass().getResourceAsStream("res/map_res/map.jpg")));
        backgroundImageView = new ImageView(mapa);
        backgroundImageView.setFitHeight(height);
        backgroundImageView.setFitWidth(width);
    }

    public ImageView getBackgroundImageView() {
        return backgroundImageView;
    }
}
