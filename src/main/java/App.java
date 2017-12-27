import domain.Product;
import domain.Transaction;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import spark.ModelAndView;

import javax.persistence.TypedQuery;
import java.util.HashMap;

import static spark.Spark.get;

public class App {
    private static final SessionFactory ourSessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            ourSessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    public static void main(String[] args) {
        get("/", ((request, response) -> "Hello!"));

        get("/order", (request, response) -> {
            HashMap<String, String> model = new HashMap<>();
            String productName = request.queryParams("name");
            String quantity = request.queryParams("quantity");
            if (productName != null && quantity != null && notEmpty(productName, quantity)) {
                addOrder(quantity, productName);
                model.put("template", "templates/orderedForm.vtl");
            } else {
                model.put("template", "templates/addOrderForm.vtl");
            }
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());
    }

    private static boolean notEmpty(String productName, String quantity) {
        return !productName.isEmpty() && !quantity.isEmpty();
    }

    private static void addOrder(String quantity, String productName) {
        Session session = getSession();
        org.hibernate.Transaction transaction = session.beginTransaction();

        TypedQuery<Product> query = session.createQuery("from Product where productName=:productName", Product.class);
        query.setParameter("productName", productName);
        Product product = query.getSingleResult();

        Transaction transaction1 = new Transaction(Integer.parseInt(quantity));
        transaction1.addProduct(product);

        session.save(transaction1);

        transaction.commit();
        session.close();
    }
}
