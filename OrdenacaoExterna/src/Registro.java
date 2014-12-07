import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;

public class Registro implements Serializable, Comparable<Registro> {

	private static final long serialVersionUID = 627441170405465755L;

	public static int quantidade = 0;

	private int numero;
	private char[] nome = new char[50];
	private char[] sobrenome = new char[50];
	private char[] endereco = new char[256];
	private char[] telefone = new char[20];
	private float saldo;

	public Registro() {
		Random random = new Random();
		quantidade++;
		numero = quantidade;
		gerarNome(nome, 50);
		gerarNome(sobrenome, 50);
		gerarNome(endereco, 256);
		gerarNome(telefone, 20);
		if (numero % 2 == 0) {
			saldo = random.nextInt(10001) * random.nextInt(10001);
		} else {
			saldo = random.nextInt(1001) * -random.nextInt(101);
		}
		if(saldo != 0){
			saldo /= 100;
		}

	}

	private void gerarNome(char[] nomeVetor, int tamanhoVetor) {
		Random r = new Random();
		for (int i = 0; i < tamanhoVetor; i++) {
			nomeVetor[i] = (char) (r.nextInt(26) + 'a');
		}
	}

	public byte[] serialize() throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(this);
		return b.toByteArray();
	}

	public static Object deserialize(byte[] bytes) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		ObjectInputStream o = new ObjectInputStream(b);
		return o.readObject();
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append(saldo + " ");

		return builder.toString();

	}

	@Override
	public int compareTo(Registro registro) {
		if (this.saldo < registro.saldo) {
			return -1;
		} else {
			if (this.saldo > registro.saldo) {
				return 1;
			}
		}

		return 0;
	}

	public float getSaldo() {
		return saldo;
	}

}
