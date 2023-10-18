package my.test.rest.incomings.controllers.presenters;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

public class JsonbSpy implements Jsonb {

    private final String toResult;
    private Object toJsonParam;

    public JsonbSpy(String toResult) {
        this.toResult = toResult;
    }

    @Override
    public <T> T fromJson(String str, Class<T> type) throws JsonbException {
        return null;
    }

    @Override
    public <T> T fromJson(String str, Type runtimeType) throws JsonbException {
        return null;
    }

    @Override
    public <T> T fromJson(Reader reader, Class<T> type) throws JsonbException {
        return null;
    }

    @Override
    public <T> T fromJson(Reader reader, Type runtimeType) throws JsonbException {
        return null;
    }

    @Override
    public <T> T fromJson(InputStream stream, Class<T> type) throws JsonbException {
        return null;
    }

    @Override
    public <T> T fromJson(InputStream stream, Type runtimeType) throws JsonbException {
        return null;
    }

    @Override
    public String toJson(Object object) throws JsonbException {
        this.toJsonParam = object;
        return toResult;
    }

    public Object toJsonParam() {
        return toJsonParam;
    }

    @Override
    public String toJson(Object object, Type runtimeType) throws JsonbException {
        return null;
    }

    @Override
    public void toJson(Object object, Writer writer) throws JsonbException {

    }

    @Override
    public void toJson(Object object, Type runtimeType, Writer writer) throws JsonbException {

    }

    @Override
    public void toJson(Object object, OutputStream stream) throws JsonbException {

    }

    @Override
    public void toJson(Object object, Type runtimeType, OutputStream stream) throws JsonbException {

    }

    @Override
    public void close() throws Exception {

    }
}
