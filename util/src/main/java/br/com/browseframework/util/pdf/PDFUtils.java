package br.com.browseframework.util.pdf;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import javax.imageio.ImageIO;

public class PDFUtils {

	/**
	 * Converts a PDF file into an array of image bytes.
	 * @param inputStreamPDF
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public static List<ByteArrayOutputStream> convertPdfToImage(InputStream inputStreamPDF) throws IOException {
		final List<ByteArrayOutputStream> retorno = new ArrayList<ByteArrayOutputStream>();
		
		final PDDocument document = PDDocument.load(inputStreamPDF);
		try {
			final List pages = document.getDocumentCatalog().getAllPages();
			final Iterator iter = pages.iterator();
			while (iter.hasNext()) {
				final PDPage page = (PDPage) iter.next();
				BufferedImage originalImage = page.convertToImage();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write( originalImage, "jpg", baos );
				baos.flush();
				baos.close();
				retorno.add(baos);
			}
		} finally {
			document.close();
		}
		
		return retorno;
	}
}
