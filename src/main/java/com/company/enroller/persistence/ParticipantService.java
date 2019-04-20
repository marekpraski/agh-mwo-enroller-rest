package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Participant;

@Component("participantService")
public class ParticipantService {

	DatabaseConnector connector;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}

//	public Collection<Participant> getAll() {
//	    Session session = connector.getSession();
//	    Criteria criteria = session.createCriteria(Participant.class);
//
//		return criteria.list();
//	}
    public Collection<Participant> getAll() {
        String hql = "FROM Participant";
        Query query = connector.getSession().createQuery(hql);
        return query.list();
    }

	public Participant findByLogin(String login) {
        Session session = connector.getSession();
		return (Participant) session.get(Participant.class, login);
	}

	public Participant add(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(participant);
		transaction.commit();
		return participant;
		
	}
	public Participant delete(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().delete(participant);
		transaction.commit();
		return participant;
		
	}
	public Participant update(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().merge(participant);
		transaction.commit();
		return participant;
		
	}
}
