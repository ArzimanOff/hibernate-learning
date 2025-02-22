package org.arzimanoff.hibernate.starter;

import org.arzimanoff.hibernate.converter.BirthdayConverter;
import org.arzimanoff.hibernate.entity.Birthday;
import org.arzimanoff.hibernate.entity.Role;
import org.arzimanoff.hibernate.entity.User;
import org.arzimanoff.hibernate.util.HibernateUtil;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;
import java.time.Month;

public class HibernateRunner {
    public static void main(String[] args) {

        // Transient
        var user = User.builder()
                .username("arz")
                .firstname("BBB")
                .lastname("Arzimanov")
                .birthDate(new Birthday(LocalDate.of(2005, 5, 19)))
                .role(Role.USER)
                .build();

        try (var sessionFactory = HibernateUtil.buildSessionFactory()) {

            try(var session1 = sessionFactory.openSession()){
                session1.beginTransaction();

                // persistent к session1 и transient к session2
                session1.saveOrUpdate(user);
                session1.getTransaction().commit();
            }

            try(var session2 = sessionFactory.openSession()){
                session2.beginTransaction();
                session2.delete(user);
                session2.getTransaction().commit();
            }



        }

    }
}
