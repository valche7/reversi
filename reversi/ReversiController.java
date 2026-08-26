package reversi;

public class ReversiController implements IController {
    private IModel model;
    private IView view;

    @Override
    public void initialise(IModel model, IView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void startup() {
        model.clear(0);
        model.setPlayer(1);
        model.setFinished(false);
        model.setBoardContents(3, 3, 1);
        model.setBoardContents(4, 4, 1);
        model.setBoardContents(3, 4, 2);
        model.setBoardContents(4, 3, 2);
        update();
        view.refreshView();
    }

    @Override
    public void update() {
        int w = 0, b = 0;
        boolean whiteCanMove = false;
        boolean blackCanMove = false;

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                int piece = model.getBoardContents(x, y);
                if (piece == 1) w++;
                else if (piece == 2) b++;
                
                if (piece == 0) {
                    if (!whiteCanMove && isValidMove(1, x, y)) whiteCanMove = true;
                    if (!blackCanMove && isValidMove(2, x, y)) blackCanMove = true;
                }
            }
        }

        if (!whiteCanMove && !blackCanMove) {
            model.setFinished(true);
        } else {
            model.setFinished(false);
            if (model.getPlayer() == 1 && !whiteCanMove) {
                model.setPlayer(2);
            } else if (model.getPlayer() == 2 && !blackCanMove) {
                model.setPlayer(1);
            }
        }

        if (model.hasFinished()) {
            String msg;
            if (w == b) {
                msg = "Draw. Both players ended with " + w + " pieces. Restart to continue.";
            } else if (w > b) {
                msg = "White won. White " + w + " to Black " + b + ". Restart to continue.";
            } else {
                msg = "Black won. Black " + b + " to White " + w + ". Restart to continue.";
            }
            view.feedbackToUser(1, msg);
            view.feedbackToUser(2, msg);
        } else {
            if (model.getPlayer() == 1) {
                view.feedbackToUser(1, "White player - choose where to put your piece");
                view.feedbackToUser(2, "Black player - not your turn");
            } else {
                view.feedbackToUser(1, "White player - not your turn");
                view.feedbackToUser(2, "Black player - choose where to put your piece");
            }
        }
    }

    @Override
    public void squareSelected(int player, int x, int y) {
        if (model.hasFinished() || player != model.getPlayer()) return;

        if (isValidMove(player, x, y)) {
            makeMove(player, x, y);
            model.setPlayer(3 - player);
            update();
            view.refreshView();
        }
    }

    @Override
    public void doAutomatedMove(int player) {
        if (model.hasFinished() || player != model.getPlayer()) return;
        
        int bestX = -1, bestY = -1, maxFlips = -1;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (isValidMove(player, x, y)) {
                    int flips = getFlips(player, x, y);
                    if (flips > maxFlips) {
                        maxFlips = flips;
                        bestX = x;
                        bestY = y;
                    }
                }
            }
        }
        if (bestX != -1) {
            makeMove(player, bestX, bestY);
            model.setPlayer(3 - player);
            update();
            view.refreshView();
        }
    }

    private boolean isValidMove(int player, int x, int y) {
        if (model.getBoardContents(x, y) != 0) return false;
        int opponent = (player == 1) ? 2 : 1;
        int[][] dirs = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};
        
        for (int[] d : dirs) {
            int cx = x + d[0], cy = y + d[1];
            boolean foundOpponent = false;
            while (cx >= 0 && cx < 8 && cy >= 0 && cy < 8 && model.getBoardContents(cx, cy) == opponent) {
                foundOpponent = true;
                cx += d[0];
                cy += d[1];
            }
            if (foundOpponent && cx >= 0 && cx < 8 && cy >= 0 && cy < 8 && model.getBoardContents(cx, cy) == player) {
                return true;
            }
        }
        return false;
    }

    private int getFlips(int player, int x, int y) {
        if (model.getBoardContents(x, y) != 0) return 0;
        int opponent = (player == 1) ? 2 : 1;
        int[][] dirs = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};
        int totalFlips = 0;
        
        for (int[] d : dirs) {
            int cx = x + d[0], cy = y + d[1];
            int currentDirectionFlips = 0;
            while (cx >= 0 && cx < 8 && cy >= 0 && cy < 8 && model.getBoardContents(cx, cy) == opponent) {
                currentDirectionFlips++;
                cx += d[0];
                cy += d[1];
            }
            if (currentDirectionFlips > 0 && cx >= 0 && cx < 8 && cy >= 0 && cy < 8 && model.getBoardContents(cx, cy) == player) {
                totalFlips += currentDirectionFlips;
            }
        }
        return totalFlips;
    }

    private void makeMove(int player, int x, int y) {
        model.setBoardContents(x, y, player);
        int opponent = (player == 1) ? 2 : 1;
        int[][] dirs = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};
        
        for (int[] d : dirs) {
            int cx = x + d[0], cy = y + d[1];
            int currentDirectionFlips = 0;
            while (cx >= 0 && cx < 8 && cy >= 0 && cy < 8 && model.getBoardContents(cx, cy) == opponent) {
                currentDirectionFlips++;
                cx += d[0];
                cy += d[1];
            }
            if (currentDirectionFlips > 0 && cx >= 0 && cx < 8 && cy >= 0 && cy < 8 && model.getBoardContents(cx, cy) == player) {
                int fx = x + d[0], fy = y + d[1];
                while (fx != cx || fy != cy) {
                    model.setBoardContents(fx, fy, player);
                    fx += d[0];
                    fy += d[1];
                }
            }
        }
    }
}