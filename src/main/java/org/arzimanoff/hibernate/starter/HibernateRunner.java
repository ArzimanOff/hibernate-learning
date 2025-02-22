package org.arzimanoff.hibernate.starter;

import lombok.extern.slf4j.Slf4j;
import org.arzimanoff.hibernate.entity.*;
import org.arzimanoff.hibernate.util.HibernateUtil;

import java.time.LocalDate;

@Slf4j
public class HibernateRunner {


    public static void main(String[] args) {

        Company company = Company.builder()
                .name("Sber")
                .build();

        // Transient
        User user = User.builder()
                .username("arz")
                .personalInfo(
                        PersonalInfo.builder()
                                .firstname("AAA")
                                .lastname("Arzimanov")
                                .birthdate(new Birthday(LocalDate.of(2005, 5, 19)))
                                .build()
                )
                .role(Role.USER)
                .company(company)
                .build();


        log.info("Объект User создан и является Transient : {}", user);

        try (var sessionFactory = HibernateUtil.buildSessionFactory()) {

            try(var session1 = sessionFactory.openSession()){
                session1.beginTransaction();
                // persistent к session1 и transient к session2


                session1.saveOrUpdate(user);
                log.debug("Объект User был сохранен: {} в сессии {}", user, session1);
                session1.getTransaction().commit();
            }

//            try(var session2 = sessionFactory.openSession()){
//                session2.beginTransaction();
//                session2.delete(user);
//                session2.getTransaction().commit();
//            }
        } catch (Exception e){
            log.error("Exception: ", e);
            throw e;
        }
    }
}
