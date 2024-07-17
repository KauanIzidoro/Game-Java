import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import java.util.Objects;

/*

    Classe Player, esta classe é responsável por configurar os atributor e métodos do jogador
    altura, largura, velocidade, imagem, movimentação e colisão com os limites do mapa.

*/
public class Player extends ImageView {
    private final double PLAYER_HEIGHT = 74.0;
    private final double PLAYER_WIDTH = 55.0;
    private static final double WIDTH = 1000;
    private static final double HEIGHT = 600;
    private double speed = 5;
    private double life;
    private double damage;
    private MapObstacles[] obstacles;
    private ImageView imageView;
    private Image[] kn_run_r;
    private Image[] kn_run_l;
    private Image[] kn_atk_r;
    private Image[] kn_atk_l;
    private Image [] kn_dead_r;
    private Image [] kn_dead_l;
    private Image kn_idle_r;
    private Image kn_idle_l;
    private int indexRun = 0;
    private int indexAtk = 0;
    private int indexDead = 0;
    private Timeline runR;
    private Timeline runL;
    private Timeline atkR;
    private Timeline atkL;
    private Timeline deadR;
    private Timeline deadL;

    public Player(double startX, double startY) {
        kn_idle_r = new Image(Objects.requireNonNull(getClass().getResourceAsStream("res/knight_res/kn_idle_r.png")));
        kn_idle_l = new Image(Objects.requireNonNull(getClass().getResourceAsStream("res/knight_res/kn_idle_l.png")));
        imageView = new ImageView(kn_idle_r);
        imageView.setX(startX);
        imageView.setY(startY);
        include_kn_images();
        include_kn_animations();
        include_obstacles();
    }

