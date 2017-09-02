package br.com.browseframework.util.tree;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Generic n-ary tree
 * @param <T> Type of data to be stored in the Tree
 */
public class GenericTree<T> {
	@SuppressWarnings("unused")
	private Class<T> type;
	// Root
	private GenericTreeNode<T> root;

	public GenericTree() {
		
	}

	/**
	 * Returns the root node
	 * @return
	 */
	public GenericTreeNode<T> getRoot() {
		return this.root;
	}

	/**
	 * Assign root node
	 * @param root
	 */
	public void setRoot(GenericTreeNode<T> root) {
		this.root = root;
	}

	/**
	 * Returns the number of nodes stored in the tree
	 * @return
	 */
	public int getNumberOfNodes() {
		int numberOfNodes = 0;

		if (root != null) {
			numberOfNodes = auxiliaryGetNumberOfNodes(root) + 1; // +1 To consider the root node
		}

		return numberOfNodes;
	}

	/**
	 * Auxiliary recursive method for determining the number of nodes in the tree
	 * @param node
	 * @return
	 */
	private int auxiliaryGetNumberOfNodes(GenericTreeNode<T> node) {
		int numberOfNodes = node.getNumberOfChildren();

		for (GenericTreeNode<T> child : node.getChildren()) {
			numberOfNodes += auxiliaryGetNumberOfNodes(child);
		}

		return numberOfNodes;
	}

	/**
	 * Search tree and returns the object if found or not.
	 * @param dataToFind
	 * @return
	 */
	public boolean exists(T dataToFind) {
		return (find(dataToFind) != null);
	}

	/**
	 * Returns the element itself is found.
	 * @param dataToFind
	 * @return
	 */
	public GenericTreeNode<T> find(T dataToFind) {
		GenericTreeNode<T> returnNode = null;

		if (root != null) {
			returnNode = auxiliaryFind(root, dataToFind);
		}

		return returnNode;
	}

	/**
	 * Helper method for recursive search for the node.
	 * @param currentNode
	 * @param dataToFind
	 * @return
	 */
	private GenericTreeNode<T> auxiliaryFind(GenericTreeNode<T> currentNode, T dataToFind) {
		GenericTreeNode<T> returnNode = null;
		int i = 0;

		if (currentNode.getData().equals(dataToFind)) {
			returnNode = currentNode;
		}

		else if (currentNode.hasChildren()) {
			i = 0;
			while (returnNode == null && i < currentNode.getNumberOfChildren()) {
				returnNode = auxiliaryFind(currentNode.getChildAt(i),
						dataToFind);
				i++;
			}
		}

		return returnNode;
	}

	/**
	 * Returns true if there is one root element.
	 * @return
	 */
	public boolean isEmpty() {
		return (root == null);
	}

	/**
	 * Build in traversal order
	 * @param traversalOrder
	 * @return
	 */
	public List<GenericTreeNode<T>> build(GenericTreeTraversalOrderEnum traversalOrder) {
		List<GenericTreeNode<T>> returnList = null;

		if (root != null) {
			returnList = build(root, traversalOrder);
		}

		return returnList;
	}

	/**
	 * Build in traversal order
	 * @param node
	 * @param traversalOrder
	 * @return
	 */
	public List<GenericTreeNode<T>> build(GenericTreeNode<T> node,
			GenericTreeTraversalOrderEnum traversalOrder) {
		List<GenericTreeNode<T>> traversalResult = new ArrayList<GenericTreeNode<T>>();

		if (traversalOrder == GenericTreeTraversalOrderEnum.PRE_ORDER) {
			buildPreOrder(node, traversalResult);
		}

		else if (traversalOrder == GenericTreeTraversalOrderEnum.POST_ORDER) {
			buildPostOrder(node, traversalResult);
		}

		return traversalResult;
	}

	/**
	 * Build in traversal order
	 * @param node
	 * @param traversalResult
	 */
	private void buildPreOrder(GenericTreeNode<T> node,
			List<GenericTreeNode<T>> traversalResult) {
		traversalResult.add(node);

		for (GenericTreeNode<T> child : node.getChildren()) {
			buildPreOrder(child, traversalResult);
		}
	}

	/**
	 * Build in traversal order
	 * @param node
	 * @param traversalResult
	 */
	private void buildPostOrder(GenericTreeNode<T> node,
			List<GenericTreeNode<T>> traversalResult) {
		
		for (GenericTreeNode<T> child : node.getChildren()) {
			buildPostOrder(child, traversalResult);
		}

		traversalResult.add(node);
	}

	/**
	 * Build with depth
	 * @param traversalOrder
	 * @return
	 */
	public Map<GenericTreeNode<T>, Integer> buildWithDepth(GenericTreeTraversalOrderEnum traversalOrder) {
		Map<GenericTreeNode<T>, Integer> returnMap = null;

		if (root != null) {
			returnMap = buildWithDepth(root, traversalOrder);
		}

		return returnMap;
	}

	/**
	 * Build with depth
	 * @param node
	 * @param traversalOrder
	 * @return
	 */
	public Map<GenericTreeNode<T>, Integer> buildWithDepth(
			GenericTreeNode<T> node,
			GenericTreeTraversalOrderEnum traversalOrder) {
		Map<GenericTreeNode<T>, Integer> traversalResult = new LinkedHashMap<GenericTreeNode<T>, Integer>();

		if (traversalOrder == GenericTreeTraversalOrderEnum.PRE_ORDER) {
			buildPreOrderWithDepth(node, traversalResult, 0);
		}

		else if (traversalOrder == GenericTreeTraversalOrderEnum.POST_ORDER) {
			buildPostOrderWithDepth(node, traversalResult, 0);
		}

		return traversalResult;
	}

	/**
	 * Build with depth
	 * @param node
	 * @param traversalResult
	 * @param depth
	 */
	private void buildPreOrderWithDepth(GenericTreeNode<T> node,
			Map<GenericTreeNode<T>, Integer> traversalResult, int depth) {
		traversalResult.put(node, depth);

		for (GenericTreeNode<T> child : node.getChildren()) {
			buildPreOrderWithDepth(child, traversalResult, depth + 1);
		}
	}

	/**
	 * Build with depth
	 * @param node
	 * @param traversalResult
	 * @param depth
	 */
	private void buildPostOrderWithDepth(GenericTreeNode<T> node,
			Map<GenericTreeNode<T>, Integer> traversalResult, int depth) {
		for (GenericTreeNode<T> child : node.getChildren()) {
			buildPostOrderWithDepth(child, traversalResult, depth + 1);
		}

		traversalResult.put(node, depth);
	}

	/**
	 * Returns string with the nodes of the tree
	 */
	public String toString() {
		String stringRepresentation = "";

		if (root != null) {
			stringRepresentation = build(
					GenericTreeTraversalOrderEnum.PRE_ORDER).toString();

		}

		return stringRepresentation;
	}

	/**
	 * Returns string concatenating the depth of each element
	 * @return
	 */
	public String toStringWithDepth() {
		String stringRepresentation = "";

		if (root != null) {
			stringRepresentation = buildWithDepth(
					GenericTreeTraversalOrderEnum.PRE_ORDER).toString();
		}

		return stringRepresentation;
	}
	
}