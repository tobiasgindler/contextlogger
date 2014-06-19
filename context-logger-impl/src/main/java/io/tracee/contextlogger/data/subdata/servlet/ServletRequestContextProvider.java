package io.tracee.contextlogger.data.subdata.servlet;

import io.tracee.contextlogger.api.TraceeContextProvider;
import io.tracee.contextlogger.api.TraceeContextProviderMethod;
import io.tracee.contextlogger.api.WrappedContextData;
import io.tracee.contextlogger.data.Order;
import io.tracee.contextlogger.data.subdata.NameObjectValuePair;
import io.tracee.contextlogger.data.subdata.NameStringValuePair;
import io.tracee.contextlogger.profile.ProfilePropertyNames;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Context provider for HttpServletRequest.
 * Created by Tobias Gindler, holisticon AG on 17.03.14.
 */

@TraceeContextProvider(displayName = "servletRequest", order = Order.SERVLET)
public final class ServletRequestContextProvider implements WrappedContextData<HttpServletRequest> {


    private HttpServletRequest request;

    public ServletRequestContextProvider() {

    }

    public ServletRequestContextProvider(final HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void setContextData(Object instance) throws ClassCastException {
        this.request = (HttpServletRequest) instance;
    }

    @Override
    public Class<HttpServletRequest> getWrappedType() {
        return HttpServletRequest.class;
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "url",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_URL,
            order = 10)
    public String getUrl() {
        if (this.request != null && request.getRequestURL() != null) {
            return request.getRequestURL().toString();
        }
        return null;
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "http-method",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_HTTP_METHOD,
            order = 20)
    public String getHttpMethod() {
        if (this.request != null) {
            return this.request.getMethod();
        }
        return null;
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "http-parameters",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_PARAMETERS,
            order = 30)
    public List<NameStringValuePair> getHttpParameters() {

        final List<NameStringValuePair> list = new ArrayList<NameStringValuePair>();

        if (this.request != null) {
            final Enumeration<String> httpHeaderNamesEnum = this.request.getParameterNames();
            while (httpHeaderNamesEnum.hasMoreElements()) {

                final String httpHeaderName = httpHeaderNamesEnum.nextElement();

                final String[] values = this.request.getParameterValues(httpHeaderName);
                if (values != null) {
                    for (final String value : values) {
                        list.add(new NameStringValuePair(httpHeaderName, value));
                    }
                }

            }
        }

        return list.size() > 0 ? list : null;

    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "http-request-headers",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_HTTP_HEADER,
            order = 40)
    public List<NameStringValuePair> getHttpRequestHeaders() {

        final List<NameStringValuePair> list = new ArrayList<NameStringValuePair>();

        if (this.request != null) {
            final Enumeration<String> httpHeaderNamesEnum = this.request.getHeaderNames();
            while (httpHeaderNamesEnum.hasMoreElements()) {

                final String httpHeaderName = httpHeaderNamesEnum.nextElement();
                final String value = this.request.getHeader(httpHeaderName);
                list.add(new NameStringValuePair(httpHeaderName, value));

            }
        }

        return list.size() > 0 ? list : null;
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "http-request-attributes",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_ATTRIBUTES,
            order = 45)
    public List<NameObjectValuePair> getHttpRequestAttributes() {

        final List<NameObjectValuePair> list = new ArrayList<NameObjectValuePair>();

        if (this.request != null) {
            final Enumeration<String> attributeNames = this.request.getAttributeNames();
            while (attributeNames.hasMoreElements()) {

                final String attributeName = attributeNames.nextElement();
                final Object value = this.request.getAttribute(attributeName);
                list.add(new NameObjectValuePair(attributeName, value));

            }
        }

        return list.size() > 0 ? list : null;
    }


    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "cookies",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_COOKIES,
            order = 50)
    public List<ServletCookieContextProvider> getCookies() {

        List<ServletCookieContextProvider> wrappedCookies = new ArrayList<ServletCookieContextProvider>();

        if (this.request != null) {
            for (Cookie cookie : request.getCookies()) {
                wrappedCookies.add(new ServletCookieContextProvider(cookie));
            }
        }

        return wrappedCookies.size() > 0 ? wrappedCookies : null;
    }


    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "http-remote-address",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_REMOTE_ADDRESS,
            order = 150)
    public String getHttpRemoteAddress() {
        if (this.request != null) {
            return request.getRemoteAddr();
        }
        return null;
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "http-remote-host",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_REMOTE_HOST,
            order = 160)
    public String getHttpRemoteHost() {
        if (this.request != null) {
            return this.request.getRemoteHost();
        }
        return null;
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "http-remote-port",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_REMOTE_PORT,
            order = 170)
    public Integer getHttpRemotePort() {
        if (this.request != null) {
            return this.request.getRemotePort();
        }
        return null;
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "scheme",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_SCHEME,
            order = 200)
    public String getScheme() {
        if (this.request != null) {
            return this.request.getScheme();
        }

        return null;
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "is-secure",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_IS_SECURE,
            order = 210)
    public Boolean getSecure() {
        if (this.request != null) {
            return this.request.isSecure();
        }

        return null;
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "content-type",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_CONTENT_TYPE,
            order = 220)
    public String getContentType() {
        if (this.request != null) {
            return this.request.getContentType();
        }

        return null;
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "content-length",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_CONTENT_LENGTH,
            order = 230)
    public Integer getContentLength() {
        if (this.request != null) {
            return this.request.getContentLength();
        }
        return null;
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "locale",
            propertyName = ProfilePropertyNames.SERVLET_REQUEST_LOCALE,
            order = 240)
    public String getLocale() {
        if (this.request != null && this.request.getLocale() != null) {
            return this.request.getLocale().toString();
        }
        return null;
    }
}
