package com.clashsoft.hypercube.input;

import com.clashsoft.hypercube.HypercubeIDE;
import com.clashsoft.hypercube.instruction.Instructions;
import com.clashsoft.hypercube.instruction.LoadInstruction;
import com.clashsoft.hypercube.instruction.PushInstruction;
import com.clashsoft.hypercube.instruction.StoreInstruction;
import com.clashsoft.hypercube.state.Direction;
import com.clashsoft.hypercube.util.I18n;
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

	public void uiKeyTyped(KeyEvent event)
	{
		switch (event.getCode())
		{
		case N:
			if (event.isControlDown() || event.isMetaDown())
			{
				event.consume();
				this.ide.newProject();
				return;
			}
			return;
		case O:
			if (event.isControlDown() || event.isMetaDown())
			{
				event.consume();
				this.ide.open();
				return;
			}
			return;
		case S:
			if (event.isControlDown() || event.isMetaDown())
			{
				event.consume();
				this.ide.save(event.isShiftDown());
				return;
			}
			return;
		}
	}

	public void keyTyped(KeyEvent event)
	{
		this.uiKeyTyped(event);
		if (event.isConsumed())
		{
			return;
		}

		switch (event.getCode())
		{
		case UP:
			this.ide.offsetPosition(Direction.NORTH);
			return;
		case DOWN:
			this.ide.offsetPosition(Direction.SOUTH);
			return;
		case LEFT:
			this.ide.offsetPosition(Direction.WEST);
			return;
		case RIGHT:
			this.ide.offsetPosition(Direction.EAST);
			return;
		case PAGE_UP:
			this.ide.offsetPosition(Direction.UP);
			return;
		case PAGE_DOWN:
			this.ide.offsetPosition(Direction.DOWN);
			return;
		case HOME:
			this.ide.offsetPosition(Direction.ATA);
			return;
		case END:
			this.ide.offsetPosition(Direction.KANA);
			return;
		case DELETE:
		case BACK_SPACE:
			this.ide.setInstruction(null);
			return;
		case N:
		{
			final String input = this.inputText(I18n.getString("instruction.push.number.title"),
			                                    I18n.getString("instruction.push.number.info"));
			try
			{
				this.ide.setInstruction(new PushInstruction(Double.valueOf(input)));
			}
			catch (NumberFormatException ignored)
			{
			}
			return;
		}
		case T:
			this.ide.setInstruction(new PushInstruction(this.inputText(I18n.getString("instruction.push.text.title"),
			                                                           I18n.getString("instruction.push.text.info"))));
			return;
		case K:
			this.ide.setInstruction(new StoreInstruction(this.inputText(I18n.getString("instruction.store.title"),
			                                                            I18n.getString("instruction.store.info"))));
			return;
		case L:
			this.ide.setInstruction(new LoadInstruction(this.inputText(I18n.getString("instruction.load.title"),
			                                                           I18n.getString("instruction.load.info"))));
			return;
		case O:
			this.ide.setInstruction(Instructions.OUTPUT);
			return;
		case R:
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
		case W:
			this.ide.setInstruction(Instructions.FORWARD);
			return;
		case S:
			this.ide.setInstruction(Instructions.BACKWARD);
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
		case "%":
			this.ide.setInstruction(Instructions.MODULO);
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
