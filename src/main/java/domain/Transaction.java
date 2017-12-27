package domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int transactionID;

    private int quantity;

    @ManyToMany(mappedBy = "transactions", cascade = CascadeType.PERSIST)
    private Set<Product> products;

    public Transaction(int quantity) {
        this.quantity = quantity;
        products = new HashSet<>();
    }

    public Transaction() {
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
        product.getTransactions().add(this);
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionID=" + transactionID +
                ", quantity=" + quantity +
                '}';
    }
}
