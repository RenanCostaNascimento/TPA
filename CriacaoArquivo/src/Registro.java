import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Registro implements Serializable{

	public static int quantidade = 0;
	
	private int numero;
	private String nome;
	private String sobrenome;

	public Registro() {
		quantidade++;
		numero = quantidade;
		nome = "nome " + quantidade;
	}

	public byte[] serialize() throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(this);
		return b.toByteArray();
	}
	
	public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return o.readObject();
    }
	
	public String toString(){
		return numero + " - " + nome;
	}

}
