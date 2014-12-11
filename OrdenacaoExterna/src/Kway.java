import java.io.IOException;

public class Kway {

	private int quantidadeArquivos;
	private int[] agrupamentoArquivos;
	private int quantidadeMemoriaDisponivel;
	private String nomeArquivoEntrada;
	private int numeroArquivoEntrada;

	private Merge merge;

	public Kway(int quantidadeArquivos, int[] agrupamentoArquivos,
			int quantidadeMemoriaDisponivel, String nomeArquivoEntrada) {
		this.quantidadeArquivos = quantidadeArquivos;
		this.agrupamentoArquivos = agrupamentoArquivos;
		this.nomeArquivoEntrada = nomeArquivoEntrada;
		this.quantidadeMemoriaDisponivel = quantidadeMemoriaDisponivel;
	}

	public void kway() {
		if (validarAgrupamentoArquivos()) {
			System.out.println(agrupamentoArquivos.length
					+ "-way merge detectado!");
			String nomeArquivoEntrada = "";
			for (int posicaoAgrupamento = 0; posicaoAgrupamento < agrupamentoArquivos.length; posicaoAgrupamento++) {
				System.out.println("\n--- INICIANDO " + (posicaoAgrupamento + 1)
						+ "° ETAPA ---\n");

				int quantidadeMergesEtapa = quantidadeArquivos
						/ agrupamentoArquivos[posicaoAgrupamento];
				quantidadeArquivos = quantidadeMergesEtapa;
				numeroArquivoEntrada = 1;

				try {
					for (int numeroMerge = 0; numeroMerge < quantidadeMergesEtapa; numeroMerge++) {

						System.out.println("Realizando o " + (numeroMerge + 1)
								+ "° merge da " + (posicaoAgrupamento + 1)
								+ "° etapa.");

						merge = new Merge(quantidadeMemoriaDisponivel,
								agrupamentoArquivos[posicaoAgrupamento],
								this.nomeArquivoEntrada, numeroArquivoEntrada);
						nomeArquivoEntrada = merge.merge();

						numeroArquivoEntrada += agrupamentoArquivos[posicaoAgrupamento];
					}
					Merge.setNumeroArquivoSaida(1);
					this.nomeArquivoEntrada = nomeArquivoEntrada;

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		} else {
			System.out
					.println("Agrupamento de arquivos não é compatível com a quantidade de arquivos passados.");
		}

	}

	/**
	 * Verifica se o valores passados no agrupamento de arquivos corresponde a
	 * quantidade de arquivos passados.
	 * 
	 * @return verdadeiro se há correspondência
	 */
	private boolean validarAgrupamentoArquivos() {
		int quantidadeArquivosAgrupamento = 1;
		for (int i = 0; i < agrupamentoArquivos.length; i++) {
			quantidadeArquivosAgrupamento *= agrupamentoArquivos[i];
		}
		if (quantidadeArquivosAgrupamento == quantidadeArquivos) {
			return true;
		}
		return false;
	}
}
