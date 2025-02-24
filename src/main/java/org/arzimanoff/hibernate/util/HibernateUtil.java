package org.arzimanoff.hibernate.util;

import org.arzimanoff.hibernate.converter.BirthdayConverter;
import org.arzimanoff.hibernate.entity.Company;
import org.arzimanoff.hibernate.entity.Profile;
import org.arzimanoff.hibernate.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    public static SessionFactory buildSessionFactory(){
        Configuration configuration = new Configuration()
                .configure()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Company.class)
                .addAnnotatedClass(Profile.class)
                .addAttributeConverter(new BirthdayConverter(), true);

        return configuration.buildSessionFactory();
    }
}
