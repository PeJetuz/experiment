package my.test.rest.incomings.controllers;

import io.helidon.common.http.AlreadyCompletedException;
import io.helidon.common.http.DataChunk;
import io.helidon.common.http.Http.ResponseStatus;
import io.helidon.common.http.MediaType;
import io.helidon.common.reactive.Single;
import io.helidon.media.common.MessageBodyFilter;
import io.helidon.media.common.MessageBodyStreamWriter;
import io.helidon.media.common.MessageBodyWriter;
import io.helidon.media.common.MessageBodyWriterContext;
import io.helidon.webserver.ResponseHeaders;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.WebServer;
import java.util.concurrent.Flow.Publisher;
import java.util.function.Function;
import java.util.function.Predicate;

public class ServerResponseSpy implements ServerResponse {

    private String content;
    private ResponseStatus status;

    public int sendCount = 0;

    public String content() {
        return this.content;
    }

    @Override
    public WebServer webServer() {
        return null;
    }

    @Override
    public ResponseStatus status() {
        return status;
    }

    @Override
    public ServerResponse status(ResponseStatus status) throws AlreadyCompletedException, NullPointerException {
        this.status = status;
        return this;
    }

    @Override
    public ResponseHeaders headers() {
        return null;
    }

    @Override
    public MessageBodyWriterContext writerContext() {
        return null;
    }

    @Override
    public Void send(Throwable content) {
        return null;
    }

    @Override
    public <T> Single<ServerResponse> send(T content) {
        this.content = (String) content;
        return null;
    }

    @Override
    public <T> Single<ServerResponse> send(Publisher<T> content, Class<T> clazz) {
        return null;
    }

    @Override
    public Single<ServerResponse> send(Publisher<DataChunk> content) {
        return null;
    }

    @Override
    public Single<ServerResponse> send(Publisher<DataChunk> content, boolean applyFilters) {
        return null;
    }

    @Override
    public Single<ServerResponse> send(Function<MessageBodyWriterContext, Publisher<DataChunk>> function) {
        return null;
    }

    @Override
    public Single<ServerResponse> send() {
        sendCount++;
        return null;
    }

    @Override
    public <T> ServerResponse registerWriter(Class<T> type, Function<T, Publisher<DataChunk>> function) {
        return null;
    }

    @Override
    public <T> ServerResponse registerWriter(Class<T> type, MediaType contentType,
            Function<? extends T, Publisher<DataChunk>> function) {
        return null;
    }

    @Override
    public <T> ServerResponse registerWriter(Predicate<?> accept, Function<T, Publisher<DataChunk>> function) {
        return null;
    }

    @Override
    public <T> ServerResponse registerWriter(Predicate<?> accept, MediaType contentType,
            Function<T, Publisher<DataChunk>> function) {
        return null;
    }

    @Override
    public ServerResponse registerFilter(Function<Publisher<DataChunk>, Publisher<DataChunk>> function) {
        return null;
    }

    @Override
    public ServerResponse registerFilter(MessageBodyFilter filter) {
        return null;
    }

    @Override
    public ServerResponse registerWriter(MessageBodyWriter<?> writer) {
        return null;
    }

    @Override
    public ServerResponse registerWriter(MessageBodyStreamWriter<?> writer) {
        return null;
    }

    @Override
    public Single<ServerResponse> whenSent() {
        return null;
    }

    @Override
    public long requestId() {
        return 0;
    }
}
