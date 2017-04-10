package org.zky.tool.magnetsearch.search;

import org.zky.tool.magnetsearch.search.factory.SearchSource;
import org.zky.tool.magnetsearch.utils.http.StringConverterFactory;
import org.zky.tool.magnetsearch.utils.http.StringRequestBodyConverter;
import org.zky.tool.magnetsearch.utils.http.StringResponseBodyConverter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 *
 * Created by zhangkun on 2017/2/21.
 */

public class SearchConverterFactory extends Converter.Factory {

    private SearchSource mSearchSource;

    public static SearchConverterFactory create(SearchSource searchSource) {
        return new SearchConverterFactory(searchSource);
    }

    private SearchConverterFactory(SearchSource searchSource) {
        mSearchSource =searchSource;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        return new SearchResponseBodyConverter(mSearchSource);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new StringRequestBodyConverter();
    }
}
