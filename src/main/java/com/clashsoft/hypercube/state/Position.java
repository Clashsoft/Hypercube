package com.clashsoft.hypercube.state;

public class Position
{
	public final int w;
	public final int x;
	public final int y;
	public final int z;

	public Position(int w, int x, int y, int z)
	{
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Position offset(Direction direction)
	{
		return this.offset(direction.deltaW, direction.deltaX, direction.deltaY, direction.deltaZ);
	}

	public Position offset(int w, int x, int y, int z)
	{
		return new Position(this.w + w, this.x + x, this.y + y, this.z + z);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (!(o instanceof Position))
		{
			return false;
		}

		Position position = (Position) o;

		return w == position.w && x == position.x && y == position.y && z == position.z;
	}

	@Override
	public int hashCode()
	{
		int result = w;
		result = 31 * result + x;
		result = 31 * result + y;
		result = 31 * result + z;
		return result;
	}
}
