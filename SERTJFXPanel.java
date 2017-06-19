package fr.sert.util.javafx;

import java.awt.Window;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * JFXPanel pour une intégration de JavaFX dans SERT
 *
 */
public class SERTJFXPanel extends JFXPanel implements Runnable {

	//.....................
	
	public SERTJFXPanel(String fxmlFile, final Object controller) {
		super();
		//permettre au thread javafx de ne pas se terminer même après la fermeture du dernier panel
		Platform.setImplicitExit(false);
		// chargement de la vue JavaFX depuis le XML
		this.fxmlFile = fxmlFile;
		try (InputStream stream = getClass().getResourceAsStream(fxmlFile)) {
			stream.close();
		} catch (IOException e) {
			throw new IllegalArgumentException("Le fichier " + fxmlFile + " n'est pas dans le classpath", e);
		}
		this.fxmlLoader = new FXMLLoader();
		this.fxmlLoader.setController(controller);
		// initialisation dans le thread JavaFX
		Platform.runLater(this);
	}

	
	@Override
	public void run() {
		// initialisation
		try {
		    fxmlLoader.setLocation(getClass().getResource(fxmlFile));
			Parent rootNode = (Parent) fxmlLoader.load();
			scene = new Scene(rootNode, 400, 200);
			scene.getStylesheets().add("/javafx/css/sert.css");
			setScene(scene);
			init = true;
		} catch (IOException e) {
			LOGGER.error("Le fichier " + fxmlFile + " n'est pas dans le classpath", e);
		}
	}

	//.................................

}
