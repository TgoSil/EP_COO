package classes;

import java.util.LinkedList;

public abstract class Boss extends Inimigos{

    protected int vida; 

    public Boss(double x, double y, double vx, double vy, double angulo, double vR, LinkedList<Projetil> listaProjeteis, int vida) {
        super(x, y, vx, vy, angulo, vR, listaProjeteis);
        this.vida = vida;
    }

    @Override
    public boolean colision(LinkedList<Projetil> projeteis, long currentTime, double c){
    
        for (Projetil projetil : projeteis) {
            double dx = projetil.getX() - ponto.getX();
            double dy = projetil.getY() - ponto.getY();
            double dist = Math.sqrt(dx * dx + dy * dy);
            
            if(dist < ((this.raio + projetil.getRaio()) * c)){
                return true;
            }
        }
        return false;
    }

    public abstract void desenha(long currentTime);

    public abstract boolean atualizaEstado(long deltaTime, long currentTime, double PlayerY, LinkedList<Projetil> projetilInimigo);

}
