package org.example;

import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Schema;
import org.apache.kudu.Type;
import org.apache.kudu.client.CreateTableOptions;
import org.apache.kudu.client.KuduClient;
import org.apache.kudu.client.KuduException;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateTable {
    public static void main(String[] args) throws KuduException {
        String kuduTableName = "test";
        KuduClient kuduClient = KuduFactory.getKuduClient("118.195.133.251:7051,118.195.133.251:7151,118.195.133.251:7251");
        try {
            if(!kuduClient.tableExists(kuduTableName)){
                //定义表中每个字段类型
                ArrayList<ColumnSchema> columnSchemas = new ArrayList<ColumnSchema>();
                columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("id", Type.INT32).key(true).build());
                columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("name", Type.STRING).key(false).build());
                columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("age", Type.INT32).key(false).build());

                //设置所建表的额外属性
                CreateTableOptions createTableOptions = new CreateTableOptions();
                //指定用于hash分区的字段，第一个参数是按照什么字段进行分区，第二个字段表示哈希桶的数量
                createTableOptions.addHashPartitions(Arrays.asList("id"),6);

                //正式创建表
                kuduClient.createTable(kuduTableName,new Schema(columnSchemas),createTableOptions);
            }
        }finally {
            KuduFactory.closeKudu();
        }

    }
}
