package br.com.browseframework.util.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Node GenericTree. Stores information, parent and child nodes list.
 * @param <T> Type of data to be stored in the Tree
 */
public class GenericTreeNode<T> {

    private T data;
    private List<GenericTreeNode<T>> children;
    private GenericTreeNode<T> parent;

    public GenericTreeNode() {
        super();
        children = new ArrayList<GenericTreeNode<T>>();
    }

    public GenericTreeNode(T data) {
        this();
        setData(data);
    }

    /**
     * Returns the parent node
     * @return
     */
    public GenericTreeNode<T> getParent() {
        return this.parent;
    }

    /**
     * Returns list of child nodes
     * @return
     */
    public List<GenericTreeNode<T>> getChildren() {
        return this.children;
    }

    /**
     * Number of children for this level
     * @return
     */
    public int getNumberOfChildren() {
        return getChildren().size();
    }

    /**
     * Returns true if level has children
     * @return
     */
    public boolean hasChildren() {
        return (getNumberOfChildren() > 0);
    }

    /**
     * Assigns list of child nodes
     * @param children
     */
    public void setChildren(List<GenericTreeNode<T>> children) {
        for(GenericTreeNode<T> child : children) {
           child.parent = this;
        }

        this.children = children;
    }

    /**
     * Adds a child node of the list of child nodes
     * @param child
     */
    public void addChild(GenericTreeNode<T> child) {
        child.parent = this;
        children.add(child);
    }
    
    /**
     * Checks if the object is contained in the children's current level
     * @param obj
     * @return
     */
    public boolean contains(T obj)  {
    	boolean retorno = false;
    	for (GenericTreeNode<T> localObj: getChildren()){
    		if (localObj.getData().equals(obj)){
    			retorno = true;
    			break;
    		}
    	}
    	return retorno;
    }
    /**
     * Adds a child node of the list of child nodes to the specified position
     * @param index
     * @param child
     * @throws IndexOutOfBoundsException
     */
    public void addChildAt(int index, GenericTreeNode<T> child) throws IndexOutOfBoundsException {
        child.parent = this;
        children.add(index, child);
    }

    /**
     * Clears the list of children
     */
    public void removeChildren() {
        this.children = new ArrayList<GenericTreeNode<T>>();
    }

    /**
     * Removes the child node list of child nodes
     * @param index
     * @throws IndexOutOfBoundsException
     */
    public void removeChildAt(int index) throws IndexOutOfBoundsException {
        children.remove(index);
    }

    /**
     * Returns child node stored at the specified position
     * @param index
     * @return
     * @throws IndexOutOfBoundsException
     */
    public GenericTreeNode<T> getChildAt(int index) throws IndexOutOfBoundsException {
        return children.get(index);
    }

    /**
     * Returns the information stored in the node
     * @return
     */
    public T getData() {
        return this.data;
    }

    /**
     * Assigns the information to node
     * @param data
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Returns a String to the information stored
     */
    public String toString() {
        return getData().toString();
    }

    @Override
    /**
     * Compares node using "equals"
     */
    public boolean equals(Object obj) {
        if (this == obj) {
           return true;
        }
        if (obj == null) {
           return false;
        }
        if (getClass() != obj.getClass()) {
           return false;
        }
        GenericTreeNode<?> other = (GenericTreeNode<?>) obj;
        if (data == null) {
           if (other.data != null) {
              return false;
           }
        } else if (!data.equals(other.data)) {
           return false;
        }
        return true;
    }

    @Override
    /**
     * Hash to node
     */
    public int hashCode() {
       final int prime = 31;
       int result = 1;
       result = prime * result + ((data == null) ? 0 : data.hashCode());
       return result;
    }

    /**
     * Hash to node
     * @return
     */
    public String toStringVerbose() {
        String stringRepresentation = getData().toString() + ":[";

        for (GenericTreeNode<T> node : getChildren()) {
            stringRepresentation += node.getData().toString() + ", ";
        }

        Pattern pattern = Pattern.compile(", $", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(stringRepresentation);

        stringRepresentation = matcher.replaceFirst("");
        stringRepresentation += "]";

        return stringRepresentation;
    }
    
    /**
     * Returns whether a sheet is based on the children.
     * @return
     */
    public boolean isLeaf(){
    	boolean retorno = false;
    	if ( getChildren() == null || ( getChildren() != null && getChildren().isEmpty()) ){
    		retorno = true;
    	}
    	return retorno;
    }
    
    /**
     * Indece Returns the node to the list of objects stored in the level of the parent node.
     * @param node 
     * @return returns -1 in case of not finding the node in the parent informed
     */
    public int getIndexOfChild(GenericTreeNode<T> parent, GenericTreeNode<T> node){
    	int index = 0;
    	boolean found = false;
    	for (GenericTreeNode<T> itNode: parent.getChildren()){
    		if (itNode.equals(node)){
    			found = true;
    			break;
    		}
    		index++;
    	}
    	// If not found returns -1
    	if (!found){
    		index = -1;
    	}
    	return index;
    }
}
