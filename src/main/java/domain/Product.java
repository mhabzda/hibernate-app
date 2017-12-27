package domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int productID;

    private String productName;
    private int unitsInStock;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<Transaction> transactions;

    public Product(String productName, int unitsInStock) {
        this.productName = productName;
        this.unitsInStock = unitsInStock;
        transactions = new HashSet<>();
    }

    public Product() {
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.getProducts().add(this);
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public String getProductName() {
        return productName;
    }

    public int getUnitsInStock() {
        return unitsInStock;
    }


    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setUnitsInStock(int unitsInStock) {
        this.unitsInStock = unitsInStock;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productID=" + productID +
                ", productName='" + productName + '\'' +
                ", unitsInStock=" + unitsInStock +
                '}';
    }
}

