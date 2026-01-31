import java.util.ArrayList;
import java.util.Scanner;

class Stock {
    String symbol;
    String name;
    double price;

    Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }
}

class User {
    String name;
    double cashBalance;
    ArrayList<PortfolioItem> portfolio;

    User(String name, double cashBalance) {
        this.name = name;
        this.cashBalance = cashBalance;
        this.portfolio = new ArrayList<>();
    }

    boolean buyStock(Stock stock, int quantity) {
        double totalCost = stock.price * quantity;
        if (totalCost > cashBalance) {
            System.out.println("Insufficient funds to buy " + quantity + " shares of " + stock.symbol);
            return false;
        }
        cashBalance -= totalCost;

        boolean found = false;
        for (PortfolioItem item : portfolio) {
            if (item.stock.symbol.equals(stock.symbol)) {
                item.quantity += quantity;
                found = true;
                break;
            }
        }
        if (!found) {
            portfolio.add(new PortfolioItem(stock, quantity));
        }

        System.out.println("Bought " + quantity + " shares of " + stock.symbol);
        return true;
    }

    boolean sellStock(Stock stock, int quantity) {
        for (PortfolioItem item : portfolio) {
            if (item.stock.symbol.equals(stock.symbol)) {
                if (quantity > item.quantity) {
                    System.out.println("Not enough shares to sell.");
                    return false;
                }
                item.quantity -= quantity;
                cashBalance += stock.price * quantity;
                System.out.println("Sold " + quantity + " shares of " + stock.symbol);
                if (item.quantity == 0) portfolio.remove(item);
                return true;
            }
        }
        System.out.println("You don't own any shares of " + stock.symbol);
        return false;
    }

    void displayPortfolio() {
        System.out.println("\n--- Portfolio for " + name + " ---");
        System.out.printf("%-10s %-15s %-10s %-10s\n", "Symbol", "Stock Name", "Quantity", "Value");
        double totalValue = 0;
        for (PortfolioItem item : portfolio) {
            double value = item.stock.price * item.quantity;
            totalValue += value;
            System.out.printf("%-10s %-15s %-10d %-10.2f\n", item.stock.symbol, item.stock.name, item.quantity, value);
        }
        System.out.println("Cash Balance: $" + String.format("%.2f", cashBalance));
        System.out.println("Total Portfolio Value: $" + String.format("%.2f", totalValue + cashBalance));
    }
}

class PortfolioItem {
    Stock stock;
    int quantity;

    PortfolioItem(Stock stock, int quantity) {
        this.stock = stock;
        this.quantity = quantity;
    }
}

public class StockTradingPlatform {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ArrayList<Stock> market = new ArrayList<>();
        market.add(new Stock("AAPL", "Apple Inc.", 150));
        market.add(new Stock("GOOGL", "Alphabet Inc.", 2800));
        market.add(new Stock("AMZN", "Amazon.com", 3400));
        market.add(new Stock("TSLA", "Tesla Inc.", 700));

        System.out.print("Enter your name: ");
        String userName = scanner.nextLine();
        User user = new User(userName, 10000);

        while (true) {
            System.out.println("\n--- Stock Trading Platform ---");
            System.out.println("1. View Market Stocks");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("\n--- Market Stocks ---");
                    System.out.printf("%-10s %-15s %-10s\n", "Symbol", "Name", "Price");
                    for (Stock s : market) {
                        System.out.printf("%-10s %-15s $%-10.2f\n", s.symbol, s.name, s.price);
                    }
                    break;
                case 2:
                    System.out.print("Enter stock symbol to buy: ");
                    String buySymbol = scanner.nextLine().toUpperCase();
                    Stock buyStock = findStock(market, buySymbol);
                    if (buyStock == null) {
                        System.out.println("Stock not found.");
                        break;
                    }
                    System.out.print("Enter quantity to buy: ");
                    int buyQty = scanner.nextInt();
                    scanner.nextLine();
                    user.buyStock(buyStock, buyQty);
                    break;
                case 3:
                    System.out.print("Enter stock symbol to sell: ");
                    String sellSymbol = scanner.nextLine().toUpperCase();
                    Stock sellStock = findStock(market, sellSymbol);
                    if (sellStock == null) {
                        System.out.println("Stock not found.");
                        break;
                    }
                    System.out.print("Enter quantity to sell: ");
                    int sellQty = scanner.nextInt();
                    scanner.nextLine();
                    user.sellStock(sellStock, sellQty);
                    break;
                case 4:
                    user.displayPortfolio();
                    break;
                case 5:
                    System.out.println("Exiting... Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static Stock findStock(ArrayList<Stock> market, String symbol) {
        for (Stock s : market) {
            if (s.symbol.equalsIgnoreCase(symbol)) return s;
        }
        return null;
    }
}