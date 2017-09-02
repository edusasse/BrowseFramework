package br.com.browseframework.jsfprimefaces.util;

public class Util {

	/**
	 * Retrieves a context type for the given file extension.
	 * @param fileExtension
	 * @return
	 */
	public static String getContentType(String fileExtension) {
		if (fileExtension.equalsIgnoreCase("htm")
				|| fileExtension.equalsIgnoreCase("html")
				|| fileExtension.equalsIgnoreCase("log")) {
			return "text/HTML";
		}
		if (fileExtension.equalsIgnoreCase("txt")) {
			return "text/plain";
		}
		if (fileExtension.equalsIgnoreCase("doc")
				|| fileExtension.equalsIgnoreCase("docx")) {
			return "application/ms-word";
		}
		if (fileExtension.equalsIgnoreCase("tiff")
				|| fileExtension.equalsIgnoreCase("tif")) {
			return "image/tiff";
		}
		if (fileExtension.equalsIgnoreCase("asf")) {
			return "video/x-ms-asf";
		}
		if (fileExtension.equalsIgnoreCase("avi")) {
			return "video/avi";
		}
		if (fileExtension.equalsIgnoreCase("zip")) {
			return "application/zip";
		}
		if (fileExtension.equalsIgnoreCase("xls")
				|| fileExtension.equalsIgnoreCase("xlsx")
				|| fileExtension.equalsIgnoreCase("csv")) {
			return "application/vnd.ms-excel";
		}
		if (fileExtension.equalsIgnoreCase("gif")) {
			return "image/gif";
		}
		if (fileExtension.equalsIgnoreCase("jpg")
				|| fileExtension.equalsIgnoreCase("jpeg")) {
			return "image/jpeg";
		}
		if (fileExtension.equalsIgnoreCase("png")
				|| fileExtension.equalsIgnoreCase("png")) {
			return "image/png";
		}
		if (fileExtension.equalsIgnoreCase("bmp")) {
			return "image/bmp";
		}
		if (fileExtension.equalsIgnoreCase("wav")) {
			return "audio/wav";
		}
		if (fileExtension.equalsIgnoreCase("mp3")) {
			return "audio/mpeg3";
		}
		if (fileExtension.equalsIgnoreCase("mpg")
				|| fileExtension.equalsIgnoreCase("mpeg")) {
			return "video/mpeg";
		}
		if (fileExtension.equalsIgnoreCase("rtf")) {
			return "application/rtf";
		}
		if (fileExtension.equalsIgnoreCase("asp")) {
			return "text/asp";
		}
		if (fileExtension.equalsIgnoreCase("pdf")) {
			return "application/pdf";
		}
		if (fileExtension.equalsIgnoreCase("fdf")) {
			return "application/vnd.fdf";
		}
		if (fileExtension.equalsIgnoreCase("ppt")
				|| fileExtension.equalsIgnoreCase("ppts")) {
			return "application/mspowerpoint";
		}
		if (fileExtension.equalsIgnoreCase("dwg")) {
			return "image/vnd.dwg";
		}
		if (fileExtension.equalsIgnoreCase("msg")) {
			return "application/msoutlook";
		}
		if (fileExtension.equalsIgnoreCase("xml")
				|| fileExtension.equalsIgnoreCase("sdxl")) {
			return "application/xml";
		}
		if (fileExtension.equalsIgnoreCase("xdp")) {
			return "application/vnd.adobe.xdp+xml";
		} else {
			return "application/octet-stream";
		}
	}
	
}
