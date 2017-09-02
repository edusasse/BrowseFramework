package br.com.browseframework.util.jar;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarException;
import java.util.jar.JarFile;
import java.util.logging.Logger;

import br.com.browseframework.util.tree.GenericTree;
import br.com.browseframework.util.tree.GenericTreeNode;

public class JarFileUtils {
	
	static Logger log = Logger.getLogger(JarFileUtils.class.getName());
	private static JarFileUtils jarFileUtils = null;
	 
	/**
	 * Returns an instance of JarFileUtils (Singleton)
	 * 
	 * @return
	 */
	public static JarFileUtils getInstance() {
		if (jarFileUtils == null) {
			jarFileUtils = new JarFileUtils();
		}

		return jarFileUtils;
	}

	/**
	 * List the contents of a jar
	 * 
	 * @param jarPath
	 * @return
	 * @throws IOException
	 */
	public static List<String> getJarContent(String jarPath) throws IOException {
		List<String> content = new ArrayList<String>();
		JarFile jarFile = new JarFile(jarPath);
		Enumeration<JarEntry> e = jarFile.entries();

		while (e.hasMoreElements()) {
			JarEntry entry = (JarEntry) e.nextElement();
			String name = entry.getName();
			content.add(name);
		}
		return content;
	}
	
	/**
	 * List the contents of a jar
	 * 
	 * @param jarPath
	 * @return
	 * @throws IOException
	 */
	public static List<String> getJarContent(String jarPath, String root) throws IOException {
		List<String> content = new ArrayList<String>();
		JarFile jarFile = new JarFile(jarPath);
		Enumeration<JarEntry> e = jarFile.entries();

		while (e.hasMoreElements()) {
			JarEntry entry = (JarEntry) e.nextElement();
			String name = entry.getName();
			if (name.startsWith(root)){
				content.add(name);
			}
		}
		return content;
	}

	/**
	 * Returns a GenericTree informmado from the path containing the entire structure of folders and files on it.
	 * 
	 * @param list
	 * @param root
	 * @return
	 */
	public static GenericTree<String> getTreeFromJarFilesList(List<String> list, String root) {
		// Tree
		// --------
		GenericTree<String> result = new GenericTree<String>();
		// Creates the path to the root
		createNodeForPath(result, root);

		// --------------------------------------
		// For all files in the jar
		for (String path : list) {
			// Checks if the file path is contained in the root desired
			if (root != null && !(path.startsWith(root))) {
				// Iteration continues to not being
				continue;
			}
			createNodeForPath(result, path);
		}

		return result;
	}

	/**
	 * Method responsible for making the assembly tree
	 * 
	 * @param arv
	 * @param caminho
	 * @return
	 */
	private static GenericTreeNode<String> createNodeForPath(GenericTree<String> arv, String caminho) {
		// Root
		// --------
		GenericTreeNode<String> result = null;

		// You will need to create nodes to the path
		String[] caminhoSeparadoPorBarra = caminho.split("/");
		for (String pasta : caminhoSeparadoPorBarra) {
			// Checks if the path is already created in the ROOT tree
			if (arv.getRoot() != null) {
				boolean continuar = false;
				// Checks if the WORK NODE is null
				if (result == null) {
					// .. checks whether the first folder is equal to the root node
					if (arv.getRoot().getData().equals(pasta)) {
						result = arv.getRoot();
					} else {
						result = arv.getRoot();
						continuar = true;
					}
				} else {
					continuar = true;
				}

				if (continuar) {
					continuar = false;
					// Check for the CHILDREN of the WORKING NODES
					if (result.getChildren() != null) {
						boolean achou = false;  
						for (GenericTreeNode<String> filhoNoTrab : result.getChildren()) {
							// There is a folder
							// ============================
							// Remove the slash "/" at the end
							// ---------------------------
							String aux = filhoNoTrab.getData();
							// Remove whitespace at the end
							aux = aux.trim();
							// Get the last character
							String lastChar = aux.substring(aux.length()-1);
							while (lastChar.equals("/")){
								// .. remove this character
								aux = aux.substring(0, aux.length()-1);
								if (aux.length() > 0){
									lastChar = aux.substring(aux.length()-1);
								} else {
									break; // End the loop, the word is blank
								}
							}

							final int ind = aux.lastIndexOf("/");
							if (ind >= 0){
								aux = aux.substring(ind+1, aux.length());
							}
							// --------------
							if (pasta.equals(aux)) {
								result = filhoNoTrab; // sets the child to the WORK NODE
								achou = true;
								break; 
							}
						}
						// Will create the node if you have NOT found
						if (!achou) {
							GenericTreeNode<String> novo = new GenericTreeNode<String>(caminho);
							result.addChild(novo); // adiciona como filho
							result = novo; // troca o NÓ DE TRABALHO pelo novo
						}
					}
				}
			} else {
				// If a return root node does not exist will be created in this
				// first moment
				result = new GenericTreeNode<String>(caminho);// work node
				arv.setRoot(result); // root node
			}
		} // end FOR

		return result;

	}

	/**
	 * Returns the path of the Jar performing this method
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getThisJarFolder() throws UnsupportedEncodingException,JarException {
		return getThisJarFolder(this.getClass());
	}
	
	/**
	 * Returns the path of the Jar performing this method
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getThisJarFolder(@SuppressWarnings("rawtypes") Class clazz) throws UnsupportedEncodingException,
			JarException {
		String result = null;

		try {
			String caminhoCompletoJar = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
			String decodedPath = URLDecoder.decode(caminhoCompletoJar, "UTF-8");
			decodedPath = decodedPath.replace('/', File.separatorChar);
			result = decodedPath;
		} catch (NullPointerException npe) {
			throw new JarException(
					"Certifique-se de estar executando a partir de um JAR");
		}

		return result;
	}

}