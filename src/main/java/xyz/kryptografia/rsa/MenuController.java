package xyz.kryptografia.rsa;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import xyz.kryptografia.rsa.liczby.UFatInt;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;


public class MenuController {

	private final Stage window;
	private String isBased;
	private final Szyfr szyfr;

	@FXML
	private TextField pubKey;
	@FXML
	private Button pubKeyLoad;
	@FXML
	private Button pubKeySave;

	@FXML
	private TextField privKey;
	@FXML
	private Button privKeyLoad;
	@FXML
	private Button privKeySave;

	@FXML
	private TextArea plainText;
	@FXML
	private Button plainTextLoad;
	@FXML
	private Button plainTextSave;
	@FXML
	private Label plainTextLabel;
	@FXML
	private Button switchPlainText;

	@FXML
	private TextArea cipherText;
	@FXML
	private Button cipherTextLoad;
	@FXML
	private Button cipherTextSave;

	@FXML
	private ComboBox<Integer> numBits;
	@FXML
	private Button generateKeys;

	@FXML
	private Button encryptButton;
	@FXML
	private Button decryptButton;

	public MenuController() {

		this.window = new Stage();
		this.szyfr = new AlgorytmRSA();

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Menu.fxml"));
			fxmlLoader.setController(this);

			Scene scene = new Scene(fxmlLoader.load());
			scene.getStylesheets().add("style.css");
			this.window.setScene(scene);

			this.privKeyLoad.setOnAction((e) -> this.privKey.setText(this.loadKey()));
			this.privKeySave.setOnAction((e) -> this.saveKey(this.privKey.getText()));

			this.pubKeyLoad.setOnAction((e) -> this.pubKey.setText(this.loadKey()));
			this.pubKeySave.setOnAction((e) -> this.saveKey(this.pubKey.getText()));

			this.generateKeys.setOnAction((e) -> this.genKey());
			this.numBits.getItems().addAll(64, 128, 256, 512, 1024);
			this.numBits.setValue(64);

			this.plainTextLoad.setOnAction((e) -> {
				this.plainText.setText(this.loadPlainText());
				this.plainTextLabel.setText(this.isBased);
			});
			this.plainTextSave.setOnAction((e) -> this.savePlainText());
			this.switchPlainText.setOnAction((e) -> {
				if (this.plainTextLabel.getText().contains("base64"))
					this.plainTextLabel.setText("Plain text");
				else
					this.plainTextLabel.setText("Plain text (base64)");
			});
			this.isBased = "Plain text";

			this.cipherTextLoad.setOnAction((e) -> this.cipherText.setText(this.loadCipherText()));
			this.cipherTextSave.setOnAction((e) -> this.saveCipherText());

			this.encryptButton.setOnAction((e) -> this.encrypt());
			this.decryptButton.setOnAction((e) -> this.decrypt());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void decrypt() {
		System.out.println("\nDECRYPT");
		if (Objects.equals(this.cipherText.getText(), "") || Objects.equals(this.pubKey.getText(), ""))
			return;

		UFatInt[] privKeyData = Converter.splitToNums(
				Base64.getDecoder().decode(this.privKey.getText()),
				this.numBits.getValue());
		UFatInt[] cipherTextData = Converter.splitToNums(
				Base64.getDecoder().decode(this.cipherText.getText()),
				this.numBits.getValue());

		UFatInt[] nums = this.szyfr.decrypt(cipherTextData, privKeyData);

		System.out.println("Odczytane: " + Arrays.toString(cipherTextData));
		System.out.println("Priv: " + Arrays.toString(privKeyData) + ", obliczone: " + Arrays.toString(nums));

		byte[] data = Converter.compactNums(nums, this.numBits.getValue());
		String tmp = new String(data);

		if (tmp.matches("[^\\x00\\x08\\x0B\\x0C\\x0E-\\x1F]+")) {
			this.plainTextLabel.setText("Plain text");
			this.plainText.setText(tmp);
		} else {
			this.plainTextLabel.setText("Plain text (base64)");
			this.plainText.setText(Base64.getEncoder().encodeToString(data));
		}
	}

	private void encrypt() {
		System.out.println("\nENCRYPT");
		if (Objects.equals(this.plainText.getText(), "") || Objects.equals(this.pubKey.getText(), ""))
			return;

		UFatInt[] pubKeyData = Converter.splitToNums(
				Base64.getDecoder().decode(this.pubKey.getText()),
				this.numBits.getValue());

		UFatInt[] plainTextData = Converter.splitToNums(
				this.plainText.getText().getBytes(),
				this.numBits.getValue());

		UFatInt[] nums = this.szyfr.encrypt(plainTextData, pubKeyData);

		System.out.println("Odczytane: " + Arrays.toString(plainTextData));
		System.out.println("Pub: " + Arrays.toString(pubKeyData) + ", obliczone: " + Arrays.toString(nums));

		this.cipherText.setText(Base64.getEncoder().encodeToString(Converter.compactNums(nums,
				this.numBits.getValue())));
	}

	public void showStage() {
		this.window.show();
	}

	public void genKey() {

		int len = this.numBits.getValue();
		System.out.println("Ilość bitów: " + len);

		long start = System.currentTimeMillis();
		UFatInt[][] tmp = this.szyfr.genKey(len);
		System.out.println("Generowanie trwało: " + (System.currentTimeMillis() - start));

		this.privKey.setText(Base64.getEncoder().encodeToString(Converter.compactNums(tmp[0], len)));
		this.pubKey.setText(Base64.getEncoder().encodeToString(Converter.compactNums(tmp[1], len)));
	}

	public void saveKey(String data) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save file");
		File file = fileChooser.showSaveDialog(this.window);

		try (FileOutputStream outStream = new FileOutputStream(file)) {

			outStream.write(data.getBytes());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String loadKey() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open file");
		File file = fileChooser.showOpenDialog(this.window);

		try (FileInputStream inStream = new FileInputStream(file)) {
			byte[] data = inStream.readAllBytes();
			return new String(data);

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String loadPlainText() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open file");
		File file = fileChooser.showOpenDialog(this.window);

		try (FileInputStream inStream = new FileInputStream(file)) {
			byte[] data = inStream.readAllBytes();
			byte[] tmp;

			if (new String(data).matches("[^\\x00\\x08\\x0B\\x0C\\x0E-\\x1F]+")) {
				this.isBased = "Plain text";
				tmp = data;
			} else {
				this.isBased = "Plain text (base64)";
				tmp = Base64.getEncoder().encode(data);
			}
			return new String(tmp);

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String loadCipherText() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open file");
		File file = fileChooser.showOpenDialog(this.window);

		try (FileInputStream inStream = new FileInputStream(file)) {
			byte[] data = inStream.readAllBytes();
			return Base64.getEncoder().encodeToString(data);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void saveCipherText() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save file");
		File file = fileChooser.showSaveDialog(this.window);
		byte[] data = Base64.getDecoder().decode(this.cipherText.getText());

		try (FileOutputStream outStream = new FileOutputStream(file)) {

			outStream.write(data);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void savePlainText() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save file");
		File file = fileChooser.showSaveDialog(this.window);

		byte[] data;

		try (FileOutputStream outStream = new FileOutputStream(file)) {
			if (this.isBased.contains("base64"))
				data = Base64.getDecoder().decode(this.plainText.getText());
			else
				data = this.plainText.getText().getBytes();
			outStream.write(data);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}