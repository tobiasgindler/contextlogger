package de.holisticon.util.tracee.contextlogger.jaxws.container;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeLogger;
import de.holisticon.util.tracee.contextlogger.ImplicitContext;
import de.holisticon.util.tracee.contextlogger.builder.TraceeContextLogger;
import de.holisticon.util.tracee.contextlogger.data.wrapper.JaxWsWrapper;
import de.holisticon.util.tracee.jaxws.AbstractTraceeHandler;
import de.holisticon.util.tracee.jaxws.container.TraceeServerHandler;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.util.Set;

/**
 * JaxWs container side handler that detects uncaught exceptions and outputs contextual informations.
 *
 * @author Tobias Gindler (Holisticon AG)
 */
public class TraceeServerErrorLoggingHandler extends AbstractTraceeHandler {

    private final TraceeLogger traceeLogger = this.getTraceeBackend().getLoggerFactory().getLogger(
			TraceeServerHandler.class);

    private static final ThreadLocal<String> THREAD_LOCAL_SOAP_MESSAGE_STR = new ThreadLocal<String>();

	TraceeServerErrorLoggingHandler(TraceeBackend traceeBackend) {
		super(traceeBackend);
	}

	public TraceeServerErrorLoggingHandler() {
		this(Tracee.getBackend());
	}

	@Override
    public final boolean handleFault(SOAPMessageContext context) {

        // Must pipe out Soap envelope
        SOAPMessage soapMessage = context.getMessage();

        TraceeContextLogger.createDefault().logJsonWithPrefixedMessage(
                "TRACEE JMS ERROR CONTEXT LISTENER",
                ImplicitContext.COMMON,
                ImplicitContext.TRACEE,
                JaxWsWrapper.wrap(THREAD_LOCAL_SOAP_MESSAGE_STR.get(),
                        getSoapMessageAsString(soapMessage)));

        // cleanup thread local request soap message
        THREAD_LOCAL_SOAP_MESSAGE_STR.remove();

        return true;

    }

    /**
     * Converts a SOAPMessage instance to string representation.
     */
    private String getSoapMessageAsString(SOAPMessage soapMessage) {

        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            soapMessage.writeTo(os);
            return new String(os.toByteArray(), "UTF-8");
        } catch (Exception e) {
            return "ERROR";
        }
    }


    @Override
    protected final void handleIncoming(SOAPMessageContext context) {
        // Save soap request message in thread local storage for error logging
        SOAPMessage soapMessage = context.getMessage();
        if (soapMessage != null) {
            String soapMessageAsString = getSoapMessageAsString(soapMessage);
            THREAD_LOCAL_SOAP_MESSAGE_STR.set(soapMessageAsString);
        }

    }

    @Override
    protected final void handleOutgoing(SOAPMessageContext context) {
        // Do nothing
    }


    @Override
    public final Set<QName> getHeaders() {
        return null;
    }

}
