package com.challenge.lendtech.database;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@Repository
public class LogData {
    static final String LOG_EVENT_SP_SQL = "{CALL ASP_ELEVATOR_LOG_EVENTS(?,?,?)}";

    @Autowired
    private EntityManager entityManager;

    public Map<String,String> insertLogData(String eId, String eventType, String description){
        Map<String, String> response = new HashMap<>();
        Map<String, Object> mapList = new HashMap<>();
        try {
            Session session = entityManager.unwrap(Session.class);
            session.doWork(conn -> {
                conn.setAutoCommit(false);
                try (CallableStatement statement = conn.prepareCall(LOG_EVENT_SP_SQL)) {
                    statement.setString(1, eventType);
                    statement.setString(2, description);
//                    statement.setString(3, description);
                    statement.executeQuery();
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                       String responseCode = resultSet.getString(1);
                        if (responseCode.equalsIgnoreCase("00")) {
                            response.put("field39", "00");
                            response.put("field48", "Successful");
                            response.put("field54", resultSet.getString(2));
                        }else {
                            response.put("field39", "57");
                            response.put("field48", "Failed");
                            response.put("field54", "0");
                        }
                    }
                    conn.commit();
                    conn.setAutoCommit(true);
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            response.put("result", "99");
            response.put("result_desc", "invalid client");
        }
        return response;
    }
}
