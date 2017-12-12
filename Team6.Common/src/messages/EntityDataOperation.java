
package messages;

import entities.IEntity;

/**
 *
 * EntityDataOperation: An operation which does on {@link EntityData}.
 * 
 */
public enum EntityDataOperation {

	/**
	 * Add specific {@link IEntity} operation.
	 */
	Add,

	/**
	 * Update specific {@link IEntity} operation.
	 */
	Update,

	/**
	 * Remove specific {@link IEntity} operation.
	 */
	Remove,

	/**
	 * Get specific {@link IEntity} operation.
	 */
	Get,
	
	/**
	 * Without specific {@link IEntity} operation.
	 */
	None
}
