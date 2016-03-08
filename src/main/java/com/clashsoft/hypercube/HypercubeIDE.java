package com.clashsoft.hypercube;

import com.clashsoft.hypercube.grid.Grid;
import com.clashsoft.hypercube.grid.GridElement;
import com.clashsoft.hypercube.input.InputManager;
import com.clashsoft.hypercube.instruction.Instruction;
import com.clashsoft.hypercube.state.Direction;
import com.clashsoft.hypercube.state.Position;
import com.clashsoft.hypercube.util.TextureLoader;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class HypercubeIDE extends Application
{
	public static final int WINDOW_WIDTH  = 1024;
	public static final int WINDOW_HEIGHT = 576;

	public static final PhongMaterial SELECTED_MATERIAL  = new PhongMaterial(Color.rgb(0xFF, 0xA5, 0, 0.5));
	public static final PhongMaterial EXECUTION_MATERIAL = new PhongMaterial(Color.rgb(0, 0xFF, 0, 0.5));

	private Position selectedPosition = new Position(0, 0, 0, 0);
	private ExecutionThread executionThread;
	private InputManager    inputManager = new InputManager(this);

	private Grid              grid;
	private SubScene          subScene;
	private PerspectiveCamera camera;
	private Box               selectedBox;

	private Box      executionBox;
	private TextArea console;

	private Label instructionInfo;

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
		this.create3DScene();

		final Group ui = this.createUI();

		Scene scene = new Scene(new Group(this.subScene, ui));
		this.subScene.requestFocus();

		return scene;
	}

	private Group createUI()
	{
		final ImageView startButton = uiButton("start");
		startButton.setX(WINDOW_WIDTH - 140);
		startButton.setY(20);
		startButton.setOnMouseClicked(mouseHandler(MouseButton.PRIMARY, this::startExecution));

		final ImageView pauseButton = uiButton("pause");
		pauseButton.setX(WINDOW_WIDTH - 100);
		pauseButton.setY(20);
		pauseButton.setOnMouseClicked(mouseHandler(MouseButton.PRIMARY, this::pauseExecution));

		final ImageView stopButton = uiButton("stop");
		stopButton.setX(WINDOW_WIDTH - 60);
		stopButton.setY(20);
		stopButton.setOnMouseClicked(mouseHandler(MouseButton.PRIMARY, this::stopExecution));

		this.console = new TextArea();
		this.console.setTranslateX(WINDOW_WIDTH - 240 - 20);
		this.console.setTranslateY(80);
		this.console.setMaxWidth(240);

		this.console.setMinHeight(WINDOW_HEIGHT - 100);
		this.console.setMaxHeight(WINDOW_HEIGHT - 100);
		this.console.setEditable(false);

		this.instructionInfo = new Label("<empty>");
		this.instructionInfo.setTranslateX(20);
		this.instructionInfo.setTranslateY(20);

		final Group ui = new Group();
		ui.getChildren().addAll(startButton, pauseButton, stopButton, this.console, this.instructionInfo);
		return ui;
	}

	private void create3DScene()
	{// Camera
		Translate cameraTranslate = new Translate(0, 0, -42);
		this.camera = new PerspectiveCamera(true);
		this.camera.getTransforms()
		           .addAll(new Rotate(-45, Rotate.Y_AXIS), new Rotate(-20, Rotate.X_AXIS), cameraTranslate);
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

		final Box xAxis = new Box(0.0125, 0.0125, 1000);
		xAxis.setMaterial(new PhongMaterial(Color.RED));

		final Box yAxis = new Box(1000, 0.0125, 0.0125);
		yAxis.setMaterial(new PhongMaterial(Color.GREEN));

		final Box zAxis = new Box(0.0125, 1000, 0.0125);
		zAxis.setMaterial(new PhongMaterial(Color.BLUE));

		// Build the Scene Graph
		final Group sceneGroup = new Group();
		sceneGroup.getChildren()
		          .addAll(this.grid.mainGroup, this.selectedBox, this.executionBox, xAxis, yAxis, zAxis, this.camera);
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
		this.subScene.setOnScroll(event -> {
			final double cameraTranslateZ = cameraTranslate.getZ() + event.getDeltaY();
			if (cameraTranslateZ < -1)
			{
				cameraTranslate.setZ(cameraTranslateZ);
			}
		});
		this.subScene.setOnMouseClicked(event -> this.subScene.requestFocus());
		this.subScene.setOnKeyPressed(this.inputManager::keyTyped);
	}

	private static ImageView uiButton(String texture)
	{
		final ImageView startButton = new ImageView(TextureLoader.load("ui", texture));
		startButton.setPreserveRatio(true);
		startButton.setFitHeight(32);
		startButton.setCursor(Cursor.HAND);
		return startButton;
	}

	public void startExecution()
	{
		if (this.executionThread != null)
		{
			this.executionThread.setPaused(false);
			return;
		}

		this.executionThread = new ExecutionThread(this, this.grid);
		this.executionThread.start();
	}

	public void pauseExecution()
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

	public void offsetPosition(Direction direction)
	{
		this.selectPosition(this.selectedPosition.offset(direction));
	}

	public void selectPosition(Position position)
	{
		this.selectedPosition = position;
		this.camera.setTranslateX(position.x);
		this.camera.setTranslateY(position.y);
		this.camera.setTranslateZ(position.z);

		this.selectedBox.setTranslateX(position.x);
		this.selectedBox.setTranslateY(position.y);
		this.selectedBox.setTranslateZ(position.z);

		GridElement element = this.grid.createElement(position);
		this.updateInstructionDesc(element);
	}

	private void updateInstructionDesc(GridElement element)
	{
		Instruction instruction = element.getInstruction();

		if (instruction != null)
		{
			this.instructionInfo.setText(instruction.getDescription());
		}
		else
		{
			this.instructionInfo.setText("<empty>");
		}
	}

	public void setInstruction(Instruction instruction)
	{
		GridElement element = this.grid.createElement(this.selectedPosition);

		element.setInstruction(instruction);

		this.updateInstructionDesc(element);
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
