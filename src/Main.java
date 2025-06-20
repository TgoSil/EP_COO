import classes.*;
import java.awt.Color;
import java.util.*;

public class Main {
/* Espera, sem fazer nada, até que o instante de tempo atual seja */
	/* maior ou igual ao instante especificado no parâmetro "time.    */

public static void busyWait(long time)
    {
		while(System.currentTimeMillis() < time) Thread.yield();
	}
	
public static void main(String [] args)
{
	/* Indica que o jogo está em execução */

		boolean running = true;

		/* variáveis usadas no controle de tempo efetuado no main loop */
		long delta;
		long currentTime = System.currentTimeMillis();

		 /*declaração das variáveis player */
		 // coordenada x, coordenada y, velocidade no eixo x, velocidade no eixo y, raio do player, tempo do próximo tiro.
		Player player = new Player(GameLib.WIDTH / 2, GameLib.HEIGHT * 0.90, 0.25, 0.25, 12.0, currentTime); 
		LinkedList<Inimigos> inimigos = new LinkedList();
		
		

		/* estrelas */
		ArrayList<Estrela> estrelaPlano1 = new ArrayList<>();
		ArrayList<Estrela> estrelaPlano2 = new ArrayList<>();
		
		for (int i = 0; i < 50; i++){
		estrelaPlano1.add(new EstrelaPlano1());
		}

		for (int i = 0; i < 20; i++){
		estrelaPlano2.add(new EstrelaPlano2());
		}

		for (Estrela estrela : estrelaPlano1) {
			estrela.mover();
			estrela.desenhar();

		for (Estrela estrela : estrelaPlano2) {
			estrela.mover();
			estrela.desenhar();
		}

		
	
		
		
		/* iniciado interface gráfica */
		GameLib.initGraphics();

		/*************************************************************************************************/
		/*                                                                                               */
		/* Main loop do jogo                                                                             */
		/* -----------------                                                                             */
		/*                                                                                               */
		/* O main loop do jogo executa as seguintes operações:                                           */
		/*                                                                                               */
		/* 1) Verifica se há colisões e atualiza estados dos elementos conforme a necessidade.           */
		/*                                                                                               */
		/* 2) Atualiza estados dos elementos baseados no tempo que correu entre a última atualização     */
		/*    e o timestamp atual: posição e orientação, execução de disparos de projéteis, etc.         */
		/*                                                                                               */
		/* 3) Processa entrada do usuário (teclado) e atualiza estados do player conforme a necessidade. */
		/*                                                                                               */
		/* 4) Desenha a cena, a partir dos estados dos elementos.                                        */
		/*                                                                                               */
		/* 5) Espera um período de tempo (de modo que delta seja aproximadamente sempre constante).      */
		/*                                                                                               */
		/*************************************************************************************************/
		while(running){
			delta = System.currentTimeMillis() - currentTime;
			currentTime = System.currentTimeMillis();
			// player.desenha(delta);

			/* chamada a display() da classe GameLib atualiza o desenho exibido pela interface do jogo. */
			GameLib.display();

			/* faz uma pausa de modo que cada execução do laço do main loop demore aproximadamente 3 ms. */
			busyWait(currentTime + 3);

			
			/* desenhando plano de fundo próximo */
			
			GameLib.setColor(Color.GRAY);
			GameLib.drawCircle(10, 10, 10);
			
						
		}

		System.exit(0);

	}
}