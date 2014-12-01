import java.io.IOException;

public class Main {

	public static void main(String args[]) {
		ManipularArquivo manipularArquivo;
		try {
			manipularArquivo = new ManipularArquivo();
			
//			manipularArquivo.gerarArquivo(Constantes.CAMINHO_ARQUIVO
//			+ Constantes.ARQUIVO_1_GIGA, Constantes.TAMANHO_GIGA);
//			
//			manipularArquivo.ordenarArquivo(Constantes.CAMINHO_ARQUIVO
//					+ Constantes.ARQUIVO_1_GIGA, Constantes.TAMANHO_GIGA/2);
			
			manipularArquivo.fileToString(Constantes.CAMINHO_ARQUIVO+"1Giga_Saida2");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		
		
	}
}
