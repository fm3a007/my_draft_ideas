/**
 *
 *  
 * These source files are released under the GPLv3 license.
 */

package unit_test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import my.frmwk.util.qrcode.BufferedImageLuminanceSource;
import my.frmwk.util.qrcode.MatrixToImageWriter;

public class TestQrCode {

	public static int testWriteQrCode() {
		try {

			String content = "http://192.168.128.22:8080/";
			String path = "E:/David/Desktop";

			MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

			Map hints = new HashMap();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400, hints);
			File file1 = new File(path, "test_qr_code.jpg");
			MatrixToImageWriter.writeToFile(bitMatrix, "jpg", file1);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	public static int testReadQrCode() {

		try {
			MultiFormatReader formatReader = new MultiFormatReader();
			String filePath = "E:/David/Desktop/test_qr_code.jpg";
			File file = new File(filePath);
			BufferedImage image = ImageIO.read(file);
			;
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			Binarizer binarizer = new HybridBinarizer(source);
			BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
			Map hints = new HashMap();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			Result result = formatReader.decode(binaryBitmap, hints);

			System.out.println("result = " + result.toString());
			System.out.println("resultFormat = " + result.getBarcodeFormat());
			System.out.println("resultText = " + result.getText());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}
	
	public	static	void	main( String[] args){
		
		System.out.println(" hello ...");
		
		int ret = 0;
		ret = testWriteQrCode();
		
		ret = testReadQrCode();
		
	}

}
