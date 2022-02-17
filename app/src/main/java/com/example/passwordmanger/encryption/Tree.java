package com.example.passwordmanger.encryption;

import java.util.Arrays;

public class Tree {

	private Node arr[];
	private String binaryText;
	Node root;

	Tree(String binaryText) {
		arr = new Node[8];
		this.binaryText = binaryText;
		setTree();
	}

	private void setTree() {
		Node node = new Node();
		Arrays.fill(arr, node);
		root = arr[0];
		fillArray();
	}

	private void fillArray() {
		int currentCounter = 7;
		for (int counter = binaryText.length() - 1; counter >= 0; counter--) {
			// get current bit
			char currentChar = binaryText.charAt(counter);
			// create new node
			Node newNode = new Node();
			// set node value
			newNode.value = currentChar;
			// replace node
			arr[currentCounter] = newNode;
			// decrement
			currentCounter--;
		}

	}

	// Function to print tree nodes in preOrder fashion
	public String preOrder(Node root, String text, int index) {
		if (index > 7) {
			return text;
		}
		text += arr[index].value;
		text = preOrder(root, text, 2 * index + 1);
		text = preOrder(root, text, 2 * index + 2);
		return text;
	}

	// Function to print tree nodes in InOrder fashion
	public String inOrder(Node root, String text, int index) {
		if (index > 7) {
			return text;
		}
		text = inOrder(root, text, 2 * index + 1);
		text += arr[index].value;
		text = inOrder(root, text, 2 * index + 2);
		return text;
	}

	// Function to print tree nodes in postOrder fashion
	public String postOrder(Node root, String text, int index) {
		if (index > 7) {
			return text;
		}
		text = postOrder(root, text, 2 * index + 1);
		text = postOrder(root, text, 2 * index + 2);
		text += arr[index].value;
		return text;
	}

}
