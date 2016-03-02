package com.clashsoft.hypercube.state;

public enum Direction
{
	FORWARD(0, 0, 0, 1),
	BACKWARD(0, 0, 0, -1),
	LEFT(0, -1, 0, 0),
	RIGHT(0, 1, 0, 0),
	UP(0, 0, 1, 0),
	DOWN(0, 0, -1, 0),
	NIM(1, 0, 0, 0),
	BOR(-1, 0, 0, 0);

	public final int deltaW;
	public final int deltaX;
	public final int deltaY;
	public final int deltaZ;

	Direction(int deltaW, int deltaX, int deltaY, int deltaZ)
	{
		this.deltaW = deltaW;
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		this.deltaZ = deltaZ;
	}
}
