package za.co.datumza.snake.board;

import lombok.Getter;
import za.co.datumza.snake.player.Direction;
import za.co.datumza.snake.player.Player;
import za.co.datumza.snake.player.PlayerType;
import za.co.datumza.snake.player.pathfinding.CpuPathFinder;
import za.co.datumza.snake.player.pathfinding.PathFindingAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class State {
    private final int MAX_STATES = 50000;
    private final int STATE_INCREMENT = 1;
    private static final double MAX_POISON_APPLE_RATIO = 0.4;
    private static final int ZOMBIE_MOVE_INTERVAL = 2;
    private static final PathFindingAlgorithm[] CPU_ALGORITHMS = {
            PathFindingAlgorithm.GREEDY,
            PathFindingAlgorithm.BREADTH_FIRST,
            PathFindingAlgorithm.A_STAR,
            PathFindingAlgorithm.RANDOM_SAFE
    };

    private Board board;
    private List<Player> players;
    private List<Apple> apples;
    private List<Square> cleanupSquares;
    private List<Integer> cleanupPlayerIds;
    private CpuPathFinder cpuPathFinder;
    private int playerCount;
    private int zombieCount;
    private int currentState;
    private boolean isGameOver;

    public State(int boardWidth, int boardHeight, int playerCount, int appleCount) {
        this(boardWidth, boardHeight, playerCount, 1, appleCount);
    }

    public State(int boardWidth, int boardHeight, int playerCount, int zombieCount, int appleCount) {
        this.board = new Board(boardWidth, boardHeight);
        this.cleanupSquares = new ArrayList<>();
        this.cleanupPlayerIds = new ArrayList<>();
        this.cpuPathFinder = new CpuPathFinder();
        this.playerCount = playerCount;
        this.zombieCount = zombieCount;
        this.currentState = 0;
        this.isGameOver = false;

        createPlayers();
        createApples(appleCount);
    }

    public void progress() {
        if (currentState >= MAX_STATES) {
            this.isGameOver = true;
            return;
        }

        List<Boolean> movingPlayers = getMovingPlayers();
        updateCpuPlayers(movingPlayers);

        List<Square> nextSquares = getNextSquares(movingPlayers);
        checkBlockedSquareCollisions(nextSquares, movingPlayers);
        checkHeadOnCollisions(nextSquares, movingPlayers);

        for (Player player : players) {
            if (!player.isAlive()) {
                addCleanupSquares(player);
            }
        }

        for (int i = 0; i < players.size(); i++) {
            if (movingPlayers.get(i)) {
                players.get(i).handleMove(board, apples);
            }
        }

        for (int i = 0; i < cleanupSquares.size(); i++) {
            cleanupSquares.get(i).unblock(cleanupPlayerIds.get(i));
        }

        cleanupSquares.clear();
        cleanupPlayerIds.clear();
        respawnDeadPlayers();

        for (Apple apple : apples) {
            if (!apple.isEaten()) {
                apple.tick();
            }

            if (apple.isEaten() || apple.isExpired()) {
                apple.move(board.getOpenSquare(), getNextAppleType(apple));
            }
        }

        currentState += STATE_INCREMENT;
    }

    private void createPlayers() {
        this.players = new ArrayList<>();
        int totalPlayers = playerCount + zombieCount;

        for (int i = 0; i < totalPlayers; i++) {
            PlayerType type = i >= playerCount ? PlayerType.ZOMBIE : PlayerType.PLAYER;
            Direction direction = Direction.random();
            Player player = new Player(
                    i,
                    type,
                    direction,
                    board.getOpenSnakeHead(Player.DEFAULT_SIZE, direction),
                    board
            );
            this.players.add(player);
        }
    }

    private void createApples(int playerCount) {
        this.apples = new ArrayList<>();

        for (int i = 0; i < playerCount; i++) {
            Apple apple = new Apple(board.getOpenSquare());
            this.apples.add(apple);
            apple.move(apple.getPosition(), getNextAppleType(apple));
        }
    }

    public void onKeyPress(Direction direction) {
        this.players.getFirst().getMovement().setDirection(direction);
    }

    private void addCleanupSquares(Player player) {
        for (Square square : player.getBody()) {
            cleanupSquares.add(square);
            cleanupPlayerIds.add(player.getId());
        }
    }

    private void respawnDeadPlayers() {
        for (Player player : players) {
            if (!player.isAlive()) {
                player.respawn(board);
            }
        }
    }

    private List<Boolean> getMovingPlayers() {
        List<Boolean> movingPlayers = new ArrayList<>();

        for (Player player : players) {
            movingPlayers.add(!player.isZombie() || currentState % ZOMBIE_MOVE_INTERVAL == 0);
        }

        return movingPlayers;
    }

    private void updateCpuPlayers(List<Boolean> movingPlayers) {
        for (int i = 1; i < players.size(); i++) {
            Player player = players.get(i);

            if (!movingPlayers.get(i) || !player.isAlive()) {
                continue;
            }

            Direction direction = player.isZombie()
                    ? cpuPathFinder.chooseDirectionToClosestPlayer(player, board, players)
                    : cpuPathFinder.chooseDirection(player, board, apples, getCpuAlgorithm(player));

            player.changeDirection(direction);
        }
    }

    private PathFindingAlgorithm getCpuAlgorithm(Player player) {
        return CPU_ALGORITHMS[(player.getId() - 1) % CPU_ALGORITHMS.length];
    }

    private List<Square> getNextSquares(List<Boolean> movingPlayers) {
        List<Square> nextSquares = new ArrayList<>();

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);

            if (!movingPlayers.get(i)) {
                nextSquares.add(null);
                continue;
            }

            try {
                nextSquares.add(player.getNextSquare(board));
            } catch (Exception e) {
                player.die(board);
                movingPlayers.set(i, false);
                nextSquares.add(null);
            }
        }

        return nextSquares;
    }

    private void checkBlockedSquareCollisions(List<Square> nextSquares, List<Boolean> movingPlayers) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            Square nextSquare = nextSquares.get(i);

            if (player.isAlive() && nextSquare != null && nextSquare.isBlocked()) {
                if (player.isZombie()) {
                    if (isPlayerHeadCollision(nextSquare)) {
                        awardZombieKills(player, nextSquare, movingPlayers);
                    } else {
                        movingPlayers.set(i, false);
                        nextSquares.set(i, null);
                    }
                } else if (isLethalPlayerCollision(player, nextSquare)) {
                    player.die(board);
                    awardKills(player, nextSquare);
                    movingPlayers.set(i, false);
                }
            }
        }
    }

    private void checkHeadOnCollisions(List<Square> nextSquares, List<Boolean> movingPlayers) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            Square nextSquare = nextSquares.get(i);

            if (!movingPlayers.get(i) || !player.isAlive() || nextSquare == null) {
                continue;
            }

            List<Player> collidingPlayers = new ArrayList<>();
            collidingPlayers.add(player);

            for (int j = i + 1; j < players.size(); j++) {
                Player otherPlayer = players.get(j);
                Square otherNextSquare = nextSquares.get(j);

                if (movingPlayers.get(j) && otherPlayer.isAlive() && nextSquare.equals(otherNextSquare)) {
                    collidingPlayers.add(otherPlayer);
                }
            }

            if (collidingPlayers.size() > 1) {
                awardHeadOnKills(collidingPlayers, movingPlayers);
            }
        }
    }

    private void awardHeadOnKills(List<Player> collidingPlayers, List<Boolean> movingPlayers) {
        boolean hasZombie = collidingPlayers.stream().anyMatch(Player::isZombie);

        if (hasZombie) {
            long zombieCount = collidingPlayers.stream().filter(Player::isZombie).count();

            for (Player killer : collidingPlayers) {
                if (!killer.isZombie()) {
                    continue;
                }

                for (Player victim : collidingPlayers) {
                    if (victim.isPlayer()) {
                        victim.die(board);
                        movingPlayers.set(victim.getId(), false);
                        killer.kill();
                    }
                }
            }

            if (zombieCount > 1) {
                for (Player zombie : collidingPlayers) {
                    if (zombie.isZombie()) {
                        movingPlayers.set(zombie.getId(), false);
                    }
                }
            }

            return;
        }

        for (Player victim : collidingPlayers) {
            victim.die(board);
            movingPlayers.set(victim.getId(), false);
        }

        for (Player killer : collidingPlayers) {
            for (Player victim : collidingPlayers) {
                if (killer.getId() != victim.getId()) {
                    killer.kill();
                }
            }
        }
    }

    private void awardKills(Player victim, Square collisionSquare) {
        for (Integer playerId : collisionSquare.getPlayers()) {
            Player killer = players.get(playerId);

            if (killer.getId() == victim.getId()) {
                continue;
            }

            if (killer.isZombie() && !killer.getHead().equals(collisionSquare)) {
                continue;
            }

            killer.kill();
        }
    }

    private boolean isLethalPlayerCollision(Player victim, Square collisionSquare) {
        return collisionSquare.isBlocked();
    }

    private boolean isPlayerHeadCollision(Square collisionSquare) {
        for (Integer playerId : collisionSquare.getPlayers()) {
            Player player = players.get(playerId);

            if (player.isPlayer() && player.getHead().equals(collisionSquare)) {
                return true;
            }
        }

        return false;
    }

    private void awardZombieKills(Player zombie, Square collisionSquare, List<Boolean> movingPlayers) {
        for (Integer playerId : collisionSquare.getPlayers()) {
            Player victim = players.get(playerId);

            if (victim.isPlayer() && victim.getHead().equals(collisionSquare)) {
                victim.die(board);
                movingPlayers.set(victim.getId(), false);
                zombie.kill();
            }
        }
    }

    private AppleType getNextAppleType(Apple movingApple) {
        int maxPoisonApples = (int) Math.floor(apples.size() * MAX_POISON_APPLE_RATIO);
        int currentPoisonApples = getPoisonAppleCount();

        if (movingApple.getType() == AppleType.POISONOUS) {
            --currentPoisonApples;
        }

        if (currentPoisonApples >= maxPoisonApples) {
            return AppleType.NORMAL;
        }

        return ThreadLocalRandom.current().nextDouble() < MAX_POISON_APPLE_RATIO
                ? AppleType.POISONOUS
                : AppleType.NORMAL;
    }

    private int getPoisonAppleCount() {
        int count = 0;

        for (Apple apple : apples) {
            if (!apple.isEaten() && apple.getType() == AppleType.POISONOUS) {
                ++count;
            }
        }

        return count;
    }
}
