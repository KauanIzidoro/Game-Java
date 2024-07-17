import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Boss extends ImageView {
    private double life;
    private double damage;
    private double speed = 1;
    private boolean attacking = false; // Flag para controlar se o boss está atacando
    private Image[] boss_idle_rage_r;
    private Image[] boss_idle_rage_l;
    private Image[] boss_atk_rage_r;
    private Image[] boss_atk_rage_l;
    private Image[] boss_idle_l;
    private Image[] boss_idle_r;
    private Image[] boss_atk_l;
    private Image[] boss_atk_r;
    private Timeline atkrageL;
    private Timeline atkrageR;
    private Timeline idlerageL;
    private Timeline idlerageR;
    private Timeline idleL;
    private Timeline idleR;
    private Timeline atkL;
    private Timeline atkR;
    private int indexAtk = 0;
    private int indexIdle = 0;

    public Boss(double x, double y) {
        setX(x);
        setY(y);
        include_boss_image();
        include_boss_animations();
    }

    private void include_boss_image() {
        boss_idle_rage_l = new Image[6];
        boss_idle_rage_r = new Image[6];
        boss_atk_rage_l = new Image[11];
        boss_atk_rage_r = new Image[11];
        boss_idle_r = new Image[6];
        boss_idle_l = new Image[6];
        boss_atk_l = new Image[10];
        boss_atk_r = new Image[10];
        for (int i = 0; i < boss_atk_rage_l.length; i++) {
            boss_atk_rage_l[i] = new Image("res/boss_res/rage_boss/boss_atk_rage_l" + i + ".png");
            boss_atk_rage_r[i] = new Image("res/boss_res/rage_boss/boss_atk_rage_r" + i + ".png");
        }
        for (int i = 0; i < boss_idle_rage_l.length; i++) {
            boss_idle_rage_l[i] = new Image("res/boss_res/rage_boss/boss_idle_rage_l" + i + ".png");
            boss_idle_rage_r[i] = new Image("res/boss_res/rage_boss/boss_idle_rage_r" + i + ".png");
        }
        for (int i = 0; i < boss_idle_l.length; i++) {
            boss_idle_l[i] = new Image("res/boss_res/normal_boss/boss" + i + ".png");
            boss_idle_r[i] = new Image("res/boss_res/normal_boss/boss_r" + i + ".png");
        }
        for (int i = 0; i < boss_atk_l.length; i++) {
            boss_atk_l[i] = new Image("res/boss_res/normal_boss/boss_atk" + i + ".png");
            boss_atk_r[i] = new Image("res/boss_res/normal_boss/boss_atk_r" + i + ".png");
        }
    }

    private void include_boss_animations() {
        // movimentação no modo raiva
        idlerageL = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            indexIdle = (indexIdle + 1) % boss_idle_rage_l.length;
            setImage(boss_idle_rage_l[indexIdle]);
        }));
        idlerageL.setCycleCount(Timeline.INDEFINITE);
        idlerageR = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            indexIdle = (indexIdle + 1) % boss_idle_rage_r.length;
            setImage(boss_idle_rage_r[indexIdle]);
        }));
        idlerageR.setCycleCount(Timeline.INDEFINITE);

        // ataque no modo raiva
        atkrageL = new Timeline(new KeyFrame(Duration.millis(200), e -> {
            indexAtk = (indexAtk + 1) % boss_atk_rage_l.length;
            setImage(boss_atk_rage_l[indexAtk]);
        }));
        atkrageL.setCycleCount(boss_atk_rage_l.length);
        atkrageR = new Timeline(new KeyFrame(Duration.millis(200), e -> {
            indexAtk = (indexAtk + 1) % boss_atk_rage_r.length;
            setImage(boss_atk_rage_r[indexAtk]);
        }));
        atkrageR.setCycleCount(boss_atk_rage_r.length);

        // movimentação no modo normal
        idleL = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            indexIdle = (indexIdle + 1) % boss_idle_l.length;
            setImage(boss_idle_l[indexIdle]);
        }));
        idleL.setCycleCount(Timeline.INDEFINITE);
        idleR = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            indexIdle = (indexIdle + 1) % boss_idle_r.length;
            setImage(boss_idle_r[indexIdle]);
        }));
        idleR.setCycleCount(Timeline.INDEFINITE);

        // ataque modo normal
        atkL = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            indexAtk = (indexAtk + 1) % boss_atk_l.length;
            setImage(boss_atk_l[indexAtk]);
        }));
        atkL.setCycleCount(boss_atk_l.length);
        atkR = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            indexAtk = (indexAtk + 1) % boss_atk_r.length;
            setImage(boss_atk_r[indexAtk]);
        }));
        atkR.setCycleCount(boss_atk_r.length);
    }

    public void run(Player player) {
        movement(player);
        if (new_boss_collision_hit(player)) {
            System.out.println("BOSS HIT");
        }
        idleL.play(); // Ou outra animação de idle
    }
    public void run_rage(Player player) {
        movement_rage(player);
        if (new_boss_collision_hit_rage(player)) {
            System.out.println("BOSS HIT");
        }
        idlerageL.play(); // Ou outra animação de idle
    }


