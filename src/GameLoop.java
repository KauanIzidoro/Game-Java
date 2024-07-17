
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import java.util.Random;

/*

    Classe GameLoop é responsável por gerenciar os eventos do jogo
    além de possuir os algoritmos de combate, verificações de colisão e movimentação do player.

 */
public class GameLoop {

    private Player player;
    private Pane pane;
    private Enemy[] enemies = new Enemy[3];
    private Boss boss;
    private GameEnvironment gameE = new GameEnvironment();
    private AnimationTimer animationTimer;
    private MapObstacles[] obstacles;
    private Hud[] hud;
    private Random Xr = new Random();
    private Random Yr  = new Random();
    private boolean flag_enemy = true;
    private boolean flag_spawn = true;
    private boolean boss_rage_mode = false;
    private int flag_cycle = 0;
    private char direction_player;
    private int enemylife = 4;
    private int bosslife = 6;
    private int playerlife = 250;
    private int kill = 0;

    public GameLoop(Scene scene, Player player, Pane pane, Stage primaryStage) {
        this.player = player;
        this.pane = pane;
        inputSetup(scene, primaryStage);
        setupRefresh(primaryStage);
        include_obstacles();
    }

    // Método responsável por atualizar os eventos do jogo continuamente
    private void setupRefresh(Stage primaryStage) {
        animationTimer = new AnimationTimer() {
            private long hit_player = 0; // salvar o tempo quando houver colisão
            private long boss_hit_player = 0; // salvar o tempo quando houver colisão com o Boss

            @Override
            public void handle(long now) {
                if (flag_enemy && kill < 3) {
                    for (Enemy enemy : enemies) {
                        if (enemy != null && enemy.isVisible()) {
                            enemy.followPlayer(player);
                            hud[0].update_enemy_hud_position(0, enemy.getX() + enemy.getFitWidth() / 4, enemy.getY() - 50);
                            hud[1].update_enemy_hud_position(1, enemy.getX() + enemy.getFitWidth() / 4, enemy.getY() - 50);

                            // Garanta que o HUD não esteja visível ao iniciar
                            hud[0].set_hud_visible(true);
                            hud[1].set_hud_visible(false);

                            if (enemy.enemy_collision_hit(player)) {
                                if (now - hit_player >= 3_000_000_000L) {
                                    playerlife--;
                                    hit_player = now; // Atualiza o tempo da última colisão
                                    System.out.println("Player hp: " + playerlife);
                                }
                            } else {
                                enemy.getAtkL().stop();
                                enemy.getAtkR().stop();
                            }

                            if (playerlife == 0) {
                                enemy.getAtkL().stop();
                                enemy.getAtkR().stop();
                                SoundsFX.playHit();
                                player.getDead().play();
                                System.out.println("Game Over");
                                kill = 0;
                                GameOverScreen gameover = new GameOverScreen();
                                gameover.start(primaryStage);
                                animationTimer.stop();
                            }
                        }
                    }
                }
                if (boss != null && boss.isVisible()) {
                    if(boss_rage_mode){
                        boss.run_rage(player);
                    }else{
                        boss.run(player);
                    }
                    if(now - boss_hit_player >= 3_000_000_000L && !boss_rage_mode){
                        boss.getAtkL().play();
                        boss_hit_player = now;
                    }
                    if(now - boss_hit_player >= 3_000_000_000L && boss_rage_mode){
                        boss.getAtkrage().play();
                        boss_hit_player = now;
                    }
                }
            }
        };
    }


    // Método usado para instânciar inimigos na tela
    private void spawnEnemy() {
        hud = new Hud[3];
        hud[0] = new Hud(0,0);
        hud[1] = new Hud(0,0);
        hud[0].set_hud_visible(false);
        hud[1].set_hud_visible(false);
        if (kill < 3) {
            // inimigos podem aparecer em locais aleatórios do mapa
            // Xr.nextInt(gameE.getSceneWidth()) = [0,1000[
            // Yr.nextInt(gameE.getSceneHeight()) = [0,600[
            enemies[kill] = new Enemy(Xr.nextInt(gameE.getSceneWidth()) - 100, Yr.nextInt(gameE.getSceneHeight()) - 100);
            pane.getChildren().add(hud[0].getEnemy_life(0,enemies[kill].getX(),enemies[kill].getY()));
            pane.getChildren().add(hud[1].getEnemy_life(1,enemies[kill].getX(),enemies[kill].getY()));
            hud[0].set_hud_visible(false);
            hud[1].set_hud_visible(false);
            pane.getChildren().add(enemies[kill]);
        }
    }

