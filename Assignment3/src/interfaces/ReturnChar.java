package interfaces;

import java.io.Serializable;

/**
 * Data type to return both an identification value and an integer state value.
 *
 * Used in calls on remote objects.
 */

public class ReturnChar implements Serializable {
	/**
	 * Serialization key.
	 */

	public static final long serialVersionUID = 2021L;

	/**
	 * Integer identification value.
	 */

	private char val;

	/**
	 * Integer state value.
	 */

	private int state;

	/**
	 * ReturnBoolean instantiation.
	 *
	 * @param val   integer identification value
	 * @param state integer state value
	 */

	public ReturnChar(char val, int state) {
		this.val = val;
		this.state = state;
	}

	/**
	 * Getting integer identification value.
	 *
	 * @return integer identification value
	 */

	public int getCharVal() {
		return (val);
	}

	/**
	 * Getting integer state value.
	 *
	 * @return integer state value
	 */

	public int getIntStateVal() {
		return (state);
	}
}
