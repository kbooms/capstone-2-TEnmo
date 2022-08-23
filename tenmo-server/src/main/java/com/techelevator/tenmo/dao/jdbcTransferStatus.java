package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.StatusTransfer;
import org.springframework.jdbc.core.JdbcTemplate;
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
      return jdbcTemplate.queryForObject(sql,StatusTransfer.class,statusId);
    }
}
