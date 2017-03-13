package org.zky.tool.magnetsearch.utils.http;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;


/**
 *
 * Created by zhangkun on 2017/3/8.
 */

public class CusGsonConverterFactory extends Converter.Factory {

    public static CusGsonConverterFactory create() {
        return create(new Gson());
    }

    public static CusGsonConverterFactory create(Gson gson) {
        return new CusGsonConverterFactory(gson);
    }

    private final Gson gson;

    private CusGsonConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new CusGsonResponseBodyConverter<>(adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new CusGsonRequestBodyConverter<>(gson, adapter);
    }
}
