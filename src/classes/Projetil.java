package classes;

import java.awt.Color;

//classe Projetil
public abstract class Projetil{
	protected Ponto2D ponto;
    protected double raio;
    
	protected Projetil(double x, double y, double vx, double vy){
		this.ponto = new Ponto2D(x, y, vx, vy);
	}

    public double getX(){ 
        return this.ponto.getX();
    }
    
    public double getY(){ 
        return this.ponto.getY();
    }

    public double getRaio()
    {
        return this.raio;
    }

    public abstract void desenha(long currentTime);

    public abstract boolean atualizaEstado(long deltaTime, long currentTime);

}

//classe ProjetilPlayer que herda Projetil e implementa interface Entidade

class Projetilplayer extends Projetil{

    protected Projetilplayer(double x, double y, double vx, double vy){
        super(x, y, vx, vy);
        this.raio = 0.0;
    }
    
    @Override
    public void desenha(long currentTime){

        GameLib.setColor(Color.GREEN);
		GameLib.drawLine(ponto.getX(), ponto.getY() - 5, ponto.getX(), ponto.getY() + 5);
		GameLib.drawLine(ponto.getX() - 1, ponto.getY() - 3, ponto.getX() - 1, ponto.getY() + 3);
		GameLib.drawLine(ponto.getX() + 1, ponto.getY() - 3, ponto.getX() + 1, ponto.getY() + 3);
        
    }

    @Override
    public boolean atualizaEstado(long deltaTime, long currentTime){
        this.ponto.setX(this.ponto.getX() + this.ponto.getvX() * deltaTime);
        this.ponto.setY(this.ponto.getY() + this.ponto.getvY() * deltaTime);
        if(this.ponto.getY() < 0 || this.ponto.getX() > GameLib.WIDTH || this.ponto.getX() < 0) return false;
	    return true;
    }

}

//classe ProjetilInimigo (de inimigo 1) que herda Projetil e implementa interface Entidade

class ProjetilInimigo extends Projetil{

    protected ProjetilInimigo (double x, double y, double vx, double vy){
        super(x, y, vx, vy);
        this.raio = 2.0;
    }
    
    @Override
    public void desenha(long currentTime)
    {
		GameLib.setColor(Color.RED);
		GameLib.drawCircle(ponto.getX(), ponto.getY(), this.raio);
	}

    @Override
    public boolean atualizaEstado(long deltaTime, long currentTime){
        this.ponto.setX(this.ponto.getX() + this.ponto.getvX() * deltaTime);
        this.ponto.setY(this.ponto.getY() + this.ponto.getvY() * deltaTime);
        if(this.ponto.getY() > GameLib.HEIGHT || this.ponto.getX() > GameLib.WIDTH || this.ponto.getX() < 0) return false;
	    return true;
    }
    
}
