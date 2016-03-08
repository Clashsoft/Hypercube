package com.clashsoft.hypercube.input;

import com.clashsoft.hypercube.HypercubeIDE;
import com.clashsoft.hypercube.instruction.*;
import com.clashsoft.hypercube.state.Direction;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;

import java.util.Optional;

public class InputManager
{
	private final HypercubeIDE ide;

	public InputManager(HypercubeIDE ide)
	{
		this.ide = ide;
	}

	public void keyTyped(KeyEvent event)
	{
		switch (event.getCode())
		{
		case UP:
			this.ide.offsetPosition(Direction.FORWARD);
			return;
		case DOWN:
			this.ide.offsetPosition(Direction.BACKWARD);
			return;
		case LEFT:
			this.ide.offsetPosition(Direction.LEFT);
			return;
		case RIGHT:
			this.ide.offsetPosition(Direction.RIGHT);
			return;
		case PAGE_UP:
			this.ide.offsetPosition(Direction.UP);
			return;
		case PAGE_DOWN:
			this.ide.offsetPosition(Direction.DOWN);
			return;
		case HOME:
			this.ide.offsetPosition(Direction.NIM);
			return;
		case END:
			this.ide.offsetPosition(Direction.BOR);
			return;
		case DELETE:
		case BACK_SPACE:
			this.ide.setInstruction(null);
			return;
		case N:
		{
			final String input = this.inputText("Push Number", "Enter a Number");
			this.ide.setInstruction(new PushInstruction(Double.valueOf(input)));
			return;
		}
		case T:
			this.ide.setInstruction(new PushInstruction(this.inputText("Push Text", "Enter Text")));
			return;
		case K:
			this.ide.setInstruction(new StoreInstruction(this.inputText("Store Variable", "Enter Variable Name")));
			return;
		case L:
			this.ide.setInstruction(new LoadInstruction(this.inputText("Load Variable", "Enter Variable Name")));
			return;
		case O:
			this.ide.setInstruction(Instructions.OUTPUT);
			return;
		case R :
			this.ide.setInstruction(Instructions.INPUT_TEXT);
			return;
		case F:
			this.ide.setInstruction(Instructions.INPUT_NUMBER);
			return;
		case P:
			this.ide.setInstruction(Instructions.POP);
			return;
		case I:
			this.ide.setInstruction(Instructions.DUP);
			return;
		case J:
			this.ide.setInstruction(Instructions.COMPARE);
			return;
		case W:
			this.ide.setInstruction(Instructions.FORWARD);
			return;
		case S:
			this.ide.setInstruction(Instructions.BACKWARD);
			return;
		case A:
			this.ide.setInstruction(Instructions.LEFT);
			return;
		case D:
			this.ide.setInstruction(Instructions.RIGHT);
			return;
		case Q:
			this.ide.setInstruction(Instructions.UP);
			return;
		case Y:
			this.ide.setInstruction(Instructions.DOWN);
			return;
		case PLAY:
			this.ide.startExecution();
			return;
		case PAUSE:
			this.ide.pauseExecution();
			return;
		case STOP:
			this.ide.stopExecution();
			return;
		}

		switch (event.getText())
		{
		case "+":
			this.ide.setInstruction(Instructions.ADD);
			return;
		case "-":
			this.ide.setInstruction(Instructions.SUBTRACT);
			return;
		case "*":
			this.ide.setInstruction(Instructions.MULTIPLY);
			return;
		case "/":
			this.ide.setInstruction(Instructions.DIVIDE);
			return;
		}

		return;
	}

	public String inputText(String title, String info)
	{
		final TextInputDialog textInputDialog = new TextInputDialog();
		textInputDialog.setTitle(title);
		textInputDialog.setHeaderText(info);

		Optional<String> input = textInputDialog.showAndWait();
		return input.orElse(null);
	}
}
