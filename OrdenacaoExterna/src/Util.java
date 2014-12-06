
public class Util {
	
	/**
	 * Concatena dois vetores em um único vetor.
	 * 
	 * @param vetor1
	 *            o primeiro vetor a ser concatenado.
	 * @param vetor2
	 *            o segundo vetor a ser concatenado.
	 * @return um terceiro vetor correspondendo à concatenação dos dois vetores
	 *         passados como parâmetro.
	 */
	public static byte[] concat(byte[] vetor1, byte[] vetor2) {
		byte[] novoVetor = new byte[vetor1.length + vetor2.length];
		int posicaoNovoVetor = 0;

		for (int i = 0; i < vetor1.length; i++) {
			novoVetor[posicaoNovoVetor] = vetor1[i];
			posicaoNovoVetor++;
		}
		for (int i = 0; i < vetor2.length; i++) {
			novoVetor[posicaoNovoVetor] = vetor2[i];
			posicaoNovoVetor++;
		}

		return novoVetor;
	}
	
	/**
	 * Faz uma cópia de um pedaço de um vetor. Semelhante ao substring de
	 * String. O vetor original não é alterado.
	 * 
	 * @param vetorOrigem
	 *            o vetor de onde se quer copiar a informação.
	 * @param posicaoInicial
	 *            a posição inicial em que a informação desejada está
	 *            localizada.
	 * @param posicaoFinal
	 *            a posição final em que a informação desejada está localizada.
	 * @return a cópia do pedaço do vetor original em um outro vetor.
	 * 
	 */
	public static byte[] subvetor(byte[] vetorOrigem, int posicaoInicial,
			int posicaoFinal) {
		byte[] novoVetor = new byte[posicaoFinal - posicaoInicial + 1];
		int posicaoNovoVetor = 0;

		for (int i = posicaoInicial; i <= posicaoFinal; i++) {
			novoVetor[posicaoNovoVetor] = vetorOrigem[i];
			posicaoNovoVetor++;
		}

		return novoVetor;
	}

}
