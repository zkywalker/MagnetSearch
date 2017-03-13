package org.zky.tool.magnetsearch.utils.http;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import static okhttp3.internal.Util.UTF_8;

/**
 *
 * Created by zhangkun on 2017/3/8.
 */

final class CusGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final TypeAdapter<T> adapter;
    private final Gson gson;

    CusGsonResponseBodyConverter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
        this.gson = new Gson();
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        //get the response string data
        String response = value.string();

        //in there,you can check your data
        // for exp,you can catch the exception and return the entity with error flag
        //or just return the gson entity,like this:
        MediaType contentType = value.contentType();
        Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
        Reader reader = new InputStreamReader(inputStream, charset);
        JsonReader jsonReader = gson.newJsonReader(reader);

        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}