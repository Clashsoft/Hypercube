package com.clashsoft.hypercube;

import com.clashsoft.hypercube.input.InputManager;
import com.clashsoft.hypercube.instruction.Instruction;
import com.clashsoft.hypercube.project.GridElement;
import com.clashsoft.hypercube.project.Project;
import com.clashsoft.hypercube.state.Direction;
import com.clashsoft.hypercube.state.Position;
import com.clashsoft.hypercube.util.TextureLoader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;

public class HypercubeIDE extends Application
{
	public static final int WINDOW_WIDTH  = 1024;
	public static final int WINDOW_HEIGHT = 576;

	public static final PhongMaterial SELECTED_MATERIAL  = new PhongMaterial(Color.rgb(0xFF, 0xA5, 0, 0.5));
	public static final PhongMaterial EXECUTION_MATERIAL = new PhongMaterial(Color.rgb(0, 0xFF, 0, 0.5));

	public static final String          FILE_EXTENSION   = ".hcp";
	public static final ExtensionFilter EXTENSION_FILTER = new ExtensionFilter("Hypercube Project Files (*"
		                                                                           + FILE_EXTENSION + ")", "*" + FILE_EXTENSION);

	private Project project;
	private File    saveFile;

	private ExecutionThread executionThread;
	private InputManager inputManager = new InputManager(this);

	private Stage             primaryStage;
	private SubScene          subScene;
	private PerspectiveCamera camera;
	private Group             gridGroup;
	private Box               selectedBox;

	private Box      executionBox;
	private TextArea console;
	private Label    instructionInfo;
	private Label    positionInfo;

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
		this.primaryStage = primaryStage;

		primaryStage.setResizable(false);

		primaryStage.setScene(this.createContent());

		this.setProject(new Project(this, "New Project"));

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

		this.positionInfo = new Label();
		this.positionInfo.setTranslateX(20);
		this.positionInfo.setTranslateY(20);

		this.instructionInfo = new Label();
		this.instructionInfo.setTranslateX(20);
		this.instructionInfo.setTranslateY(40);

		final Button saveButton = new Button("Save");
		saveButton.setTranslateX(20);
		saveButton.setTranslateY(WINDOW_HEIGHT - 50);
		saveButton.setTooltip(new Tooltip("Save the current Project to disk (Shift-Click to Save As)"));
		saveButton.setOnMouseClicked(event -> {
			if (event.getButton() != MouseButton.PRIMARY)
			{
				return;
			}
			if (event.isShiftDown())
			{
				this.saveFile = null; // Force save file dialog
			}
			this.save();
		});

		final Button openButton = new Button("Open");
		openButton.setTranslateX(80);
		openButton.setTranslateY(WINDOW_HEIGHT - 50);
		openButton.setTooltip(new Tooltip("Open a Project from a file on disk"));
		openButton.setOnMouseClicked(mouseHandler(MouseButton.PRIMARY, this::open));

		final Button newButton = new Button("New");
		newButton.setTranslateX(140);
		newButton.setTranslateY(WINDOW_HEIGHT - 50);
		newButton.setTooltip(new Tooltip("Create a new Project"));
		newButton.setOnMouseClicked(mouseHandler(MouseButton.PRIMARY, this::newFile));

