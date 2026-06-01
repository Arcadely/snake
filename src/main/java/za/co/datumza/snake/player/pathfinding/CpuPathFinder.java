package za.co.datumza.snake.player.pathfinding;

import za.co.datumza.snake.board.Apple;
import za.co.datumza.snake.board.Board;
import za.co.datumza.snake.board.Square;
import za.co.datumza.snake.player.Direction;
import za.co.datumza.snake.player.Player;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class CpuPathFinder {
    public Direction chooseDirection(Player player, Board board, List<Apple> apples, PathFindingAlgorithm algorithm) {
        Square target = findNearestApple(player.getHead(), apples);

        if (target == null) {
            return chooseRandomSafeDirection(player, board);
        }

        return switch (algorithm) {
            case RANDOM_SAFE -> chooseRandomSafeDirection(player, board);
            case GREEDY -> chooseGreedyDirection(player, board, target);
            case BREADTH_FIRST -> chooseBreadthFirstDirection(player, board, target);
            case A_STAR -> chooseAStarDirection(player, board, target);
        };
    }

    private Direction chooseRandomSafeDirection(Player player, Board board) {
        List<Direction> safeDirections = getSafeDirections(player, board);

        if (safeDirections.isEmpty()) {
            return player.getMovement().getDirection();
        }

        return safeDirections.get(ThreadLocalRandom.current().nextInt(safeDirections.size()));
    }

    private Direction chooseGreedyDirection(Player player, Board board, Square target) {
        return getSafeDirections(player, board).stream()
                .min(Comparator.comparingInt(direction -> distance(getNextSquare(player, board, direction), target)))
                .orElse(player.getMovement().getDirection());
    }

    private Direction chooseBreadthFirstDirection(Player player, Board board, Square target) {
        Queue<Square> queue = new ArrayDeque<>();
        Set<Square> visited = new HashSet<>();
        Map<Square, Direction> firstSteps = new HashMap<>();

        Square head = player.getHead();
        queue.add(head);
        visited.add(head);

        while (!queue.isEmpty()) {
            Square current = queue.remove();

            if (current.equals(target)) {
                return firstSteps.getOrDefault(current, player.getMovement().getDirection());
            }

            for (Direction direction : Direction.values()) {
                if (current.equals(head) && isOppositeDirection(player, direction)) {
                    continue;
                }

                Square next = getNextSquare(board, current, direction);

                if (next == null || visited.contains(next) || isBlocked(next, target)) {
                    continue;
                }

                visited.add(next);
                firstSteps.put(next, current.equals(head) ? direction : firstSteps.get(current));
                queue.add(next);
            }
        }

        return chooseGreedyDirection(player, board, target);
    }

    private Direction chooseAStarDirection(Player player, Board board, Square target) {
        PriorityQueue<PathNode> open = new PriorityQueue<>(Comparator.comparingInt(PathNode::score));
        Map<Square, Integer> bestCosts = new HashMap<>();

        Square head = player.getHead();
        open.add(new PathNode(head, null, 0, distance(head, target)));
        bestCosts.put(head, 0);

        while (!open.isEmpty()) {
            PathNode current = open.remove();

            if (current.square().equals(target)) {
                return current.firstStep() == null ? player.getMovement().getDirection() : current.firstStep();
            }

            for (Direction direction : Direction.values()) {
                if (current.square().equals(head) && isOppositeDirection(player, direction)) {
                    continue;
                }

                Square next = getNextSquare(board, current.square(), direction);

                if (next == null || isBlocked(next, target)) {
                    continue;
                }

                int nextCost = current.cost() + 1;
                if (nextCost >= bestCosts.getOrDefault(next, Integer.MAX_VALUE)) {
                    continue;
                }

                Direction firstStep = current.firstStep() == null ? direction : current.firstStep();
                bestCosts.put(next, nextCost);
                open.add(new PathNode(next, firstStep, nextCost, nextCost + distance(next, target)));
            }
        }

        return chooseGreedyDirection(player, board, target);
    }

    private List<Direction> getSafeDirections(Player player, Board board) {
        List<Direction> directions = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            Square nextSquare = getNextSquare(player, board, direction);

            if (!isOppositeDirection(player, direction) && nextSquare != null && nextSquare.isOpen()) {
                directions.add(direction);
            }
        }

        return directions;
    }

    private Square findNearestApple(Square head, List<Apple> apples) {
        return apples.stream()
                .filter(apple -> !apple.isEaten())
                .map(Apple::getPosition)
                .min(Comparator.comparingInt(apple -> distance(head, apple)))
                .orElse(null);
    }

    private Square getNextSquare(Player player, Board board, Direction direction) {
        try {
            return player.getNextSquare(board, direction);
        } catch (Exception e) {
            return null;
        }
    }

    private Square getNextSquare(Board board, Square square, Direction direction) {
        try {
            return switch (direction) {
                case UP -> square.getUp(board);
                case DOWN -> square.getDown(board);
                case LEFT -> square.getLeft(board);
                case RIGHT -> square.getRight(board);
            };
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isBlocked(Square square, Square target) {
        return square.isBlocked() && !square.equals(target);
    }

    private boolean isOppositeDirection(Player player, Direction direction) {
        return switch (player.getMovement().getDirection()) {
            case UP -> direction == Direction.DOWN;
            case DOWN -> direction == Direction.UP;
            case LEFT -> direction == Direction.RIGHT;
            case RIGHT -> direction == Direction.LEFT;
        };
    }

    private int distance(Square start, Square end) {
        return Math.abs(start.getX() - end.getX()) + Math.abs(start.getY() - end.getY());
    }

    private record PathNode(Square square, Direction firstStep, int cost, int score) {
    }
}
