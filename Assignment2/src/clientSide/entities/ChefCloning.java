package clientSide.entities;

/**
 * Cloning Chef.
 *
 * It specifies his own attributes. Implementation of a client-server model of
 * type 2 (server replication). Communication is based on a communication
 * channel under the TCP protocol.
 */
public interface ChefCloning {

	/**
	 * Set chef state.
	 *
	 * @param state new chef state
	 */
	public void setChefState(int state);

	/**
	 * Get chef state.
	 *
	 * @return chef state
	 */
	public int getChefState();
}