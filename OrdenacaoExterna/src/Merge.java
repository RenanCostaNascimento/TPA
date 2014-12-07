import java.io.IOException;
import java.io.RandomAccessFile;

public class Merge {

	private int tamanhoRegistro;
	private long tamanhoFinalArquivoSaida = 0;
	private static int numeroArquivoSaida = 1;
	private int numeroArquivoEntrada;

	private RandomAccessFile[] arquivos;
	private Registro[] registros;
	private Buffer[] buffersEntrada;
	private Buffer bufferSaida;
	private RandomAccessFile arquivoSaida;

	private String nomeArquivoEntrada;
	private String nomeArquivoSaida;
	private int quantidadeMemoriaDisponivel;
	private int quantidadeArquivos;

	public Merge(int quantidadeMemoriaDisponivel, int quantidadeArquivos,
			String nomeArquivoEntrada, int numeroArquivoEntrada)
			throws IOException {
		this.numeroArquivoEntrada = numeroArquivoEntrada;
		this.quantidadeMemoriaDisponivel = quantidadeMemoriaDisponivel;
		this.quantidadeArquivos = quantidadeArquivos;
		this.nomeArquivoEntrada = Constantes.CAMINHO_ARQUIVO
				+ nomeArquivoEntrada;
		nomeArquivoSaida = this.nomeArquivoEntrada
				+ Constantes.ARQUIVO_SAIDA_MERGE + numeroArquivoSaida;
		this.arquivoSaida = new RandomAccessFile(nomeArquivoSaida, "rw");
		numeroArquivoSaida++;

		arquivos = new RandomAccessFile[quantidadeArquivos];
		registros = new Registro[quantidadeArquivos];
		buffersEntrada = new Buffer[quantidadeArquivos];

		// criarBuffersEntrada(quantidadeMemoriaDisponivel, quantidadeArquivos);
		bufferSaida = new Buffer(quantidadeMemoriaDisponivel
				/ (quantidadeArquivos + 1));
		tamanhoRegistro = new Registro().serialize().length;
	}

