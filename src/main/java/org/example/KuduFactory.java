package org.example;

import org.apache.kudu.client.KuduClient;
import org.apache.kudu.client.KuduException;

public class KuduFactory {
  private static volatile KuduClient kuduClient;
  private String tableName;

  public static KuduClient getKuduClient(String masterAddresses){
    if(kuduClient == null){
      synchronized (KuduFactory.class){
        if(kuduClient==null){
          KuduClient.KuduClientBuilder kuduClientBuilder = new KuduClient.KuduClientBuilder(masterAddresses);
          kuduClientBuilder.defaultAdminOperationTimeoutMs(10000);
          kuduClient = kuduClientBuilder.build();
        }
      }
    }
    return kuduClient;
  }
  public static void closeKudu(){
    if (kuduClient!=null){
      synchronized (KuduFactory.class){
        if(kuduClient!=null){
          try {
            kuduClient.close();
          } catch (KuduException e) {
            throw new RuntimeException(e);
          }finally {
            kuduClient = null;
          }
        }
      }
    }
  }
}
