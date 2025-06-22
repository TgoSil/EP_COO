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
        double y = this.ponto.getY() + this.ponto.getvY() * deltaTime;
        double x = this.ponto.getX() + this.ponto.getvX() * deltaTime;
        if(y < 0 || x > GameLib.WIDTH || x < 0) return false;
        this.ponto.setX(x);
        this.ponto.setY(y);
        
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
        double y = this.ponto.getY() + this.ponto.getvY() * deltaTime;
        double x = this.ponto.getX() + this.ponto.getvX() * deltaTime;
        if(y > GameLib.HEIGHT || x > GameLib.WIDTH || x < 0) return false;
        this.ponto.setX(x);
        this.ponto.setY(y);
	    return true;
    }
}

    class ProjetilBoss2 extends Projetil{
        long sumir;

    protected ProjetilBoss2 (double x, double y, double vx, double vy, long currentTime){
        super(x, y, vx, vy);
        this.sumir = currentTime + 12500;
        this.raio = 4.0;
    }
    
    @Override
    public void desenha(long currentTime)
    {
		GameLib.setColor(Color.ORANGE);
		GameLib.drawCircle(ponto.getX(), ponto.getY(), this.raio);
	}

    @Override
    public boolean atualizaEstado(long deltaTime, long currentTime){
        double y = this.ponto.getY() + this.ponto.getvY() * deltaTime;
        double x = this.ponto.getX() + this.ponto.getvX() * deltaTime;
        if(x > GameLib.WIDTH || x < 0) this.ponto.setvX(this.ponto.getvX() * -1); //Altera a direção do ataque 2
        if(y > GameLib.HEIGHT || y < 0) this.ponto.setvY(this.ponto.getvY() * -1); 
        this.ponto.setX(x);
        this.ponto.setY(y);
        if (currentTime > this.sumir) return false;
	    return true;
    }



}

