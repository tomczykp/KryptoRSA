package xyz.kryptografia.rsa;

import javafx.application.Application;
import javafx.stage.Stage;
import xyz.kryptografia.rsa.liczby.UFatInt;

import java.util.Arrays;
import java.util.Collections;

public class MenuApp extends Application {

	@Override
	public void start (Stage stage) {

		MenuController menu = new MenuController();
		menu.showStage();
	}

	public static void main (String[] args) {
//		System.out.println("Statystyki generowania kluczy: ");
//		int n = 20;
//		int[] keys = {64, 128, 256, 512, 1024};
//		Long[][] stat = new Long[keys.length][n];
//		AlgorytmRSA rsa = new AlgorytmRSA();
//
//		for (int j = 0; j < keys.length; j++) {
//
//			for (int i = 0; i < n; i++) {
//				long start = System.currentTimeMillis();
//				UFatInt[][] tmp = rsa.genKey(keys[j]);
//				stat[j][i] = (System.currentTimeMillis() - start);
//			}
//		}
//
//		for (int i = 0; i < keys.length; i++) {
//			long min = Collections.min(Arrays.asList(stat[i]));
//			long max = Collections.max(Arrays.asList(stat[i]));
//			long srd = 0;
//			for (long l : stat[i])
//				srd += l;
//			srd /= stat[i].length;
//
//			System.out.println("Dla klucza: " + keys[i]);
//			System.out.println("min = " + min);
//			System.out.println("avg = " + srd);
//			System.out.println("max = " + max);
//
//		}

		launch();
	}

}