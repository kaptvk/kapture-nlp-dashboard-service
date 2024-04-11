package com.kapturecrm.nlpdashboardservice.repository;

import com.kapturecrm.nlpdashboardservice.model.NLPDPrompt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;


@Repository
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NLPDPromptRepository {

    private final SessionFactory sessionFactory;

    public boolean addPrompt(NLPDPrompt nlpdPrompt) {
        Session session = null;
        Transaction tx = null;
        boolean success = false;
        try {
            if (nlpdPrompt != null) {
                session = sessionFactory.openSession();
                tx = session.beginTransaction();
                session.saveOrUpdate(nlpdPrompt);
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

    public List<NLPDPrompt> getRecentPrompts(int cmId, int empId) {
        Session session = null;
        List<NLPDPrompt> prompList = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            TypedQuery<NLPDPrompt> query = session.createQuery(
                    "from NLPDPrompt where cmId = :cmId and empId = :empId order by createTime desc", NLPDPrompt.class);
            prompList = query.setParameter("cmId", cmId).setParameter("empId", empId)
                    .setMaxResults(5)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Error in getPrompt", ex);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return prompList;
    }

}
