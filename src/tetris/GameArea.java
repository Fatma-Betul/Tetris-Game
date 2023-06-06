
package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import javax.swing.JPanel;
import tetrisblocks.*;                // import tetrisblocks.IShape;

public class GameArea extends JPanel{
   
    private int hucreSatırı;           // gridRows
    private int hucreSutunu;           // gridColumns
    private int hucreBoyutu;           // gridCellSize
    
    private Color [][] background;
    
    private TetrisBlock blok ;         //        ? 
    
    private TetrisBlock[] blocks ;
    
    
    public GameArea(JPanel ph,int sutun){     // ph = placeholder = Oyun alanının yeri
        
       // ph.setVisible(false);
        this.setBounds(ph.getBounds());    // Oyun alanının boyutunu gameform un dizaynından belirlediğimiz gibi ayarladı.
        this.setBackground(ph.getBackground());
        this.setBorder(ph.getBorder()); 
        
        hucreSutunu = sutun;                                  
        hucreBoyutu = this.getBounds().width / hucreSutunu;   
        hucreSatırı= this.getBounds().height / hucreBoyutu;  
        
        blocks = new TetrisBlock[]{new IShape(),
                                   new JShape(),
                                   new LShape(),   
                                   new OShape(),
                                   new SShape(),
                                   new TShape(),
                                   new ZShape() };
    }
    
    public void initBackgroundArray(){
        background = new Color[hucreSatırı][hucreSutunu];
    }
    
    public void spawnBlock() {
        
        Random r = new Random();
        blok = blocks[r.nextInt( blocks.length)];
        blok.spawn(hucreSutunu);
    }
    
    public boolean isBlockOutOfBounds(){
        
        if(blok.getY()<0) {
            blok = null;
            return true;
        }
        return false;
    }
    
    public boolean moveBlockDown() {
        
        if( checkBottom() ==  false)  {
          
            return false;
        }
        
        blok.moveDown();
        repaint();                        // ?
        
        return true;
    }
    public void moveBlockRight(){
        
        if(blok == null)
            return;
        if( !checkRight() ) { return ;}
            
        blok.moveRight();
        repaint(); 
    }
    public void moveBlockLeft(){  
        
        if(blok == null)
            return;
        if( !checkLeft()) { return ;}
        
        blok.moveLeft();
        repaint(); 
    }
    public void dropBlock(){       // bloğu aşağıya hızlı indirir.
        
        if(blok == null)
            return;
        while(checkBottom()){
            blok.moveDown();
        }
        repaint();
    }   
    public void rotateBlock(){    // aşağıya ineceği vakit değdiği an blok artır orada kalır. yana haraket edemez.
 
        if(blok == null)
            return;
        blok.rotate();
        
        if(blok.getLeftCheck()<0)
            blok.setX(0);
        if(blok.getRightCheck() >= hucreSutunu)
            blok.setX(hucreSutunu - blok.getWidth());
        if(blok.getBottomEdge() >= hucreSatırı)
            blok.setY(hucreSatırı - blok.getHeight());
        repaint();
    }

    private boolean checkBottom(){                 // blok alt sınır çizgisini geçmeyecek
        
        if( blok.getBottomEdge() == hucreSatırı)
            return false;
        
        int [][] shape = blok.getShape();
        int w = blok.getWidth();
        int h = blok.getHeight();
        
        for(int col=0 ; col<w ; col++ ){
            for(int row=h-1 ; row>=0 ; row--){
                if(shape[row][col]!=0){
                    
                    int x = col+ blok.getX();
                    int y = row+ blok.getY()+1 ;
                    if(y<0) 
                        break;
                    if(background[y][x]!=null)
                        return false;
                    break;
                }
            }
        }
        return true;
    } 
 
    private boolean checkLeft(){           // blok sol sınır çizgisini geçmeyecek
        if( blok.getLeftCheck()== 0 )
            return false;
        
        int [][] shape = blok.getShape();
        int w = blok.getWidth();
        int h = blok.getHeight();
        
        for(int row=0 ; row<h ; row++ ){
            for(int col=0 ; col<w ; col++){
                if(shape[row][col]!=0){
                    
                    int x = col+ blok.getX()-1;
                    int y = row+ blok.getY();
                    if(y<0) 
                        break;
                    if(background[y][x]!=null)
                        return false;
                    break;
                }
            }
        }
        return true;
    }
    
