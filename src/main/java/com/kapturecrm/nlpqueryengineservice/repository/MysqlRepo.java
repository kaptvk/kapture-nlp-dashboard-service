package com.kapturecrm.nlpqueryengineservice.repository;

import com.kapturecrm.nlpqueryengineservice.Object.NlpDashboardPrompt;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
@Slf4j
public class MysqlRepo {

    @Autowired
    private SessionFactory sessionFactory;

    public boolean addPrompt(NlpDashboardPrompt nlpDashboardprompt) {
        Session session = null;
        Transaction tx = null;
        boolean success = false;
        try {
            if (nlpDashboardprompt != null) {
                session = sessionFactory.openSession();
                tx = session.beginTransaction();
                session.saveOrUpdate(nlpDashboardprompt);
                tx.commit();
                success = true;
            }
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("Error in addPrompt", ex);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return success;
    }

    public List<NlpDashboardPrompt> getPrompt(int cmId, int empId) {
        Session session = null;
        List<NlpDashboardPrompt> nlpDashboardPromptList = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            nlpDashboardPromptList = session.createQuery("from NlpDashboardPrompt where cmId = :cmId and empId = :empId", NlpDashboardPrompt.class).setParameter("cmId", cmId).setParameter("empId", empId).getResultList();
        } catch (Exception ex) {
            log.error("Error in getPrompt", ex);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return nlpDashboardPromptList;
    }

}
