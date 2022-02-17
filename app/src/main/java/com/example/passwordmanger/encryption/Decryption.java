package com.example.passwordmanger.encryption;



public class Decryption {
	private String plaintext = "";

	public Decryption(String cipherText, String key) {
		int counter = cipherText.length() / 4;
		for (int x = 0; x < counter; x++) {
			// substring
			String subString = cipherText.substring(4 * x, (x + 1) * 4);
			this.plaintext += (char)getDecimal(stepOne(subString, key));
		}
	}

	private String stepOne(String subString, String key) {
		String binaryText = hexToBin(subString);
		String byteOne = binaryText.substring(0, 8);
		String byteTwo = binaryText.substring(8, 16);
		String XORValue = XORFunction(byteOne, byteTwo);
		return stepTwo(XORValue, key);
	}

	private String stepTwo(String XORValue, String key) {
		String stepTwo = "";
		switch (key) {
		case "preorder": {
			for (int x = 0; x < 8 / 2; x++) {
				if (stepTwo.length() != 0) {
					Tree tree = new Tree(stepTwo);
					stepTwo = tree.preOrder(tree.root, "", 0);
				} else {
					Tree tree = new Tree(XORValue);
					stepTwo = tree.preOrder(tree.root, "", 0);
				}
			}
			break;
		}
		case "inorder": {
			for (int x = 0; x < 8 -2; x++) {
				if (stepTwo.length() != 0) {
					Tree tree = new Tree(stepTwo);
					stepTwo = tree.inOrder(tree.root, "", 0);
				} else {
					Tree tree = new Tree(XORValue);
					stepTwo = tree.inOrder(tree.root, "", 0);
				}
			}
			break;
		}case "postorder": {
			for (int x = 0; x < 8 -1; x++) {
				if (stepTwo.length() != 0) {
					Tree tree = new Tree(stepTwo);
					stepTwo = tree.postOrder(tree.root, "", 0);
				} else {
					Tree tree = new Tree(XORValue);
					stepTwo = tree.postOrder(tree.root, "", 0);
				}
			}
			break;
		}
		}
		return stepTwo;
	}

	public String getPlaintext() {
		return plaintext;
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

	private String hexToBin(String hex) {
		hex = hex.replaceAll("0", "0000");
		hex = hex.replaceAll("1", "0001");
		hex = hex.replaceAll("2", "0010");
		hex = hex.replaceAll("3", "0011");
		hex = hex.replaceAll("4", "0100");
		hex = hex.replaceAll("5", "0101");
		hex = hex.replaceAll("6", "0110");
		hex = hex.replaceAll("7", "0111");
		hex = hex.replaceAll("8", "1000");
		hex = hex.replaceAll("9", "1001");
		hex = hex.replaceAll("A", "1010");
		hex = hex.replaceAll("B", "1011");
		hex = hex.replaceAll("C", "1100");
		hex = hex.replaceAll("D", "1101");
		hex = hex.replaceAll("E", "1110");
		hex = hex.replaceAll("F", "1111");
		return hex;
	}

	private int getDecimal(String binary) {
		int decimal = 0;
		for (int x = binary.length() - 1; x >= 0; x--) {
			int num = Integer.parseInt(String.valueOf(binary.charAt(x)));
			if (num == 1) {
				decimal += Math.pow(num * 2, (binary.length() - 1 - x));

			}
		}
		return decimal;
	}
}
