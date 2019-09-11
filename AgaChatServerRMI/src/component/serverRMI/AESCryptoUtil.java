/**
 * Class: AESCryptoUtil
 * 
 * @version  1.0
 * 
 * @since 07-09-2019
 * 
 * @autor Aga
 *
 */

package component.serverRMI;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class AESCryptoUtil {

	public static Key getKey(String seed) {

		Key key = null;

		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			key = keyGenerator.generateKey();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}

		return key;
	}

	public static byte[] getTextCipher(String text,Key key) {

		byte[] cipher = null;

		try {

			Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
			aes.init(Cipher.ENCRYPT_MODE, key);
			cipher = aes.doFinal(text.getBytes());

		} catch (Exception e) {
			
			e.printStackTrace();
		}

		return cipher;

	}

	public static String getTextPlain(byte[] cipher,Key key) {

		String textPlain = null;

		try {

			Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
			aes.init(Cipher.DECRYPT_MODE, key);
			byte[] desCipher = aes.doFinal(cipher);
			textPlain = new String(desCipher);

		} catch (Exception e) {
			
			e.printStackTrace();
		}

		return textPlain;

	}

	
}