    //Método para inicializar as imagens
    private void include_kn_images() {
        kn_run_r = new Image[7];
        kn_run_l = new Image[7];
        kn_atk_r = new Image[5];
        kn_atk_l = new Image[5];
        kn_dead_r = new Image[6];
        kn_dead_l = new Image[6];
        for (int i = 0; i < kn_dead_r.length; i++) {
            kn_dead_r[i] = new Image("res/knight_res/kn_dead_r" + i + ".png");
            kn_dead_l[i] = new Image("res/knight_res/kn_dead_l" + i + ".png");
        }
        for (int i = 0; i < kn_atk_r.length; i++) {
            kn_atk_r[i] = new Image("res/knight_res/kn_atk_r" + i + ".png");
            kn_atk_l[i] = new Image("res/knight_res/kn_atk_l" + i + ".png");
        }
        for (int i = 0; i < kn_run_r.length; i++) {
            kn_run_r[i] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("res/knight_res/kn_run_r" + i + ".png")));
            kn_run_l[i] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("res/knight_res/kn_run_l" + i + ".png")));
        }

    }

    private void include_kn_animations() {
        // Observe o uso de um contador circular para cada Timeline
        // é necessário esta manipulação para que  a variável de controle seja sempre
        // um valor entre 0 e array.length - 1, de forma a criar um ciclo de imagens.

        // corrida
        runR = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            indexRun = (indexRun + 1) % kn_run_r.length;
            imageView.setImage(kn_run_r[indexRun]);
        }));
        runR.setCycleCount(Timeline.INDEFINITE);
        runL = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            indexRun = (indexRun + 1) % kn_run_l.length;
            imageView.setImage(kn_run_l[indexRun]);
        }));
        runL.setCycleCount(Timeline.INDEFINITE);

        // ataque
        atkR = new Timeline(new KeyFrame(Duration.millis(150), e -> {
            indexAtk = (indexAtk + 1) % kn_atk_r.length;
            imageView.setImage(kn_atk_r[indexAtk]);
        }));
        // cada animação executada incrementa em uma unidade a váriavel 'indexAtk',
        // se usarmos como parâmetro o comprimento do vetor de imagens para animação
        // teremos percorrido o array inteiro a cada chamada do método na Classe GameLoop
        atkR.setCycleCount(kn_atk_r.length);
        atkL = new Timeline(new KeyFrame(Duration.millis(150), e -> {
            indexAtk = (indexAtk + 1) % kn_atk_l.length;
            imageView.setImage(kn_atk_l[indexAtk]);
        }));
        atkL.setCycleCount(kn_atk_l.length);

        // morte
        deadR = new Timeline(new KeyFrame(Duration.millis(300), e -> {
            indexDead = (indexDead + 1) % kn_dead_r.length;
            imageView.setImage(kn_dead_r[indexDead]);
        }));
        deadR.setCycleCount(kn_dead_r.length);
        deadL = new Timeline(new KeyFrame(Duration.millis(300), e -> {
            indexDead = (indexDead + 1) % kn_dead_l.length;
            imageView.setImage(kn_dead_l[indexDead]);
        }));
        deadL.setCycleCount(kn_dead_l.length);
    }

    public ImageView getImageView() {return imageView;}

    public void include_obstacles(){
        // Posicionamento dos obstáculos do mapa em uma camada acima a do player
        obstacles = new MapObstacles[4];
        // Imagem da pedra em uma camada acima do player
        obstacles[0] = new MapObstacles(216,416,0);
        // Árvore
        obstacles[1] = new MapObstacles(349,361,1);
        // Altar
        obstacles[2] = new MapObstacles(105,246,2);
        // Coluna
        obstacles[3] = new MapObstacles(483,520,3);
    }


    // Verificações para q0,q1,q2:

    // Verificação para q0 - pedra
    public boolean player_rock_collision_q0(){
        return imageView.getX() + PLAYER_WIDTH/2 > 216 &&
                imageView.getX() + PLAYER_WIDTH/2 < 216+55 &&
                imageView.getY() + PLAYER_HEIGHT < 510 &&
                imageView.getY() + PLAYER_HEIGHT > 500;
    }

    // Verificar para q1 - pedra
    public boolean player_rock_collision_q1(){
        return imageView.getX() + PLAYER_WIDTH > 216 &&
                    imageView.getX() + PLAYER_WIDTH < 216+55 &&
                    imageView.getY() + PLAYER_HEIGHT < 510 &&
                    imageView.getY() + PLAYER_HEIGHT > 500;

    }

    // Verificar para q2 - pedra
    public boolean player_rock_collision_q2(){
        return imageView.getX()  > 216 &&
                imageView.getX()  < 216+55 &&
                imageView.getY() + PLAYER_HEIGHT < 510 &&
                imageView.getY() + PLAYER_HEIGHT > 500;
    }

    // Verificar para q0 - altar
    public boolean player_alt_collision_q0(){
        return imageView.getX() + PLAYER_WIDTH/2 > 105 &&
                imageView.getX() + PLAYER_WIDTH/2 < 105+62 &&
                imageView.getY() + PLAYER_HEIGHT < 385 &&
                imageView.getY() + PLAYER_HEIGHT > 375;
    }

    // Verificar para q1 - altar
    public boolean player_alt_collision_q1(){
        return imageView.getX() + PLAYER_WIDTH > 105 &&
                imageView.getX() + PLAYER_WIDTH < 105+62 &&
                imageView.getY() + PLAYER_HEIGHT < 385 &&
                imageView.getY() + PLAYER_HEIGHT > 375;

    }

    // Verificar para q2 - altar
    public boolean player_alt_collision_q2(){
        return imageView.getX() > 105 &&
                imageView.getX() < 105+62 &&
                imageView.getY() + PLAYER_HEIGHT > 385 &&
                imageView.getY() + PLAYER_HEIGHT < 375;
    }

    // Verificações para p0,p1,p2:

    // Verificação para p0 - pedra
    public boolean player_rock_collision_p0(){
        return imageView.getX() + PLAYER_WIDTH/2 > 216 &&
                imageView.getX() + PLAYER_WIDTH/2 < 216+55 &&
                imageView.getY() < 510 &&
                imageView.getY() > 500;
    }

    // Verificação para p1 - pedra
    public boolean player_rock_collision_p1(){
        return imageView.getX() + PLAYER_WIDTH > 216 &&
                imageView.getX() + PLAYER_WIDTH < 216+55 &&
                imageView.getY() < 510 &&
                imageView.getY() > 500;
    }

    // Verificação para p2 - pedra
    public boolean player_rock_collision_p2(){
        return imageView.getX() > 216 &&
                imageView.getX() < 216+55 &&
                imageView.getY() < 510 &&
                imageView.getY() > 500;
    }

    // Verificação para p0 - altar
    public boolean player_alt_collision_p0(){
        return imageView.getX() + PLAYER_WIDTH/2 > 105 &&
                imageView.getX() + PLAYER_WIDTH/2 < 105+62 &&
                imageView.getY() < 385 &&
                imageView.getY() > 375;
    }

    // Verificar para p1 - altar
    public boolean player_alt_collision_p1(){
        return imageView.getX() + PLAYER_WIDTH > 105 &&
                imageView.getX() + PLAYER_WIDTH < 105+62 &&
                imageView.getY() < 385 &&
                imageView.getY() > 375;
    }

    // Verificar para p2 - altar
    public boolean player_alt_collision_p2(){
        return imageView.getX() > 105 &&
                imageView.getX() < 105+62 &&
                imageView.getY() > 385 &&
                imageView.getY() < 375;
    }

