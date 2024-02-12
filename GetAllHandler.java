package ie.test;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

// ... (existing imports)

public class GetAllHandler implements Handler<RoutingContext> {
    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;

    public GetAllHandler(MongoClient mongoClient, MongoDatabase mongoDatabase) {
        this.mongoClient = mongoClient;
        this.mongoDatabase = mongoDatabase;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();

        routingContext.vertx().executeBlocking(promise -> {
            try {
                MongoCollection<Document> applicationFormCollection = mongoDatabase.getCollection("application_form");

                FindIterable<Document> findResult = applicationFormCollection.find();

                List<Document> resultDocuments = new ArrayList<>();
                for (Document document : findResult) {
                    resultDocuments.add(document);
                }

                promise.complete(resultDocuments);
            } catch (Exception e) {
                e.printStackTrace();
                promise.fail(e);
            }
        }, result -> {
            if (result.succeeded()) {
                List<Document> getAllData = (List<Document>) result.result();
                response.end(Json.encodePrettily(getAllData));
            } else {
                System.out.println("Exception happened because of the following issue: " + result.cause().getMessage());
                response.end("Get all operation failed.");
            }
        });
    }
}
