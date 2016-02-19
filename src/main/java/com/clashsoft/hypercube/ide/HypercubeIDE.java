package com.clashsoft.hypercube.ide;

import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class HypercubeIDE extends Application
{
	public Parent createContent() throws Exception
	{
		final Font font = Font.font("Monaco", 20);

		// Text
		Text text = new Text("Hello World");
		text.setFont(font);
		text.setTranslateX(10);
		text.setTranslateY(20);

		// Box
		Box testBox = new Box(16, 16, 16);
		final PhongMaterial buttonMaterial = new PhongMaterial(Color.WHITE);
		testBox.setMaterial(buttonMaterial);
		testBox.setDrawMode(DrawMode.FILL);

		Button button = new Button("Click Me");
		button.setFont(font);
		button.setTranslateX(30);
		button.setTranslateY(50);
		button.setOnMouseClicked(event -> buttonMaterial.setDiffuseColor(randomColor()));

		// Create and position camera
		PerspectiveCamera camera = new PerspectiveCamera(true);
		camera.getTransforms()
		      .addAll(new Rotate(-45, Rotate.Y_AXIS), new Rotate(-20, Rotate.X_AXIS), new Translate(0, 0, -40));
		camera.setNearClip(0.1);
		camera.setFarClip(2000.0);
		camera.setFieldOfView(35);
		camera.setRotationAxis(new Point3D(0, 1, 0));

		// Build the Scene Graph
		Group root = new Group();
		root.getChildren().add(testBox);
		root.getChildren().add(camera);

		// Use a SubScene
		SubScene subScene = new SubScene(root, 800, 560);
		subScene.setFill(Color.ALICEBLUE);
		subScene.setOnMouseDragged(e -> camera.setRotate(e.getX()));
		subScene.setCamera(camera);

		Group group = new Group();
		group.getChildren().add(subScene);
		group.getChildren().add(text);
		group.getChildren().add(button);

		return group;
	}

	private static Color randomColor()
	{
		return Color.hsb(Math.random() * 360, 1, 1);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.setResizable(false);

		Scene scene = new Scene(this.createContent());

		primaryStage.setScene(scene);
		primaryStage.setTitle("Hypercube");
		primaryStage.show();
	}

	/**
	 * Java main for when running without JavaFX launcher
	 */
	public static void main(String[] args)
	{
		launch(args);
	}
}
