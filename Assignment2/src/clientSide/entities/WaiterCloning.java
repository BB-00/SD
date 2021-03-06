package clientSide.entities;

/**
 * Cloning Waiter.
 *
 * It specifies his own attributes. Implementation of a client-server model of
 * type 2 (server replication). Communication is based on a communication
 * channel under the TCP protocol.
 */
public interface WaiterCloning {
	/**
	 * Set waiter state.
	 *
	 * @param state new waiter state
	 */
	public void setWaiterState(int state);

	/**
	 * Get waiter state.
	 *
	 * @return waiter state
	 */
	public int getWaiterState();
}