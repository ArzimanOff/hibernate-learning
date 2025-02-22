package org.arzimanoff.hibernate.starter;

import lombok.extern.slf4j.Slf4j;
import org.arzimanoff.hibernate.entity.Birthday;
import org.arzimanoff.hibernate.entity.Role;
import org.arzimanoff.hibernate.entity.User;
import org.arzimanoff.hibernate.util.HibernateUtil;

import java.time.LocalDate;

@Slf4j
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


        log.info("Объект User создан и является Transient : {}", user);

        try (var sessionFactory = HibernateUtil.buildSessionFactory()) {

            try(var session1 = sessionFactory.openSession()){
                session1.beginTransaction();
                // persistent к session1 и transient к session2
                user.setFirstname("Ivan");
                log.warn("Объект User был изменен: {}", user);
                session1.saveOrUpdate(user);
                log.debug("Объект User был сохранен: {} в сессии {}", user, session1);
                session1.getTransaction().commit();
            }

            try(var session2 = sessionFactory.openSession()){
                session2.beginTransaction();
                session2.delete(user);
                session2.getTransaction().commit();
            }
        } catch (Exception e){
            log.error("Exception: ", e);
            throw e;
        }
    }
}
