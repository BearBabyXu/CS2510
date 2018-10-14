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
    public boolean SendWriteRequest(String target, int quantity);

    /**
     *
     * @param requestor ID
     * @param messagetype
     * @param update data content
     * @return finished Request object
     */
    public boolean SendReadRequest(String target);

    public boolean Send(ClientRequest request);
}
