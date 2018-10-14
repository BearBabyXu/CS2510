package Client;

import Request.ClientRequest;

public interface ClientInterface {

    /**
     *
     * @param requestor ID
     * @param messagetype
     * @param update data content
     * @return finished Request object
     */
    public boolean WriteRequest(int quantity, String ticket);

    /**
     *
     * @param requestor ID
     * @param messagetype
     * @param update data content
     * @return finished Request object
     */
    public boolean ReadRequest();

    public boolean Send(ClientRequest request);
}
