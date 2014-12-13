import java.io.IOException;

public class Main {

	public static void main(String args[]) {
		OrdenacaoInterna ordenacaoInterna;
		try {
			ordenacaoInterna = new OrdenacaoInterna();

//			ordenacaoInterna.gerarArquivo(Constantes.CAMINHO_ARQUIVO
//					+ "1Mega_Entrada1", Constantes.TAMANHO_MEGA);
//			ordenacaoInterna.gerarArquivo(Constantes.CAMINHO_ARQUIVO
//					+ "1Mega_Entrada2", Constantes.TAMANHO_MEGA);
			
			
			// ordenacaoInterna.ordenarArquivo(Constantes.CAMINHO_ARQUIVO
			// + Constantes.ARQUIVO_1_GIGA, Constantes.TAMANHO_MEGA/2);
			

			// int[] agrupamentoArquivos = {5,5,2,2};
			//
			// Kway kway = new Kway(40, agrupamentoArquivos,
			// Constantes.TAMANHO_MEGA/2,
			// "1Giga_Saida");
			//
			// kway.kway();
			
			
//			MergeSortExterno mergeSortExterno = new MergeSortExterno("1Mega", 2);
//			mergeSortExterno.mergeSortExterno();
			

			 ordenacaoInterna.fileToString(Constantes.CAMINHO_ARQUIVO+"1Mega_Entrada1");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
//		catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}
}
