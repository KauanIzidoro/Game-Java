import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import java.util.Objects;

/*

    Classe inimigo, usada para instânciar inimigos no mapa
    esta classe possui como atributo uma imagem que será usada para representar os inimigo no mapa

 */
public class Enemy extends ImageView {
    private double life;
    private double speed = 1;
    private double damage;
    private Image sk_idle_l;
    private Image sk_idle_r;
    private Image[] sk_run_r;
    private Image[] sk_run_l;
    private Image[] sk_atk_r;
    private Image[] sk_atk_l;
    private Image[] sk_dead_r;
    private Image[] sk_dead_l;
    private int indexRun = 0;
    private int indexAtk = 0;
    private int indexDead = 0;
    private Timeline runR;
    private Timeline runL;
    private Timeline atkR;
    private Timeline atkL;
    private Timeline deadR;
    private Timeline deadL;
    private MapObstacles obstacles = new MapObstacles();

    public Enemy(double x, double y) {
        sk_idle_l = new Image(Objects.requireNonNull(getClass().getResourceAsStream("res/enemy_res/sk_idle_l.png")));
        sk_idle_r = new Image(Objects.requireNonNull(getClass().getResourceAsStream("res/enemy_res/sk_idle_r.png")));
        setImage(sk_idle_l);
        setX(x);
        setY(y);
        include_sk_images();
        include_sk_animations();
        obstacles.toFront();
    }

    private void include_sk_images() {
        sk_run_r = new Image[8];
        sk_run_l = new Image[8];
        sk_atk_l = new Image[4];
        sk_atk_r = new Image[4];
        sk_dead_l = new Image[4];
        sk_dead_r = new Image[4];
        for (int i = 0; i < sk_atk_r.length; i++) {
            sk_atk_r[i] = new Image("res/enemy_res/sk_atk_r" + i + ".png");
            sk_atk_l[i] = new Image("res/enemy_res/sk_atk_l" + i + ".png");
            sk_dead_l[i] = new Image("res/enemy_res/sk_dead_l" + i + ".png");
            sk_dead_r[i] = new Image("res/enemy_res/sk_dead_r" + i + ".png");
        }
        for (int i = 0; i < sk_run_r.length; i++) {
            sk_run_r[i] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("res/enemy_res/sk_run_r" + i + ".png")));
            sk_run_l[i] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("res/enemy_res/sk_run_l" + i + ".png")));
        }
    }

    private void include_sk_animations() {
        // corrida
        runR = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            indexRun = (indexRun + 1) % sk_run_r.length;
            setImage(sk_run_r[indexRun]);
        }));
        runR.setCycleCount(Timeline.INDEFINITE);
        runL = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            indexRun = (indexRun + 1) % sk_run_l.length;
            setImage(sk_run_l[indexRun]);
        }));
        runL.setCycleCount(Timeline.INDEFINITE);

        // ataque
        atkR = new Timeline(new KeyFrame(Duration.millis(200), e -> {
            indexAtk = (indexAtk + 1) % sk_atk_r.length;
            setImage(sk_atk_r[indexAtk]);
        }));
        atkR.setCycleCount(sk_atk_r.length);
        atkL = new Timeline(new KeyFrame(Duration.millis(200), e -> {
            indexAtk = (indexAtk + 1) % sk_atk_l.length;
            setImage(sk_atk_l[indexAtk]);
        }));
        atkL.setCycleCount(sk_atk_l.length);

        // morte
        deadL = new Timeline(new KeyFrame(Duration.millis(500), e -> {
            indexDead = (indexDead + 1) % sk_dead_l.length;
            setImage(sk_dead_l[indexDead]);
        }));
        deadL.setCycleCount(sk_dead_l.length);
        deadR = new Timeline(new KeyFrame(Duration.millis(1000), e -> {
            indexDead = (indexDead + 1) % sk_dead_r.length;
            setImage(sk_dead_r[indexDead]);
        }));
        deadR.setCycleCount(sk_dead_r.length);
    }

    public void followPlayer(Player player) {
        if (enemy_collision_hit(player)) {
            return; // Parar o movimento se houver colisão
        }else{
            setSpeed(1);
        }
        // Posicionamento dos pontos player em X
        double playerX = player.getImageView().getX();
        double playerWidth = player.getImageView().getFitWidth();
        double playerY = player.getImageView().getY();

        // Posicionamento do inimigo
        double enemyX = getX();
        double enemyY = getY();

        // Calculo das distâncias entre os pontos dos inimigos para os pontos do player
        double deltaX0 = playerX - (enemyX + getFitWidth());
        double deltaX1 = (playerX + playerWidth) - enemyX;
        double deltaX = Math.abs(deltaX0) < Math.abs(deltaX1) ? deltaX0 : deltaX1;
        double deltaY = playerY - enemyY;

        // Verificar a maior distância entre o Player e o inimigo
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (deltaX > 0) {
                setX(getX() + speed); // movimenta o inimigo
                runR.play();
                runL.stop();
            } else {
                setX(getX() - speed);
                runL.play();
                runR.stop();
            }
        } else {
            if (deltaY > 0) {
                setY(getY() + speed);
                runR.play();
                runL.stop();
            } else {
                setY(getY() - speed);
                runL.play();
                runR.stop();
            }
        }
    }




    // Colisão Player | Enemy
    public boolean enemy_collision_hit(Player player){
        if(getX() + getFitWidth()  == player.getImageView().getX()){
            setSpeed(0);
            runR.stop();
            runL.stop();
            atkR.play();
            SoundsFX.playCollision();
            // Ataque da esquerda para direita, ou seja, colisão com o ponto p0 do player
            return true;
        }else if(getX() == player.getImageView().getX() + player.getImageView().getFitWidth()){
            setSpeed(0);
            runR.stop();
            runL.stop();
            atkL.play();
            SoundsFX.playCollision();
            // Ataque da direita para esquerta, ou seja, colisão com o ponto p1 do player
            return true;
        }
        return false;
    }



    public Timeline getAtkL() {
        return atkL;
    }

    public Timeline getAtkR(){
        return atkR;
    }

    public Timeline getDeadL(){return deadL;}

    public void setSpeed(double n){
        speed = n;
    }

    public void setskatkT(Timeline skatkT) {
        this.atkL = skatkT;
    }

}
