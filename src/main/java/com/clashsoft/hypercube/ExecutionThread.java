package com.clashsoft.hypercube;

import com.clashsoft.hypercube.grid.Grid;
import com.clashsoft.hypercube.grid.GridElement;
import com.clashsoft.hypercube.instruction.Instruction;
import com.clashsoft.hypercube.state.Direction;
import com.clashsoft.hypercube.state.ExecutionException;
import com.clashsoft.hypercube.state.ExecutionState;
import com.clashsoft.hypercube.state.Position;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class ExecutionThread extends Thread implements ExecutionState
{
	private final HypercubeIDE ide;
	private final Grid         grid;

	private volatile boolean paused;
	private volatile boolean running;
	private volatile long delay = 1000;

	private Direction     direction = Direction.FORWARD;
	private Deque<Object> stack     = new ArrayDeque<>();
	private Map<String, Object> variables = new HashMap<>();
	private Position      position  = new Position(0, 0, 0, 0);

	public ExecutionThread(HypercubeIDE ide, Grid grid)
	{
		this.ide = ide;
		this.grid = grid;
	}

	public void setDelay(long delay)
	{
		this.delay = delay;
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
	public void run()
	{
		this.print("Starting Execution...");
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
				Thread.sleep(this.delay);
			}
			catch (InterruptedException ignored)
			{
			}
		}

		this.ide.onExecutionStopped();
		this.print("Stopping Execution.");
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
