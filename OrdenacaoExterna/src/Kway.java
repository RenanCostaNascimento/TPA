import java.io.IOException;

public class Kway {

	private int quantidadeArquivos;
	private int quantidadeMergesIniciais;
	private int quantidadeMemoriaDisponivel;
	private String nomeArquivoEntrada;

	private Merge merge;

	public Kway(int quantidadeArquivos, int quantidadeMergesIniciais,
			int quantidadeMemoriaDisponivel, String nomeArquivoEntrada) {
		this.quantidadeArquivos = quantidadeArquivos;
		this.quantidadeMergesIniciais = quantidadeMergesIniciais;
		this.nomeArquivoEntrada = nomeArquivoEntrada;
		this.quantidadeMemoriaDisponivel = quantidadeMemoriaDisponivel;
	}

	public void kway() {
		int quantidadeArquivos = this.quantidadeArquivos
				/ this.quantidadeMergesIniciais;

		try {
			String nomeArquivoMerge = "";
			for (int i = 0; i < quantidadeMergesIniciais; i++) {
				System.out.println("Realizando o " + (i + 1)
						+ "° merge entre " + quantidadeArquivos
						+ " arquivos...");

				merge = new Merge(quantidadeMemoriaDisponivel,
						quantidadeArquivos, nomeArquivoEntrada, i
								* quantidadeArquivos + 1);

				nomeArquivoMerge = merge.merge();

			}

			if (quantidadeMergesIniciais != 1) {
				System.out.println("Realizando o merge final entre os "
						+ quantidadeMergesIniciais
						+ " arquivos resultantes dos merges anteriores...");
				merge = new Merge(quantidadeMemoriaDisponivel,
						quantidadeMergesIniciais, nomeArquivoMerge, 1);

				merge.merge();

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