     private boolean checkRight(){         // blok sağ sınır çizgisini geçmeyecek
        if( blok.getRightCheck()== hucreSutunu )
            return false;
        
        int [][] shape = blok.getShape();
        int w = blok.getWidth();
        int h = blok.getHeight();
        
        for(int row=0 ; row<h ; row++ ){
            for(int col=w-1 ; col >=0 ; col--){
                if(shape[row][col]!=0){
                    
                    int x = col+ blok.getX()+1;
                    int y = row+ blok.getY();
                    if(y<0) 
                        break;
                    if(background[y][x]!=null)
                        return false;
                    break;
                }
            }
        }
        return true;
    }
     
    public int clearLines(){   // çizginin tam dolu olduğu takdirde o çizgiyi boşaltır.
         
        boolean lineFilled;
        int LinesCleared = 0;
         
        for(int r=hucreSatırı-1 ; r>=0; r--){  
            lineFilled = true;
            for(int c=0; c<hucreSutunu ; c++){    // çizgi doldu
                if(background[r][c] == null){     
                    lineFilled = false;
                    break;                        // çizgi dolmadı
                }
            }
            
            if(lineFilled){
                
                LinesCleared++;  // Puan her bloğun silinmesinde 1 artıyor.
                clearLine(r);
                shiftDown(r);
                clearLine(0);
                r++;
                repaint();
            }
        }
        return LinesCleared;
    }
    private void clearLine(int r){               //  çizgideki blokları temizler.
        for(int i=0 ; i<hucreSutunu ; i++){
            background[r][i]= null;
        }
    }
    private void shiftDown(int r){
        for(int row=r ; row>0 ; row-- ){
            for(int col=0 ; col<hucreSutunu ; col++){
                background[row][col]= background[row-1][col];
            }
        }
    }
     
    public void moveBlockToBacground(){
        
        int [][] shape = blok.getShape();
        int h = blok.getHeight();
        int w = blok.getWidth();
        
        int xPos = blok.getX();
        int yPos = blok.getY();
        
        Color color = blok.getColor();
        
        for(int r=0 ; r<h ; r++){
            for( int c=0 ; c<w ; c++){ 
                if( shape[r][c] == 1){
                    background[r+yPos][c+xPos] = color;
                }
            }
        }
       
    }
    
    private void drawBlock(Graphics g){
        
        int h = blok.getHeight();
        int w = blok.getWidth();
        Color c = blok.getColor();
        int [][] shape = blok.getShape(); 
        
        for(int str=0; str < h ;str++){           // row = satır(str),
            for(int stn=0;stn < w; stn++){    // column = sütun(stn)
                if(shape[str][stn]==1){
                    
                    int x = ( blok.getX() + stn )* hucreBoyutu;
                    int y = ( blok.getY() + str )* hucreBoyutu;
                    
                    drawGridSquare(g, c, x, y);
                    
                }
            }
        }
    }   
    
    private void drawBackground(Graphics g){
        
        Color color;
        
        for(int r =0 ; r < hucreSatırı;r++) {
            for(int c=0 ; c < hucreSutunu ; c++ ){
                
                color = background[r][c]; 
                
                if(color!=null) {
                    int x = c*hucreBoyutu;
                    int y = r*hucreBoyutu;
                    
                    drawGridSquare(g, color, x, y);
                }
            }
        }
    }
    
    private void drawGridSquare(Graphics g,Color color,int x,int y){
        
        g.setColor(color);
        g.fillRect(x, y, hucreBoyutu, hucreBoyutu);
        g.setColor(Color.black);
        g.drawRect(x, y, hucreBoyutu, hucreBoyutu);
    }
    
    @Override
    protected void paintComponent(Graphics g){    // JPanel metodu -> override
        
        super.paintComponent(g);    // Background ' ın renginin görünmesini sağlar.
        drawBackground(g);
        drawBlock(g);
       
   }             
}
