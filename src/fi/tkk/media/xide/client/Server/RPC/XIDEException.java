package fi.tkk.media.xide.client.Server.RPC;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Exception, which is used to transfer information about errors happen on the server-side to the client
 * @author evgeniasamochadina
 *
 */
public class XIDEException extends Throwable
        implements IsSerializable
{
    String message;

    public XIDEException(Exception e)
    {
        message = e.getMessage();
        
    }

    public XIDEException()
    {
    }

    public XIDEException(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public String getUserFriendlyMessage(String operation) {
    	return "Unfortunately " + operation + " has failed on a server. The following error returned: \n" + message;
    }
    public void setMessage(String message)
    {
        this.message = message;
    }
}

