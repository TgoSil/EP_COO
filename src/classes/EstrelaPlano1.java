package classes;
import java.awt.Color;


public class EstrelaPlano1 extends Estrela {
    public EstrelaPlano1(){
        super(Math.random() * GameLib.WIDTH, Math.random() * GameLib.HEIGHT, 0.045 );
    }

    @Override
    public void desenhar(){
        GameLib.setColor(Color.DARK_GRAY);
        GameLib.fillRect(x, y, 2, 2);

    }

}