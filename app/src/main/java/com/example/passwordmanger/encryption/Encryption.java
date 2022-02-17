package com.example.passwordmanger.encryption;



public class Encryption {
	private String cipherText = "";

	public Encryption(String plainText, String key) {
		encrypte(plainText, key);
	}

	private void encrypte(String plainText, String key) {
		for (int counter = 0; counter < plainText.length(); counter++) {
			char currentChar = plainText.charAt(counter);
			String stepone = stepOne(currentChar, key);

			this.cipherText += stepone;
		}

	}

	private String stepOne(char currentChar, String key) {
		String binaryText = Integer.toBinaryString((int) currentChar);
		Tree tree = new Tree(binaryText);
		String stepone = "";
		switch (key) {
		case "preorder": {
			stepone = tree.preOrder(tree.root, "", 0);
			
			break;
		}case "inorder": {
			stepone = tree.inOrder(tree.root, "", 0);
			
			break;
		}case "postorder": {
			stepone = tree.postOrder(tree.root, "", 0);

			break;
		}


		}

		return stepTwo(stepone);
	}

	private String stepTwo(String stepOne) {
		int sum = 0;
		for (int counter = 0; counter < stepOne.length(); counter++) {
			if (stepOne.charAt(counter) == '1') {
				sum += 1;
			}
		}
		int result = 8 - sum;
		String binaryText = optimizeBinary(Integer.toBinaryString(String.valueOf(result).charAt(0)));
		String XORValue = XORFunction(stepOne, binaryText);

		String stepTwo = "";
		String temp = binaryText + XORValue;
		for (int x = 0; x < temp.length() / 4; x++) {
			stepTwo += BinToHex(temp.substring(4 * x, 4 * (x + 1)));
		}

		return stepTwo;
	}

	public String getCipherText() {
		return cipherText;
	}

	// helping method
	private String XORFunction(String stringOne, String stringTwo) {

		String XORValue = "";
		for (int x = 0; x < stringOne.length(); x++) {
			if (stringOne.charAt(x) == stringTwo.charAt(x)) {
				XORValue += "0";
			} else {
				XORValue += "1";
			}
		}
		return XORValue;
	}

	private String BinToHex(String bin) {
		bin = bin.replaceAll("0000", "0");
		bin = bin.replaceAll("0001", "1");
		bin = bin.replaceAll("0010", "2");
		bin = bin.replaceAll("0011", "3");
		bin = bin.replaceAll("0100", "4");
		bin = bin.replaceAll("0101", "5");
		bin = bin.replaceAll("0110", "6");
		bin = bin.replaceAll("0111", "7");
		bin = bin.replaceAll("1000", "8");
		bin = bin.replaceAll("1001", "9");
		bin = bin.replaceAll("1010", "A");
		bin = bin.replaceAll("1011", "B");
		bin = bin.replaceAll("1100", "C");
		bin = bin.replaceAll("1101", "D");
		bin = bin.replaceAll("1110", "E");
		bin = bin.replaceAll("1111", "F");
		return bin;
	}

	private String optimizeBinary(String binary) {
		if (binary.length() < 8) {
			while (binary.length() != 8) {
				binary = "0" + binary;
			}
		}
		return binary;
	}

}