	/**
	 * Abre os arquivos que deverão ser "mergeados".
	 * 
	 * @param nomeArquivoEntrada
	 *            o nome base do arquivo. Esse nome será tratado de modo a gerar
	 *            o nome real dos arquivos.
	 */
	private void abrirArquivos() {
		for (int i = 0; i < quantidadeArquivos; i++) {
			try {
				arquivos[i] = new RandomAccessFile(nomeArquivoEntrada
						+ numeroArquivoEntrada, "r");
				tamanhoFinalArquivoSaida += arquivos[i].length();
				numeroArquivoEntrada++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * Fecha os arquivos abertos.
	 */
	private void fecharArquivos() {
		for (int i = 0; i < quantidadeArquivos; i++) {
			try {
				arquivos[i].close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Realiza o merge dos arquivos especificados nos atributos da classe.
	 * 
	 * @throws IOException
	 */
	protected String merge() throws IOException {
		inicializarMerge();

		while (arquivoSaida.length() < tamanhoFinalArquivoSaida) {
			int posicaoMenorRegistro = encontrarMenorRegistro();
			if (posicaoMenorRegistro != -1) {
				encherBufferSaida(posicaoMenorRegistro);
				registros[posicaoMenorRegistro] = recuperarProximoRegistro(posicaoMenorRegistro);
			}else{
				escreverBuffer(bufferSaida.getBuffer());;
			}
		}

		fecharArquivos();

		return nomeArquivoSaida;

	}

	/**
	 * Faz os preparativos iniciais para o merge: abrir os arquivos, carregar os
	 * buffers de entrada, recuperar o primeiro registro de cada buffer.
	 */
	private void inicializarMerge() {
		System.out.println("Inicializando merge...");
		abrirArquivos();

		for (int i = 0; i < quantidadeArquivos; i++) {
			preencherBuffer(i);
		}

		for (int i = 0; i < quantidadeArquivos; i++) {
			registros[i] = recuperarProximoRegistro(i);
		}
	}

	/**
	 * Escreve um vetor de bytes no arquivo de saída.
	 * 
	 * @param buffer
	 *            o buffer que deverá ser escrito.
	 * @throws IOException
	 */
	private void escreverBuffer(byte[] buffer) throws IOException {
		System.out
				.println("Buffer cheio, descarregando no arquivo...");
		arquivoSaida.write(buffer);
	}

	/**
	 * Enche o buffer de saida com um registro. Uma vez que o buffer esteja
	 * cheio, escreve o buffer no arquivo de saida e esvazia o buffer.
	 * 
	 * @param posicaoRegistro
	 *            a posição do registro no vetor de registros que deverá ser
	 *            adicionado ao buffer.
	 */
	private void encherBufferSaida(int posicaoRegistro) {
		try {
			// é possível adicionar um registro completo no buffer
			if (bufferSaida.getQuantidadeDados() + tamanhoRegistro <= bufferSaida
					.getBuffer().length) {
				bufferSaida.adicionarDados(registros[posicaoRegistro]
						.serialize());
			} else {
				escreverBuffer(bufferSaida.getBuffer());
				bufferSaida = new Buffer(quantidadeMemoriaDisponivel
						/ (quantidadeArquivos + 1));
				bufferSaida.adicionarDados(registros[posicaoRegistro]
						.serialize());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Encontra o menor Registro dado um vetor de Registros.
	 * 
	 * @return a posição do menor Registro.
	 */
	private int encontrarMenorRegistro() {
		int menorPosicao = -1;
		float menorSaldo = 1000001;

		for (int i = 0; i < quantidadeArquivos; i++) {
			if (registros[i] != null && registros[i].getSaldo() < menorSaldo) {
				menorSaldo = registros[i].getSaldo();
				menorPosicao = i;
			}
		}
		return menorPosicao;
	}

	/**
	 * Recupera o próximo registro no buffer de entrada especificado. O método
	 * também faz o tratamento de "registros remanescentes", preechendo o buffer
	 * se for necessário.
	 * 
	 * @param posicaoBuffer
	 *            especifica de qual buffer o próximo registro será recuperado.
	 * @return o Registro recuperado.
	 */
	private Registro recuperarProximoRegistro(int numeroBuffer) {

		Buffer bufferEntrada = buffersEntrada[numeroBuffer];

		try {
			// é possível ler mais um registro completo
			if (bufferEntrada.getPonteiro() + tamanhoRegistro <= bufferEntrada
					.getBuffer().length) {
				return (Registro) Registro.deserialize(bufferEntrada
						.recuperarDados(tamanhoRegistro));
			}
			// registro remanescente
			else {
				if (arquivos[numeroBuffer].length()
						- arquivos[numeroBuffer].getFilePointer() > 0) {
					return recuperarRegistroRemanescente(numeroBuffer);
				}
				System.out
						.println("Arquivo "
								+ numeroBuffer+1
								+ " completamente percorrido. Bytes faltantes: "
								+ (arquivos[numeroBuffer].length() - arquivos[numeroBuffer]
										.getFilePointer()));
				return null;
			}

		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Recupera um registro remanescente: um registro em que metade de seus
	 * bytes estão em um buffer n, e a outra metade se encontra em um buffer
	 * n+1.
	 * 
	 * @param numeroBuffer
	 *            numero/posição do buffer no vetor de buffers.
	 * @return o registro remanescente.
	 */
	private Registro recuperarRegistroRemanescente(int numeroBuffer) {
		Buffer bufferEntrada = buffersEntrada[numeroBuffer];
		try {

			int bytesRemanescentesFinalBuffer = bufferEntrada.getBuffer().length
					- bufferEntrada.getPonteiro();

			byte[] remanescenteFinalBuffer = bufferEntrada
					.recuperarDados(bytesRemanescentesFinalBuffer);

			preencherBuffer(numeroBuffer);
			bufferEntrada = buffersEntrada[numeroBuffer];

			byte[] faltanteInicioBuffer = bufferEntrada
					.recuperarDados(tamanhoRegistro
							- bytesRemanescentesFinalBuffer);

			return (Registro) Registro.deserialize(Util.concat(
					remanescenteFinalBuffer, faltanteInicioBuffer));
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Preenche um buffer de entrada se seu arquivo correspondente.
	 * 
	 * @param posicaoBuffer
	 *            a posição do buffer no vetor de buffers que se deseja
	 *            preencher.
	 */
	private void preencherBuffer(int posicaoBuffer) {
		buffersEntrada[posicaoBuffer] = new Buffer(quantidadeMemoriaDisponivel
				/ (quantidadeArquivos + 1));
		try {
			System.out.println("Lendo arquivo " + (posicaoBuffer + 1));
			// é possível ler um buffer inteiro do arquivo
			if (arquivos[posicaoBuffer].length()
					- arquivos[posicaoBuffer].getFilePointer() >= buffersEntrada[posicaoBuffer]
						.getBuffer().length) {
				arquivos[posicaoBuffer].read(buffersEntrada[posicaoBuffer]
						.getBuffer());
			} else {
				buffersEntrada[posicaoBuffer] = new Buffer(
						(int) (arquivos[posicaoBuffer].length() - arquivos[posicaoBuffer]
								.getFilePointer()));
				arquivos[posicaoBuffer].read(buffersEntrada[posicaoBuffer]
						.getBuffer());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
