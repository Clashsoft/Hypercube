package com.clashsoft.hypercube.state;

public enum Direction
{
	NORTH(0, 1, 0, 0),
	SOUTH(0, -1, 0, 0),
	WEST(0, 0, -1, 0),
	EAST(0, 0, 1, 0),
	UP(0, 0, 0, 1),
	DOWN(0, 0, 0, -1),
	ATA(1, 0, 0, 0),
	KANA(-1, 0, 0, 0);

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
