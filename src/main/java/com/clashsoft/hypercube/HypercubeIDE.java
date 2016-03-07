package com.clashsoft.hypercube;

import com.clashsoft.hypercube.grid.Grid;
import com.clashsoft.hypercube.util.TextureLoader;
import com.clashsoft.hypercube.instruction.Instruction;
import com.clashsoft.hypercube.instruction.Instructions;
import com.clashsoft.hypercube.instruction.PushInstruction;
import com.clashsoft.hypercube.state.Direction;
import com.clashsoft.hypercube.state.Position;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.util.Optional;

public class HypercubeIDE extends Application
{
	public static final int           WINDOW_WIDTH      = 1024;
	public static final int           WINDOW_HEIGHT     = 576;

	public static final PhongMaterial SELECTED_MATERIAL = new PhongMaterial(Color.rgb(0xFF, 0xA5, 0, 0.5));
	public static final PhongMaterial EXECUTION_MATERIAL = new PhongMaterial(Color.rgb(0, 0xFF, 0, 0.5));

	private Position selectedPosition = new Position(0, 0, 0, 0);
	private Box selectedBox;
	private Box executionBox;

	private Grid              grid;
	private PerspectiveCamera camera;
	private ExecutionThread   executionThread;
	private TextArea          console;
	private SubScene          subScene;

	/**
	 * Java main for when running without JavaFX launcher
	 */
	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.setResizable(false);

		primaryStage.setScene(this.createContent());
		primaryStage.setTitle("Hypercube");

		primaryStage.show();
	}

	public Scene createContent() throws Exception
	{
		// Camera
		this.camera = new PerspectiveCamera(true);
		this.camera.getTransforms()
		           .addAll(new Rotate(-45, Rotate.Y_AXIS), new Rotate(-20, Rotate.X_AXIS), new Translate(0, 0, -42));
		this.camera.setNearClip(0.1);
		this.camera.setFarClip(2000.0);
		this.camera.setFieldOfView(40);
		this.camera.setRotationAxis(new Point3D(0, 1, 0));

		this.selectedBox = new Box(1, 1, 1);
		this.selectedBox.setMaterial(SELECTED_MATERIAL);
		this.selectedBox.setDrawMode(DrawMode.LINE);

		this.executionBox = new Box(1, 1, 1);
		this.executionBox.setMaterial(EXECUTION_MATERIAL);
		this.executionBox.setDrawMode(DrawMode.LINE);

		this.grid = new Grid(this);
		this.grid.createElement(this.selectedPosition);

		// Build the Scene Graph
		final Group sceneGroup = new Group();
		sceneGroup.getChildren().addAll(this.grid.mainGroup, this.selectedBox, this.executionBox, this.camera);
		sceneGroup.setCursor(Cursor.OPEN_HAND);

		// Use a SubScene
		this.subScene = new SubScene(sceneGroup, WINDOW_WIDTH, WINDOW_HEIGHT, true, SceneAntialiasing.BALANCED);
		this.subScene.setFill(Color.LIGHTBLUE);
		this.subScene.setCamera(this.camera);
		this.subScene.setBlendMode(BlendMode.MULTIPLY);
		this.subScene.setDepthTest(DepthTest.ENABLE);
		this.subScene.setOnMouseDragged(event -> {
			if (event.getButton() == MouseButton.SECONDARY)
			{
				this.camera.setRotate(event.getX());
			}
		});

		final ImageView startButton = uiButton("start");
		startButton.setX(20);
		startButton.setY(20);
		startButton.setOnMouseClicked(mouseHandler(MouseButton.PRIMARY, this::startExecution));

		final ImageView pauseButton = uiButton("pause");
		pauseButton.setX(60);
		pauseButton.setY(20);
		pauseButton.setOnMouseClicked(mouseHandler(MouseButton.PRIMARY, this::pauseExecution));

		final ImageView stopButton = uiButton("stop");
		stopButton.setX(100);
		stopButton.setY(20);
		stopButton.setOnMouseClicked(mouseHandler(MouseButton.PRIMARY, this::stopExecution));

		this.console = new TextArea();
		this.console.setTranslateX(WINDOW_WIDTH - 256 - 20);
		this.console.setTranslateY(20);
		this.console.setMaxWidth(256);
		this.console.setMinHeight(256);
		this.console.setMaxHeight(WINDOW_HEIGHT - 40);
		this.console.setEditable(false);

		final Group ui = new Group();
		ui.getChildren().addAll(startButton, pauseButton, stopButton, this.console);

		this.subScene.setOnMouseClicked(event -> this.subScene.requestFocus());
		this.subScene.setOnKeyPressed(this::keyTyped);

		Scene scene = new Scene(new Group(this.subScene, ui));
		this.subScene.requestFocus();

		return scene;
	}

	private static ImageView uiButton(String texture)
	{
		final ImageView startButton = new ImageView(TextureLoader.load("ui", texture));
		startButton.setPreserveRatio(true);
		startButton.setFitHeight(32);
		startButton.setCursor(Cursor.HAND);
		return startButton;
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
		case O :
			this.setInstruction(Instructions.OUTPUT);
			return;
		case PLAY:
			this.startExecution();
			return;
		case PAUSE:
			this.pauseExecution();
			return;
		case STOP:
			this.stopExecution();
			return;
		}

		switch (event.getText())
		{
		case "+":
			this.setInstruction(Instructions.ADD);
			return;
		case "-":
			this.setInstruction(Instructions.SUBTRACT);
			return;
		case "*":
			this.setInstruction(Instructions.MULTIPLY);
			return;
		case "/":
			this.setInstruction(Instructions.DIVIDE);
			return;
		}

		return;
	}

	private void startExecution()
	{
		if (this.executionThread != null)
		{
			this.executionThread.setPaused(false);
			return;
		}

		this.executionThread = new ExecutionThread(this, this.grid);
		this.executionThread.start();
	}

	private void pauseExecution()
	{
		if (this.executionThread != null)
		{
			this.executionThread.setPaused(true);
		}
	}

	public void stopExecution()
	{
		if (this.executionThread != null)
		{
			this.executionThread.stopExecution();
			this.executionThread = null;
		}
	}

	public void onExecutionStopped()
	{
		this.executionThread = null;
		this.setExecutionPosition(new Position(0, 0, 0, 0));
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
		this.grid.createElement(position);

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

	public void setExecutionPosition(Position position)
	{
		this.executionBox.setTranslateX(position.x);
		this.executionBox.setTranslateY(position.y);
		this.executionBox.setTranslateZ(position.z);
	}

	public void output(String message)
	{
		this.console.setText(this.console.getText() + message + '\n');
	}

	private static EventHandler<MouseEvent> mouseHandler(MouseButton button, Runnable runnable)
	{
		return event -> {
			if (event.getButton() == button)
			{
				runnable.run();
			}
		};
	}
}
