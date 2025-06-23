package classes;


/*Classe Power Ups que implementa a interface Entidade e será usada no PowerUps de Invulnerabilidade e TiroRapido */
public abstract class PowerUp {
    protected Ponto2D ponto;
    protected double raio = 10.0;

    public PowerUp(double x, double y) {
        this.ponto = new Ponto2D(x, y, 0, 0.1);
    }

    public boolean atualizaEstado(long deltaTime, long currentTime, double limitador) {
        ponto.setY(ponto.getY() + ponto.getvY() * deltaTime);
        return ponto.getY() < limitador;
    }

    public boolean colisao(Player player) {
        double dx = ponto.getX() - player.getX();
        double dy = ponto.getY() - player.getY();
        double dist = Math.sqrt(dx * dx + dy * dy);
        return dist < (raio + player.raio);
    }

    public abstract void aplicarEfeito(Player player, long currentTime);

    public abstract void desenha(long currentTime);
}
