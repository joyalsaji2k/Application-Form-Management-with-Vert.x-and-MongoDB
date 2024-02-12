package ie.test;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

// ... (imports)

public class Starter {
    private final Vertx vertx = Vertx.vertx();
    private final HttpServer server = vertx.createHttpServer();
    private final Router router = Router.router(vertx);

    public static void main(String[] args) {
        Starter starter = new Starter();
        starter.bootStrapApplication();
    }

    private void bootStrapApplication() {
        MongoClient mongoClient = getMongoClient();
        MongoDatabase mongoDatabase = getMongoDatabase(mongoClient);

        router.route(HttpMethod.POST, "/api/createuser")
                .handler(BodyHandler.create())
                .blockingHandler(new CreateHandler(mongoClient, mongoDatabase));
//        router.route(HttpMethod.POST, "/api/login/api/login")
//                .handler(BodyHandler.create())
//                .blockingHandler(new LoginHandler(mongoClient, mongoDatabase));

        router.route(HttpMethod.GET, "/api/getall")
            .handler(new AggregationHandler(mongoClient, mongoDatabase));


        router.route(HttpMethod.PUT, "/api/update/:id")
                .handler(BodyHandler.create())
                .blockingHandler(new UpdateHandler(mongoClient, mongoDatabase));




        server.requestHandler(router).listen(8080);
        System.out.println("Server listens on port 8080");
    }

    public MongoClient getMongoClient() {
        return MongoClients.create("mongodb://localhost:27017/form");
    }

    public MongoDatabase getMongoDatabase(MongoClient mongoClient) {
        return mongoClient.getDatabase("form");
    }
}
