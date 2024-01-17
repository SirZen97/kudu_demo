package org.example;

import org.apache.kudu.client.*;

import java.util.ArrayList;

public class CRUD {
    public static void insert(KuduClient kuduClient,String tableName) throws KuduException {
        System.out.println("1. 插入十条数据");


        //使用session进行插入
        KuduSession kuduSession = kuduClient.newSession();
        kuduSession.setFlushMode(SessionConfiguration.FlushMode.AUTO_FLUSH_SYNC);

        KuduTable kuduTable = kuduClient.openTable(tableName);
        for (int i = 0; i < 10; i++) {
            Insert insert = kuduTable.newInsert();
            PartialRow row = insert.getRow();
            row.addInt("id",i);
            row.addString("name","测试员"+i);
            row.addInt("age",18+i);
            kuduSession.apply(insert);

        }
        System.out.println("*************插入完毕***************");
    }

    public static void delete(KuduClient kuduClient,String tableName) throws KuduException {
        System.out.println("3. 开始删除数据");
        KuduSession kuduSession = kuduClient.newSession();
        kuduSession.setFlushMode(SessionConfiguration.FlushMode.AUTO_FLUSH_SYNC);

        KuduTable kuduTable = kuduClient.openTable(tableName);
        Delete delete = kuduTable.newDelete();
        PartialRow deleteRow = delete.getRow();
        deleteRow.addInt("id",2);
        kuduSession.apply(delete);

        System.out.println("*************删除完毕***************");

    }

    public static void update(KuduClient kuduClient,String tableName) throws KuduException {
        System.out.println("2. 开始修改表中数据");

        KuduSession kuduSession = kuduClient.newSession();
        kuduSession.setFlushMode(SessionConfiguration.FlushMode.AUTO_FLUSH_SYNC);
        KuduTable kuduTable = kuduClient.openTable(tableName);

        Update update = kuduTable.newUpdate();
        PartialRow updateRow = update.getRow();

        //修改id为1的用户姓名，注意主键必须存在，否则不会报错，但是也不做任何改变
        updateRow.addInt("id",1);
        updateRow.addString("name","新测试员1");

        kuduSession.apply(update);

        System.out.println("*************修改完毕***************");

    }

    public static void query(KuduClient kuduClient,String tableName) throws KuduException {
        KuduScanner.KuduScannerBuilder kuduScannerBuilder = kuduClient.newScannerBuilder(kuduClient.openTable(tableName));

        //指定要输出哪些字段
        ArrayList<String> columns = new ArrayList<String>();
        columns.add("id");
        columns.add("name");
        columns.add("age");

        kuduScannerBuilder.setProjectedColumnNames(columns);
        //返回结果集
        KuduScanner scanner = kuduScannerBuilder.build();

        System.out.println("当前table中信息如下");

        //遍历结果集
        while (scanner.hasMoreRows()){
            RowResultIterator rowResults = scanner.nextRows();
            while (rowResults.hasNext()){
                RowResult result = rowResults.next();
                System.out.println("id: "+result.getInt("id")+" name: "+result.getString("name")
                        +" age: "+result.getInt("age"));
            }
        }
    }

    public static void main(String[] args) throws KuduException {
        KuduClient kuduClient = KuduFactory.getKuduClient("118.195.133.251:7051,118.195.133.251:7151,118.195.133.251:7251");
        String tableName = "test";
        try {
            if(kuduClient.tableExists("test")){
                System.out.println("=======开始CRUD===========");
                insert(kuduClient,tableName);
                query(kuduClient,tableName);
                update(kuduClient,tableName);
                query(kuduClient,tableName);
                delete(kuduClient,tableName);
                query(kuduClient,tableName);

            }
            System.out.println("=======CRUD成功！！=======");
        }finally {
            kuduClient.close();
        }
    }
}

