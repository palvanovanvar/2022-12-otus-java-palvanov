package homework;


import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    TreeMap<Customer, String> customers;

    public CustomerService() {
        customers = new TreeMap<Customer, String>(Comparator.comparing(Customer::getScores));
    }

    public Map.Entry<Customer, String> getSmallest() {
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        Map.Entry<Customer, String> entry = customers.firstEntry();
        Customer customerTmp = new Customer( entry.getKey().getId(), entry.getKey().getName(), entry.getKey().getScores());
        return Map.entry(customerTmp, entry.getValue()); // это "заглушка, чтобы скомилировать"
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> entry = customers.higherEntry(customer);
        if (entry == null) { return null; };
        Customer customerTmp = new Customer( entry.getKey().getId(), entry.getKey().getName(), entry.getKey().getScores());
        return Map.entry(customerTmp, entry.getValue()); // это "заглушка, чтобы скомилировать"
    }

    public void add(Customer customer, String data) {
        customers.put(customer, data);
    }
}
