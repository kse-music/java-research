package cn.hiboot.java.research.design.filter;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/7/21 17:05
 */
public class FilterDemo {

    @Test
    public void filter() {
        List<Employee> persons = new ArrayList<>();

        persons.add(new Employee("Tom", "Male", "YES"));
        persons.add(new Employee("Jack", "Male", "NO"));
        persons.add(new Employee("Jane", "Female", "NO"));
        persons.add(new Employee("Diana", "Female", "YES"));
        persons.add(new Employee("Mike", "Male", "NO"));
        persons.add(new Employee("Bob", "Male", "YES"));

        Criteria male = new CriteriaMale();
        Criteria female = new CriteriaFemale();
        Criteria retire = new CriteriaRetire();
        Criteria retireMale = new AndCriteria(retire, male);
        Criteria retireOrFemale = new OrCriteria(retire, female);

        System.out.println("Males: ");
        printPersons(male.meetCriteria(persons));

        System.out.println("Females: ");
        printPersons(female.meetCriteria(persons));

        System.out.println("Retire Males: ");
        printPersons(retireMale.meetCriteria(persons));

        System.out.println("Retire Or Females: ");
        printPersons(retireOrFemale.meetCriteria(persons));
    }

    private void printPersons(List<Employee> persons) {
        for (Employee person : persons) {
            System.out.println(person);
        }
    }
}