    private void kill_enemy(int index) {
        if (index >= 0 && index < enemies.length && enemies[index] != null) {
            enemies[index].getAtkL().stop();
            enemies[index].setSpeed(0);
            enemies[index].getDeadL().play();
            pane.getChildren().remove(enemies[index]);
            hud[0].set_hud_visible(false);
            hud[1].set_hud_visible(false);
            enemies[index] = null;
            SoundsFX.playEnemyDead();
            System.out.println("Inimigo eliminado e removido do pane.");
        }
    }
    private void kill_boss(Stage primaryStage){
        pane.getChildren().remove(boss);
        boss.setVisible(false);
        SoundsFX.playEnemyDead();
        WinScreen ws = new WinScreen();
        ws.start(primaryStage);
    }
    private void spawnBoss(){
        boss = new Boss(500,270);
        hud[2] = new Hud(0,0);
        pane.getChildren().addAll(boss,hud[2].getBoss_life(0,350,500));
    }

    private void include_obstacles(){
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

        for (int i = 0; i < obstacles.length; i++) {
            pane.getChildren().add(obstacles[i].getMap_obj(i));
            obstacles[i].toFront();
        }
    }

    private void inputSetup(Scene scene, Stage primaryStage) {
        // Receber teclas pressionadas
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                keyboard_events(event.getCode(), primaryStage);
                if (event.getCode() == KeyCode.B || flag_spawn) {
                    flag_enemy = true;
                    flag_spawn = !flag_spawn;
                    spawnEnemy();
                    animationTimer.start();
                }
            }
        });

        // Receber quando houver teclas soltas
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                player.stopAnimation();
                if(event.getCode() == KeyCode.A){
                    player.setKn_idle_l();
                    direction_player = 'L';
                }else if(event.getCode() == KeyCode.D){
                    player.setKn_idle_r();
                    direction_player = 'R';
                }
            }
        });
    }
    // Atualiza o HUD com base na vida do inimigo
    private void updateHud(int enemylife) {
        if (enemylife == 3) {
            hud[0].set_hud_visible(true);
            hud[1].set_hud_visible(false);
        } else if (enemylife == 2) {
            hud[0].set_hud_visible(false);
            hud[1].set_hud_visible(true);
        } else {
            hud[0].set_hud_visible(false);
            hud[1].set_hud_visible(false);
        }
    }

    private void keyboard_events(KeyCode code, Stage primaryStage) {

        // Movimentação do player
        switch (code) {
            case A:
                player.moveLeft();
                player.setKn_run('L');
                break;
            case W:
                player.moveUp();
                player.setKn_run(direction_player);
                break;
            case S:
                player.moveDown();
                player.setKn_run(direction_player);
                break;
            case D:
                player.moveRight();
                player.setKn_run('R');
                break;
            default:
                break;
        }

        // Entrar na tela de pausa
        if(code == KeyCode.P){
            PauseScreen pause = new PauseScreen();
            pause.start(primaryStage);
        }else{
            animationTimer.start();
        }
        if (code == KeyCode.SPACE) {
            player.setKn_atk(1,direction_player);
            SoundsFX.playAtk();
        } else {
            player.setKn_atk(0,'n');
        }

        // Combate entre o player e os inimigos
        if (flag_enemy) {
            for (int i = 0; i < enemies.length; i++) {
                if (enemies[i] != null && enemies[i].isVisible()) {
                    if (player.player_enemy_collision(enemies[i]) && code == KeyCode.SPACE) {
                        enemies[i].getAtkL().stop();
                        updateHud(enemylife);
                        System.out.println("hit");
                        enemylife--;
                        SoundsFX.playEnemyhit();
                        if (enemylife == 0) {
                            kill_enemy(i);
                            kill++;
                            if (kill < 3) {
                                enemylife = 4;
                                spawnEnemy();
                            }
                            System.out.println("matou");
                        }
                    } else {
                        enemies[i].getAtkL().play();
                    }
                }
            }
        }
        if(boss != null){
            if(player.player_boss_collision(boss) && code == KeyCode.SPACE){
                boss.getAtkL().stop();
                bosslife--;
                SoundsFX.playEnemyhit();
                if(bosslife == 3){
                    boss_rage_mode = true;
                }
                if(bosslife==0){
                    kill_boss(primaryStage);
                    System.out.println("WIN!");
                }
            }

        }

        // Inicia o boss quando o player eliminar todos os inimigos do mapa
        if(kill == 3 && flag_cycle == 0){
            flag_cycle++;
            spawnBoss();
        }

        // Verificação de colisões do player com os limites da tela
        if (player.player_collisionXleft()) {
            player.setSpeed(0);
            if (code == KeyCode.D || code == KeyCode.S || code == KeyCode.W)
                player.setSpeed(5);
        }
        if (player.player_collisionXright()) {
            player.setSpeed(0);
            if (code == KeyCode.A || code == KeyCode.W || code == KeyCode.S)
                player.setSpeed(5);
        }
        if (player.player_collisionYdown()) {
            player.setSpeed(0);
            if (code == KeyCode.W || code == KeyCode.A || code == KeyCode.D)
                player.setSpeed(5);
        }
        if (player.player_collisionYup()) {
            player.setSpeed(0);
            if (code == KeyCode.S || code == KeyCode.A || code == KeyCode.D)
                player.setSpeed(5);
        }

        // Verificação de colisões do player com os cantos da tela
        if(player.player_collisionYup() && player.player_collisionXleft()){
            player.setSpeed(0);
            if(code == KeyCode.S || code == KeyCode.D){
                player.setSpeed(5);
            }
        }
        if(player.player_collisionYdown() && player.player_collisionXleft()){
            player.setSpeed(0);
            if(code == KeyCode.W || code == KeyCode.D){
                player.setSpeed(5);
            }
        }
        if(player.player_collisionYup() && player.player_collisionXright()){
            player.setSpeed(0);
            if(code == KeyCode.S || code == KeyCode.A){
                player.setSpeed(5);
            }
        }
        if(player.player_collisionYdown() && player.player_collisionXright()){
            player.setSpeed(0);
            if(code == KeyCode.A || code == KeyCode.W){
                player.setSpeed(5);
            }
        }

        // Verificação de colisões do player com os obstáculos do mapa

        // p0,p1,p2 - pedra
        if(player.player_rock_collision_p0()||player.player_rock_collision_p1()||player.player_rock_collision_p2()){
            System.out.println("COLLISION UP");
            player.setSpeed(0);
            if(code == KeyCode.S || code == KeyCode.A || code == KeyCode.D){
                player.setSpeed(5);
            }
        }
        // q0,q1,q2 - pedra
        if(player.player_rock_collision_q0() || player.player_rock_collision_q1() || player.player_rock_collision_q2()){
            System.out.println("COLLISION DOWN");
            player.setSpeed(0);
            if(code == KeyCode.W || code == KeyCode.A || code == KeyCode.D){
                player.setSpeed(5);
            }
        }
        // p0,p1,p2 - altar
        if(player.player_alt_collision_p0() || player.player_alt_collision_p1() || player.player_alt_collision_p2()){
            player.setSpeed(0);
            if(code == KeyCode.S || code == KeyCode.A || code == KeyCode.D){
                player.setSpeed(5);
            }
        }
        // q0,q1,q2 - altar
        if(player.player_alt_collision_q0() || player.player_alt_collision_q1() || player.player_alt_collision_q2()){
            player.setSpeed(0);
            if(code == KeyCode.W || code == KeyCode.A || code == KeyCode.D){
                player.setSpeed(5);
            }
        }
    }


    public void start() {
    }
}
