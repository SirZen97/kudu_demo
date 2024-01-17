package org.example;

import org.apache.kudu.client.KuduClient;
import org.apache.kudu.client.KuduException;

public class DropTable {
    public static void main(String[] args) throws KuduException {
        KuduClient kuduClient = KuduFactory.getKuduClient("118.195.133.251:7051,118.195.133.251:7151,118.195.133.251:7251");
        try {
            kuduClient.deleteTable("Test");
        }finally {
            kuduClient.close();
        }
    }
}
