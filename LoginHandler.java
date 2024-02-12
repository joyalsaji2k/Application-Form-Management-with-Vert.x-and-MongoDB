package ie.test;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.bson.Document;

public class LoginHandler implements Handler<RoutingContext> {

    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;

    public LoginHandler(MongoClient mongoClient, MongoDatabase mongoDatabase) {
        this.mongoClient = mongoClient;
        this.mongoDatabase = mongoDatabase;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();

        JsonObject loginData = routingContext.body().asJsonObject();

        routingContext.vertx().executeBlocking(promise -> {
            try {
                MongoCollection<Document> collection = mongoDatabase.getCollection("students");

                // Check if the user with provided email and password exists
                Document query = new Document("email", loginData.getString("email"))
                        .append("otp", loginData.getString("otp")); // Assuming OTP is used for login

                Document user = collection.find(query).first();

                promise.complete(user);
            } catch (Exception e) {
                e.printStackTrace();
                promise.fail(e);
            }
        }, result -> {
            if (result.succeeded()) {
                Document user = (Document) result.result();

                if (user != null) {
                    response.send("Login successful.");
                } else {
                    response.send("Invalid email or OTP.");
                }
            } else {
                response.send("Login failed.");
            }
        });
    }
}
