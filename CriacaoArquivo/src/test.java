import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class test {

	private static final float GIGA_SIZE = 1073741824;
	private static final float QUANT_GIGA = 100;

	public static void main(String args[]) {
		try {
			BufferedOutputStream boss = new BufferedOutputStream(
					new FileOutputStream(
							"C:\\Users\\20121bsi0252\\Downloads\\test.bin"));

			for (int i = 0; i < QUANT_GIGA; i++) {
				double bufferSize = 0;
				while (bufferSize < GIGA_SIZE) {
					byte[] registoByte = new Registro().serialize();
					boss.write(registoByte);
					bufferSize += registoByte.length;
				}
				boss.flush();
			}

			boss.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
