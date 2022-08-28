package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TypeTransfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class jdbcTransferType implements TransferTypeDao {

    private JdbcTemplate jdbcTemplate;

    public jdbcTransferType(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TypeTransfer getType(int typeId) {
        String sql = "Select * from transfer_type where transfer_type_id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql,typeId);
        //return jdbcTemplate.queryForObject(sql, TypeTransfer.class, typeId);
        TypeTransfer typeTransfer = new TypeTransfer();
        if ( rs.next()){
            typeTransfer.setTransferTypeId(rs.getInt("transfer_type_id"));
            typeTransfer.setTransferTypeDesc(rs.getString("transfer_type_desc"));
        }
        return typeTransfer;
    }
}
