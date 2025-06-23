import java.util.*;
import java.io.*;
import classes.*;

public class Teste{
	public static void main(String args[]){
		String configPath = "arquivos/Config.txt";
		try{
			
			File configFile = new File(configPath);
			Scanner config = new Scanner(configFile);
			int playerVida = config.nextInt();
			int qtdFases = config.nextInt();
			File fase1File = new File(config.next());
			File fase2File = new File(config.next());
			Scanner fase1 = new Scanner(fase1File);
			Scanner fase2 = new Scanner(fase2File);
			
		}catch(FileNotFoundException e){
			System.err.println("Arquivo não encontrado: " + e.getMessage());
		}
		
		if(faseAtual <= qtdFases){
			try{
				File faseFile = new File(config.next());
				Scanner fase = new Scanner(faseFile);
			}catch{
			}
		}
				
	}
	
	
}