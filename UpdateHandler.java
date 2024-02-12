package ie.test;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.stream.Collectors;

public class UpdateHandler implements Handler<RoutingContext> {
    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;

    public UpdateHandler(MongoClient mongoClient, MongoDatabase mongoDatabase) {
        this.mongoClient = mongoClient;
        this.mongoDatabase = mongoDatabase;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();

        String documentId = routingContext.request().getParam("id");
        System.out.println("Document ID to update: " + documentId);




        try {
            ObjectId objectId = new ObjectId(documentId);
            JsonObject updateData = routingContext.getBodyAsJson();

            System.out.println("Update Data: " + updateData.encodePrettily());

            if (documentId == null || documentId.isEmpty()) {
                response.end("Document ID is required.");
                return;
            }

            if (updateData == null || updateData.isEmpty()) {
                response.end("Update data is required.");
                return;
            }

            routingContext.vertx().executeBlocking(promise -> {
                try {
                    MongoCollection<Document> applicationFormCollection = mongoDatabase.getCollection("application_form");

                    Bson filter = Filters.eq("_id", objectId);

                    Bson update = Updates.combine(updateData.getMap().entrySet().stream()
                            .map(entry -> Updates.set(entry.getKey(), entry.getValue()))
                            .collect(Collectors.toList()));

                    UpdateResult updateResult = applicationFormCollection.updateOne(filter, update);

                    promise.complete(updateResult.wasAcknowledged());
                } catch (Exception e) {
                    e.printStackTrace();
                    promise.fail(e);
                }
            }, result -> {
                if (result.succeeded()) {
                    response.end("Update operation successful.");
                } else {
                    System.out.println("Exception happened because of the following issue: " + result.cause().getMessage());
                    response.end("Update operation failed.");
                }
            });
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            response.end("Invalid document ID format.");
        }
    }
}
