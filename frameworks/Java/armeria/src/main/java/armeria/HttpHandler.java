package armeria;

import static com.linecorp.armeria.common.HttpHeaderNames.CONTENT_LENGTH;
import static com.linecorp.armeria.common.HttpHeaderNames.DATE;
import static com.linecorp.armeria.common.HttpHeaderNames.SERVER;
import static com.linecorp.armeria.common.HttpStatus.OK;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.linecorp.armeria.common.HttpData;
import com.linecorp.armeria.common.HttpHeaders;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.ResponseConverter;

import io.netty.util.concurrent.FastThreadLocal;

public class HttpHandler {
    private static final FastThreadLocal<DateFormat> FORMAT = new FastThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        }
    };

    private static final MediaType PLAIN_TEXT = MediaType.create("text", "plain");
    private static final HttpData STATIC_PLAINTEXT = HttpData.ofAscii("Hello, World!");
    private static volatile String date = FORMAT.get().format(new Date());

    public static void updateDate() {
        date = FORMAT.get().format(new Date());
    }

    public static HttpResponse makeResponse(MediaType mediaType, HttpData data) {
        return HttpResponse.of(HttpHeaders.of(OK)
                                          .contentType(mediaType)
                                          .addLong(CONTENT_LENGTH, data.length())
                                          .add(SERVER, "Armeria")
                                          .add(DATE, date),
                               data);
    }

    @Get("/plaintext")
    public HttpResponse plaintext() {
        return makeResponse(PLAIN_TEXT, STATIC_PLAINTEXT);
    }

    @Get("/json")
    @ResponseConverter(MessageResponseConverter.class)
    public Message json() {
        return new Message("Hello, World!");
    }
}
