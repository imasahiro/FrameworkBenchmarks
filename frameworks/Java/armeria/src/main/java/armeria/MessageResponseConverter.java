package armeria;

import static armeria.HttpHandler.makeResponse;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;

import com.linecorp.armeria.common.HttpData;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.linecorp.armeria.server.annotation.ResponseConverterFunction;

class MessageResponseConverter implements ResponseConverterFunction {
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new AfterburnerModule());

    @Override
    public HttpResponse convertResponse(ServiceRequestContext ctx,
                                        @Nullable Object result) throws Exception {
        byte[] json = objectMapper.writeValueAsBytes(result);
        return makeResponse(MediaType.JSON, HttpData.of(json));
    }
}
