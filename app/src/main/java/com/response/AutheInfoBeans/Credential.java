package com.response.AutheInfoBeans;

/**
 *     "credential":{
 *         "head":{},
 *         "load":{},
 *         "serverSign":{},
 *         "clientSign":{}
 *     }
 *     Created by xh.w on 2020.10.20
 */
public class Credential {

    private Head head;
    private Load load;
    private ServerSign serverSign;
    private ClientSign clientSign;

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public Load getLoad() {
        return load;
    }

    public void setLoad(Load load) {
        this.load = load;
    }

    public ServerSign getServerSign() {
        return serverSign;
    }

    public void setServerSign(ServerSign serverSign) {
        this.serverSign = serverSign;
    }

    public ClientSign getClientSign() {
        return clientSign;
    }

    public void setClientSign(ClientSign clientSign) {
        this.clientSign = clientSign;
    }
}
