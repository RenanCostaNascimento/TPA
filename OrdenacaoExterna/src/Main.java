public class Main {

	public static void main(String args[]) {
		ManipularArquivo manipularArquivo = new ManipularArquivo();

		manipularArquivo.gerarArquivo(Constantes.CAMINHO_ARQUIVO
				+ Constantes.ARQUIVO_1_GIGA, Constantes.TAMANHO_GIGA);

		manipularArquivo.ordenarArquivo(Constantes.CAMINHO_ARQUIVO
				+ Constantes.ARQUIVO_1_GIGA, Constantes.TAMANHO_GIGA);

	}
}
