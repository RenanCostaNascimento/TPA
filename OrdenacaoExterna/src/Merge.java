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
		nomeArquivoSaida = nomeArquivoEntrada + Constantes.ARQUIVO_SAIDA_MERGE;
		this.nomeArquivoEntrada = Constantes.CAMINHO_ARQUIVO
				+ nomeArquivoEntrada;
		this.arquivoSaida = new RandomAccessFile(Constantes.CAMINHO_ARQUIVO
				+ nomeArquivoSaida + numeroArquivoSaida, "rw");
		numeroArquivoSaida++;

		arquivos = new RandomAccessFile[quantidadeArquivos];
		registros = new Registro[quantidadeArquivos];
		buffersEntrada = new Buffer[quantidadeArquivos];

		tamanhoRegistro = new Registro().serialize().length;
		bufferSaida = new Buffer(
				otimizarTamanhoMemoria(quantidadeMemoriaDisponivel
						/ (quantidadeArquivos + 1)));

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
			} else {
				escreverBufferSaida(bufferSaida.getBuffer());
			}
		}

		fecharArquivos();

		return nomeArquivoSaida;

	}

//	private void validarBufferSaida() {
//		int quantidadeRegistros = 0;
//		while (bufferSaida.getPonteiro() < bufferSaida.getQuantidadeDados()) {
//			try {
//				Registro registro = (Registro) Registro.deserialize(bufferSaida
//						.recuperarDados(tamanhoRegistro));
//				quantidadeRegistros++;
//			} catch (ClassNotFoundException | IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		System.out.println(quantidadeRegistros);
//
//	}

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
	private void escreverBufferSaida(byte[] buffer) throws IOException {
//		validarBufferSaida();
		System.out.println("Buffer cheio, descarregando " + buffer.length
				+ " bytes no arquivo...");
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
			}
			// buffer cheio
			else {
				escreverBufferSaida(bufferSaida.getBuffer());
				esvaziarBufferSaida();
				if (bufferSaida.getBuffer().length != 0) {
					bufferSaida.adicionarDados(registros[posicaoRegistro]
							.serialize());
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Esvazia o buffer se saida.
	 * 
	 * @throws IOException
	 */
	private void esvaziarBufferSaida() throws IOException {
		int possivelTamanhoBuffer = 0;
		int tamanhoBufferOtimo = otimizarTamanhoMemoria(quantidadeMemoriaDisponivel
				/ (quantidadeArquivos + 1));
		
		for (int i = 0; i < quantidadeArquivos; i++) {
			possivelTamanhoBuffer += (arquivos[i].length() - arquivos[i]
					.getFilePointer());
			possivelTamanhoBuffer += (buffersEntrada[i]
					.getQuantidadeDados() - buffersEntrada[i].getPonteiro());
			if(registros[i] != null){
				possivelTamanhoBuffer += tamanhoRegistro;
			}
			if (possivelTamanhoBuffer >= tamanhoBufferOtimo) {
				break;
			}
		}
		
		if (possivelTamanhoBuffer <= tamanhoBufferOtimo) {
			tamanhoBufferOtimo = possivelTamanhoBuffer;
		}

		bufferSaida = new Buffer(tamanhoBufferOtimo);

	}

	/**
	 * Encontra o menor Registro dado um vetor de Registros.
	 * 
	 * @return a posição do menor Registro.
	 */
	private int encontrarMenorRegistro() {
		int menorPosicao = -1;
		float menorSaldo = Float.MAX_VALUE;

		for (int i = 0; i < quantidadeArquivos; i++) {
			if (registros[i] != null && registros[i].getSaldo() <= menorSaldo) {
				menorSaldo = registros[i].getSaldo();
				menorPosicao = i;
			}
		}
		return menorPosicao;
	}

	/**
	 * Fecha os arquivos abertos.
	 */
	private void fecharArquivos() {
		System.out.println("Fechando arquivos abertos...\n");
		try {
			for (int i = 0; i < quantidadeArquivos; i++) {

				arquivos[i].close();

			}
			arquivoSaida.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			// cabe mais um registro no buffer?
			if (bufferEntrada.getPonteiro() + tamanhoRegistro <= bufferEntrada
					.getQuantidadeDados()) {
				return (Registro) Registro.deserialize(bufferEntrada
						.recuperarDados(tamanhoRegistro));
			} else {
				if (arquivos[numeroBuffer].length()
						- arquivos[numeroBuffer].getFilePointer() != 0) {
					preencherBuffer(numeroBuffer);
					bufferEntrada = buffersEntrada[numeroBuffer];
					return (Registro) Registro.deserialize(bufferEntrada
							.recuperarDados(tamanhoRegistro));
				}
				return null;
			}

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
		buffersEntrada[posicaoBuffer] = new Buffer(
				otimizarTamanhoMemoria(quantidadeMemoriaDisponivel
						/ (quantidadeArquivos + 1)));
		try {
			System.out.println("Lendo arquivo " + (posicaoBuffer + 1));
			// é possível ler um buffer inteiro do arquivo
			if (buffersEntrada[posicaoBuffer].getBuffer().length > arquivos[posicaoBuffer]
					.length() - arquivos[posicaoBuffer].getFilePointer()) {
				buffersEntrada[posicaoBuffer] = new Buffer(
						(int) (arquivos[posicaoBuffer].length() - arquivos[posicaoBuffer]
								.getFilePointer()));
			}
			arquivos[posicaoBuffer].read(buffersEntrada[posicaoBuffer]
					.getBuffer());
			buffersEntrada[posicaoBuffer]
					.setQuantidadeDados(buffersEntrada[posicaoBuffer]
							.getBuffer().length);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Otimiza o tamanho da memória disponível, tornando-a o maior valor
	 * possível que seja múltiplo do tamanho de um registro, sendo menor ou
	 * igual à memória disponível. Dessa forma é possível garantir que um buffer
	 * sempre conterá registro completos.
	 * 
	 * @param quantidadeMemoriaDisponivel
	 *            a quantidade de memória que se deseja otimizar.
	 * @return a quantidade de memória otimizada.
	 */
	private int otimizarTamanhoMemoria(int quantidadeMemoriaDisponivel) {
		int quantidadeMemoriaOtimizada = quantidadeMemoriaDisponivel;

		if (quantidadeMemoriaDisponivel % tamanhoRegistro != 0) {
			quantidadeMemoriaOtimizada = (quantidadeMemoriaDisponivel / tamanhoRegistro)
					* tamanhoRegistro;
		}

		return quantidadeMemoriaOtimizada;
	}

	public static void setNumeroArquivoSaida(int numeroArquivoSaida) {
		Merge.numeroArquivoSaida = numeroArquivoSaida;
	}
}
