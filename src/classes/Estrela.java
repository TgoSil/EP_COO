package classes;

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