//    public void atkL() {
//        atkL.play();
//    }
//    public void atkR(){
//        atkR.play();
//    }

    public void atk_rageL() {
        atkrageL.play();
    }

    public void atk_rageR() {
        atkrageR.play();
    }

    public void idle_rageL() {
        idlerageL.play();
    }

    public void idle_rageR() {
        idlerageR.play();
    }

    public void movement(Player player) {
        if(new_boss_collision_hit(player)){
            return;
        }
        idleL.play();
        atkL.stop();
        // Posição do Player
        double playerX = player.getImageView().getX() + player.getWidth(); //este é o p1 - 'atemporal_document'
//        double playerX_new = player.getImageView().getX(); //este é o p0
        double playerY = player.getImageView().getY() - 50;

        // Distância entre o Boss e o player
        double deltaX = playerX - getX();
        double deltaY = playerY - getY();

        // Verificar a maior distância entre o Player e o Boss
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (deltaX > 0) {
                idleR.play();
                idleL.stop();
                setX(getX() + speed);
            } else {
                setX(getX() - speed);
                idleL.play();
                idleR.stop();
            }
        } else {
            if (deltaY > 0) {
                setY(getY() + speed);
                idleR.play();
                idleL.stop();
            } else {
                setY(getY() - speed);
                idleL.play();
                idleR.stop();
            }
        }
    }
    public void movement_rage(Player player) {
        if(new_boss_collision_hit_rage(player)){
            return;
        }
        idleL.stop();
        idleR.stop();
        atkR.stop();
        atkL.stop();
        idlerageL.play();
        atkrageL.stop();
        // Posição do Player
        double playerX = player.getImageView().getX() + player.getWidth(); //este é o p1 - 'atemporal_document'
//        double playerX_new = player.getImageView().getX(); //este é o p0
        double playerY = player.getImageView().getY() - 50;

        // Distância entre o Boss e o player
        double deltaX = playerX - getX();
        double deltaY = playerY - getY();

        // Verificar a maior distância entre o Player e o Boss
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (deltaX > 0) {
                idlerageR.play();
                idlerageL.stop();
                setX(getX() + speed);
            } else {
                setX(getX() - speed);
                idlerageL.play();
                idlerageR.stop();
            }
        } else {
            if (deltaY > 0) {
                setY(getY() + speed);
                idlerageR.play();
                idlerageL.stop();
            } else {
                setY(getY() - speed);
                idlerageL.play();
                idlerageR.stop();
            }
        }
    }

    public boolean boss_hit_player_rage(Player player){
        // Coordenadas do Player
        double playerX = player.getImageView().getX();
        double playerY = player.getImageView().getY();
        double playerWidth = player.getImageView().getFitWidth();
        double playerHeight = player.getImageView().getFitHeight();

        // Coordenadas do Boss
        double bossX = getX();
        double bossY = getY();
        double bossWidth = getFitWidth();
        double bossHeight = getFitHeight();

        // Verifica colisão
        boolean isColliding = playerX <= bossX + bossWidth &&
                playerX + playerWidth >= bossX &&
                playerY <= bossY + bossHeight &&
                playerY + playerHeight >= bossY;

        if (isColliding) {
            if (playerX + playerWidth / 2 < bossX + bossWidth / 2) {
                // O player está à esquerda do Boss
                idlerageR.stop();
                idlerageL.stop();
                atkrageL.play();
            } else {
                // O player está à direita do Boss
                idlerageR.stop();
                idlerageL.stop();
                atkrageL.play();
            }
//        SoundsFX.playCollision();
            return true;
        }

        return false;
    }

    // Colisão Player | Enemy
    public boolean new_boss_collision_hit(Player player){
        if(getX() + getFitWidth() - 20  == player.getImageView().getX()){
            idleR.stop();
            idleL.stop();
            atkL.play();
//            SoundsFX.playCollision();
            // Ataque da esquerda para direita, ou seja, colisão com o ponto p0 do player
            return true;
        }else if(getX() - 20  == player.getImageView().getX() + player.getImageView().getFitWidth()){
            idleR.stop();
            idleL.stop();
            atkL.play();
//            SoundsFX.playCollision();
            // Ataque da direita para esquerta, ou seja, colisão com o ponto p1 do player
            return true;
        }
        return false;
    }
    public boolean new_boss_collision_hit_rage(Player player){
        if(getX() + getFitWidth() - 20 == player.getImageView().getX()){
            atkR.stop();
            atkL.stop();
            idleL.stop();
            idleR.stop();
            idlerageR.stop();
            idlerageL.stop();
            atkrageL.play();
//            SoundsFX.playCollision();
            // Ataque da esquerda para direita, ou seja, colisão com o ponto p0 do player
            return true;
        }else if(getX() - 20 == player.getImageView().getX() + player.getImageView().getFitWidth()){
            idlerageR.stop();
            idlerageL.stop();
            atkR.stop();
            atkL.stop();
            idleL.stop();
            idleR.stop();
            idlerageR.stop();
            idlerageL.stop();
            atkrageL.play();
//            SoundsFX.playCollision();
            // Ataque da direita para esquerta, ou seja, colisão com o ponto p1 do player
            return true;
        }
        return false;
    }

    public boolean boss_hit_player(Player player) {
        // Coordenadas do Player
        double playerX = player.getImageView().getX();
        double playerY = player.getImageView().getY();
        double playerWidth = player.getImageView().getFitWidth();
        double playerHeight = player.getImageView().getFitHeight();

        // Coordenadas do Boss
        double bossX = getX();
        double bossY = getY();
        double bossWidth = getFitWidth();
        double bossHeight = getFitHeight();

        // Verifica colisão
        boolean isColliding = playerX <= bossX + bossWidth &&
                playerX + playerWidth >= bossX &&
                playerY <= bossY + bossHeight &&
                playerY + playerHeight >= bossY;

        if (isColliding) {
            if (playerX + playerWidth / 2 < bossX + bossWidth / 2) {
                // O player está à esquerda do Boss
                idleR.stop();
                idleL.stop();
                atkL.play();
            } else {
                // O player está à direita do Boss
                idleR.stop();
                idleL.stop();
                atkL.play();
            }
//        SoundsFX.playCollision();
            return true;
        }

        return false;
//        // Coordenadas do Player
//        double playerX = player.getImageView().getX();
//        double playerY = player.getImageView().getY();
//        double playerWidth = player.getImageView().getFitWidth();
//        double playerHeight = player.getImageView().getFitHeight();
//
//        // Coordenadas do Boss
//        double bossX = getX();
//        double bossY = getY();
//        double bossWidth = getFitWidth();
//        double bossHeight = getFitHeight();
//
//        // Verifica colisão
//        return playerX < bossX + bossWidth &&
//                playerX + playerWidth > bossX &&
//                playerY < bossY + bossHeight &&
//                playerY + playerHeight > bossY;
//            if(getX() + getFitWidth()  == player.getImageView().getX()){
//                idleR.stop();
//                idleL.stop();
//                atkR.play();
////                SoundsFX.playCollision();
//                // Ataque da esquerda para direita, ou seja, colisão com o ponto p0 do player
//                return true;
//            }else if(getX() == player.getImageView().getX() + player.getImageView().getFitWidth()){
//                idleR.stop();
//                idleL.stop();
//                atkL.play();
////                SoundsFX.playCollision();
//                // Ataque da direita para esquerta, ou seja, colisão com o ponto p1 do player
//                return true;
//            }
//            return false;
    }


    public double getLife() {
        return life;
    }

    public double getSpeed() {
        return speed;
    }

    public Timeline getAtkrage() {
        return atkrageL;
    }
    public Timeline getAtkL(){
        return atkL;
    }

    public Timeline getIdlerage() {
        return idlerageL;
    }
}
