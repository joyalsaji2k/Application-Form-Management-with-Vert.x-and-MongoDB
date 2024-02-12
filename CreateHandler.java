package ie.test;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.bson.Document;

public class CreateHandler implements Handler<RoutingContext> {
    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;

    public CreateHandler(MongoClient mongoClient, MongoDatabase mongoDatabase) {
        this.mongoClient = mongoClient;
        this.mongoDatabase = mongoDatabase;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();

        JsonObject data = routingContext.body().asJsonObject();

        if (data == null) {
            // Handle the case where data is null
            response.end("Data is null. Unable to process the request.");
            return;
        }



        routingContext.vertx().executeBlocking(promise -> {
            try {
                // Insert data into 'applicant' collection
                Document applicantDocument = new Document("name", data.getString("applicant_name"))
                        .append("age", data.getInteger("applicant_age"))
                        .append("address", data.getString("applicant_address"))
                        .append("post", data.getString("applicant_post"))
                        .append("gender", data.getString("applicant_gender"));

                MongoCollection<Document> applicantCollection = mongoDatabase.getCollection("applicant");
                applicantCollection.insertOne(applicantDocument);

                // Insert data into 'parental' collection
                Document parentalDocument = new Document("parent_name", data.getString("parent_name"))
                        .append("occupation", data.getString("parent_occupation"));

                MongoCollection<Document> parentalCollection = mongoDatabase.getCollection("parental");
                parentalCollection.insertOne(parentalDocument);

                // Insert data into 'application_form' collection with reference IDs
                Document applicationFormDocument = new Document("applicant_id", applicantDocument.getObjectId("_id"))
                        .append("parental_id", parentalDocument.getObjectId("_id"));

                MongoCollection<Document> applicationFormCollection = mongoDatabase.getCollection("application_form");
                applicationFormCollection.insertOne(applicationFormDocument);

                // Complete the promise with success
                promise.complete(true);
            } catch (Exception e) {
                // Handle exceptions
                e.printStackTrace(); // Print stack trace for debugging
                promise.fail(e);
            }
        }, result -> {
            if (result.succeeded()) {
                response.end("Data is inserted successfully.");
            } else {
                System.out.println("Exception happened because of the following issue: " + result.cause().getMessage());
                response.end("Data insertion is failed.");
            }
        });
    }
}
