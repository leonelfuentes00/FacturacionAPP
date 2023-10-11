package ExceptionsManage;

public class StockInsuficienteException extends Exception {
    private int stockActual;

    public StockInsuficienteException(String message, int stockActual) {
        super(message);
        this.stockActual = stockActual;
    }

    public int getStockActual() {
        return stockActual;
    }
}


