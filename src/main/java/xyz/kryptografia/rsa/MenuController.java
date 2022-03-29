package xyz.kryptografia.rsa;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.File;
import java.util.Base64;


public class MenuController {

	private final Stage window;
	private boolean isBased;
	private Szyfr szyfr;

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

			this.privKeyLoad.setOnAction((e) -> this.privKey.setText(new String(this.loadFile(false, true))));
			this.privKeySave.setOnAction((e) -> this.saveFile(this.privKey.getText()));

			this.pubKeyLoad.setOnAction((e) -> this.pubKey.setText(new String(this.loadFile(false, true))));
			this.pubKeySave.setOnAction((e) -> this.saveFile(this.pubKey.getText()));

			this.generateKeys.setOnAction((e) -> this.genKey());
			this.numBits.getItems().addAll(64, 128, 256, 512, 1024);
			this.numBits.setValue(64);

			this.plainTextLoad.setOnAction((e) -> {
						this.plainText.setText(new String(this.loadFile(true, false)));
						if (this.isBased)
							this.plainTextLabel.setText("Plain text (base64)");
						else
							this.plainTextLabel.setText("Plain text");
					}
			);

			this.plainTextSave.setOnAction((e) -> this.saveFile(this.plainText.getText()));

			this.cipherTextLoad.setOnAction((e) -> this.cipherText.setText(new String(this.loadFile(false, true))));
			this.cipherTextSave.setOnAction((e) -> this.saveFile(this.cipherText.getText()));

			this.encryptButton.setOnAction((e) -> this.encrypt());
			this.decryptButton.setOnAction((e) -> this.decrypt());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void decrypt() {
		byte[] cipherTextData = Base64.getDecoder().decode(this.cipherText.getText());
		Num[] privKeyData = Converter.decode(this.privKey.getText());

		System.out.println("Priv: " + privKeyData[0] + ", " + privKeyData[1]);

		byte[] data = this.szyfr.decrypt(cipherTextData, privKeyData);

		// text <-> encrypt
		// bin <-> to_base64 <-> from_base64 <-> encrypt
		String tmp = new String(data);
		if (tmp.matches("[^\\x00\\x08\\x0B\\x0C\\x0E-\\x1F]+")) {
			// był to text
			this.plainTextLabel.setText("Plain text");
			this.plainText.setText(tmp);
		} else {
			this.plainTextLabel.setText("Plain text (base64)");
			this.plainText.setText(Base64.getEncoder().encodeToString(data));
		}
	}

	private void encrypt() {
		byte[] plainTextData;
		if (this.isBased)
			plainTextData = Base64.getDecoder().decode(this.plainText.getText());
		else
			plainTextData = this.plainText.getText().getBytes();

		Num[] pubKeyData = Converter.decode(this.pubKey.getText());
		System.out.println("Pub: " + pubKeyData[0] + ", " + pubKeyData[1]);

		byte[] data = this.szyfr.encrypt(plainTextData, pubKeyData);

		// text <-> encrypt
		// bin <-> to_base64 <-> from_base64 <-> encrypt

		this.cipherText.setText(Base64.getEncoder().encodeToString(data));
	}

	public void showStage() {
		this.window.show();
	}

	public void genKey() {

		int len = this.numBits.getValue();
		System.out.println("Ilość bitów: " + len);

		Num[][] tmp = this.szyfr.genKey(len);

		this.privKey.setText(Converter.encode(tmp[0]));
		this.pubKey.setText(Converter.encode(tmp[1]));
	}

	public void saveFile(String data) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save file");
		File file = fileChooser.showSaveDialog(this.window);

		try (FileOutputStream outStream = new FileOutputStream(file)) {

			byte[] tmp;
			try {
				tmp = Base64.getDecoder().decode(data);
			} catch (IllegalArgumentException e) {
				tmp = data.getBytes();
			}
			outStream.write(tmp);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] loadFile(boolean plainText, boolean doLoadBin) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open file");
		File file = fileChooser.showOpenDialog(this.window);

		try (FileInputStream inStream = new FileInputStream(file)) {
			byte[] data = inStream.readAllBytes();
			byte[] tmp;
			boolean wasBased;

			if (doLoadBin) {
				wasBased = true;
				tmp = Base64.getEncoder().encode(data);
			} else if (new String(data).matches("[^\\x00\\x08\\x0B\\x0C\\x0E-\\x1F]+")) {
				wasBased = false;
				tmp = data;
			} else {
				wasBased = true;
				tmp = Base64.getEncoder().encode(data);
			}

			if (plainText)
				this.isBased = wasBased;

			return tmp;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

}