package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        var possibleMoves = new java.util.ArrayList<ChessMove>();

        switch(board.getPiece(myPosition).getPieceType()) {
            case PieceType.PAWN -> {
                PieceType[] promotionType = {PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP, PieceType.QUEEN};
                int direction;
                int startRow;
                int promotionRow;
                int row = myPosition.getRow();
                int col = myPosition.getColumn();
                if (this.getTeamColor() == ChessGame.TeamColor.BLACK) {
                    direction = -1;
                    startRow = 7;
                    promotionRow = 2;
                } else {
                    direction = 1;
                    startRow = 2;
                    promotionRow = 7;
                }
                boolean canPromote = row == promotionRow;

                //move forward
                var newPos = new ChessPosition(row + direction, col);
                var target = board.getPiece(newPos);
                if (target == null) {
                    if (canPromote) {
                        for (PieceType type : promotionType) {
                            possibleMoves.add(new ChessMove(myPosition, newPos, type));
                        }
                    } else {
                        possibleMoves.add(new ChessMove(myPosition, newPos, null));
                        if (row == startRow) {
                            var newPos1 = new ChessPosition(row + 2 * direction, col);
                            var target1 = board.getPiece(newPos1);
                            if (target1 == null) {
                                possibleMoves.add(new ChessMove(myPosition, newPos1, null));
                            }
                        }
                    }
                }
                //capture
                int[] capturePiece = {-1, 1};
                for (int captureDirection : capturePiece) {
                    int newRow = row + direction;
                    int newCol = col + captureDirection;
                    if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                        var newPos2 = new ChessPosition(newRow, newCol);
                        var target2 = board.getPiece(newPos2);
                        if (target2 != null) {
                            if (canPromote) {
                                for (PieceType type : promotionType) {
                                    possibleMoves.add(new ChessMove(myPosition, newPos2, type));
                                }
                            } else {
                                if (target2.getTeamColor() != this.getTeamColor()) {
                                    possibleMoves.add(new ChessMove(myPosition, newPos2, null));
                                }
                            }
                        }
                    }
                }

            }
            case PieceType.ROOK -> {
                int[] rowDirections = {-1, 0, 1, 0};
                int[] colDirections = {0, -1, 0, 1};
                for (int d = 0; d < rowDirections.length; d++) {
                    int newRow = myPosition.getRow();
                    int newCol = myPosition.getColumn();
                    int rowDir = rowDirections[d];
                    int colDir = colDirections[d];
                    while (true) {
                        newRow += rowDir;
                        newCol += colDir;
                        if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) break;
                        var newPos = new ChessPosition(newRow, newCol);
                        var target = board.getPiece(newPos);
                        if (target == null) {
                            possibleMoves.add(new ChessMove(myPosition, newPos, null));
                        } else {
                            if (target.getTeamColor() != this.getTeamColor()) {
                                possibleMoves.add(new ChessMove(myPosition, newPos, null));
                            }
                            break;
                        }
                    }
                }
            }
            case PieceType.KNIGHT -> {
                int[] rowDirections = {2, 1, -1, -2, 2, 1, -1, -2};
                int[] colDirections = {1, 2, 2, 1, -1, -2, -2, -1};
                for (int d = 0; d < rowDirections.length; d++) {
                    int newRow = myPosition.getRow() + rowDirections[d];
                    int newCol = myPosition.getColumn() + colDirections[d];
                    if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                        var newPos = new ChessPosition(newRow, newCol);
                        var target = board.getPiece(newPos);
                        if (target == null) {
                            possibleMoves.add(new ChessMove(myPosition, newPos, null));
                        } else {
                            if (target.getTeamColor() != this.getTeamColor()) {
                                possibleMoves.add(new ChessMove(myPosition, newPos, null));
                            }
                        }
                    }
                }
            }
            case PieceType.BISHOP -> {
                int[] rowDirections = {-1, -1, 1, 1};
                int[] colDirections = {-1, 1, -1, 1};
                for (int d = 0; d < rowDirections.length; d++) {
                    int newRow = myPosition.getRow();
                    int newCol = myPosition.getColumn();
                    int rowDir = rowDirections[d];
                    int colDir = colDirections[d];
                    while (true) {
                        newRow += rowDir;
                        newCol += colDir;
                        if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) break;
                        var newPos = new ChessPosition(newRow, newCol);
                        var target = board.getPiece(newPos);
                        if (target == null) {
                            possibleMoves.add(new ChessMove(myPosition, newPos, null));
                        } else {
                            if (target.getTeamColor() != this.getTeamColor()) {
                                possibleMoves.add(new ChessMove(myPosition, newPos, null));
                            }
                            break;
                        }
                    }
                }
            }
            case PieceType.QUEEN -> {
                int[] rowDirections = {-1, 0, 1, 0, -1, -1, 1, 1};
                int[] colDirections = {0, -1, 0, 1, -1, 1, -1, 1};
                for (int d = 0; d < rowDirections.length; d++) {
                    int newRow = myPosition.getRow();
                    int newCol = myPosition.getColumn();
                    int rowDir = rowDirections[d];
                    int colDir = colDirections[d];
                    while (true) {
                        newRow += rowDir;
                        newCol += colDir;
                        if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) break;
                        var newPos = new ChessPosition(newRow, newCol);
                        var target = board.getPiece(newPos);
                        if (target == null) {
                            possibleMoves.add(new ChessMove(myPosition, newPos, null));
                        } else {
                            if (target.getTeamColor() != this.getTeamColor()) {
                                possibleMoves.add(new ChessMove(myPosition, newPos, null));
                            }
                            break;
                        }
                    }
                }
            }
            case PieceType.KING -> {
                int[] rowDirections = {-1, 0, 1, 0, -1, -1, 1, 1};
                int[] colDirections = {0, -1, 0, 1, -1, 1, -1, 1};
                for (int d = 0; d < rowDirections.length; d++) {
                    int newRow = myPosition.getRow() + rowDirections[d];
                    int newCol = myPosition.getColumn() + colDirections[d];
                    if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                        var newPos = new ChessPosition(newRow, newCol);
                        var target = board.getPiece(newPos);
                        if (target == null) {
                            possibleMoves.add(new ChessMove(myPosition, newPos, null));
                        } else {
                            if (target.getTeamColor() != this.getTeamColor()) {
                                possibleMoves.add(new ChessMove(myPosition, newPos, null));
                            }
                        }
                    }
                }
            }
        }
        return possibleMoves;
    }
}
