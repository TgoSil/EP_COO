package classes;
import java.awt.Color;

/*classe do powerup de tiro rapido, que usa a class abstract PowerUp */
public class PowerUpTiroRapido extends PowerUp {

    public PowerUpTiroRapido(double x, double y) {
        super(x, y);
    }

    @Override
    public void aplicarEfeito(Player player, long currentTime) {
        player.ativarTiroRapido(currentTime, 5000); /*5 seg */
    }

    /*desenha um triangulo amarelo*/
    @Override
    public void desenha(long currentTime) {
        GameLib.setColor(Color.YELLOW);
        GameLib.drawLine(ponto.getX(), ponto.getY() - raio, ponto.getX() - raio, ponto.getY() + raio);
        GameLib.drawLine(ponto.getX() - raio, ponto.getY() + raio, ponto.getX() + raio, ponto.getY() + raio);
        GameLib.drawLine(ponto.getX() + raio, ponto.getY() + raio, ponto.getX(), ponto.getY() - raio);
    }
}