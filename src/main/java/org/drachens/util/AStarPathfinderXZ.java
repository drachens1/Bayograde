package org.drachens.util;

import net.minestom.server.coordinate.Pos;
import org.drachens.Manager.per_instance.ProvinceManager;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Province;
import org.drachens.interfaces.AStarPathfinderVoids;

import java.util.*;

public class AStarPathfinderXZ {
    private final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    private final ProvinceManager provinceManager;

    public AStarPathfinderXZ(ProvinceManager provinceManager){
        this.provinceManager=provinceManager;
    }

    public List<Node> findPath(Province start, Province end, Country country, AStarPathfinderVoids aStarPathfinderVoids) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.fCost));
        Map<String, Node> allNodes = new HashMap<>();
        Pos s = start.getPos();
        Pos e = end.getPos();
        int startX = s.blockX();
        int startZ = s.blockZ();
        int goalX = e.blockX();
        int goalZ = e.blockZ();

        Node startNode = new Node(startX, startZ, null, 0, heuristic(startX, startZ, goalX, goalZ), provinceManager);
        openSet.add(startNode);
        allNodes.put(hashKey(startX, startZ), startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.x == goalX && current.z == goalZ) {
                return reconstructPath(current);
            }

            for (int[] dir : DIRECTIONS) {
                int newX = current.x + dir[0];
                int newZ = current.z + dir[1];

                if (!isValid(newX, newZ, country, aStarPathfinderVoids)) continue;

                int newG = current.gCost + 1;
                String key = hashKey(newX, newZ);
                Node neighbor = allNodes.getOrDefault(key, new Node(newX, newZ, null, Integer.MAX_VALUE, 0, provinceManager));

                if (newG < neighbor.gCost) {
                    neighbor.gCost = newG;
                    neighbor.fCost = newG + heuristic(newX, newZ, goalX, goalZ);
                    neighbor.parent = current;

                    if (!allNodes.containsKey(key)) {
                        openSet.add(neighbor);
                        allNodes.put(key, neighbor);
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    private boolean isValid(int x, int z, Country country, AStarPathfinderVoids aStarPathfinderVoids) {
        Province p = provinceManager.getProvince(x,z);
        return null != p && aStarPathfinderVoids.isWalkable(p, country);
    }

    private int heuristic(int x, int z, int goalX, int goalZ) {
        return Math.abs(x - goalX) + Math.abs(z - goalZ);
    }

    private String hashKey(int x, int z) {
        return x + "," + z;
    }
    private List<Node> reconstructPath(Node node) {
        List<Node> path = new ArrayList<>();
        while (null != node) {
            path.add(node);
            node = node.parent;
        }
        Collections.reverse(path);
        return path;
    }

    public static class Node {
        public final Province province;
        final int x, z;
        int gCost, fCost;
        Node parent;

        public Node(int x, int z, Node parent, int gCost, int fCost, ProvinceManager provinceManager) {
            this.x = x;
            this.z = z;
            this.parent = parent;
            this.gCost = gCost;
            this.fCost = fCost;
            this.province = provinceManager.getProvince(x,z);
        }
    }
}