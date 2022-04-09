module xyz.kryptografia.rsa {
	requires javafx.controls;
	requires javafx.fxml;

	requires org.controlsfx.controls;
	requires com.dlsc.formsfx;
	requires org.kordamp.bootstrapfx.core;

	opens xyz.kryptografia.rsa to javafx.fxml;
	exports xyz.kryptografia.rsa;
	exports xyz.kryptografia.rsa.liczby;
	opens xyz.kryptografia.rsa.liczby to javafx.fxml;
}