		final Group ui = new Group();
		ui.getChildren().addAll(saveButton, openButton, newButton, startButton, pauseButton, stopButton, this.console,
		                        this.positionInfo, this.instructionInfo);
		return ui;
	}

	private void create3DScene()
	{
		// Camera
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

		final Box xAxis = new Box(0.0125, 0.0125, 1000);
		xAxis.setMaterial(new PhongMaterial(Color.RED));

		final Box yAxis = new Box(1000, 0.0125, 0.0125);
		yAxis.setMaterial(new PhongMaterial(Color.GREEN));

		final Box zAxis = new Box(0.0125, 1000, 0.0125);
		zAxis.setMaterial(new PhongMaterial(Color.BLUE));

		this.gridGroup = new Group();

		// Build the Scene Graph
		final Group sceneGroup = new Group();
		sceneGroup.getChildren()
		          .addAll(this.gridGroup, this.selectedBox, this.executionBox, xAxis, yAxis, zAxis, this.camera);
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

	private void save()
	{
		final File target;

		if (this.saveFile == null)
		{
			final FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save Project");
			this.setupFileChooser(fileChooser);

			target = fileChooser.showSaveDialog(this.primaryStage);
			if (target == null)
			{
				return;
			}
		}
		else
		{
			target = this.saveFile;
		}

		this.saveFile = target;
		Platform.runLater(() -> this.project.writeTo(target));
	}

	private void open()
	{
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Project");

		this.setupFileChooser(fileChooser);

		final File target = fileChooser.showOpenDialog(this.primaryStage);

		if (target == null)
		{
			return;
		}

		this.saveFile = target;
		String targetName = target.getName();
		if (targetName.endsWith(FILE_EXTENSION))
		{
			targetName = targetName.substring(0, targetName.length() - FILE_EXTENSION.length());
		}

		this.setProject(new Project(this, targetName));
		Platform.runLater(() -> this.project.readFrom(target));
	}

	private void setupFileChooser(FileChooser fileChooser)
	{
		fileChooser.getExtensionFilters().add(EXTENSION_FILTER);

		if (this.saveFile == null)
		{
			fileChooser.setInitialFileName(this.project.getName() + FILE_EXTENSION);
		}
		else
		{
			fileChooser.setInitialFileName(this.saveFile.getName());
			fileChooser.setInitialDirectory(this.saveFile.getParentFile());
		}
	}

	private void newFile()
	{
		this.saveFile = null;

		final String name = this.inputManager.inputText("New Project", "Enter Project Name");
		if (name == null) // Clicked Cancel
		{
			return;
		}

		final Project project = new Project(this, name);
		this.setProject(project);
	}

	public void setProject(Project project)
	{
		this.project = project;
		this.primaryStage.setTitle(project.getName() + " – Hypercube IDE");

		this.loadProject(project);
	}

	public void startExecution()
	{
		if (this.executionThread != null)
		{
			this.executionThread.setPaused(false);
			return;
		}

		this.executionThread = new ExecutionThread(this, this.project);
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

	public void setInstruction(Instruction instruction)
	{
		final GridElement element = this.project.getGrid().createElement(this.project.getSelectedPosition());
		element.setInstruction(instruction);
		this.updateInstructionDesc(element);
	}

	public void offsetPosition(Direction direction)
	{
		this.selectPosition(this.project.getSelectedPosition().offset(direction));
	}

	public void selectPosition(Position position)
	{
		this.project.setSelectedPosition(position);

		final GridElement element = this.project.getGrid().createElement(position);

		this.camera.setTranslateX(position.getDisplayX());
		this.camera.setTranslateY(position.getDisplayY());
		this.camera.setTranslateZ(position.getDisplayZ());

		this.selectedBox.setTranslateX(position.getDisplayX());
		this.selectedBox.setTranslateY(position.getDisplayY());
		this.selectedBox.setTranslateZ(position.getDisplayZ());

		this.updateInstructionDesc(element);
	}

	public void updateInstructionDesc(GridElement element)
	{
		final Position position = element.position;

		this.positionInfo.setText(
			String.format("Position:\t\tw: %d x: %d y: %d z: %d", position.w, position.x, position.y, position.z));

		final Instruction instruction = element.getInstruction();
		if (instruction != null)
		{
			this.instructionInfo.setText("Instruction:\t" + instruction.getDescription());
		}
		else
		{
			this.instructionInfo.setText("Instruction:\t<empty>");
		}
	}

	public void setExecutionPosition(Position position)
	{
		this.executionBox.setTranslateX(position.getDisplayX());
		this.executionBox.setTranslateY(position.getDisplayY());
		this.executionBox.setTranslateZ(position.getDisplayZ());
	}

	public void loadProject(Project project)
	{
		this.selectPosition(project.getSelectedPosition());

		ObservableList<Node> children = this.gridGroup.getChildren();
		children.clear();
		children.add(project.getGrid().mainGroup);
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
