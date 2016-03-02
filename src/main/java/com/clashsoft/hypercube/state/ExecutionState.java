package com.clashsoft.hypercube.state;

import java.util.Stack;

public class ExecutionState
{
	private Stack<Object> stack;

	private Direction direction = Direction.FORWARD;

	public void push(Object value)
	{
		this.stack.push(value);
	}

	public Object pop()
	{
		return this.stack.pop();
	}

	public void setDirection(Direction direction)
	{
		this.direction = direction;
	}

	public Direction getDirection()
	{
		return this.direction;
	}
}
