import java.io.IOException;

public class Main {

	public static void main(String args[]) {
		OrdenacaoInterna ordenacaoInterna;
		try {
			ordenacaoInterna = new OrdenacaoInterna();

//			 ordenacaoInterna.gerarArquivo(Constantes.CAMINHO_ARQUIVO
//			 + Constantes.ARQUIVO_1_GIGA, Constantes.TAMANHO_MEGA);
			
//			 ordenacaoInterna.ordenarArquivo(Constantes.CAMINHO_ARQUIVO
//			 + Constantes.ARQUIVO_1_GIGA, Constantes.TAMANHO_MEGA/4);

//			Kway kway = new Kway(4, 2, Constantes.TAMANHO_MEGA/2,
//					"1Giga_Saida");
//			
//			kway.kway();

			ordenacaoInterna.fileToString(Constantes.CAMINHO_ARQUIVO+"1Giga_Saida_Merge_Merge3");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
