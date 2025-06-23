package classes;
import java.awt.Color;

/*classe do powerup de invulnerabilidade, que usa a class abstract PowerUp */
public class PowerUpInvulnerabilidade extends PowerUp {

    public PowerUpInvulnerabilidade(double x, double y) {
        super(x, y);
    }

    @Override
    public void aplicarEfeito(Player player, long currentTime) {
        player.setInvulneravel(currentTime, 5000); /*5 seg */
    }

    /*desenha um triangulo laranja*/
    @Override
    public void desenha(long currentTime) {
        GameLib.setColor(Color.ORANGE);
        GameLib.drawLine(ponto.getX(), ponto.getY() - raio, ponto.getX() - raio, ponto.getY() + raio);
        GameLib.drawLine(ponto.getX() - raio, ponto.getY() + raio, ponto.getX() + raio, ponto.getY() + raio);
        GameLib.drawLine(ponto.getX() + raio, ponto.getY() + raio, ponto.getX(), ponto.getY() - raio);  
    }
}
