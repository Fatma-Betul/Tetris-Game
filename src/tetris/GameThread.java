
package tetris;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GameThread extends Thread{
    private GameArea ga ;
    private GameForm gf;
    private int score;
    private int level = 1;
    private int scorePerLevel = 3;
    
    private int pause = 750;
    private int speedupPerLevel = 75;
    
    public GameThread(GameArea ga , GameForm gf){
        
        this.ga=ga;
        this.gf=gf;
        
        gf.updateScore(score);
        gf.updateLevel(level);
    }
    
    @Override
    public void run(){
        
        while(true){
            
            ga.spawnBlock();
            while ( ga.moveBlockDown()) {
            
                try {
                    Thread.sleep(pause);   // 1000 mil
                } catch (InterruptedException ex) {
                    return ;
                }
            }
            
            if(ga.isBlockOutOfBounds()){
                Tetris.gameOver(score);
                break;
            }
            ga.moveBlockToBacground();
            score+= ga.clearLines();
            gf.updateScore(score);
            
            int lvl =score / scorePerLevel + 1;
            if(lvl>level){
                level = lvl;
                gf.updateLevel(level);
                pause -= speedupPerLevel;
                
            }
        }
    }
}
