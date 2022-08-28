package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.StatusTransfer;
import com.techelevator.tenmo.model.TypeTransfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component

public class jdbcTransferStatus implements TransferStatusDao {

    private JdbcTemplate jdbcTemplate;

    public jdbcTransferStatus(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public StatusTransfer getStatus(int statusId) {
        String sql = "Select * from transfer_status where transfer_status_id = ?";
      //return jdbcTemplate.queryForObject(sql,StatusTransfer.class,statusId);
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql,statusId);
        //return jdbcTemplate.queryForObject(sql, TypeTransfer.class, typeId);
        StatusTransfer statusTransfer = new StatusTransfer();
        if ( rs.next()){
            statusTransfer.setTransferStatusId((rs.getInt("transfer_status_id")));
            statusTransfer.setTransferStatusDesc(rs.getString("transfer_status_desc"));
        }
        return statusTransfer;
    }
}
