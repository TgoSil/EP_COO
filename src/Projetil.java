import java.awt.Color;
//classe Projetil
public abstract class Projetil implements Entidade{
	protected Ponto2D ponto;
    protected double raio;
    
	public Projetil(double x, double y, double vx, double vy){
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

    @Override
    public abstract void desenha(long currentTime);

    @Override
    public abstract boolean atualizaEstado(long deltaTime);

}

//classe ProjetilPlayer que herda Projetil e implementa interface Entidade

class Projetilplayer extends Projetil implements Entidade {
    private double raio = 0.0;

    public Projetilplayer(double x, double y, double vx, double vy){
        super(x, y, vx, vy);
    }
    
    @Override
    public void desenha(long currentTime){

        GameLib.setColor(Color.GREEN);
		GameLib.drawLine(ponto.getX(), ponto.getY() - 5, ponto.getX(), ponto.getY() + 5);
		GameLib.drawLine(ponto.getX() - 1, ponto.getY() - 3, ponto.getX() - 1, ponto.getY() + 3);
		GameLib.drawLine(ponto.getX() + 1, ponto.getY() - 3, ponto.getX() + 1, ponto.getY() + 3);
        
    }

    @Override
    public boolean atualizaEstado(long deltaTime){
        this.ponto.setX(this.ponto.getX() + this.ponto.getvX() * deltaTime);
        this.ponto.setY(this.ponto.getY() + this.ponto.getvY() * deltaTime);
        if(this.ponto.getY() < 0) return false;
	    else return true;
    }

}

//classe ProjetilInimigo (de inimigo 1) que herda Projetil e implementa interface Entidade

class ProjetilInimigo extends Projetil implements Entidade {
    private double raio = 2.0;

    public ProjetilInimigo (double x, double y, double vx, double vy){
        super(x, y, vx, vy);
    }
    
    @Override
    public void desenha(long currentTime)
    {
		GameLib.setColor(Color.RED);
		GameLib.drawCircle(ponto.getX(), ponto.getY(), this.raio);
	}

    @Override
    public boolean atualizaEstado(long deltaTime){
        this.ponto.setX(this.ponto.getX() + this.ponto.getvX() * deltaTime);
        this.ponto.setY(this.ponto.getY() + this.ponto.getvY() * deltaTime);
        if(this.ponto.getY() > GameLib.HEIGHT) return false;
	    else return true;
    }
    
}

