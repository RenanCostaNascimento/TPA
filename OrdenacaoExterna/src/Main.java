import java.io.IOException;
import java.util.GregorianCalendar;

public class Main {
	
	

	public static void main(String args[]) throws ClassNotFoundException {

		int quantidadeMemoriaDisponível = Constantes.TAMANHO_GIGA / 2;
		long tempoInicial, tempoFinal, tamanhoArquivo;
		
//		tamanhoArquivo = Constantes.TAMANHO_GIGA_LONG*1;
////
//		 criarArquivo(Constantes.CAMINHO_ARQUIVO
//		 + Constantes.ARQUIVO_1_GIGA, tamanhoArquivo);
		
		tempoInicial = GregorianCalendar.getInstance().getTimeInMillis();

//		ordenarArquivo(Constantes.CAMINHO_ARQUIVO
//		 + Constantes.ARQUIVO_10_GIGA, quantidadeMemoriaDisponível);
		
//		int[] agrupamentoArquivos = {5, 2, 2};
//		kway(20, agrupamentoArquivos, quantidadeMemoriaDisponível, "10Giga_Saida");
		
		mergeSortExterno("1Giga", 2);
		
		tempoFinal =  GregorianCalendar.getInstance().getTimeInMillis();
		
		System.out.println("Tempo final: " + (tempoFinal - tempoInicial));
		System.out.println("Quantidade de seeks: " + MergeSortExterno.seeks);
		

	}

	/**
	 * Cria o arquivo de tamanho e nome fornecido.
	 * 
	 * @param nomeArquivo
	 * @param tamanhoArquivo
	 * @throws IOException
	 */
	public static void criarArquivo(String nomeArquivo, long tamanhoArquivo) {
		OrdenacaoInterna ordenacaoInterna;
		try {
			ordenacaoInterna = new OrdenacaoInterna();
			ordenacaoInterna.gerarArquivo(nomeArquivo, tamanhoArquivo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Ordena o arquivo especificado, gerando n arquivos menores, todos
	 * ordenados. A quantidade de arquivos n depende da quantidade de memória
	 * disponível.
	 * 
	 * @param nomeArquivo
	 * @param quantidadeMemoriaDisponivel
	 * @throws IOException
	 */
	public static void ordenarArquivo(String nomeArquivo,
			int quantidadeMemoriaDisponivel) {
		OrdenacaoInterna ordenacaoInterna;
		try {
			ordenacaoInterna = new OrdenacaoInterna();
			ordenacaoInterna.ordenarArquivo(nomeArquivo,
					quantidadeMemoriaDisponivel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Realizar o k-way de um arquivo. O mesmo método é utilizado para k-way
	 * simples e múltiplo. Qual destes será executado, assim como quantos
	 * níveis, depende dos parâmetros passados.
	 * 
	 * @param quantidadeArquivos
	 *            a quantidade de arquivos que se quer realizar o merge.
	 * @param agrupamentoArquivos
	 *            representa o agrupamento em que os arquivos serão "mergeados".
	 *            O tamanho do vetor indica a quantidade de níveis no merge.
	 *            Cada posição do vetor indica quantos arquivos serão agrupados
	 *            para a realização do merge. O valor contido na posição i do
	 *            vetor deve ser maior ou igual à posição i+1, caso exista. O
	 *            produto entre todos os valores do vetor deve ser igual à
	 *            quantidade de arquivos. Exemplo: utilizando 40 arquivos de
	 *            entrada, os possíveis vetores são possíveis: [5, 8] - 2
	 *            níveis, 1° nível de 5 em 5 arquivos (gera 8 arquivos), 2°
	 *            nível de 8 arquivos (gera 1 arquivo final); [5, 4, 2] - 3
	 *            níveis, 1° nível de 5 em 5 arquivos (gera 8 arquivos), 2°
	 *            nível de 4 em 4 arquivos (gera 2 arquivos), 3° nível de 2
	 *            arquivos (gera 1 arquivo final).
	 * @param memoriaDisponivel
	 *            a quantidade de memória disponível.
	 * @param nomeArquivo
	 *            o nome do arquivo base de entrada. Se os nomes dos arquivos de
	 *            entrada forem, por exemplo entrada1, entrada2, ..., entradaN;
	 *            então o nome do arquivo base é entrada.
	 */
	public static void kway(int quantidadeArquivos, int[] agrupamentoArquivos,
			int memoriaDisponivel, String nomeArquivo) {

		Kway kway = new Kway(quantidadeArquivos, agrupamentoArquivos,
				memoriaDisponivel, nomeArquivo);

		kway.kway();
	}

	/**
	 * Realiza o mergeSort de N arquivos.
	 * 
	 * @param nomeArquivo
	 * @param quantidadeArquivos
	 */
	public static void mergeSortExterno(String nomeArquivo,
			int quantidadeArquivos) {
		MergeSortExterno mergeSortExterno;
		try {
			mergeSortExterno = new MergeSortExterno(nomeArquivo,
					quantidadeArquivos);
			mergeSortExterno.mergeSortExterno();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
