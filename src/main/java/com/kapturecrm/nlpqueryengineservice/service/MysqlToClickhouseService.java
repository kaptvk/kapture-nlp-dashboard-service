package com.kapturecrm.nlpqueryengineservice.service;

import com.kapturecrm.customer.objects.Customer;
import com.kapturecrm.nlpqueryengineservice.repository.MysqlToClickhouseRepo;
import com.kapturecrm.object.Employee;
import com.kapturecrm.ticket.objects.Ticket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class MysqlToClickhouseService {

    private final MysqlToClickhouseRepo mysqlToClickhouseRepository;

    public void migrateDataFromMysqlToClickhouse(int cmId) {
        try {
            cmId = 415;
            List<Ticket> ticket = mysqlToClickhouseRepository.getTicket(cmId);
            if (ticket != null) {
                int i = 1;
                for (Ticket t : ticket) {
                    mysqlToClickhouseRepository.saveTicket(t);
                    log.error("Ticket migrated successfully" + i++);
                }
            }
        } catch (Exception e) {
            log.error("Error in migrateDataFromMysqlToClickhouse", e);
        }

    }

    public void migrateDataFromMysqlToClickhouse(String cmId1) {
        try {
            int cmId = 415;
            List<Customer> customer = mysqlToClickhouseRepository.getCustomer(cmId);
            if (customer != null) {
                int i = 1;
                for (Customer c : customer) {
                    mysqlToClickhouseRepository.saveCustomer(c);
                    log.error("Customer migrated successfully" + i++);
                }
            }
        } catch (Exception e) {
            log.error("Error in migrateDataFromMysqlToClickhouse", e);
        }

    }


    public void migrateDataFromMysqlToClickhouse() {
        try {
            int cmId = 415;
            List<Employee> employee = mysqlToClickhouseRepository.getEmployee(cmId);
            if (employee != null) {
                int i = 1;
                for (Employee e : employee) {
                    mysqlToClickhouseRepository.saveEmployee(e);
                    log.error("Employee migrated successfully" + i++);
                    System.out.println(i);
                }
            }
        } catch (Exception e) {
            log.error("Error in migrateDataFromMysqlToClickhouse", e);
        }

    }
}
