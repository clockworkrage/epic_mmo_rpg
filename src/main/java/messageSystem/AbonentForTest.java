package messagesystem;

/**
 * Created by Андрей on 21.12.2015.
 */


public class AbonentForTest implements Abonent{
    private final Address address = new Address();

    @Override
    public Address getAddress(){
        return address;
    }
}
