package classes;
import java.awt.Color;

public abstract class Estrela {
    protected double x;
    protected double y;
    protected double velocidade;
   
   public Estrela(double x, double y, double velocidade){
    this.x = x;
    this.y = y;
    this.velocidade = velocidade;

   }
   public void mover() {
    y += velocidade;
    if (y > GameLib.HEIGHT){
        y = 0;
        x = Math.random() * GameLib.WIDTH;
    }

   }

   public abstract void desenhar();

}

class EstrelaPlano1 extends Estrela {
    public EstrelaPlano1(){
        super(Math.random() * GameLib.WIDTH, Math.random() * GameLib.HEIGHT, 0.045 );
    }

    @Override
    public void desenhar(){
        GameLib.setColor(Color.DARK_GRAY);
        GameLib.fillRect(x, y, 2, 2);

    }

}

class EstrelaPlano2 extends Estrela {
    public EstrelaPlano2(){
        super(Math.random() * GameLib.WIDTH, Math.random() * GameLib.HEIGHT, 0.070 );
    }

    @Override

    public void desenhar(){
        GameLib.setColor(Color.GRAY);
        GameLib.fillRect(x, y, 3, 3);

    }

}
