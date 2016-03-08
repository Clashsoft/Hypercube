package com.clashsoft.hypercube.state;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

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

	public static Position readFrom(DataInput dataInput) throws IOException
	{
		return new Position(dataInput.readInt(), dataInput.readInt(), dataInput.readInt(), dataInput.readInt());
	}

	public void writeTo(DataOutput dataOutput) throws IOException
	{
		dataOutput.writeInt(this.w);
		dataOutput.writeInt(this.x);
		dataOutput.writeInt(this.y);
		dataOutput.writeInt(this.z);
	}

	public double getDisplayX()
	{
		return this.y - this.w * 100;
	}

	public double getDisplayY()
	{
		return -this.z;
	}

	public double getDisplayZ()
	{
		return this.x + this.w * 100;
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

	@Override
	public String toString()
	{
		return "Position(" + "w: " + this.w + ", x: " + this.x + ", y: " + this.y + ", z: " + this.z + ')';
	}
}
