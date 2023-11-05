package it.voltats.gestionepista.business;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HolidayCalendar {
    private static List<Date> listDate = new ArrayList<>();
    private static int currentYear = LocalDate.now().getYear();

    private static void addDate(){
        listDate.add(new Date(currentYear-1900,1,1, 0 ,0 ,0));
        listDate.add(new Date(currentYear-1900,1,6, 0 ,0 ,0));
        listDate.add(new Date(currentYear-1900,4,25, 0 ,0 ,0));
        listDate.add(new Date(currentYear-1900,5,1, 0 ,0 ,0));
        listDate.add(new Date(currentYear-1900,6,2, 0 ,0 ,0));
        listDate.add(new Date(currentYear-1900,8,15, 0 ,0 ,0));
        listDate.add(new Date(currentYear-1900,11,1, 0 ,0 ,0));
        listDate.add(new Date(currentYear-1900,12,8, 0 ,0 ,0));
        listDate.add(new Date(currentYear-1900,12,25, 0 ,0 ,0));
        listDate.add(new Date(currentYear-1900,12,26, 0 ,0 ,0));
    }

    public static boolean isHoliday(Date date){
        if(date.getDay() == 5 || date.getDay() == 6)
            return true;

        if(listDate.size() == 0)
            addDate();

        for(Date d: listDate){
            if(d.getYear() == date.getYear() && d.getMonth() == date.getMonth() && d.getDate() == date.getDate())
                return true;
        }

        return false;
    }

}
