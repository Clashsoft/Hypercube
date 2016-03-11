package com.clashsoft.hypercube;

import com.clashsoft.hypercube.instruction.Instruction;
import com.clashsoft.hypercube.project.Grid;
import com.clashsoft.hypercube.project.GridElement;
import com.clashsoft.hypercube.project.Project;
import com.clashsoft.hypercube.state.Direction;
import com.clashsoft.hypercube.state.ExecutionException;
import com.clashsoft.hypercube.state.ExecutionState;
import com.clashsoft.hypercube.state.Position;
import com.clashsoft.hypercube.util.I18n;
import javafx.application.Platform;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ExecutionThread extends Thread implements ExecutionState
{
	private final HypercubeIDE ide;
	private final Grid         grid;

	private volatile boolean paused;
	private volatile boolean running;

	private Direction           direction = Direction.NORTH;
	private Deque<Object>       stack     = new LinkedList<>();
	private Map<String, Object> variables = new HashMap<>();
	private Position position;

	public ExecutionThread(HypercubeIDE ide, Project project)
	{
		this.ide = ide;
		this.position = project.getExecutionStartPosition();
		this.grid = project.getGrid();
	}

	public void setPaused(boolean paused)
	{
		this.paused = paused;
	}

	public boolean isPaused()
	{
		return this.paused;
	}

	@Override
	public void push(Object value)
	{
		this.stack.push(value);
	}

	@Override
	public Object pop()
	{
		return this.stack.pollFirst();
	}

	@Override
	public void store(String key, Object value)
	{
		this.variables.put(key, value);
	}

	@Override
	public Object load(String key)
	{
		return this.variables.get(key);
	}

	@Override
	public Position getPosition()
	{
		return this.position;
	}

	@Override
	public void setPosition(Position position)
	{
		this.position = position;
	}

	@Override
	public void setDirection(Direction direction)
	{
		this.direction = direction;
	}

	@Override
	public Direction getDirection()
	{
		return this.direction;
	}

	@Override
	public void print(String message)
	{
		this.ide.output(message);
	}

	@Override
	public String readInput(String prompt)
	{
		final String[] reference = new String[1];

		Platform.runLater(() -> reference[0] = this.ide.input(prompt));

		while (reference[0] == null)
		{
			try
			{
				Thread.sleep(10);
			}
			catch (InterruptedException ignored)
			{
			}
		}

		return reference[0];
	}

	public void run()
	{
		this.print(I18n.getString("execution.start"));
		this.running = true;
		this.paused = false;

		while (this.running)
		{
			if (!this.paused)
			{
				this.step();
			}

			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException ignored)
			{
			}
		}

		this.ide.onExecutionStopped();
		this.print(I18n.getString("execution.stop"));
	}

	private void step()
	{
		final Position position = this.position;
		final GridElement element = this.grid.getElement(position);

		if (element == null)
		{
			this.running = false;
			return;
		}

		final Instruction instruction = element.getInstruction();
		if (instruction != null)
		{
			try
			{
				instruction.execute(this);
			}
			catch (ExecutionException e)
			{
				this.print(e.getLocalizedMessage());
			}
		}

		this.position = position.offset(this.direction);
		this.ide.setExecutionPosition(this.position);
	}

	public void stopExecution()
	{
		this.running = false;
	}
}
