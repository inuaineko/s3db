package com.sleazyweasel.s3db;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.sleazyweasel.s3db.storage.S3Store;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.UUID;

class PostWithoutKey implements Route {
    private final S3Store s3Store;

    @Inject
    PostWithoutKey(S3Store s3Store) {
        this.s3Store = s3Store;
    }

    @Override
    public Object handle(Request request, Response response) {
        String collection = request.params(":collection");
        String id = UUID.randomUUID().toString();
        String body = request.body();
        byte[] bytes = body.getBytes(Charsets.UTF_8);

        s3Store.putObject(collection, id, bytes, request.contentType());

        response.type("application/json");
        return new Gson().toJson(CreationResponse.build(request, collection, id));
    }

}