//    // Verificar colisão com o boss
//    public boolean player_collision_boss(Boss boss){
//        return imageView.getX() + PLAYER_WIDTH > boss.getX() &&
//                imageView.getX() + PLAYER_WIDTH < boss.getX() + boss.getFitWidth() &&
//                imageView.getY() > boss.getY() &&
//                imageView.getY() < boss.getY() + boss.getFitHeight();
//    }

    // Colisão com limites do mapa

    public boolean player_collisionXleft(){
        return imageView.getX() <= 0;
    }
    public boolean player_collisionXright(){
        return imageView.getX() + PLAYER_WIDTH >= WIDTH;
    }
    public boolean player_collisionYup(){
        return imageView.getY() <= 0;
    }
    public boolean player_collisionYdown(){
        return imageView.getY() + PLAYER_HEIGHT >= HEIGHT;
    }

    public boolean player_enemy_collision(Enemy enemy){
        return imageView.getX() + PLAYER_WIDTH >= enemy.getX();
    }
    public boolean player_boss_collision(Boss boss){
        return imageView.getX() + PLAYER_WIDTH >= boss.getX();
    }

    // Métodos para movimentação do player baseado na atualização da posição atual + velocidade
    public void moveLeft() {
        imageView.setX(imageView.getX() - speed);
//        System.out.println("P0(x) position: " + imageView.getX());
        SoundsFX.playRunSound(0.30f);
//        runL.play();
    }

    public void moveRight() {
        imageView.setX(imageView.getX() + speed);
//        System.out.println("P0(x) position: " + imageView.getX());
        SoundsFX.playRunSound(0.30f);
//        runR.play();
    }

    public void moveUp() {
        imageView.setY(imageView.getY() - speed);
//        System.out.println("P0(y) position: " + imageView.getY());
        SoundsFX.playRunSound(0.30f);
//        runR.play();
    }

    public void moveDown() {
        imageView.setY(imageView.getY() + speed);
//        System.out.println("P0(y) position: " + imageView.getY());
        SoundsFX.playRunSound(0.30f);
//        runR.play();
    }

    public void stopAnimation() {
        runR.stop();
        runL.stop();
        imageView.setImage(kn_idle_r);
    }

    // Getters e Setters
    public void setX(int x){
        imageView.setX(x);
    }
    public void setY(int y){
        imageView.setY(y);
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getWidth() {return PLAYER_WIDTH;}
    public double getHeight() {return PLAYER_HEIGHT;}

    public void setKn_idle_l(){imageView.setImage(kn_idle_l);}
    public void setKn_idle_r(){imageView.setImage(kn_idle_r);}
    public void setKn_run(char direction){
        if(direction == 'L'){
            runL.play();
            runR.stop();
        }else if(direction == 'R'){
            runR.play();
            runL.stop();
        }
    }
    public void setKn_atk(int state, char direction){
        if (state == 1 && direction == 'L') {
            atkL.play();
            atkR.stop();
        } else if(state == 1 && direction == 'R'){
            atkR.play();
            atkL.stop();
        }else{
            atkR.stop();
            atkL.stop();
        }
    }

    public Timeline getDead(){return deadR;}


}
