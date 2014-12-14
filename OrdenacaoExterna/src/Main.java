import java.io.IOException;
import java.util.GregorianCalendar;

public class Main {
	
	

	public static void main(String args[]) throws ClassNotFoundException {

		int quantidadeMemoriaDispon�vel = Constantes.TAMANHO_GIGA / 2;
		long tempoInicial, tempoFinal, tamanhoArquivo;
		
//		tamanhoArquivo = Constantes.TAMANHO_GIGA_LONG*1;
////
//		 criarArquivo(Constantes.CAMINHO_ARQUIVO
//		 + Constantes.ARQUIVO_1_GIGA, tamanhoArquivo);
		
		tempoInicial = GregorianCalendar.getInstance().getTimeInMillis();

//		ordenarArquivo(Constantes.CAMINHO_ARQUIVO
//		 + Constantes.ARQUIVO_10_GIGA, quantidadeMemoriaDispon�vel);
		
//		int[] agrupamentoArquivos = {5, 2, 2};
//		kway(20, agrupamentoArquivos, quantidadeMemoriaDispon�vel, "10Giga_Saida");
		
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
	 * ordenados. A quantidade de arquivos n depende da quantidade de mem�ria
	 * dispon�vel.
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
	 * Realizar o k-way de um arquivo. O mesmo m�todo � utilizado para k-way
	 * simples e m�ltiplo. Qual destes ser� executado, assim como quantos
	 * n�veis, depende dos par�metros passados.
	 * 
	 * @param quantidadeArquivos
	 *            a quantidade de arquivos que se quer realizar o merge.
	 * @param agrupamentoArquivos
	 *            representa o agrupamento em que os arquivos ser�o "mergeados".
	 *            O tamanho do vetor indica a quantidade de n�veis no merge.
	 *            Cada posi��o do vetor indica quantos arquivos ser�o agrupados
	 *            para a realiza��o do merge. O valor contido na posi��o i do
	 *            vetor deve ser maior ou igual � posi��o i+1, caso exista. O
	 *            produto entre todos os valores do vetor deve ser igual �
	 *            quantidade de arquivos. Exemplo: utilizando 40 arquivos de
	 *            entrada, os poss�veis vetores s�o poss�veis: [5, 8] - 2
	 *            n�veis, 1� n�vel de 5 em 5 arquivos (gera 8 arquivos), 2�
	 *            n�vel de 8 arquivos (gera 1 arquivo final); [5, 4, 2] - 3
	 *            n�veis, 1� n�vel de 5 em 5 arquivos (gera 8 arquivos), 2�
	 *            n�vel de 4 em 4 arquivos (gera 2 arquivos), 3� n�vel de 2
	 *            arquivos (gera 1 arquivo final).
	 * @param memoriaDisponivel
	 *            a quantidade de mem�ria dispon�vel.
	 * @param nomeArquivo
	 *            o nome do arquivo base de entrada. Se os nomes dos arquivos de
	 *            entrada forem, por exemplo entrada1, entrada2, ..., entradaN;
	 *            ent�o o nome do arquivo base � entrada.
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
