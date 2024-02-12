package ie.test;// ... (existing imports)
import com.mongodb.client.AggregateIterable;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AggregationHandler implements Handler<RoutingContext> {
    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;

    public AggregationHandler(MongoClient mongoClient, MongoDatabase mongoDatabase) {
        this.mongoClient = mongoClient;
        this.mongoDatabase = mongoDatabase;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();

        routingContext.vertx().executeBlocking(promise -> {
            try {
                // Use the MongoDB Aggregation Framework to retrieve joined data
                MongoCollection<Document> applicationFormCollection = mongoDatabase.getCollection("application_form");

                List<Bson> pipeline = Arrays.asList(
                        Aggregates.lookup("applicant", "applicant_id", "_id", "application_formcant_details"),
                        Aggregates.lookup("parental", "parental_id", "_id", "parental_details")
                );

                AggregateIterable<Document> aggregationResult = applicationFormCollection.aggregate(pipeline);

                List<Document> joinedData = new ArrayList<>();
                for (Document document : aggregationResult) {
                    joinedData.add(document);
                }

                // Complete the promise with the joined data
                promise.complete(joinedData);
            } catch (Exception e) {
                // Handle exceptions
                e.printStackTrace(); // Print stack trace for debugging
                promise.fail(e);
            }
        }, result -> {
            if (result.succeeded()) {
                List<Document> joinedData = (List<Document>) result.result();
                response.end("Joined Data: " + joinedData);
            } else {
                System.out.println("Exception happened because of the following issue: " + result.cause().getMessage());
                response.end("Aggregation failed.");
            }
        });
    }
}
