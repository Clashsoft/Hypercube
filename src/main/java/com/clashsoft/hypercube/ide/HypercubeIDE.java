package com.clashsoft.hypercube.ide;

import com.clashsoft.hypercube.ide.grid.Grid;
import com.clashsoft.hypercube.ide.grid.GridElement;
import com.clashsoft.hypercube.instruction.Instruction;
import com.clashsoft.hypercube.instruction.NullaryInstructions;
import com.clashsoft.hypercube.instruction.PushInstruction;
import com.clashsoft.hypercube.state.Direction;
import com.clashsoft.hypercube.state.Position;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.util.Optional;

public class HypercubeIDE extends Application
{
	private Position selectedPosition = new Position(0, 0, 0, 0);

	private Grid              grid;
	private PerspectiveCamera camera;
	private Box               selectedBox;

	public Parent createContent() throws Exception
	{
		// Camera

		this.camera = new PerspectiveCamera(true);
		camera.getTransforms()
		      .addAll(new Rotate(-45, Rotate.Y_AXIS), new Rotate(-20, Rotate.X_AXIS), new Translate(0, 0, -42));
		camera.setNearClip(0.1);
		camera.setFarClip(2000.0);
		camera.setFieldOfView(40);
		camera.setRotationAxis(new Point3D(0, 1, 0));

		this.selectedBox = new Box(1, 1, 1);
		this.selectedBox.setMaterial(GridElement.SELECTED_MATERIAL);
		this.selectedBox.setDrawMode(DrawMode.LINE);

		grid = new Grid(this);

		grid.getElement(selectedPosition);

		// Build the Scene Graph
		final Group root = new Group();
		root.getChildren().addAll(grid.mainGroup, selectedBox, camera);
		root.setCursor(Cursor.OPEN_HAND);

		// Use a SubScene
		final SubScene subScene = new SubScene(root, 1024, 576, true, SceneAntialiasing.BALANCED);
		subScene.setFill(Color.LIGHTBLUE);
		subScene.setCamera(camera);
		subScene.setBlendMode(BlendMode.MULTIPLY);
		subScene.setDepthTest(DepthTest.ENABLE);
		subScene.setOnMouseDragged(e -> {
			if (e.getButton() == MouseButton.SECONDARY)
			{
				camera.setRotate(e.getX());
			}
		});

		final Group group = new Group();
		group.getChildren().addAll(subScene);

		return group;
	}

	private void keyTyped(KeyEvent event)
	{
		switch (event.getCode())
		{
		case UP:
			this.selectPosition(this.selectedPosition.offset(Direction.FORWARD));
			return;
		case DOWN:
			this.selectPosition(this.selectedPosition.offset(Direction.BACKWARD));
			return;
		case LEFT:
			this.selectPosition(this.selectedPosition.offset(Direction.LEFT));
			return;
		case RIGHT:
			this.selectPosition(this.selectedPosition.offset(Direction.RIGHT));
			return;
		case PAGE_UP:
			this.selectPosition(this.selectedPosition.offset(Direction.DOWN));
			return;
		case PAGE_DOWN:
			this.selectPosition(this.selectedPosition.offset(Direction.UP));
			return;
		case DELETE:
		case BACK_SPACE:
			this.setInstruction(null);
			return;
		case N:
			final String input = this.inputText("Enter a Number");
			this.setInstruction(new PushInstruction(Double.valueOf(input)));
			return;

		}

		switch (event.getText())
		{
		case "+":
			this.setInstruction(NullaryInstructions.ADD);
			return;
		case "-":
			this.setInstruction(NullaryInstructions.SUBTRACT);
			return;
		case "*":
			this.setInstruction(NullaryInstructions.MULTIPLY);
			return;
		case "/":
			this.setInstruction(NullaryInstructions.DIVIDE);
			return;
		}
		return;
	}

	private String inputText(String s)
	{
		final TextInputDialog textInputDialog = new TextInputDialog();
		textInputDialog.setHeaderText(s);

		Optional<String> input = textInputDialog.showAndWait();
		return input.orElse(null);
	}

	public void selectPosition(Position position)
	{
		this.selectedPosition = position;
		this.grid.getElement(position);

		this.camera.setTranslateX(position.x);
		this.camera.setTranslateY(position.y);
		this.camera.setTranslateZ(position.z);

		this.selectedBox.setTranslateX(position.x);
		this.selectedBox.setTranslateY(position.y);
		this.selectedBox.setTranslateZ(position.z);
	}

	private void setInstruction(Instruction instruction)
	{
		this.grid.setInstruction(this.selectedPosition, instruction);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.setResizable(false);

		Scene scene = new Scene(this.createContent());

		scene.setOnKeyPressed(this::keyTyped);

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
