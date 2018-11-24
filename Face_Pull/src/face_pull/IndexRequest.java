/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

/**
 *
 * @author Egan
 */
public class IndexRequest {

    private String dir;

    public IndexRequest(String dir) {
        this.dir = dir;
    }

    public String getRequest() {
        return this.dir;

    }

}
