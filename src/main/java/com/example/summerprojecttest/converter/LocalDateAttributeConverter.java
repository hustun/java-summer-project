package com.example.summerprojecttest.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

//@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {
    @Override
    public Date convertToDatabaseColumn(LocalDate localDate) {
        System.out.println("---A----");
        System.out.println(localDate);
        System.out.println("---A----");

        return localDate == null ? null : Date.valueOf(localDate);
    }

    @Override
    public LocalDate convertToEntityAttribute(Date date) {
        System.out.println("---B----");
        System.out.println(date);
        System.out.println("---B----");
        return date == null ? null : date.toLocalDate();
    }
}
