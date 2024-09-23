package com.ecosystem.distribution.entityDistribution;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.ecosystem.entities.abstracts.Entity;
import com.ecosystem.utils.Consts;

public class Quadtree<T extends Entity> {

	private final int MAX_ENTITIES = 4; // Max entities before subdivision
	private final int MAX_LEVELS = 5; // Max depth of quadtree

	private int level;
	private List<T> entities;
	private Rectangle bounds;
	private Quadtree<T>[] nodes;

	private ReentrantReadWriteLock lock;

	@SuppressWarnings("unchecked")
	public Quadtree(int level, Rectangle bounds) {
		this.level = level;
		this.entities = new ArrayList<>();
		this.bounds = bounds;
		this.nodes = new Quadtree[4]; // 4 quadrants
		this.lock = new ReentrantReadWriteLock();
	}

	public Quadtree() {
		this(0, new Rectangle(-Consts.getInstance().WORLD_SIZE_X, -Consts.getInstance().WORLD_SIZE_Y,
				2 * Consts.getInstance().WORLD_SIZE_X, 2 * Consts.getInstance().WORLD_SIZE_Y));
	}

	// Clear the quadtree
	public void clear() {
		lock.writeLock().lock();
		try {
			entities.clear();
			for (int i = 0; i < nodes.length; i++) {
				if (nodes[i] != null) {
					nodes[i].clear();
					nodes[i] = null;
				}
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	// Split the node into four subnodes
	private void split() {
		float subWidth = bounds.width / 2f;
		float subHeight = bounds.height / 2f;
		float x = bounds.x;
		float y = bounds.y;

		nodes[0] = new Quadtree<>(level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight));
		nodes[1] = new Quadtree<>(level + 1, new Rectangle(x, y, subWidth, subHeight));
		nodes[2] = new Quadtree<>(level + 1, new Rectangle(x, y + subHeight, subWidth, subHeight));
		nodes[3] = new Quadtree<>(level + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
	}

	// Get the index of the node that fully contains the entity
	private int getIndex(T entity) {
		int index = -1;
		float verticalMidpoint = bounds.x + bounds.width / 2;
		float horizontalMidpoint = bounds.y + bounds.height / 2;

		boolean topQuadrant = entity.getPosition().y > horizontalMidpoint;
		boolean bottomQuadrant = entity.getPosition().y <= horizontalMidpoint;

		if (entity.getPosition().x > verticalMidpoint) {
			if (topQuadrant)
				index = 3; // Top-right quadrant
			else if (bottomQuadrant)
				index = 0; // Bottom-right quadrant
		} else {
			if (topQuadrant)
				index = 2; // Top-left quadrant
			else if (bottomQuadrant)
				index = 1; // Bottom-left quadrant
		}

		return index;
	}

	// Insert an entity into the quadtree
	public void insert(T entity) {
		lock.writeLock().lock();
		try {
			if (nodes[0] != null) {
				int index = getIndex(entity);

				if (index != -1) {
					nodes[index].insert(entity);
					return;
				}
			}

			entities.add(entity);

			if (entities.size() > MAX_ENTITIES && level < MAX_LEVELS) {
				if (nodes[0] == null)
					split();

				int i = 0;
				while (i < entities.size()) {
					int index = getIndex(entities.get(i));
					if (index != -1) {
						nodes[index].insert(entities.remove(i));
					} else {
						i++;
					}
				}
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	// Retrieve entities within a search area
	public List<T> retrieve(Rectangle searchArea) {
		lock.readLock().lock();
		try {
			List<T> returnEntities = new ArrayList<>();

			if (!bounds.overlaps(searchArea)) {
				return returnEntities; // Return empty list if not overlapping
			}

			for (T entity : entities) {
				if (searchArea.contains(entity.getPosition())) {
					returnEntities.add(entity);
				}
			}

			if (nodes[0] != null) {
				for (Quadtree<T> node : nodes) {
					returnEntities.addAll(node.retrieve(searchArea));
				}
			}

			return returnEntities;
		} finally {
			lock.readLock().unlock();
		}
	}

	// Retrieve all entities for rendering
	public List<T> retrieveAll() {
		lock.readLock().lock();
		try {
			List<T> allEntities = new ArrayList<>(entities);
			if (nodes[0] != null) {
				for (Quadtree<T> node : nodes) {
					allEntities.addAll(node.retrieveAll());
				}
			}
			return allEntities;
		} finally {
			lock.readLock().unlock();
		}
	}

	public int size() {
		lock.readLock().lock();
		try {
			int totalSize = entities.size();
			if (nodes[0] != null) {
				for (Quadtree<T> node : nodes) {
					totalSize += node.size();
				}
			}
			return totalSize;
		} finally {
			lock.readLock().unlock();
		}
	}

	public boolean isEmpty() {
		lock.readLock().lock();
		try {
			return entities.isEmpty() && (nodes[0] == null || allNodesEmpty());
		} finally {
			lock.readLock().unlock();
		}
	}

	private boolean allNodesEmpty() {
		for (Quadtree<T> node : nodes) {
			if (!node.isEmpty())
				return false;
		}
		return true;
	}

	// Get a random entity from the quadtree
	public T getRandomEntity() {
		lock.readLock().lock();
		try {
			List<T> allEntities = retrieveAll(); // Flatten the tree into a list
			if (allEntities.isEmpty())
				return null; // Return null if no entities
			return allEntities.get(MathUtils.random(allEntities.size() - 1));
		} finally {
			lock.readLock().unlock();
		}
	}

	// Update all entities in the quadtree, reinserting them if they move
	// Optimized Update function that only reinserts moved entities
    public void update(float delta) {
        lock.writeLock().lock();
        try {
            updateEntitiesInTree(this, delta);
        } finally {
            lock.writeLock().unlock();
        }
    }

    // Helper method to update entities recursively in the tree
    private void updateEntitiesInTree(Quadtree<T> node, float delta) {
        // Iterate through entities in the current node
    	List<T> toReinsert = new ArrayList<>();
        for (T entity : node.entities) {
            entity.update(delta);

            // Check if the entity still fits in the current node after the update
            if (!node.bounds.contains(entity.getBoundingBox())) {
                // If the entity no longer fits, remove and reinsert it into the appropriate node
            	toReinsert.add(entity);
            }
        }
        
        for (T entity : toReinsert) {
        	node.entities.remove(entity);
            insert(entity);
        }

        // Recursively update entities in child nodes
        if (node.nodes[0] != null) {
            for (Quadtree<T> childNode : node.nodes) {
                if (childNode != null) {
                    updateEntitiesInTree(childNode, delta);
                }
            }
        }
    }
	
	// Dispose all entities and the quadtree itself
    public void dispose() {
        lock.writeLock().lock();
        try {
            List<T> allEntities = retrieveAll();
            for (T entity : allEntities) {
                entity.dispose();
            }
            clear();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
