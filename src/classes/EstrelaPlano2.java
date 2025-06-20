package classes;
import java.awt.Color;

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