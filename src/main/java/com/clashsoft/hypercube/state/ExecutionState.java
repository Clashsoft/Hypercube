package com.clashsoft.hypercube.state;

public interface ExecutionState
{
	void push(Object value);

	Object pop();

	void store(String key, Object value);

	Object load(String key);

	Position getPosition();

	void setPosition(Position position);

	void setDirection(Direction direction);

	Direction getDirection();

	void print(String message);
}
