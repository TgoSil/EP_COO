
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

public class EstrelaPlano2 extends Estrela {
    public EstrelaPlano2(){
        super(Math.random() * GameLib.WIDTH, Math.random() * GameLib.HEIGHT, 0.070 );
    }

    @Override

    public void desenhar(){
        GameLib.setColor(Color.GRAY);
        GameLib.fillRect(x, y, 3, 3);

    }

}


/*
para declarar na main
Estrela[] estrelasFundo = new EstrelaPlano1[50]; 
Estrela[] estrelasFrente = new EstrelaPlano2[20]; 

//Inicialização
for(int i = 0; i < estrelasFundo.length; i++) {
    estrelasFundo[i] = new EstrelaPlano1();
}

for(int i = 0; i < estrelasFrente.length; i++) {
    estrelasFrente[i] = new EstrelaPlano2();
}

//No loop do game
for(Estrela estrela : estrelasFundo) {
    estrela.mover();
    estrela.desenhar();
}

for(Estrela estrela : estrelasFrente) {
    estrela.mover();
    estrela.desenhar();
}

    
*/